package dev.jeziellago.compose.markdown

val htmlContent = "<hr>\n" +
        "<p><strong>Advertisement \uD83D\uDE03</strong></p>\n" +
        "<ul>\n" +
        "<li><strong><a href=\"https://nodeca.github.io/pica/demo/\">pica</a></strong> - high quality and fast image\n" +
        "resize in browser.</li>\n" +
        "<li><strong><a href=\"https://github.com/nodeca/babelfish/\">babelfish</a></strong> - developer friendly\n" +
        "i18n with plurals support and easy syntax.</li>\n" +
        "</ul>\n" +
        "<p>You will like those projects!</p>\n" +
        "<hr>\n" +
        "<h1>h1 Heading \uD83D\uDE0E</h1>\n" +
        "<h2>h2 Heading</h2>\n" +
        "<h3>h3 Heading</h3>\n" +
        "<h4>h4 Heading</h4>\n" +
        "<h5>h5 Heading</h5>\n" +
        "<h6>h6 Heading</h6>\n" +
        "<h2>Horizontal Rules</h2>\n" +
        "<hr>\n" +
        "<hr>\n" +
        "<hr>\n" +
        "<h2>Typographic replacements</h2>\n" +
        "<p>Enable typographer option to see result.</p>\n" +
        "<p>© © ® ® ™ ™ § § ±</p>\n" +
        "<p>test… test… test… test?.. test!..</p>\n" +
        "<p>!!! ??? ,  – —</p>\n" +
        "<p>“Smartypants, double quotes” and ‘single quotes’</p>\n" +
        "<h2>Emphasis</h2>\n" +
        "<p><strong>This is bold text</strong></p>\n" +
        "<p><strong>This is bold text</strong></p>\n" +
        "<p><em>This is italic text</em></p>\n" +
        "<p><em>This is italic text</em></p>\n" +
        "<p><s>Strikethrough</s></p>\n" +
        "<h2>Blockquotes</h2>\n" +
        "<blockquote>\n" +
        "<p>Blockquotes can also be nested…</p>\n" +
        "<blockquote>\n" +
        "<p>…by using additional greater-than signs right next to each other…</p>\n" +
        "<blockquote>\n" +
        "<p>…or with spaces between arrows.</p>\n" +
        "</blockquote>\n" +
        "</blockquote>\n" +
        "</blockquote>\n" +
        "<h2>Lists</h2>\n" +
        "<p>Unordered</p>\n" +
        "<ul>\n" +
        "<li>Create a list by starting a line with <code>+</code>, <code>-</code>, or <code>*</code></li>\n" +
        "<li>Sub-lists are made by indenting 2 spaces:\n" +
        "<ul>\n" +
        "<li>Marker character change forces new list start:\n" +
        "<ul>\n" +
        "<li>Ac tristique libero volutpat at</li>\n" +
        "</ul>\n" +
        "<ul>\n" +
        "<li>Facilisis in pretium nisl aliquet</li>\n" +
        "</ul>\n" +
        "<ul>\n" +
        "<li>Nulla volutpat aliquam velit</li>\n" +
        "</ul>\n" +
        "</li>\n" +
        "</ul>\n" +
        "</li>\n" +
        "<li>Very easy!</li>\n" +
        "</ul>\n" +
        "<p>Ordered</p>\n" +
        "<ol>\n" +
        "<li>\n" +
        "<p>Lorem ipsum dolor sit amet</p>\n" +
        "</li>\n" +
        "<li>\n" +
        "<p>Consectetur adipiscing elit</p>\n" +
        "</li>\n" +
        "<li>\n" +
        "<p>Integer molestie lorem at massa</p>\n" +
        "</li>\n" +
        "<li>\n" +
        "<p>You can use sequential numbers…</p>\n" +
        "</li>\n" +
        "<li>\n" +
        "<p>…or keep all the numbers as <code>1.</code></p>\n" +
        "</li>\n" +
        "</ol>\n" +
        "<p>Start numbering with offset:</p>\n" +
        "<ol start=\"57\">\n" +
        "<li>foo</li>\n" +
        "<li>bar</li>\n" +
        "</ol>\n" +
        "<h2>Code</h2>\n" +
        "<p>Inline <code>code</code></p>\n" +
        "<p>Indented code</p>\n" +
        "<pre><code>// Some comments\n" +
        "line 1 of code\n" +
        "line 2 of code\n" +
        "line 3 of code\n" +
        "</code></pre>\n" +
        "<p>Block code “fences”</p>\n" +
        "<pre class=\"hljs\"><code>Sample text here...\n" +
        "</code></pre>\n" +
        "<p>Syntax highlighting</p>\n" +
        "<pre class=\"hljs language-js\"><code><span class=\"hljs-keyword\">var</span> foo = <span class=\"hljs-function\"><span class=\"hljs-keyword\">function</span> (<span class=\"hljs-params\">bar</span>) </span>{\n" +
        "  <span class=\"hljs-keyword\">return</span> bar++;\n" +
        "};\n" +
        "\n" +
        "<span class=\"hljs-built_in\">console</span>.log(foo(<span class=\"hljs-number\">5</span>));\n" +
        "</code></pre>\n" +
        "<h2>Tables</h2>\n" +
        "<table>\n" +
        "<thead>\n" +
        "<tr>\n" +
        "<th>Option</th>\n" +
        "<th>Description</th>\n" +
        "</tr>\n" +
        "</thead>\n" +
        "<tbody>\n" +
        "<tr>\n" +
        "<td>data</td>\n" +
        "<td>path to data files to supply the data that will be passed into templates.</td>\n" +
        "</tr>\n" +
        "<tr>\n" +
        "<td>engine</td>\n" +
        "<td>engine to be used for processing templates. Handlebars is the default.</td>\n" +
        "</tr>\n" +
        "<tr>\n" +
        "<td>ext</td>\n" +
        "<td>extension to be used for dest files.</td>\n" +
        "</tr>\n" +
        "</tbody>\n" +
        "</table>\n" +
        "<p>Right aligned columns</p>\n" +
        "<table>\n" +
        "<thead>\n" +
        "<tr>\n" +
        "<th style=\"text-align:right\">Option</th>\n" +
        "<th style=\"text-align:right\">Description</th>\n" +
        "</tr>\n" +
        "</thead>\n" +
        "<tbody>\n" +
        "<tr>\n" +
        "<td style=\"text-align:right\">data</td>\n" +
        "<td style=\"text-align:right\">path to data files to supply the data that will be passed into templates.</td>\n" +
        "</tr>\n" +
        "<tr>\n" +
        "<td style=\"text-align:right\">engine</td>\n" +
        "<td style=\"text-align:right\">engine to be used for processing templates. Handlebars is the default.</td>\n" +
        "</tr>\n" +
        "<tr>\n" +
        "<td style=\"text-align:right\">ext</td>\n" +
        "<td style=\"text-align:right\">extension to be used for dest files.</td>\n" +
        "</tr>\n" +
        "</tbody>\n" +
        "</table>\n" +
        "<h2>Links</h2>\n" +
        "<p><a href=\"http://dev.nodeca.com\">link text</a></p>\n" +
        "<p><a href=\"http://nodeca.github.io/pica/demo/\" title=\"title text!\">link with title</a></p>\n" +
        "<p>Autoconverted link <a href=\"https://github.com/nodeca/pica\">https://github.com/nodeca/pica</a> (enable linkify to see)</p>\n" +
        "<h2>Images</h2>\n" +
        "<p><img src=\"https://octodex.github.com/images/minion.png\" alt=\"Minion\">\n" +
        "<img src=\"https://octodex.github.com/images/stormtroopocat.jpg\" alt=\"Stormtroopocat\" title=\"The Stormtroopocat\"></p>\n" +
        "<p>Like links, Images also have a footnote style syntax</p>\n" +
        "<p><img src=\"https://octodex.github.com/images/dojocat.jpg\" alt=\"Alt text\" title=\"The Dojocat\"></p>\n" +
        "<p>With a reference later in the document defining the URL location:</p>\n" +
        "<h2>Plugins</h2>\n" +
        "<p>The killer feature of <code>markdown-it</code> is very effective support of\n" +
        "<a href=\"https://www.npmjs.org/browse/keyword/markdown-it-plugin\">syntax plugins</a>.</p>\n" +
        "<h3><a href=\"https://github.com/markdown-it/markdown-it-emoji\">Emojies</a></h3>\n" +
        "<blockquote>\n" +
        "<p>Classic markup: \uD83D\uDE09 :crush: \uD83D\uDE22 :tear: \uD83D\uDE06 \uD83D\uDE0B</p>\n" +
        "<p>Shortcuts (emoticons): \uD83D\uDE03 \uD83D\uDE26 \uD83D\uDE0E \uD83D\uDE09</p>\n" +
        "</blockquote>\n" +
        "<p>see <a href=\"https://github.com/markdown-it/markdown-it-emoji#change-output\">how to change output</a> with twemoji.</p>\n" +
        "<h3><a href=\"https://github.com/markdown-it/markdown-it-sub\">Subscript</a> / <a href=\"https://github.com/markdown-it/markdown-it-sup\">Superscript</a></h3>\n" +
        "<ul>\n" +
        "<li>19<sup>th</sup></li>\n" +
        "<li>H<sub>2</sub>O</li>\n" +
        "</ul>\n" +
        "<h3><a href=\"https://github.com/markdown-it/markdown-it-ins\">&lt;ins&gt;</a></h3>\n" +
        "<p><ins>Inserted text</ins></p>\n" +
        "<h3><a href=\"https://github.com/markdown-it/markdown-it-mark\">&lt;mark&gt;</a></h3>\n" +
        "<p><mark>Marked text</mark></p>\n" +
        "<h3><a href=\"https://github.com/markdown-it/markdown-it-footnote\">Footnotes</a></h3>\n" +
        "<p>Footnote 1 link<sup class=\"footnote-ref\"><a href=\"#fn1\" id=\"fnref1\">[1]</a></sup>.</p>\n" +
        "<p>Footnote 2 link<sup class=\"footnote-ref\"><a href=\"#fn2\" id=\"fnref2\">[2]</a></sup>.</p>\n" +
        "<p>Inline footnote<sup class=\"footnote-ref\"><a href=\"#fn3\" id=\"fnref3\">[3]</a></sup> definition.</p>\n" +
        "<p>Duplicated footnote reference<sup class=\"footnote-ref\"><a href=\"#fn2\" id=\"fnref2:1\">[2:1]</a></sup>.</p>\n" +
        "<h3><a href=\"https://github.com/markdown-it/markdown-it-deflist\">Definition lists</a></h3>\n" +
        "<dl>\n" +
        "<dt>Term 1</dt>\n" +
        "<dd>\n" +
        "<p>Definition 1\n" +
        "with lazy continuation.</p>\n" +
        "</dd>\n" +
        "<dt>Term 2 with <em>inline markup</em></dt>\n" +
        "<dd>\n" +
        "<p>Definition 2</p>\n" +
        "<pre><code>  { some code, part of Definition 2 }\n" +
        "</code></pre>\n" +
        "<p>Third paragraph of definition 2.</p>\n" +
        "</dd>\n" +
        "</dl>\n" +
        "<p><em>Compact style:</em></p>\n" +
        "<dl>\n" +
        "<dt>Term 1</dt>\n" +
        "<dd>Definition 1</dd>\n" +
        "<dt>Term 2</dt>\n" +
        "<dd>Definition 2a</dd>\n" +
        "<dd>Definition 2b</dd>\n" +
        "</dl>\n" +
        "<h3><a href=\"https://github.com/markdown-it/markdown-it-abbr\">Abbreviations</a></h3>\n" +
        "<p>This is <abbr title=\"Hyper Text Markup Language\">HTML</abbr> abbreviation example.</p>\n" +
        "<p>It converts “<abbr title=\"Hyper Text Markup Language\">HTML</abbr>”, but keep intact partial entries like “xxxHTMLyyy” and so on.</p>\n" +
        "<h3><a href=\"https://github.com/markdown-it/markdown-it-container\">Custom containers</a></h3>\n" +
        "<div class=\"warning\">\n" +
        "<p><em>here be dragons</em></p>\n" +
        "</div>\n" +
        "<hr class=\"footnotes-sep\">\n" +
        "<section class=\"footnotes\">\n" +
        "<ol class=\"footnotes-list\">\n" +
        "<li id=\"fn1\" class=\"footnote-item\"><p>Footnote <strong>can have markup</strong></p>\n" +
        "<p>and multiple paragraphs. <a href=\"#fnref1\" class=\"footnote-backref\">↩︎</a></p>\n" +
        "</li>\n" +
        "<li id=\"fn2\" class=\"footnote-item\"><p>Footnote text. <a href=\"#fnref2\" class=\"footnote-backref\">↩︎</a> <a href=\"#fnref2:1\" class=\"footnote-backref\">↩︎</a></p>\n" +
        "</li>\n" +
        "<li id=\"fn3\" class=\"footnote-item\"><p>Text of inline footnote <a href=\"#fnref3\" class=\"footnote-backref\">↩︎</a></p>\n" +
        "</li>\n" +
        "</ol>\n" +
        "</section>"