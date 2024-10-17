package dev.jeziellago.compose.markdowntext

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.text.Spanned
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import coil.ImageLoader
import coil.imageLoader
import dev.jeziellago.compose.markdowntext.plugins.core.MardownCorePlugin
import dev.jeziellago.compose.markdowntext.plugins.syntaxhighlight.SyntaxHighlightPlugin
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.SoftBreakAddsNewLinePlugin
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import io.noties.markwon.linkify.LinkifyPlugin

internal object MarkdownRender {

    fun create(
        context: Context,
        imageLoader: ImageLoader?,
        linkifyMask: Int,
        textSizePx: Float,
        textColor: Color,
        backgroundColor: Color,
        enableSoftBreakAddsNewLine: Boolean,
        syntaxHighlightColor: Color,
        syntaxHighlightTextColor: Color,
        headingBreakColor: Color,
        beforeSetMarkdown: ((TextView, Spanned) -> Unit)? = null,
        afterSetMarkdown: ((TextView) -> Unit)? = null,
        onLinkClicked: ((String) -> Unit)? = null,
    ): Markwon {
        val coilImageLoader = imageLoader ?: context.imageLoader
        return Markwon.builderNoCore(context)
            .usePlugin(
                MardownCorePlugin(
                    syntaxHighlightColor.toArgb(),
                    syntaxHighlightTextColor.toArgb(),
                )
            )
            .usePlugin(HtmlPlugin.create())
            .usePlugin(CoilImagesPlugin.create(context, coilImageLoader))
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(TablePlugin.create(context))
            .usePlugin(LinkifyPlugin.create(linkifyMask))
            .usePlugin(TaskListPlugin.create(context))
            .usePlugin(MarkwonInlineParserPlugin.create())
            .usePlugin(JLatexMathPlugin.create(textSizePx) { builder ->
                builder.inlinesEnabled(true)
                builder.theme().backgroundProvider { ColorDrawable(backgroundColor.toArgb()) }

                // text color of LaTeX content for both inlines and blocks
                //  or more specific: `inlineTextColor` & `blockTextColor`
                builder.theme().textColor(textColor.toArgb());

//                // should block fit the whole canvas width, by default true
//                builder.theme().blockFitCanvas(true);

//                // horizontal alignment for block, by default ALIGN_CENTER
//                builder.theme().blockHorizontalAlignment(JLatexMathDrawable.ALIGN_CENTER);

//                // padding for both inlines and blocks
//                builder.theme().padding(JLatexMathTheme.Padding.all(8));
//
//                // padding for inlines
//                builder.theme().inlinePadding(JLatexMathTheme.Padding.symmetric(16, 8));
//
//                // padding for blocks
//                builder.theme().blockPadding(new JLatexMathTheme . Padding (0, 1, 2, 3));


            })
            .apply {
                if (enableSoftBreakAddsNewLine) {
                    usePlugin(SoftBreakAddsNewLinePlugin.create())
                }
            }
            .usePlugin(SyntaxHighlightPlugin())
            .usePlugin(object : AbstractMarkwonPlugin() {

                override fun beforeSetText(textView: TextView, markdown: Spanned) {
                    beforeSetMarkdown?.invoke(textView, markdown)
                }

                override fun afterSetText(textView: TextView) {
                    afterSetMarkdown?.invoke(textView)
                }

                override fun configureTheme(builder: MarkwonTheme.Builder) {
                    if (headingBreakColor == Color.Transparent) {
                        builder.headingBreakColor(1)
                    } else {
                        builder.headingBreakColor(headingBreakColor.toArgb())
                    }
                }

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
}
