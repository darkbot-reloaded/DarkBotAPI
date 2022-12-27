description = "darkbot-impl"

dependencies {
    api(project(":darkbot-util"))
    api(project(":darkbot-api"))
    api(project(":darkbot-shared"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
    testImplementation("org.mockito:mockito-core:4.10.0")
}