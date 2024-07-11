plugins {
  // Apply the application plugin to add support for building a CLI application in Java.
  application
}

repositories {
  // Use Maven Central for resolving dependencies.
  mavenCentral()
}

dependencies {
  // Use JUnit Jupiter for testing.
  testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
  testImplementation("com.google.truth:truth:1.4.3")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
  testImplementation("org.mockito:mockito-core:5.11.0")

  // Add Moshi dependencies
  implementation("com.squareup.moshi:moshi:1.12.0")
  implementation("com.squareup.moshi:moshi-adapters:1.12.0")
  implementation("com.squareup.moshi:moshi-kotlin:1.12.0")

  implementation ("org.json:json:20240303")

}

application {
  // Define the main class for the application.

  mainClass.set("view.GUI")
}


tasks.named<JavaExec>("run") {
  standardInput = System.`in`
  enableAssertions = true
}

tasks.named<Test>("test") {
  // Use JUnit Platform for unit tests.
  useJUnitPlatform()
}

// May be needed when JavaFX is used
// javafx {
//   version = "21.0.3"
//   modules(
//       "javafx.base",
//       "javafx.swing",
//       "javafx.graphics",
//       "javafx.controls",
//       "javafx.fxml",
//       "javafx.media",
//       "javafx.web")
// }
