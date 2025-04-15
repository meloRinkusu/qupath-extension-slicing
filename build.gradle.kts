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
    name = "qupath-extension-template"
    group = "io.github.qupath"
    version = "0.1.0-SNAPSHOT"
    description = "A simple QuPath extension"
    automaticModule = "io.github.qupath.extension.template"
}

// TODO: Define your dependencies here
dependencies {

    // Main dependencies for most QuPath extensions
    shadow(libs.bundles.qupath)
    shadow(libs.bundles.logging)
    shadow(libs.qupath.fxtras)

    // If you aren't using Groovy, this can be removed
    shadow(libs.bundles.groovy)

    // For testing
    testImplementation(libs.bundles.qupath)
    testImplementation(libs.junit)

}

repositories {
    mavenCentral()
    // Autres dépôts si nécessaire
}

tasks.register<Copy>("copyJarToQuPath") {
    doNotTrackState("Copying the jar file to QuPath extensions folder does not require state tracking") // Désactive le tracking d’état pour cette tâche
    dependsOn("shadowJar")
    from(layout.buildDirectory.dir("libs"))
    into("C:/Users/Admin/QuPath/v0.5/extensions/")
    include("qupath-extension-template-0.1.0-SNAPSHOT.jar")
}

tasks.named("build") {
    finalizedBy("copyJarToQuPath")
}