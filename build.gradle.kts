plugins {
    id("java")
    id("com.vanniktech.maven.publish") version "0.33.0"
}

group = "io.github.jamestoney311"
val artifactId = "org.eclipse.birt.report.engine.emitter.csv"
version = "1.0.2"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.innoventsolutions.birt.runtime:org.eclipse.birt.runtime_4.8.0-20180626:4.8.0")
}

tasks.test {
    useJUnitPlatform()
}

mavenPublishing {
    coordinates(group.toString(), artifactId, version.toString())

    pom {
        name.set("BIRT CSV Emitter Plugin")
        description.set("""
            CSV Report Emitter Plugin for BIRT

            BIRT (Business Intelligence Reporting Tools) is an open source Eclipse-based set of reporting and data visualization tools and technologies used to build rich information applications, in particular, web applications based on Java and J2EE.

            The CSV Report Emitter plugin for BIRT is an Eclipse based emitter plugin which outputs and displays your BIRT report in CSV Format.

            The CSV Report Emitter plugin currently supports emitting a single table within a report design with Column Headers as First Row. This emitter handles Row/Cell level hidden properties while emitting the output.
            """.trimIndent())
        inceptionYear.set("2025")
        url.set("https://github.com/jamestoney311/csv-emitter-birt-plugin")

        licenses {
            license {
                name.set("Eclipse Public License - v 2.0")
                url.set("https://www.eclipse.org/legal/epl-2.0/")
            }
        }
        developers {
            developer {
                id.set("jamestoney311")
                name.set("Johanz Tolentino")
            }
        }
        scm {
            url.set("https://github.com/jamestoney311/csv-emitter-birt-plugin")
            connection.set("scm:git:git://github.com/jamestoney311/csv-emitter-birt-plugin.git")
            developerConnection.set("scm:git:ssh://github.com:jamestoney311/csv-emitter-birt-plugin.git")
        }
        dependencies {
            implementation("com.innoventsolutions.birt.runtime:org.eclipse.birt.runtime_4.8.0-20180626:4.8.0")
        }
    }
}
