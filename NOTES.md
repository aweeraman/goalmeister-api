NOTES
=====

# Quick commands

```
mvn archetype:generate -DarchetypeArtifactId=jersey-quickstart-grizzly2 -DarchetypeGroupId=org.glassfish.jersey.archetypes -DinteractiveMode=false -DgroupId=com.goalmeister -DartifactId=goalmeister-webapp -Dpackage=com.goalmeister -DarchetypeVersion=2.4.1

mvn clean package

mvn exec:java

java -jar target/goalmeister-webapp-1.0-SNAPSHOT.jar

apib -w 5 -c 250 -d 20 http://localhost:8080/goalmeister/ping
```

---

# Links
[Markdown cheatsheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)

---

# Problems and resolutions

### META-INF/services/* files were not being concatenated correctly
ServicesResourceTransformer had to be invoked in the shade plugin to rectify this.
```
<transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer" />
```