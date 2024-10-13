# CFTC Commitments of Traders API

## Overview

This project is a Java Spring Boot application designed to interact with the CFTC 
(Commodity Futures Trading Commission) Commitments of Traders (COT) reports. It provides functionality to download 
historical COT reports, extract relevant data into Excel format, and expose the data through a RESTful API with 
paging and sorting capabilities.

## Features

- Download COT reports from the CFTC website for a given range of years.
- Extract and transform the data into a structured Excel format.
- Expose the data through a REST API, allowing clients to retrieve paged and sorted data.

## Getting Started

### Prerequisites

- JDK 21
- Maven 3.6.0 or later
- SQLite

### Installation

1. Clone the repository:
`git clone https://github.com/paulschick/java-cot-processor.git`
2. `cd java-cot-processor`
3. Build using Maven: `mvn clean install`
4. Run the application: `java -jar target/cot-processor-0.0.1-SNAPSHOT.jar`

### Configuration

Before running the application, ensure that the `persistence.properties` file is configured with the 
correct database settings.

## Usage

The application exposes several REST endpoints:

- `GET /download/{startYear}/{endYear}`: Triggers the download of COT reports between the specified years.
- `GET /cot/paging/`: Retrieves paged COT data with optional sorting parameters.
- `GET /process/{fileNo}`: Processes and saves the data from the specified file index based on the downloaded and 
extracted Excel files.

## API Documentation

Once the application is running, you can access the Swagger UI for the API documentation at:
`http://localhost:8080/swagger-ui/index.html#/`