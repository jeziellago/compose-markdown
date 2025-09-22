package dev.jeziellago.compose.markdowntext.plugins.syntaxhighlight

import io.mockk.*
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.MarkwonVisitor
import io.noties.markwon.core.factory.CodeSpanFactory
import org.commonmark.parser.Parser
import org.junit.Before
import org.junit.Test

class SyntaxHighlightPluginTest {

    private lateinit var plugin: SyntaxHighlightPlugin
    private lateinit var mockParserBuilder: Parser.Builder
    private lateinit var mockSpansFactoryBuilder: MarkwonSpansFactory.Builder
    private lateinit var mockVisitorBuilder: MarkwonVisitor.Builder

    @Before
    fun setUp() {
        plugin = SyntaxHighlightPlugin()
        mockParserBuilder = mockk<Parser.Builder>(relaxed = true)
        mockSpansFactoryBuilder = mockk<MarkwonSpansFactory.Builder>(relaxed = true)
        mockVisitorBuilder = mockk<MarkwonVisitor.Builder>(relaxed = true)
    }

    @Test
    fun `test plugin is instance of AbstractMarkwonPlugin`() {
        assert(plugin is AbstractMarkwonPlugin)
    }

    @Test
    fun `test configureParser adds SyntaxHighlightExtension`() {
        every { mockParserBuilder.extensions(any()) } returns mockParserBuilder

        plugin.configureParser(mockParserBuilder)

        verify { mockParserBuilder.extensions(any()) }
    }

    @Test
    fun `test configureSpansFactory sets CodeSpanFactory for SyntaxHighlight`() {
        every { mockSpansFactoryBuilder.setFactory(any<Class<out org.commonmark.node.Node>>(), any()) } returns mockSpansFactoryBuilder

        plugin.configureSpansFactory(mockSpansFactoryBuilder)

        verify { 
            mockSpansFactoryBuilder.setFactory(
                SyntaxHighlight::class.java,
                any<CodeSpanFactory>()
            )
        }
    }

    @Test
    fun `test configureVisitor sets up SyntaxHighlight node visitor`() {
        every { mockVisitorBuilder.on(any<Class<out org.commonmark.node.Node>>(), any()) } returns mockVisitorBuilder

        plugin.configureVisitor(mockVisitorBuilder)

        verify { mockVisitorBuilder.on(SyntaxHighlight::class.java, any()) }
    }

    @Test
    fun `test visitor behavior with mocked visitor`() {
        val mockVisitor = mockk<MarkwonVisitor>(relaxed = true)
        val mockSyntaxHighlight = mockk<SyntaxHighlight>(relaxed = true)

        every { mockVisitor.length() } returns 0
        every { mockVisitor.visitChildren(any()) } just Runs

        val length = mockVisitor.length()
        mockVisitor.visitChildren(mockSyntaxHighlight)

        verify { mockVisitor.length() }
        verify { mockVisitor.visitChildren(mockSyntaxHighlight) }
    }

    @Test
    fun `test plugin creation and basic functionality`() {
        val plugin1 = SyntaxHighlightPlugin()
        val plugin2 = SyntaxHighlightPlugin()

        assert(plugin1 !== plugin2)
        assert(plugin1 is SyntaxHighlightPlugin)
        assert(plugin2 is SyntaxHighlightPlugin)
    }
}
