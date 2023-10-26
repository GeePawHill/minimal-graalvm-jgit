plugins {
    id("java")
    id("application")
    id("org.graalvm.buildtools.native") version "0.9.27"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.eclipse.jgit:org.eclipse.jgit:6.5.0.202303070854-r")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("org.example.Main")
}

graalvmNative {
    metadataRepository {
        enabled = true
        version = "0.3.4"
    }

    binaries {
        named("main") {
            // Main options
            imageName.set("nitpick") // The name of the native image, defaults to the project name
            mainClass.set("org.example.Main")
        }
    }
}