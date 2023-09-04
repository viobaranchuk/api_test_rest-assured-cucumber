# restApi_BDD_maven
* https://petstore.swagger.io/#/ is used as a test API application

## Purpose
the purpose of this framework is test automation of
* API
 

## Technical stack
* Java 11
* Cucumber
* Rest Assured
* AssertJ
* Maven

## Quick setup
* Please, execute git clone ----- in appropriate folder
* Start IntelliJ and open project by picking up pom.xml
* Wait until Maven uploads all dependencies
* Build the project to ensure code integrity
* Ensure that you already have plugin that supports Cucumber feature files to be executed. Check IntelliJ preferences if this plugin is installed and enabled
* Run any cucumber feature by right click on it in IDE project tree and selecting src/test/resources/features/api/petstore.feature Run:'Feature.petstore'
* Run using JUnit: Open RunCucumberTest.java, configure CucumberOptions with feature file name or tags and click Run button for RunCucumberTest.java
* Tags that are used in features: 
@AddPet, @UploadImage, @FindByStatus, @DeletePet, @Smoke, @Negative