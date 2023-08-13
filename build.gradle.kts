plugins {
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("java")
}

application.mainClass = "com.zcalahan.bajhelper.bot" //

group = "com.zcalahan"
version = "1.0"

val jdaVersion = "5.0.0-beta.13" //

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("ch.qos.logback:logback-classic:1.2.9")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isIncremental = true

    // Set this to the version of java you want to use,
    // the minimum required for JDA is 1.8
    sourceCompatibility = "1.8"
}