plugins {
    id("java-library")
    id("maven-publish")
    id("com.diffplug.spotless") version "6.12.1"
}

val apiVersion = "0.7.0"

allprojects {
    group = "eu.darkbot"
    version = apiVersion

    apply(plugin = "java-library")
    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        withSourcesJar()
        withJavadocJar()
    }

    apply(plugin = "com.diffplug.spotless")
    spotless {
        format("misc") {
            // define the files to apply `misc` to
            target("*.gradle", "*.md", ".gitignore")

            // define the steps to apply to those files
            trimTrailingWhitespace()
            indentWithTabs() // or spaces. Takes an integer argument if you don't like 4
            endWithNewline()
        }
        java {
            importOrder("", "javax|java", "\\#")
            removeUnusedImports()
            formatAnnotations()
            indentWithSpaces(4)
            trimTrailingWhitespace()
            endWithNewline()
            toggleOffOn()

            custom("noWildcardImports") {str ->
                for (line in str.split("\n").drop(1)) {
                    // No longer reading imports, break early
                    if (!line.startsWith("import") && line.isNotBlank()) break

                    if (!line.startsWith("import static") // Static imports allow wildcards
                            && !line.contains(Regex(" javax?.(awt|swing).")) // awt/swing allow wildcards
                            && line.endsWith(".*;")) { // Prevent any other wildcard
                        throw Exception("No wildcard imports allowed")
                    }
                }
                str;
            }
        }
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
        compileOnly("org.jetbrains:annotations:23.1.0")
    }

    val javadocOpts = tasks.javadoc.get().options as StandardJavadocDocletOptions
    javadocOpts.addStringOption("Xdoclint:none", "-quiet") // no warnings about missing docs

    tasks.withType(JavaCompile::class) {
        options.isDeprecation = false // disable deprecation warnings
        dependsOn(tasks.spotlessCheck)
    }

}

description = "darkbot-common"

dependencies {
    api(project(":darkbot-util"))
    api(project(":darkbot-api"))
    api(project(":darkbot-shared"))
}

tasks.wrapper {
    gradleVersion = "7.5.1"

    // without gradle javadocs and sources
    distributionType = Wrapper.DistributionType.BIN
}