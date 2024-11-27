pluginManagement {
    plugins {
        // Gradle is awkward about declaring versions for plugins
        // Specifying it here, rather than build.gradle.kts, makes it possible
        // to include the extension as a subproject of QuPath itself
        // (which is useful during development)
        id("org.bytedeco.gradle-javacpp-platform") version "1.5.9"
    }
}

// TODO: Name your QuPath extension here!
rootProject.name = "my-qupath-extension"

// TODO: Define the QuPath version compatible with the extension
// Note that the QuPath API isn't stable; something designed for
// 0.X.a should work with 0.X.b, but not necessarily with 0.Y.a.
var qupathVersion = "0.6.0-SNAPSHOT"
gradle.extra["qupath.app.version"] = qupathVersion

dependencyResolutionManagement {

    // Access QuPath's version catalog for dependency versions
    versionCatalogs {
        create("libs") {
            from("io.github.qupath:qupath-catalog:$qupathVersion")
        }
    }

    repositories {

        mavenCentral()

        // Add scijava - which is where QuPath's jars are hosted
        maven {
            url = uri("https://maven.scijava.org/content/repositories/releases")
        }

        maven {
            url = uri("https://maven.scijava.org/content/repositories/snapshots")
        }

    }
}
