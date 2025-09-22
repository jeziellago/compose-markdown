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
    fun `test SyntaxHighlightRenderer getNodeTypes returns SyntaxHighlight class`() {
        val abstractRenderer = object : SyntaxHighlightRenderer() {
            override fun render(node: Node) {}
        }
        
        val nodeTypes = abstractRenderer.nodeTypes
        assert(nodeTypes.contains(SyntaxHighlight::class.java))
        assert(nodeTypes.size == 1)
    }

    @Test
    fun `test SyntaxHighlightNodeRenderer creation`() {
        assert(renderer is SyntaxHighlightRenderer)
        assert(renderer is SyntaxHighlightNodeRenderer)
    }

    @Test
    fun `test render writes delimiters around content`() {
        val mockNode = mockk<SyntaxHighlight>(relaxed = true)
        val mockChildNode = mockk<Node>(relaxed = true)
        
        every { mockNode.firstChild } returns mockChildNode
        every { mockChildNode.next } returns null
        every { mockWriter.write(any<Char>()) } just Runs
        every { mockContext.render(any()) } just Runs

        renderer.render(mockNode)

        verify { mockWriter.write('/') }
        verify(exactly = 2) { mockWriter.write('/') } // Opening and closing
        verify { mockContext.render(mockChildNode) }
    }

    @Test
    fun `test render with multiple child nodes`() {
        val mockNode = mockk<SyntaxHighlight>(relaxed = true)
        val mockChild1 = mockk<Node>(relaxed = true)
        val mockChild2 = mockk<Node>(relaxed = true)
        val mockChild3 = mockk<Node>(relaxed = true)
        
        every { mockNode.firstChild } returns mockChild1
        every { mockChild1.next } returns mockChild2
        every { mockChild2.next } returns mockChild3
        every { mockChild3.next } returns null
        every { mockWriter.write(any<Char>()) } just Runs
        every { mockContext.render(any()) } just Runs

        renderer.render(mockNode)

        verify { mockWriter.write('/') }
        verify(exactly = 2) { mockWriter.write('/') } // Opening and closing
        verify { mockContext.render(mockChild1) }
        verify { mockContext.render(mockChild2) }
        verify { mockContext.render(mockChild3) }
    }

    @Test
    fun `test render with no child nodes`() {
        val mockNode = mockk<SyntaxHighlight>(relaxed = true)
        
        every { mockNode.firstChild } returns null
        every { mockWriter.write(any<Char>()) } just Runs

        renderer.render(mockNode)

        verify { mockWriter.write('/') }
        verify(exactly = 2) { mockWriter.write('/') } // Opening and closing
        verify(exactly = 0) { mockContext.render(any()) }
    }

    @Test
    fun `test render handles context rendering correctly`() {
        val mockNode = mockk<SyntaxHighlight>(relaxed = true)
        val mockChildNode = mockk<Node>(relaxed = true)
        
        every { mockNode.firstChild } returns mockChildNode
        every { mockChildNode.next } returns null
        every { mockWriter.write(any<Char>()) } just Runs
        every { mockContext.render(mockChildNode) } just Runs

        renderer.render(mockNode)

        verify { mockContext.render(mockChildNode) }
    }

    @Test
    fun `test abstract SyntaxHighlightRenderer interface`() {
        val customRenderer = object : SyntaxHighlightRenderer() {
            override fun render(node: Node) {
            }
        }

        val nodeTypes = customRenderer.nodeTypes
        assert(nodeTypes.contains(SyntaxHighlight::class.java))
        assert(nodeTypes.size == 1)
    }

    @Test
    fun `test SyntaxHighlightNodeRenderer context property`() {
        val newRenderer = SyntaxHighlightNodeRenderer(mockContext)
        
        val mockNode = mockk<SyntaxHighlight>(relaxed = true)
        every { mockNode.firstChild } returns null
        every { mockWriter.write(any<Char>()) } just Runs

        newRenderer.render(mockNode)

        verify { mockWriter.write('/') }
    }
}
