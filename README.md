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

Instructions
------------

*Build latest version from source (Unix, Linux, MacOS)*

```
$ git clone https://github.com/tutikka/simple-service-monitor.git
$ cd simple-service-monitor
$ ant
$ cd dist
$ sh ./ssm.sh &
```

*Customizing look and feel*

Add the following as a VM argument to `ssm.sh` or `ssm.cmd`: 

```
-Dssm.laf.class=javax.swing.plaf.nimbus.NimbusLookAndFeel
```

Note that you can see all the installed look and feel class names for your system in the logs after startup.

*Use the screen menu bar on Mac OS*

Add the following VM argument to `ssm.sh`:

```
-Dapple.laf.useScreenMenuBar=true
```

Screenshots
-----------

![ScreenShot](/screenshots/services.png)

![ScreenShot](/screenshots/service_details.png)