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

    var isIgnored: Boolean = false
    var multiSetup: Boolean = false
    @Option(option = "multiModuled", description = "")
    fun setMulti(multiModuled: String) {
        multiSetup = multiModuled.toBoolean()
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