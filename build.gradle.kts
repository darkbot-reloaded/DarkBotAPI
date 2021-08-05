plugins {
    java
    `maven-publish`
}

subprojects {
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
                artifact(tasks.jar)
                artifact(tasks["sourcesJar"])
                artifact(tasks["javadocJar"])
            }
        }
    }
}

group = findProperty("api_group") as String
description = "darkbot-common"
version = findProperty("api_version") as String

dependencies {
    implementation(project(":darkbot-api"))
    implementation(project(":darkbot-shared"))
}

tasks.jar {
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks.jar)
        }
    }
}

