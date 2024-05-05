# Eurojackpot Results API

This is an exercise project, consisting of a backend API developed using Java Spring Boot and a front-end application in
React JS.
It loads data from a CSV file and provides it as a searchable API.

# Quick start

The simplest way to get the full application up and running is using docker. \
Simply run the following command in the root directory of the project:

### `docker-compose up --build -d`

After both images are built and the containers are running, you can access the application at \
[http://localhost](http://localhost).

# Backend

## Requirements

+ JDK 21+
+ mvn

## Available commands

In order to run it locally, execute the following command on the project root directory:

### `mvn spring-boot:run`

The backend is available by default at [http://localhost:8080](http://localhost:8080).\
It includes Swagger UI
at [http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html)

To run the unit tests, execute the following command:

### `mvn test`

To generate an executable jar file, run:

### `mvn package`

# Frontend

## Requirements

+ node v21
+ npm v10

## Available Commands

In the `/frontend` project directory, start by installing the dependencies with:

### `npm i`

After the command above finishes successfully, you can start the application running:

### `npm start`

The app will be available at [http://localhost:3000](http://localhost:3000).\
For it to work properly, make sure to have the backend up and running as well.

