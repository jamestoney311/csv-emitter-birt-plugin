BIRT CSV Emitter Plugin

This project provides a CSV emitter for the Eclipse BIRT (Business Intelligence and Reporting Tools) reporting engine. It enables BIRT reports to be exported into CSV (Comma-Separated Values) format using the BIRT emitter extension mechanism.

The code originates from the former Eclipse Labs project hosted on Google Code. This repository restores and maintains the plugin for modern Java and BIRT environments.

Features

Adds CSV export capability to the BIRT report engine
Configurable delimiters and CSV formatting behavior
Integrates directly with BIRT’s emitter extension system
Lightweight and dependency-free
Licensed under the Eclipse Public License v2.0 (EPL-2.0)

Installation

Build the project using your chosen build tool (Maven or Gradle).
Copy the generated JAR file into your BIRT installation directory under the “plugins” folder.
Restart BIRT. A CSV export option should now be available.
Building from Source

Maven build:

mvn clean package
Gradle build:

./gradlew build
The resulting plugin JAR will be located in:

target/ (for Maven)
build/libs/ (for Gradle)
Programmatic Usage Example

To use the CSV emitter programmatically with the BIRT Report Engine:

    IRunAndRenderTask task = engine.createRunAndRenderTask(design);
    CSVRenderOption options = new CSVRenderOption();
    options.setOutputFile(new File("output.csv"));
    options.setOutputFormat("csv");
    task.setRenderOption(options);
    task.run();
    task.close();

Contributing

Contributions and issue reports are welcome. For major changes or new functionality, please open an issue first to discuss direction, compatibility with BIRT versions, and design considerations.

License (EPL 2.0)

This project is made available under the Eclipse Public License v2.0.

You may obtain a copy of the license at: https://www.eclipse.org/legal/epl-2.0/

EPL-2.0 Notice:

All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse Public License v2.0 which accompanies this distribution and is available at: https://www.eclipse.org/legal/epl-2.0/

Origin and Attribution

This project is based on the original Eclipse Labs CSV emitter plugin hosted on the Google Code platform. All original work remains under the Eclipse Public License v1.0.

For more information, visit the original project page: https://code.google.com/archive/a/eclipselabs.org/p/csv-emitter-birt-plugin