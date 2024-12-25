package dev.jeziellago.compose.markdowntext.plugins.image

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.widget.TextView
import coil3.Image
import coil3.ImageLoader
import coil3.asDrawable
import coil3.request.Disposable
import coil3.request.ImageRequest
import coil3.target.Target
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.AsyncDrawableLoader
import io.noties.markwon.image.AsyncDrawableScheduler
import io.noties.markwon.image.DrawableUtils
import io.noties.markwon.image.ImageSpanFactory
import java.util.concurrent.atomic.AtomicBoolean

class ImagesPlugin private constructor(
    private val imageLoader: ImageLoader,
    private val coilStore: CoilStore,
    private val imageDrawableLoader: AnimatedImageDrawableLoader
) : AbstractMarkwonPlugin() {

    override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
        builder.setFactory(org.commonmark.node.Image::class.java, ImageSpanFactory())
    }

    override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
        builder.asyncDrawableLoader(imageDrawableLoader)
    }

    override fun beforeSetText(textView: TextView, markdown: Spanned) {
        AsyncDrawableScheduler.unschedule(textView)
    }

    override fun afterSetText(textView: TextView) {
        AsyncDrawableScheduler.schedule(textView)
    }

    companion object {
        private val cache: HashMap<AsyncDrawable, Disposable> = HashMap(2)

        fun create(context: Context, imageLoader: ImageLoader): ImagesPlugin {
            val coilStore = object : CoilStore {
                override fun load(drawable: AsyncDrawable): ImageRequest {
                    return ImageRequest.Builder(context)
                        .data(drawable.destination)
                        .build()
                }

                override fun cancel(disposable: Disposable) {
                    disposable.dispose()
                }
            }
            return ImagesPlugin(
                imageLoader = imageLoader,
                coilStore = coilStore,
                imageDrawableLoader = AnimatedImageDrawableLoader(
                    resources = context.resources,
                    coilStore = coilStore,
                    imageLoader = imageLoader
                )
            )
        }

        class AnimatedImageDrawableLoader(
            private val resources: Resources,
            private val coilStore: CoilStore,
            private val imageLoader: ImageLoader,
        ) : AsyncDrawableLoader() {

            override fun load(drawable: AsyncDrawable) {
                val loaded = AtomicBoolean(false)
                val target: Target = AnimatedDrawableTarget(drawable, loaded, resources)
                val request = coilStore.load(drawable).newBuilder()
                    .target(target)
                    .build()

                val disposable = imageLoader.enqueue(request)
                if (!loaded.get()) {
                    loaded.set(true)
                    cache[drawable] = disposable
                }
            }

            override fun cancel(drawable: AsyncDrawable) {
                val disposable = cache.remove(drawable)
                if (disposable != null) {
                    coilStore.cancel(disposable)
                }
            }

            override fun placeholder(drawable: AsyncDrawable): Drawable? = null

            class AnimatedDrawableTarget(
                private val drawable: AsyncDrawable,
                private val loaded: AtomicBoolean,
                private val resources: Resources
            ) : Target {
                override fun onSuccess(result: Image) {
                    val loadedDrawable = result.asDrawable(resources)

                    if (cache.remove(drawable) != null || !loaded.get()) {
                        loaded.set(true)
                        if (drawable.isAttached) {
                            DrawableUtils.applyIntrinsicBoundsIfEmpty(loadedDrawable)
                            drawable.result = loadedDrawable

                            if (loadedDrawable is Animatable) {
                                (loadedDrawable as Animatable).start()
                            }
                        }
                    }
                }

                override fun onStart(placeholder: Image?) {
                    if (placeholder != null && drawable.isAttached) {
                        val loadedDrawable = placeholder.asDrawable(resources)
                        DrawableUtils.applyIntrinsicBoundsIfEmpty(loadedDrawable)
                        drawable.result = loadedDrawable
                    }
                }

                override fun onError(error: Image?) {

                    if (cache.remove(drawable) != null) {
                        if (error != null && drawable.isAttached) {
                            val loadedDrawable = error.asDrawable(resources)

                            DrawableUtils.applyIntrinsicBoundsIfEmpty(loadedDrawable)
                            drawable.result = loadedDrawable
                        }
                    }
                }
            }
        }
    }

    interface CoilStore {
        fun load(drawable: AsyncDrawable): ImageRequest
        fun cancel(disposable: Disposable)
    }
}
