package dev.jeziellago.compose.markdowntext

import android.content.Context
import coil.ImageLoader
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.SoftBreakAddsNewLinePlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin

internal object MarkdownRender {

    fun create(
        context: Context,
        imageLoader: ImageLoader?,
        linkifyMask: Int,
        onLinkClicked: ((String) -> Unit)? = null
    ): Markwon {
        val coilImageLoader = imageLoader ?: createCoilImageLoader(context)
        return Markwon.builder(context)
            .usePlugin(HtmlPlugin.create())
            .usePlugin(CoilImagesPlugin.create(context, coilImageLoader))
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(TablePlugin.create(context))
            .usePlugin(LinkifyPlugin.create(linkifyMask))
            .usePlugin(SoftBreakAddsNewLinePlugin.create())
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
                    // Setting [MarkwonConfiguration.Builder.linkResolver] overrides
                    // Markwon's default behaviour - see Markwon's [LinkResolverDef]
                    // and how it's used in [MarkwonConfiguration.Builder].
                    // Only use it if the client explicitly wants to handle link clicks.
                    onLinkClicked ?: return
                    builder.linkResolver { _, link ->
                        // handle individual clicks on Textview link
                        onLinkClicked.invoke(link)
                    }
                }
            })
            .build()
    }

    private fun createCoilImageLoader(context: Context) =
        ImageLoader.Builder(context)
            .crossfade(true)
            .build()
}
