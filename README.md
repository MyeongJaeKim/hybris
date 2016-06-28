# SAP hybris Customization Modules

## hybris-dbpassword-encryption

#### Introduction

  SAP hybris is supporting two DB connection modes. One is JNDI, and another is hybris internal pool.
  If you want to use hybris internal pool, you should configure pooling paramters to local.properties file including DB access password, even if production.
  This will be security threaten. Everyone knows DB password. hybris-dbpassword-encryption is useful to it.

#### How to use it

* Download aspectj.jar.
```
 https://eclipse.org/aspectj/downloads.php
```
* Get two files in lib folder - aspectj.jar, aspectjweaver.jar.
* Copy two files to ${HYBRIS_CONFIG_DIR}/tomcat/lib
* Clone this repository.
* Export to jar, let us assume it as hybris-dbpassword-encryption.jar.
* Trying to make your DB password encrypted like below,
```
   # java -jar hybris-dbpassword-encryption.jar enc DBPassword123
   
   Encrypted --> abcdef12345
```
* Copy hybris-dbpassword-encryption.jar to ${HYBRIS_CONFIG_DIR}/tomcat/lib.
* Write down your local.properties file
```
   # DB Setting
   db.url=jdbc:sqlserver://127.0.0.1:1433;databaseName=HYBRIS;
   db.driver=com.microsoft.sqlserver.jdbc.SQLServerDriver
   db.username=hybris
   db.password=abcdef12345
   
   tomcat.generaloptions=... -javaagent:${HYBRIS_HOME_DIR}/tomcat/lib/aspectjweaver.jar
```




