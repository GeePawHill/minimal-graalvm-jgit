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
            imageName = project.name
            mainClass = "org.example.Main"
            debug = true
            verbose = true
            fallback = false

            //packages/classes to be initialized at native image build time
            val buildTimeInitClasses = listOf(
                    "org.eclipse.jgit",
                    "org.slf4j",
            )

            //packages/classes to be initialized at native image run time
            val runTimeInitClasses = listOf(
                    "org.eclipse.jgit.internal.storage.file.WindowCache",
                    "org.eclipse.jgit.lib.internal.WorkQueue",
                    "org.eclipse.jgit.lib.RepositoryCache",
                    "org.eclipse.jgit.transport.HttpAuthMethod",
            )

            //packages/classes to be re-initialized at native image run time
            val runTimeReInitClasses = listOf(
                    //all org.eclipse.jgit classes are initialized at build time
                    //(specified above), but due to SecureRandom seeding
                    //in their static initialization blocks, some JGit classes need be
                    //re-initialized at native image run time:
                    "org.eclipse.jgit.util.FileUtils:rerun",
            )

            buildArgs.add("--enable-url-protocols=http,https")
            buildArgs.add("--initialize-at-build-time=" + buildTimeInitClasses.joinToString(","))
            buildArgs.add("--initialize-at-run-time=" + runTimeInitClasses.joinToString(","))
            buildArgs.add("-H:ClassInitialization=" + runTimeReInitClasses.joinToString(","))
        }
    }
}

