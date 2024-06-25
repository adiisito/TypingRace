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

}

application {
  // Define the main class for the application.
  mainClass.set("TODO")
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


