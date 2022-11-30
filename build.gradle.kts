plugins {
    kotlin("jvm") version "1.7.21"
    id("com.github.johnrengelman.shadow") version("7.1.2")
    application
}

group = "de.joshua"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    // Kord Snapshots Repository (Optional):
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-alpha.22")
    implementation("org.reflections:reflections:0.10.2")
    implementation("mysql:mysql-connector-java:8.0.30")
}

application {
    mainClass.set("DiscordBot")
}

tasks.withType(Jar::class) {
    manifest {
        attributes["Manifest-Version"] = "1.0"
        attributes["Main-Class"] = "de.joshua.DiscordBot"
    }

    // To avoid the duplicate handling strategy error
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // To add all of the dependencies
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
