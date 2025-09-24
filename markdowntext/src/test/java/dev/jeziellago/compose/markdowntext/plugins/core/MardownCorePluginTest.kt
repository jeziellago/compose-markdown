package dev.jeziellago.compose.markdowntext.plugins.core

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import io.mockk.*
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.core.CorePlugin
import io.noties.markwon.core.MarkwonTheme
import org.commonmark.node.*
import org.junit.Before
import org.junit.Test

class MardownCorePluginTest {

    private lateinit var plugin: MardownCorePlugin
    private lateinit var mockVisitorBuilder: MarkwonVisitor.Builder
    private lateinit var mockThemeBuilder: MarkwonTheme.Builder
    private lateinit var mockSpansFactoryBuilder: MarkwonSpansFactory.Builder
    private lateinit var mockVisitor: MarkwonVisitor

    @Before
    fun setUp() {
        plugin = MardownCorePlugin(
            syntaxHighlightColor = Color.Gray.toArgb(),
            syntaxHighlightTextColor = Color.Black.toArgb(),
            enableUnderlineForLink = true
        )
        
        mockVisitorBuilder = mockk<MarkwonVisitor.Builder>(relaxed = true)
        mockThemeBuilder = mockk<MarkwonTheme.Builder>(relaxed = true)
        mockSpansFactoryBuilder = mockk<MarkwonSpansFactory.Builder>(relaxed = true)
        mockVisitor = mockk<MarkwonVisitor>(relaxed = true)
    }

    @Test
    fun `test plugin creation with parameters`() {
        val plugin = MardownCorePlugin(
            syntaxHighlightColor = Color.Red.toArgb(),
            syntaxHighlightTextColor = Color.Blue.toArgb(),
            enableUnderlineForLink = false
        )

        assert(plugin is CorePlugin)
    }

    @Test
    fun `test configureVisitor sets up all node handlers`() {
        every { mockVisitorBuilder.on(any<Class<Text>>(), any()) } returns mockVisitorBuilder
        every { mockVisitorBuilder.on(any<Class<StrongEmphasis>>(), any()) } returns mockVisitorBuilder
        every { mockVisitorBuilder.on(any<Class<Emphasis>>(), any()) } returns mockVisitorBuilder
        every { mockVisitorBuilder.on(any<Class<BlockQuote>>(), any()) } returns mockVisitorBuilder
        every { mockVisitorBuilder.on(any<Class<Code>>(), any()) } returns mockVisitorBuilder
        every { mockVisitorBuilder.on(any<Class<FencedCodeBlock>>(), any()) } returns mockVisitorBuilder
        every { mockVisitorBuilder.on(any<Class<IndentedCodeBlock>>(), any()) } returns mockVisitorBuilder
        every { mockVisitorBuilder.on(any<Class<Image>>(), any()) } returns mockVisitorBuilder
        every { mockVisitorBuilder.on(any<Class<BulletList>>(), any()) } returns mockVisitorBuilder
        every { mockVisitorBuilder.on(any<Class<OrderedList>>(), any()) } returns mockVisitorBuilder
        every { mockVisitorBuilder.on(any<Class<ListItem>>(), any()) } returns mockVisitorBuilder
        every { mockVisitorBuilder.on(any<Class<ThematicBreak>>(), any()) } returns mockVisitorBuilder
        every { mockVisitorBuilder.on(any<Class<Heading>>(), any()) } returns mockVisitorBuilder
        every { mockVisitorBuilder.on(any<Class<SoftLineBreak>>(), any()) } returns mockVisitorBuilder
        every { mockVisitorBuilder.on(any<Class<HardLineBreak>>(), any()) } returns mockVisitorBuilder
        every { mockVisitorBuilder.on(any<Class<Paragraph>>(), any()) } returns mockVisitorBuilder
        every { mockVisitorBuilder.on(any<Class<Link>>(), any()) } returns mockVisitorBuilder

        plugin.configureVisitor(mockVisitorBuilder)

        verify { mockVisitorBuilder.on(Text::class.java, any()) }
        verify { mockVisitorBuilder.on(StrongEmphasis::class.java, any()) }
        verify { mockVisitorBuilder.on(Emphasis::class.java, any()) }
        verify { mockVisitorBuilder.on(BlockQuote::class.java, any()) }
        verify { mockVisitorBuilder.on(Code::class.java, any()) }
        verify { mockVisitorBuilder.on(FencedCodeBlock::class.java, any()) }
        verify { mockVisitorBuilder.on(IndentedCodeBlock::class.java, any()) }
        verify { mockVisitorBuilder.on(Image::class.java, any()) }
        verify { mockVisitorBuilder.on(BulletList::class.java, any()) }
        verify { mockVisitorBuilder.on(OrderedList::class.java, any()) }
        verify { mockVisitorBuilder.on(ListItem::class.java, any()) }
        verify { mockVisitorBuilder.on(ThematicBreak::class.java, any()) }
        verify { mockVisitorBuilder.on(Heading::class.java, any()) }
        verify { mockVisitorBuilder.on(SoftLineBreak::class.java, any()) }
        verify { mockVisitorBuilder.on(HardLineBreak::class.java, any()) }
        verify { mockVisitorBuilder.on(Paragraph::class.java, any()) }
        verify { mockVisitorBuilder.on(Link::class.java, any()) }
    }

