plugins {
  // Apply the application plugin to add support for building a CLI application in Java.
  application
}

repositories {
  // Use Maven Central for resolving dependencies.
  mavenCentral()
}

dependencies {
  // JUnit Jupiter.
  testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
  testImplementation("com.google.truth:truth:1.4.3")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
  testImplementation("org.mockito:mockito-core:5.11.0")
  // Moshi JSON
  implementation("com.squareup.moshi:moshi:1.12.0")
  implementation("com.squareup.moshi:moshi-adapters:1.12.0")
  implementation("com.squareup.moshi:moshi-kotlin:1.12.0")

  implementation ("org.json:json:20240303")

}

application {

  mainClass.set("controller.server.GameServer")
}

tasks.register<JavaExec>("runGui") {
  mainClass.set("view.GUI")
  classpath = sourceSets.main.get().runtimeClasspath
  standardInput = System.`in`
  args = listOf("optional", "arguments", "here")
}

tasks.register<JavaExec>("runServer") {
  mainClass.set("controller.server.GameServer")
  classpath = sourceSets.main.get().runtimeClasspath
  standardInput = System.`in`
  args = listOf("optional", "server", "arguments")
}

tasks.jar {
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  manifest {
    attributes(
            "Main-Class" to "controller.server.GameServer"
    )
  }
  from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

}


tasks.named<JavaExec>("run") {
  standardInput = System.`in`
  enableAssertions = true
}

tasks.named<Test>("test") {
  // Use JUnit Platform for unit tests.
  useJUnitPlatform()
}

