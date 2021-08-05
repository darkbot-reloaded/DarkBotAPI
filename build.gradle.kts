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
        withJavadocJar()
        withSourcesJar()
    }
}


description = "darkbot-common"

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

