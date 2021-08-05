plugins {
    `maven-publish`
}

description = "darkbot-impl"

dependencies {
    compileOnly("org.jetbrains:annotations:21.0.1")
    implementation(project(":darkbot-util"))
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