plugins {
    java
    `maven-publish`
}

allprojects {
    group = findProperty("api_group") as String
    version = findProperty("api_version") as String

    apply(plugin = "java")
    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        withSourcesJar()
        withJavadocJar()
    }

    apply(plugin = "maven-publish")
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
    }

    dependencies {
        compileOnly("org.jetbrains:annotations:22.0.0")
    }

    val javadocOpts = tasks.javadoc.get().options as StandardJavadocDocletOptions
    javadocOpts.addStringOption("Xdoclint:none", "-quiet") //no warnings about missing docs

    tasks.withType(JavaCompile::class) {
        options.isDeprecation = false // disable deprecation warnings
    }
}

description = "darkbot-common"

dependencies {
    implementation(project(":darkbot-util"))
    implementation(project(":darkbot-api"))
    implementation(project(":darkbot-shared"))
}

tasks.jar {
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

val allSources = configurations.runtimeClasspath.get().incoming.dependencies.map {
    project(it.name).sourceSets["main"].allJava
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible)
            (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)

    source(allSources)
}

tasks.named<Jar>("sourcesJar") {
    from(allSources)
}
