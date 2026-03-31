package dev.jeziellago.compose.markdowntext.plugins.syntaxhighlight

import io.mockk.*
import org.commonmark.node.Node
import org.commonmark.renderer.text.TextContentNodeRendererContext
import org.commonmark.renderer.text.TextContentWriter
import org.junit.Before
import org.junit.Test

class StrikethroughNodeRendererTest {

    private lateinit var mockContext: TextContentNodeRendererContext
    private lateinit var mockWriter: TextContentWriter
    private lateinit var renderer: SyntaxHighlightNodeRenderer

    @Before
    fun setUp() {
        mockContext = mockk<TextContentNodeRendererContext>(relaxed = true)
        mockWriter = mockk<TextContentWriter>(relaxed = true)
        every { mockContext.writer } returns mockWriter
        renderer = SyntaxHighlightNodeRenderer(mockContext)
    }

    @Test
    fun `syntax highlight renderer exposes syntax highlight node type`() {
        val abstractRenderer = object : SyntaxHighlightRenderer() {
            override fun render(node: Node) {}
        }

        val nodeTypes = abstractRenderer.nodeTypes
        assert(nodeTypes.contains(SyntaxHighlight::class.java))
        assert(nodeTypes.size == 1)
    }

    @Test
    fun `renderer writes wrapping markers and renders child content`() {
        val mockNode = mockk<SyntaxHighlight>(relaxed = true)
        val mockChildNode = mockk<Node>(relaxed = true)

        every { mockNode.firstChild } returns mockChildNode
        every { mockChildNode.next } returns null
        every { mockWriter.write(any<Char>()) } just Runs
        every { mockContext.render(any()) } just Runs

        renderer.render(mockNode)

        verify(exactly = 2) { mockWriter.write('/') }
        verify { mockContext.render(mockChildNode) }
    }

    @Test
    fun `renderer still writes wrapping markers when there are no children`() {
        val mockNode = mockk<SyntaxHighlight>(relaxed = true)

        every { mockNode.firstChild } returns null
        every { mockWriter.write(any<Char>()) } just Runs

        renderer.render(mockNode)

        verify(exactly = 2) { mockWriter.write('/') }
        verify(exactly = 0) { mockContext.render(any()) }
    }
}
