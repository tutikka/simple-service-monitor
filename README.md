Simple Service Monitor
======================

![ScreenShot](/screenshots/feature.png)

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

| Endpoint                              | HTTP Method | Description                                     |
| --------------------------------------|-------------|-------------------------------------------------|
| http://[HOST]:[PORT]/services         | GET         | Return a list of current services and responses |
| http://[HOST]:[PORT]/services         | POST        | Schedule a new service                          |
| http://[HOST]:[PORT]/services?id=[ID] | PUT         | Update response to an existing service (relay)  |
| http://[HOST]:[PORT]/services?id=[ID] | DELETE      | Cancel an existing service                      |

The methods can be easily tested using a normal web browser, or from the shell using a tool such as `curl`.

### Example: List Services

```
$ curl http://localhost:10010/services
[
  {
    "host": "127.0.0.1",
    "id": "785d0a58-2a28-48f4-92e3-77435239e672",
    "name": "Local",
    "type": "icmp",
    "group": "Echo",
    "interval": 10000,
    "warning": 1000,
    "error": 5000,
    "response": {
      "status": "OK",
      "time": 1,
      "message": "Host available",
      "updated": "Dec 6, 2016 10:46:04 AM"
    }
  }
]
```

### Example: Schedule Service

```
$ curl -X POST http://localhost:10010/services -d @service.json -H "Content-Type: application/json"
```

Where `service.json` contains:

```
{
  "driver": "com.mysql.jdbc.Driver",
  "url": "jdbc:mysql://localhost/test?useSSL=false",
  "username": "test",
  "password": "test",
  "query": "SELECT 1",
  "id": "9ec97a16-33c6-4b3f-9283-25e03f361401",
  "name": "MySQL / test",
  "type": "jdbc",
  "group": "database",
  "interval": 10000,
  "warning": 1000,
  "error": 5000
}
```

### Example: Update Service

```
$ curl -X PUT "http://localhost:10010/services?id=9ec97a16-33c6-4b3f-9283-25e03f361401" -d @service.json -H "Content-Type: application/json"
```

Where `service.json` contains:

```
{
  "driver": "com.mysql.jdbc.Driver",
  "url": "jdbc:mysql://localhost/test?useSSL=false",
  "username": "test",
  "password": "test",
  "query": "SELECT 1",
  "id": "9ec97a16-33c6-4b3f-9283-25e03f361401",
  "name": "MySQL / test",
  "type": "jdbc",
  "group": "database",
  "interval": 10000,
  "warning": 1000,
  "error": 5000
}
```

### Example: Cancel Service

```
$ curl -X DELETE "http://localhost:10010/services?id=9ec97a16-33c6-4b3f-9283-25e03f361401"
```