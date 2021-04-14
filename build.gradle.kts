import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("maven-publish")
    id("idea")
	kotlin("jvm") version "1.4.31"
	kotlin("plugin.serialization") version "1.4.31"
}

group = "ehn.techiop.hcert"
version = "0.1.3-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

repositories {
	mavenLocal()
    jcenter()
	mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/ehn-digital-green-development/hcert-kotlin")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.31")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.31")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.1.0")
	implementation("com.augustcellars.cose:cose-java:1.1.0")
	implementation("com.google.zxing:core:3.4.1")
	implementation("com.google.zxing:javase:3.4.1")
	implementation("org.bouncycastle:bcpkix-jdk15to18:1.68")
	implementation("com.squareup.okhttp3:okhttp:4.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.7.1")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict", "-Xopt-in=kotlin.ExperimentalUnsignedTypes", "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
