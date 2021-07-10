plugins {
  kotlin("js") version "1.5.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib-js"))
  implementation("org.jetbrains.kotlin-wrappers:kotlin-extensions:1.0.1-pre.214-kotlin-1.5.20")

}

kotlin {
  js {
    browser {
      webpackTask {
        cssSupport.enabled = true
      }

      runTask {
        cssSupport.enabled = true
      }
    }
    binaries.executable()
  }
}
