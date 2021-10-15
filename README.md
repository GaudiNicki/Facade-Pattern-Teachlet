# Facade Pattern Teachlet
## Introduction
The application is part of a term paper for the module Software Engineering at the NORDAKADEMIE Elmshorn. The authors are Anna Engelmann, Merlin Ritsch, Niklas Witzel and Til Zöller. The objective was to create a teachlet about the facade design pattern.

## Run the application
The application can be run via an IDE or by executing a JAR file. For both ways you have to perform the following steps at first.

1. Clone the project from GitHub with `git clone https://github.com/GaudiNicki/Facade-Pattern-Teachlet.git`
2. Open the project with your IDE and wait until the importing and indexing is completed
3. Open project settings, select Java 11 SDK and adjust the language level of the project and the modules accordingly.
4. Run `npm install` inside the terminal and wait till all dependencies are successfully installed
5. Execute the following maven goal: `mvn clean install`

### Run via IDE
To run the application using an IDE, you must perform the following additional steps. In this case, the IDE used is IntelliJ. 
Please note that the process may differ for other IDEs.

6. Open the class `de.nordakademie.facadepatternteachlet.Application` and click the run-button beside the main method.
7. This runs the application and creates a run configuration that could be used for later application startups.
8. Open `localhost:8080` in your browser.

### Run via executable JAR
To run the application using an IDE, you must perform the following additional steps.

6. Make sure that you terminal uses Java version 11 by running `java -version`.
7. Open a terminal and navigate to your project root folder.
8. Run `java -jar ./target/facade-pattern-teachlet-target-system-1.0.jar`
9. Open `localhost:8080` in your browser.
