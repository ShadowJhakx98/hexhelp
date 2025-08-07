plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.hexvencraft"
version = "1.1" // Updated version

repositories {
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.7-R0.1-SNAPSHOT")

    // New dependencies for AI integration
    implementation("com.google.api-client:google-api-client:2.2.0")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks {
    shadowJar {
        archiveBaseName.set("HexHelp")
        archiveClassifier.set("")
        archiveVersion.set(project.version.toString())

        // Relocate the new libraries to prevent conflicts with other plugins
        relocate("com.google", "com.hexvencraft.hexhelp.libs.google")
    }
}
