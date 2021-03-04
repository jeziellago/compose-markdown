package dev.jeziellago.compose.markdown

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.jeziellago.compose.markdowntext.MarkdownText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleMarkdown()
        }
    }

    @Composable
    fun SampleMarkdown() {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 10.dp
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {

                MarkdownText(markdown = markdownContent)
            }
        }
    }

    val markdownContent = "# Android Jetpack\n" +
            "\n" +
            "Jetpack is a suite of libraries, tools, and guidance to help developers write high-quality apps easier. These components help you follow best practices, free you from writing boilerplate code, and simplify complex tasks, so you can focus on the code you care about.\n" +
            "\n" +
            "Jetpack comprises the `androidx.*` package libraries, unbundled from the platform APIs. This means that it offers backward compatibility and is updated more frequently than the Android platform, making sure you always have access to the latest and greatest versions of the Jetpack components.\n" +
            "\n" +
            "Our official AARs and JARs binaries are distributed through [Google Maven](https://maven.google.com).\n" +
            "\n" +
            "You can learn more about using it from [Android Jetpack landing page](https://developer.android.com/jetpack).\n" +
            "\n" +
            "# Contribution Guide\n" +
            "\n" +
            "For contributions via GitHub, see the [GitHub Contribution Guide](CONTRIBUTING.md).\n" +
            "\n" +
            "Note: The contributions workflow via GitHub is currently experimental - only contributions to the following projects are being accepted at this time:\n" +
            "* [Activity](activity)\n" +
            "* [Biometric](biometric)\n" +
            "* [Compose Compiler](compose/compiler)\n" +
            "* [Fragment](fragment)\n" +
            "* [Lifecycle](lifecycle)\n" +
            "* [Navigation](navigation)\n" +
            "* [Paging](paging)\n" +
            "* [Room](room)\n" +
            "* [WorkManager](work)\n" +
            "\n" +
            "## Code Review Etiquette\n" +
            "When contributing to Jetpack, follow the [code review etiquette](code-review.md).\n" +
            "\n" +
            "## Accepted Types of Contributions\n" +
            "* Bug fixes - needs a corresponding bug report in the [Android Issue Tracker](https://issuetracker.google.com/issues/new?component=192731&template=842428)\n" +
            "* Each bug fix is expected to come with tests\n" +
            "* Fixing spelling errors\n" +
            "* Updating documentation\n" +
            "* Adding new tests to the area that is not currently covered by tests\n" +
            "* New features to existing libraries if the feature request bug has been approved by an AndroidX team member.\n" +
            "\n" +
            "We **are not** currently accepting new modules.\n" +
            "\n" +
            "## Checking Out the Code\n" +
            "**NOTE: You will need to use Linux or Mac OS. Building under Windows is not currently supported.**\n" +
            "\n" +
            "1. Install `repo` (Repo is a tool that makes it easier to work with Git in the context of Android. For more information about Repo, see the [Repo Command Reference](https://source.android.com/setup/develop/repo))\n" +
            "\n" +
            "```bash\n" +
            "mkdir ~/bin\n" +
            "PATH=~/bin:\$PATH\n" +
            "curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo\n" +
            "chmod a+x ~/bin/repo\n" +
            "```\n" +
            "\n" +
            "2. Configure Git with your real name and email address.\n" +
            "\n" +
            "```bash\n" +
            "git config --global user.name \"Your Name\"\n" +
            "git config --global user.email \"you@example.com\"\n" +
            "```\n" +
            "\n" +
            "3. Create a directory for your checkout (it can be any name)\n" +
            "\n" +
            "```bash\n" +
            "mkdir androidx-main\n" +
            "cd androidx-main\n" +
            "```\n" +
            "\n" +
            "4. Use `repo` command to initialize the repository.\n" +
            "\n" +
            "```bash\n" +
            "repo init -u https://android.googlesource.com/platform/manifest -b androidx-main --partial-clone --clone-filter=blob:limit=10M\n" +
            "```\n" +
            "\n" +
            "5. Now your repository is set to pull only what you need for building and running AndroidX libraries. Download the code (and grab a coffee while we pull down the files):\n" +
            "\n" +
            "```bash\n" +
            "repo sync -j8 -c\n" +
            "```\n" +
            "\n" +
            "You will use this command to sync your checkout in the future - it’s similar to `git fetch`\n" +
            "\n" +
            "\n" +
            "## Using Android Studio\n" +
            "To open the project with the specific version of Android Studio recommended for developing:\n" +
            "\n" +
            "```bash\n" +
            "cd path/to/checkout/frameworks/support/\n" +
            "ANDROIDX_PROJECTS=MAIN ./gradlew studio\n" +
            "```\n" +
            "\n" +
            "and accept the license agreement when prompted. Now you're ready to edit, run, and test!\n" +
            "\n" +
            "You can also the following sets of projects: `ALL`, `MAIN`, `COMPOSE`, or `FLAN`\n" +
            "\n" +
            "If you get “Unregistered VCS root detected” click “Add root” to enable git integration for Android Studio.\n" +
            "\n" +
            "If you see any warnings (red underlines) run `Build > Clean Project`.\n" +
            "\n" +
            "## Builds\n" +
            "### Full Build (Optional)\n" +
            "You can do most of your work from Android Studio, however you can also build the full AndroidX library from command line:\n" +
            "\n" +
            "```bash\n" +
            "cd path/to/checkout/frameworks/support/\n" +
            "./gradlew createArchive\n" +
            "```\n" +
            "\n" +
            "### Testing modified AndroidX Libraries to in your App\n" +
            "You can build maven artifacts locally, and test them directly in your app:\n" +
            "\n" +
            "```bash\n" +
            "./gradlew createArchive\n" +
            "```\n" +
            "\n" +
            "And put the following at the top of your 'repositories' property in your **project** `build.gradle` file:\n" +
            "\n" +
            "```gradle\n" +
            "maven { url '/path/to/checkout/out/androidx/build/support_repo/' }\n" +
            "```\n" +
            "\n" +
            "### Continuous integration\n" +
            "[Our continuous integration system](https://ci.android.com/builds/branches/aosp-androidx-main/grid?) builds all in progress (and potentially unstable) libraries as new changes are merged. You can manually download these AARs and JARs for your experimentation.\n" +
            "\n" +
            "## Running Tests\n" +
            "\n" +
            "### Single Test Class or Method\n" +
            "1. Open the desired test file in Android Studio.\n" +
            "2. Right-click on a test class or @Test method name and select `Run FooBarTest`\n" +
            "\n" +
            "### Full Test Package\n" +
            "1. In the project side panel open the desired module.\n" +
            "2. Find the directory with the tests\n" +
            "3. Right-click on the directory and select `Run androidx.foobar`\n" +
            "\n" +
            "## Running Sample Apps\n" +
            "The AndroidX repository has a set of Android applications that exercise AndroidX code. These applications can be useful when you want to debug a real running application, or reproduce a problem interactively, before writing test code.\n" +
            "\n" +
            "These applications are named either `<libraryname>-integration-tests-testapp`, or `support-\\*-demos` (e.g. `support-v4-demos` or `support-leanback-demos`). You can run them by clicking `Run > Run ...` and choosing the desired application.\n" +
            "\n" +
            "## Password and Contributor Agreement before making a change\n" +
            "Before uploading your first contribution, you will need setup a password and agree to the contribution agreement:\n" +
            "\n" +
            "Generate a HTTPS password:\n" +
            "https://android-review.googlesource.com/new-password\n" +
            "\n" +
            "Agree to the Google Contributor Licenses Agreement:\n" +
            "https://android-review.googlesource.com/settings/new-agreement\n" +
            "\n" +
            "## Making a change\n" +
            "```bash\n" +
            "cd path/to/checkout/frameworks/support/\n" +
            "repo start my_branch_name .\n" +
            "# make needed modifications...\n" +
            "git commit -a\n" +
            "repo upload --current-branch .\n" +
            "```\n" +
            "\n" +
            "If you see the following prompt, choose `always`:\n" +
            "\n" +
            "```\n" +
            "Run hook scripts from https://android.googlesource.com/platform/manifest (yes/always/NO)?\n" +
            "```\n" +
            "\n" +
            "If the upload succeeds, you'll see output like:\n" +
            "\n" +
            "```\n" +
            "remote:\n" +
            "remote: New Changes:\n" +
            "remote:   https://android-review.googlesource.com/c/platform/frameworks/support/+/720062 Further README updates\n" +
            "remote:\n" +
            "```\n" +
            "\n" +
            "To edit your change, use `git commit --amend`, and re-upload.\n" +
            "\n" +
            "## Getting reviewed\n" +
            "* After you run repo upload, open [r.android.com](http://r.android.com)\n" +
            "* Sign in into your account (or create one if you do not have one yet)\n" +
            "* Add an appropriate reviewer (use git log to find who did most modifications on the file you are fixing or check the OWNERS file in the project's directory)\n" +
            "\n" +
            "## Handling binary dependencies\n" +
            "AndroidX uses git to store all the binary Gradle dependencies. They are stored in `prebuilts/androidx/internal` and `prebuilts/androidx/external` directories in your checkout. All the dependencies in these directories are also available from `google()`, `jcenter()`, or `mavenCentral()`. We store copies of these dependencies to have hermetic builds. You can pull in [a new dependency using our importMaven tool](development/importMaven/README.md)."
}