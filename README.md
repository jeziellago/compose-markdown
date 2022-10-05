# MarkdownText - Jetpack Compose
[![](https://androidweekly.net/issues/issue-456/badge)](https://androidweekly.net/issues/issue-456)
[![Android CI](https://github.com/jeziellago/compose-markdown/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/jeziellago/compose-markdown/actions/workflows/ci.yml) [![](https://jitpack.io/v/jeziellago/compose-markdown.svg)](https://jitpack.io/#jeziellago/compose-markdown)

[![Video]()](https://user-images.githubusercontent.com/8452419/127241077-7ccca03b-9718-40a3-aacd-1629f3e40a10.mp4)
- Markdown
- HTML
- Image
- Highlight
- Linkfy
- Table
## Setup
1. Open the file `settings.gradle` (it looks like that)
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // add jitpack here 👇🏽
        maven { url 'https://jitpack.io' }
       ...
    }
} 
...
```
2. Sync the project
3. Add `compose-markdown` dependency
```groovy
dependencies {
        implementation 'com.github.jeziellago:compose-markdown:0.3.1'
}
```

# Supported attributes

Most of the attributes of that a default `Text` composable has are also supported by `MarkdownText`. 

- color 
- fontSize
- textAlign
- maxLines
- style (only styling for supported attributes is applied)

The font can be changed by passing a font xml resource as `fontResource` parameter. 

## How to use
```kotlin  
val markdown = """  
	# Sample  
	* Markdown  
	* [Link](https://example.com)  
	![Image](https://example.com/img.png)  
	<a href="https://www.google.com/">Google</a>  
"""

//Minimal example
@Composable  
fun MinimalExampleContent() {  
    MarkdownText(markdown = markdownContent)  
} 

//Complex example
@Composable  
fun ComplexExampleContent() {  
     MarkdownText(
                modifier = Modifier.padding(8.dp),
                markdown = markdown,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = LocalContentColor.current,
                maxLines = 3,
                fontResource = R.font.montserrat_medium,
                style = MaterialTheme.typography.overline,
              
     )  
}  
```  

# License
```  
Copyright (c) 2021 Jeziel Lago  
  
Permission is hereby granted, free of charge, to any person obtaining  
a copy of this software and associated documentation files (the  
"Software"), to deal in the Software without restriction, including  
without limitation the rights to use, copy, modify, merge, publish,  
distribute, sublicense, and/or sell copies of the Software, and to  
permit persons to whom the Software is furnished to do so, subject to  
the following conditions:  
  
The above copyright notice and this permission notice shall be  
included in all copies or substantial portions of the Software.  
  
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,  
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF  
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND  
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE  
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION  
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION  
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.  
```
