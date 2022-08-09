import java.nio.charset.Charset

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20-Beta"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

repositories {
    mavenCentral()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "18"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "18"
}

abstract class SetupTask : DefaultTask() {

    var isIgnored: Boolean = true
    var multiSetup: Boolean = false
    @Option(option = "multiModule", description = "")
    fun setMulti(multiModule: String) {
        multiSetup = multiModule.toBoolean()
        isIgnored = false
    }
}

tasks {
    register<SetupTask>("setupWorkspace") {
        if (!this.isIgnored) {
            if (this.multiSetup) {
                File("./src").deleteRecursively()
            } else {
                File("./backend").deleteRecursively()
                File("./frontend").deleteRecursively()
                File("./settings.gradle.kts").writeText("", Charset.forName("utf8"))
            }
        }
    }
}