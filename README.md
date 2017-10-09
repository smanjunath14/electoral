# electoral

### Prerequisites

- [Oracle JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) (update 121 or greater)
- Simple Build Tool (`brew install sbt`)

### Building

How to compile:

- Sources: `sbt compile`
- Tests: `sbt test:compile`

### Running locally

`sbt run`


### Description

Reads all the files from input directory: {projectDir}/src/main/resources/ElectoralList . Process the electoral files and converts to json format.
Json representation is written to output directory: {projectDir}/out/processed_electoral_list 