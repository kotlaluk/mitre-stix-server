# MITRE STIX Server

Web application for MITRE ATT&CK data in STIX format written in Scala

## About

MITRE STIX Server is a web application serving a cybersecurity knowledge base built on MITRE ATT&CK framework data. It covers implementation of custom ATT&CK STIX objects, offers simple views over the ATT&CK data and allows easy manipulation with the objects. The project may be extended by implementing custom STIX objects on the top of the ATT&CK framework data.

### Features

MITRE STIX Server offers two major features:

- simple web pages allowing to browse through Enterprise ATT&CK data
- REST API allowing to perform CRUD operations on the stored ATT&CK objects

### Details

[MITRE ATT&CK®](https://attack.mitre.org/) is a globally-accessible knowledge base of adversary tactics and techniques based on real-world observations. It is a well-established framework in the cybersecurity product and service community. The ATT&CK knowledge base is used as a foundation for the development of specific threat models and methodologies in different organizations.

Apart of being published on the official [ATT&CK® website](https://attack.mitre.org/), the data is provided also in a common STIX format via [MITRE repository on GitHub](https://github.com/mitre/cti) or using the ATT&CK TAXII server. This project includes Enterprise ATT&CK sample data from the [MITRE repository](https://github.com/mitre/cti/blob/master/enterprise-attack/enterprise-attack.json).

[Structured Threat Information Expression (STIX™)](https://oasis-open.github.io/cti-documentation/stix/intro) is an open-source language and serialization format used to exchange cyber threat intelligence (CTI). STIX enables organizations to share CTI with one another in a consistent and machine readable manner, allowing security communities to better understand what computer-based attacks they are most likely to see and to anticipate and/or respond to those attacks faster and more effectively.

ATT&CK data derives from the STIX fromat, as it uses a mix of predefined and custom STIX objects to implement different ATT&CK concepts, which is [described here](https://github.com/mitre/cti/blob/master/USAGE.md#the-attck-data-model). This project covers the differences and interprets the data using ATT&CK model. The base implementation of SITX objects in Scala is provided by [scalastix](https://github.com/workingDog/scalastix).

## Build & Run

1. Clone this repostitory
2. Go to `psl-kotlaluk` directory:

   ```bash
   cd psl-kotlaluk
   ```

3. Compile and run the project with `sbt`:

   ```bash
   sbt compile && sbt run
   ```

4. HTTP server will start at [localhost:8080](http://127.0.0.1:8080) and it will load the provided [ATT&CK data](psl-kotlaluk/data/enterprise-attack.json).
5. Open a web browser and browse to http://127.0.0.1:8080. You should see a clickable ATT&CK matrix.

## Usage

### Browsing ATT&CK data

In ATT&CK matrix, you are able to view a technique detail by clicking on its name. From there, you are able to navigate to different objects that relate to the particular technique.

You can also browse to dedicated clickable list views of different objects:

| ATT&CK Object | URL Endpoint   |
|:-------------:|:--------------:|
| Techniques    | `/techniques`  |
| Mitigations   | `/mitigations` |
| Groups        | `/groups`      |
| Software      | `/software`    |

### Using STIX API

You can also use REST HTTP API to read, create, update and delete ATT&CK objects in JSON STIX format:

|  Action  | HTTP Method |     URL Endpoint     |
|:--------:|:-----------:|:--------------------:|
| Read all |     GET     |   `/api/{OBJECT_ENDPOINT}/`   |
| Read one |     GET     | `/api/{OBJECT_ENDPOINT}/{ID}` |
|  Create  |     POST    |   `/api/{OBJECT_ENDPOINT}/`   |
|  Update  |     PUT     | `/api/{OBJECT_ENDPOINT}/{ID}` |
|  Delete  |    DELETE   | `/api/{OBJECT_ENDPOINT}/{ID}` |

where `{OBJECT_ENDPOINT}` is URL endpoint for a particular ATT&CK  object (from the table above) and `{ID}` is MITRE ID of that object. For example, to retrieve STIX format of a technique *T1110 - Brute Force*, you would navigate to http://127.0.0.1:8080/api/techniques/T1110.

## Project structure

### High-level overview

```
psl-kotlaluk
├── build.sbt         - SBT build file
├── data              - contains ATT&CK data from MITRE
├── project           - SBT configuration files
├── src
│   ├── main
│       ├── resources - project resources
│       ├── scala     - Scala packages
│       └── twirl     - HTML templates
└── tests             - Postman test collection
```

### Packages

```
├── MitreServer.scala - main Scala file
├── repository        - classes for acessing and manipulating STIX objects
│   ├── sdo           - repository for STIX domain objects
│   └── sro           - repository for STIX relationship objects
├── service           - classes implementing web service
│   ├── api           - API endpoints and functionality
│   └── view          - view endpoints and functionality
└── storage           - objects providing access to data storage/loading
```

### Dependencies

This project uses the following frameworks and libraries:

- [http4s](https://http4s.org/) - functional HTTP server
- [circe](https://circe.github.io/circe/) - JSON handling
- [Twirl](https://github.com/playframework/twirl) - HTML template engine
- [scalastix](https://github.com/workingDog/scalastix) - STIX implementation in Scala

## Testing

The API part of MITRE STIX Server can be tested using [Postman](https://www.postman.com/product/api-client/). A test collection is prepared in the `test` subfolder that can be run either using Posman GUI or via its CLI utility called [Newman](https://learning.postman.com/docs/running-collections/using-newman-cli/command-line-integration-with-newman/).

To run the provided test collection using Newman:

1. Install Newman using `npm`:

   ```bash
   npm install -g newman
   ```

2. Go to `psl-kotlaluk` directory:

   ```bash
   cd psl-kotlaluk
   ```

3. And run the collection with `newman`:

   ```bash
   newman run tests/mitre-stix-server.postman_collection.json
   ```

## Author

Lukáš Kotlaba (lukas.kotlaba@gmail.com)

This project was built as a semestral project for Programming in Scala (NI-PSL) class at FIT ČVUT.

## License

The project is licensed under GNU General Public License v3.0.