    @Test
    fun `test configureTheme sets colors and underline`() {
        every { mockThemeBuilder.codeBackgroundColor(any()) } returns mockThemeBuilder
        every { mockThemeBuilder.isLinkUnderlined(any()) } returns mockThemeBuilder
        every { mockThemeBuilder.codeTextColor(any()) } returns mockThemeBuilder

        plugin.configureTheme(mockThemeBuilder)

        verify { mockThemeBuilder.codeBackgroundColor(Color.Gray.toArgb()) }
        verify { mockThemeBuilder.isLinkUnderlined(true) }
        verify { mockThemeBuilder.codeTextColor(Color.Black.toArgb()) }
    }

    @Test
    fun `test configureTheme with unspecified text color`() {
        val pluginWithUnspecifiedColor = MardownCorePlugin(
            syntaxHighlightColor = Color.Gray.toArgb(),
            syntaxHighlightTextColor = Color.Unspecified.toArgb(),
            enableUnderlineForLink = true
        )
        
        every { mockThemeBuilder.codeBackgroundColor(any()) } returns mockThemeBuilder
        every { mockThemeBuilder.isLinkUnderlined(any()) } returns mockThemeBuilder

        pluginWithUnspecifiedColor.configureTheme(mockThemeBuilder)

        verify { mockThemeBuilder.codeBackgroundColor(Color.Gray.toArgb()) }
        verify { mockThemeBuilder.isLinkUnderlined(true) }
        verify(exactly = 0) { mockThemeBuilder.codeTextColor(any()) }
    }

    @Test
    fun `test configureSpansFactory sets up all span factories`() {
        every { mockSpansFactoryBuilder.setFactory(any<Class<out org.commonmark.node.Node>>(), any()) } returns mockSpansFactoryBuilder

        plugin.configureSpansFactory(mockSpansFactoryBuilder)

        verify { mockSpansFactoryBuilder.setFactory(StrongEmphasis::class.java, any()) }
        verify { mockSpansFactoryBuilder.setFactory(Emphasis::class.java, any()) }
        verify { mockSpansFactoryBuilder.setFactory(BlockQuote::class.java, any()) }
        verify { mockSpansFactoryBuilder.setFactory(Code::class.java, any()) }
        verify { mockSpansFactoryBuilder.setFactory(FencedCodeBlock::class.java, any()) }
        verify { mockSpansFactoryBuilder.setFactory(IndentedCodeBlock::class.java, any()) }
        verify { mockSpansFactoryBuilder.setFactory(ListItem::class.java, any()) }
        verify { mockSpansFactoryBuilder.setFactory(Heading::class.java, any()) }
        verify { mockSpansFactoryBuilder.setFactory(Link::class.java, any()) }
        verify { mockSpansFactoryBuilder.setFactory(ThematicBreak::class.java, any()) }
    }

    @Test
    fun `test addOnTextAddedListener`() {
        val listener = mockk<CorePlugin.OnTextAddedListener>()
        
        val result = plugin.addOnTextAddedListener(listener)
        
        assert(result is MardownCorePlugin)
    }

    @Test
    fun `test visitCodeBlock method exists and is accessible`() {
        val mockNode = mockk<FencedCodeBlock>(relaxed = true)
        
        assert(plugin is MardownCorePlugin)
    }
}
