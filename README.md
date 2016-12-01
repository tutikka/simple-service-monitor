Simple Service Monitor
======================

Features
--------

Simple Service Monitor is a tool to monitor your services for outages or increases in latency.

- Desktop application to set up various kinds of services and provide a dashboard view
- Graphical trend of each service
- Save and open list of services

Available service types:

- URL service: monitor a URL using HTTP
- JDBC service: monitor a database using JDBC (be sure to include JDBC driver)
- TCP service: monitor a custom host and port using TCP
- ICMP service: ping a host using an ICMP ECHO REQUEST

Instructions
------------

### Build latest version from source (Unix, Linux, MacOS)

```
$ git clone https://github.com/tutikka/simple-service-monitor.git
$ cd simple-service-monitor
$ ant
$ cd dist
$ sh ./ssm.sh &
```

### Customizing look and feel

Add the following as a VM argument to `ssm.sh` or `ssm.cmd`: 

```
java -Dssm.laf.class=javax.swing.plaf.nimbus.NimbusLookAndFeel ...
```

Note that you can see all the installed look and feel class names for your system in the logs after startup.

### Use the screen menu bar on Mac OS

Add the following VM argument to `ssm.sh`:

```
java -Dapple.laf.useScreenMenuBar=true ...
```

JDBC Service
------------

Using the JDBC service requires adding an appropriate JDBC driver to your `CLASSPATH`. No JDBC drivers are distributed within the application. You can find examples for common databases below.

### MySQL

Download the MySQL JDBC Driver from:

```
https://dev.mysql.com/downloads/connector/j/
```

Extract the package and copy the JAR file (for example `mysql-connector-java-5.1.40-bin.jar`) to your `lib` folder.

Add the JAR file to your `CLASSPATH` in `ssm.sh` (Unix, Linux, MacOS):

```
java -cp .:lib/mysql-connector-java-5.1.40-bin.jar ...
```

Add the JAR file to your `CLASSPATH` in `ssm.cmd` (Windows):

```
java -cp .;lib/mysql-connector-java-5.1.40-bin.jar ...
```

When adding the JDBC service, use the following for the **JDBC driver** field:

```
com.mysql.jdbc.Driver
```

When adding the JDBC service, use the following syntax for the **JDBC URL** field;

```
jdbc:mysql://[HOST]:[PORT]/[DATABASE]
```

For example:

```
jdbc:mysql://127.0.0.1:3306/test
```

Screenshots
-----------

![ScreenShot](/screenshots/services.png)

![ScreenShot](/screenshots/service_details.png)