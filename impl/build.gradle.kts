description = "darkbot-impl"

dependencies {
    implementation(project(":darkbot-util"))
    implementation(project(":darkbot-api"))
    implementation(project(":darkbot-shared"))
}

tasks.jar {
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

val allSources = configurations.runtimeClasspath.get().incoming.dependencies.map {
    rootProject.project(it.name).sourceSets["main"].allJava
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible)
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)

    source(allSources)
}

tasks.named<Jar>("sourcesJar") {
    from(allSources)
}