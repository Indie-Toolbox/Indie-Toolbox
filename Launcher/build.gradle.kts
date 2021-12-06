plugins {
    java
}

group = "com.toolbox"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
}

val lwjglVersion = "3.3.0"
val jomlVersion = "1.10.2"

private val lwjglNatives = arrayOf(
        "linux",
        "linux-arm64",
        "linux-arm32",
        "macos",
        "macos-arm64",
        "windows",
        "windows-arm64",
        "windows-x86"
)

fun DependencyHandler.lwjgl(dependency: String) = ArrayList<Dependency>().also { dependencies ->
    if (dependency == "core") {

        dependencies += lwjglNatives.map { create("org.lwjgl:lwjgl:$lwjglVersion:natives-$it") }
        dependencies += create("org.lwjgl:lwjgl:$lwjglVersion")
        dependencies += create(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    } else "org.lwjgl:lwjgl-$dependency:$lwjglVersion".let { dependencyNotation ->

        dependencies += lwjglNatives.map { create("$dependencyNotation:natives-$it") }
        dependencies += create(dependencyNotation)

    }
}

dependencies {
    lwjgl("core").forEach(::implementation)
    lwjgl("assimp").forEach(::implementation)
    lwjgl("glfw").forEach(::implementation)
    lwjgl("openal").forEach(::implementation)
    lwjgl("opengl").forEach(::implementation)
    lwjgl("stb").forEach(::implementation)

    implementation("org.joml:joml:${jomlVersion}")
    implementation("com.googlecode.json-simple:json-simple:1.1")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.toolbox.LaunchToolbox"
    }
}

tasks.create<Jar>("fatJar") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    manifest.from(tasks.jar.get().manifest)
    archiveClassifier.set("all")
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }){
        exclude("META-INF/*.SF")
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
    }
    with(tasks.jar.get() as CopySpec)
}
