# Interstellar Transport System

Interstellar transport system for Earth's inhabitants

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

```
JDK 5+
Maven 3+
Tested on WildFly 14.0.1
```

### Installing

Run 

```
mvn clean install
```

to generate the interstellar-service-impl.war file. Deploy this artifact to the standalone/deployments folder.

## Running the programs

Once the artifact has been deployed. Run the following requests to verify data in the system:

`GET http://localhost:8080/interstellar-service-impl/api/find/planet/all` to bring back all configured planets
`GET http://localhost:8080/interstellar-service-impl/api/find/route/all` to bring back all configured routes

Check if WSDL service has been initialised by entering the following URL in the browser:

`http://localhost:8080/interstellar-service-impl/RouteService?wsdl`

Check if the shortest path algorithm can be access through the front-end by accessing this URL:

`http://localhost:8080/interstellar-service-impl/index.xhtml`

## Built With

* [WildFly](http://wildfly.org/downloads/) - The application server
* [Maven](https://maven.apache.org/) - Dependency Management