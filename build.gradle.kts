plugins {
    `java-library`
    `maven-publish`
}

val apiVersion = "0.2.5"

allprojects {
    group = "eu.darkbot"
    version = apiVersion

    apply(plugin = "java-library")
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
        compileOnly("org.jetbrains:annotations:23.0.0")
    }

    val javadocOpts = tasks.javadoc.get().options as StandardJavadocDocletOptions
    javadocOpts.addStringOption("Xdoclint:none", "-quiet") //no warnings about missing docs

    tasks.withType(JavaCompile::class) {
        options.isDeprecation = false // disable deprecation warnings
    }
}

description = "darkbot-common"

dependencies {
    api(project(":darkbot-util"))
    api(project(":darkbot-api"))
    api(project(":darkbot-shared"))
}