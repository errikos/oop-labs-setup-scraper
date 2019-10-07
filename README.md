# oop-labs-setup-scraper
Simple scraper to allocate students in the OOP course labs @ UoA

# Indicating the registration thread IDs
Edit the [`src/main/resources/oop-labs.properties`](https://github.com/errikos/oop-labs-setup-scraper/blob/master/src/main/resources/oop-labs.properties) file.

# How to run
The simplest way is to download the latest [release](https://github.com/errikos/oop-labs-setup-scraper/releases) and run:
```
scala oop-labs-setup-scraper-X.Y.jar
```
or
```
java -jar oop-labs-setup-scraper-X.Y.jar
```

Alternatively, clone the repository, `cd` to the project root and with [sbt](https://www.scala-sbt.org/):
```
sbt run
```

# Building from source
You will need [sbt](https://www.scala-sbt.org/).

To create an executable JAR, while at the root project directory, run:
```
sbt assembly
```
You will find the JAR in `target/scala-2.13`.
