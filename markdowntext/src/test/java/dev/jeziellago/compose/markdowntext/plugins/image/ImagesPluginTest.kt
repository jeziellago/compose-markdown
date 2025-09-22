package dev.jeziellago.compose.markdowntext.plugins.image

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import io.mockk.*
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.image.AsyncDrawable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ImagesPluginTest {

    private lateinit var context: Context
    private lateinit var mockImageLoader: ImageLoader
    private lateinit var plugin: ImagesPlugin
    private lateinit var mockTextView: TextView
    private lateinit var mockSpanned: Spanned

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        mockImageLoader = mockk<ImageLoader>(relaxed = true)
        mockTextView = mockk<TextView>(relaxed = true)
        mockSpanned = mockk<Spanned>(relaxed = true)
    }

    @Test
    fun `test configureSpansFactory sets ImageSpanFactory`() {
        plugin = ImagesPlugin.create(context, mockImageLoader)
        val mockBuilder = mockk<MarkwonSpansFactory.Builder>(relaxed = true)
        
        every { mockBuilder.setFactory(any<Class<out org.commonmark.node.Node>>(), any()) } returns mockBuilder
        
        plugin.configureSpansFactory(mockBuilder)
        
        verify { mockBuilder.setFactory(org.commonmark.node.Image::class.java, any()) }
    }

    @Test
    fun `test configureConfiguration sets AsyncDrawableLoader`() {
        plugin = ImagesPlugin.create(context, mockImageLoader)
        val mockBuilder = mockk<MarkwonConfiguration.Builder>(relaxed = true)
        
        every { mockBuilder.asyncDrawableLoader(any()) } returns mockBuilder
        
        plugin.configureConfiguration(mockBuilder)
        
        verify { mockBuilder.asyncDrawableLoader(any()) }
    }
}
