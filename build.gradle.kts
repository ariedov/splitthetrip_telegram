plugins {
    application
    kotlin("jvm") version "1.3.30"
}

application {
    mainClassName = "com.dleibovych.splitthetrip.MainKt"
    group = "com.dleibovych"
    version = "1.0-SNAPSHOT"
}

dependencies {
    compile(kotlin("stdlib"))
}

repositories {
    jcenter()
}