plugins {
    id("java-library")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    compileOnly("com.zaxxer:HikariCP:7.0.2")
    compileOnly("com.h2database:h2:2.4.240")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}
