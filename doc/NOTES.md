NOTES
=====

# Feature backlog
* Externalize configuration (port, location of HTML) in YAML file - done 12/16
* OAuth2 / security - done 12/21
  * Basic auth using client credentials for token endpoint - done 12/22
  * Role based authorization - done 12/21
  * Multi-tenancy for the users - done 12/21
  * SSL
* MongoDB integration - done 12/16
* Serialization and object binding - done 12/16
* Services
  * Goals - crud operations done 12/16
  * Actions
  * User management - done 12/21
  * Data sync
* Metrics
* Reverse Proxy (use two Grizzly instances)
* Logger integration


---

# Services
```
ROOT - http://localhost:8080
Heartbeat - http://localhost:8080/api/ping
```


---

# Quick commands

```
mvn archetype:generate -DarchetypeArtifactId=jersey-quickstart-grizzly2 -DarchetypeGroupId=org.glassfish.jersey.archetypes -DinteractiveMode=false -DgroupId=com.goalmeister -DartifactId=goalmeister-webapp -Dpackage=com.goalmeister -DarchetypeVersion=2.4.1

mvn clean package

mvn exec:java

java -jar target/goalmeister-webapp-1.0-SNAPSHOT.jar

apib -w 5 -c 250 -d 20 http://localhost:8080/api/ping

Username: aweeraman@gmail.com, Password: weeraman
curl -v -H "Authorization: Basic aHR0cHdhdGNoOmY=" http://localhost:8080/api/oauth2/token
```

---

# Links
[Markdown cheatsheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)

[Base64 decoder](http://www.base64encode.org/)

[Building a Web API Platform] (http://www.slideshare.net/rfeng/con6946-feng)


---

# Problems and resolutions

### META-INF/services/* files were not being concatenated correctly
ServicesResourceTransformer had to be invoked in the shade plugin to rectify this.
```
<transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer" />
```

### The Resources were not being recognized in standalone jar
The resource classes had to be manually registered in Start.java
```
rc.register(PingResource.class);
```

### Exception in thread "main" java.lang.SecurityException: Invalid signature file digest for Manifest main attributes
This was due to the shade plugin rebundling the jars. The solution was to setup a few excludes in the plugin.
```
<filter>
	<artifact>*:*</artifact>
		<excludes>
			<exclude>META-INF/*.SF</exclude>
			<exclude>META-INF/*.DSA</exclude>
			<exclude>META-INF/*.RSA</exclude>
		</excludes>
	</filter>
</filters>
```

### ObjectId serialization issue
Mongo was internally using ObjectIds for the keys and it was causing an issue where during an update it was creating a new String key instead of using the ObjectId. This was resolved by adding the @ObjectId annotation in the model so that mongojack take care of serializing to the correct ObjectId under the hood.
