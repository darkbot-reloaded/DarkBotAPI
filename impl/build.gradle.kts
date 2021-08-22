description = "darkbot-impl"

dependencies {
    compileOnly("org.jetbrains:annotations:22.0.0")
    implementation(project(":darkbot-util"))
    implementation(project(":darkbot-api"))
    implementation(project(":darkbot-shared"))
}

tasks.jar {
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
