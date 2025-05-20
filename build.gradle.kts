plugins {
    kotlin("jvm") version "2.1.20"
}

group = "org.godker"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation("org.bouncycastle:bcprov-jdk18on:1.78")
    implementation("org.bouncycastle:bcpkix-jdk18on:1.78")

    implementation("org.simpleframework:simple-xml:2.7.1")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

tasks.jar.configure {
    manifest {
        attributes(mapOf("Main-Class" to "org.godker.MainKt"))
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    exclude("META-INF/*.RSA", "META-INF/*.DSA", "META-INF/*.SF")
}