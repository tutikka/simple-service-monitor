Simple Service Monitor
======================

Status
------

[![Build Status](https://travis-ci.org/tutikka/simple-service-monitor.svg?branch=master)](https://travis-ci.org/tutikka/simple-service-monitor)

Features
--------

Simple Service Monitor is a tool to monitor your services for outages or increases in latency.

- Desktop application to set up various kinds of services and provide a dashboard view
- Graphical trend of each service
- Save, open and import list of services from disk (JSON)
- HTTP server with restful API to query services

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

HTTP Server
-----------

The built-in HTTP server can be used to query the current list of services including the latest status from each. The interface is a JSON-based restful API, as defined below.

| Endpoint                       | HTTP Method | Description                                     |
| ------------------------------ |-------------| ------------------------------------------------|
| http://HOST:PORT**/services/** | GET         | Return a list of current services and responses |

The methods can be easily tested using a normal web browser, or from the shell using a tool such as `curl`.

Screenshots
-----------

![ScreenShot](/screenshots/services.png)

![ScreenShot](/screenshots/service_details.png)