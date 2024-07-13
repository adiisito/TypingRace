plugins {
  id("java")
  id("application")
  id("checkstyle")
  id("com.diffplug.spotless") version "6.25.0"
}

repositories {
  mavenCentral()
}

dependencies {
  testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
  testImplementation("com.google.truth:truth:1.4.3")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
  testImplementation("org.mockito:mockito-core:5.11.0")

  implementation("com.squareup.moshi:moshi:1.12.0")
  implementation("com.squareup.moshi:moshi-adapters:1.12.0")
  implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
  implementation("org.json:json:20240303")
}

application {
  mainClass.set("view.GUI")
}

tasks.named<JavaExec>("run") {
  standardInput = System.`in`
  enableAssertions = true
}

tasks.named<Test>("test") {
  useJUnitPlatform()
}

tasks.withType<Javadoc> {
  options {
    this as StandardJavadocDocletOptions
    addBooleanOption("Werror", true)
  }
}

checkstyle {
  toolVersion = "10.15.0"
  configFile = file("config/checkstyle/checkstyle.xml")
}

tasks.withType<Checkstyle> {
  maxWarnings = 0
}

spotless {
  java {
    googleJavaFormat()
    lineEndings = com.diffplug.spotless.LineEnding.UNIX
  }
}

// Define a custom task for code quality checks
tasks.register("codeQuality") {
  group = "verification"
  description = "Run all code quality checks."
  dependsOn("checkstyleMain", "checkstyleTest", "spotlessCheck", "javadoc")
}

// Ensure the build task only compiles the project without running code quality checks
tasks.named("build") {
  group = "build"
  dependsOn("classes")
}
