NOTES
=====

# Feature backlog
* Externalize configuration (port, location of HTML) in YAML file - done 12/16
* OAuth2 / security - done 12/21
  * Basic auth using client credentials for token endpoint - done 12/22
  * Role based authorization - done 12/21
  * Multi-tenancy for the users - done 12/21
  * SSL - terminate at nginx reverse proxy instead of Grizzly - done 1/4
* MongoDB integration - done 12/16
* Serialization and object binding - done 12/16
* Services
  * Goals - crud operations done 12/16
  * Actions
  * User management - done 12/21
  * Data sync
* Metrics and analytics
* Reverse Proxy (nginx) - done 1/4
* Logger integration (log security violations)
* Sonar integration - done 12/22
* Adopt google code guidelines - done 12/23

---

# Services
```
ROOT - http://localhost:8080
Heartbeat - http://localhost:8080/api/ping
AWS - 54.201.14.80
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

# Sonar commands
sonar.sh start
mvn sonar:sonar
http://localhost:9000

# SSL certificates
keytool -genkey -keyalg RSA -alias devcert -keystore keystore.jks -storepass d3vp4ss -keysize 2048
keytool -exportcert -keystore keystore.jks -file devcert.cer -alias devcert
keytool -importcert -keystore truststore.jks -alias devcert -file devcert.cer 

# Nginx / SSL
openssl req -nodes -newkey rsa:2048 -keyout goalmeister_server.key -out goalmeister_server.csr
openssl x509 -in ssl/goalmeister.cer -text

# To import the self-signed certificate to the system keystore (not a good idea)
sudo keytool -import -keystore /Library/Java/Home/lib/security/cacerts -file devcert.cer -alias devcert
```

---

# Links
[Markdown cheatsheet](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)

[Base64 decoder](http://www.base64encode.org/)

[Building a Web API Platform] (http://www.slideshare.net/rfeng/con6946-feng)


---

### nginx configuration
```
server {
	# Goalmeister config
	listen 80;
	listen [::]:80;
	server_name goalmeister.com;
	return 301 https://$server_name$request_uri;
}

server {
	# Goalmeister config
	listen 443 default_server;
	listen [::]:443 ipv6only=on;
	server_name goalmeister.com;

	# Goalmeister SSL config
	ssl on;
	ssl_certificate /etc/ssl/certs/goalmeister_bundle.pem;
	ssl_certificate_key /etc/ssl/private/goalmeister.key;
	ssl_protocols SSLv3 TLSv1;
	ssl_ciphers ALL:!aNULL:!ADH:!eNULL:!LOW:!EXP:RC4+RSA:+HIGH:+MEDIUM;
	ssl_session_cache shared:SSL:10m;

	location / {
		root /home/ubuntu/goalmeister-api/src/main/html/app;
		index index.html index.htm;

		# First attempt to serve request as file, then
		# as directory, then fall back to displaying a 404.
		try_files $uri $uri/ /index.html;
	}

	location /api {
		# Goalmeister reverse proxy configuration
		proxy_pass		http://localhost:8080;
		proxy_set_header	Host $host;
		proxy_set_header	X-Real-IP $remote_addr;
		proxy_set_header	X-Forwarded-For $proxy_add_x_forwarded_for;
		proxy_connect_timeout	150;
		proxy_send_timeout	100;
		proxy_read_timeout	100;
		proxy_buffers		4 32k;
		client_max_body_size	8m;
		client_body_buffer_size	128k;
	}
```

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
