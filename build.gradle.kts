plugins {
    // Support writing the extension in Groovy (remove this if you don't want to)
    groovy
    // To optionally create a shadow/fat jar that bundle up any non-core dependencies
    id("com.gradleup.shadow") version "8.3.5"
    // QuPath Gradle extension convention plugin
    id("qupath-conventions")
}

// TODO: Configure your extension here (please change the defaults!)
qupathExtension {
    name = "qupath-extension-tileannotation"
    group = "io.github.qupath"
    version = "0.1.0-SNAPSHOT"
    description = "A QuPath extension for tile level annotation"
    automaticModule = "io.github.qupath.extension.tileannotation"
}

// TODO: Define your dependencies here
dependencies {

    // Main dependencies for most QuPath extensions
    shadow(libs.bundles.qupath)
    shadow(libs.bundles.logging)
    shadow(libs.qupath.fxtras)


    // For testing
    testImplementation(libs.bundles.qupath)
    testImplementation(libs.junit)

}

repositories {
    mavenCentral()
    // Autres dépôts si nécessaire
}

tasks.register<Copy>("copyJarToQuPath") {
    doNotTrackState("Copying the jar file to QuPath extensions folder does not require state tracking")
    dependsOn("shadowJar")
    from(layout.buildDirectory.dir("libs"))
    into("C:/Users/Admin/QuPath/v0.5/extensions/")
    include("qupath-extension-tileannotation-0.1.0-SNAPSHOT.jar")
}

tasks.named("build") {
    finalizedBy("copyJarToQuPath")
}