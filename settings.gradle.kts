rootProject.name = "darkbot-common"

include("darkbot-util", "darkbot-api", "darkbot-shared", "darkbot-impl")

project(":darkbot-util").projectDir = file("util")
project(":darkbot-api").projectDir = file("api")
project(":darkbot-shared").projectDir = file("shared")
project(":darkbot-impl").projectDir = file("impl")

dependencyResolutionManagement.repositories { mavenCentral() }