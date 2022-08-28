import java.nio.charset.Charset
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version Dependency.Kotlin.Version
    id("io.papermc.paperweight.userdev") version "1.3.8"
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
    paperDevBundle("1.19.2-R0.1-SNAPSHOT")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "17"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "17"
}

java {
    toolchain.apply {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

abstract class SetupTask : DefaultTask() {
    private var isIgnored: Boolean = true
    private var multiModule: Boolean = false

    @Option(option = "multiModule", description = "")
    fun setMulti(multiModule: String) {
        this.multiModule = multiModule.toBoolean()
        isIgnored = false
    }

    @TaskAction
    fun process() {
        if (!this.isIgnored) {
            if (this.multiModule) {
                File("./src").deleteRecursively()
            } else {
                File("./backend").deleteRecursively()
                File("./frontend").deleteRecursively()
                File("./settings.gradle.kts").writeText("", Charset.forName("utf8"))
            }
        }
    }
}

val pluginName = rootProject.name.capitalize()
val packageName = rootProject.name

val aliasName = packageName

extra.apply {
    set("pluginName", pluginName)
    set("packageName", packageName)
    set("aliasName", aliasName)

    set("kotlinVersion", Dependency.Kotlin.Version)
}

tasks {
    register<SetupTask>("setupWorkspace")

    processResources {
        outputs.upToDateWhen { false }
        filesMatching("*.yml") {
            expand(project.properties)
            expand(extra.properties)
        }
    }

    register<Jar>("pluginJar") {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")

        from(sourceSets["main"].output)
    }
}
