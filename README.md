
# Laptop Software Image Tracker README

## Introduction
Welcome to the Laptop Software Image Tracker. This document provides instructions for setting up and running the front-end and back-end components of the application. The front-end is built with Vue.js, and the back-end uses Spring Boot. Before proceeding, please ensure you have basic familiarity with these technologies.

## Prerequisites
- Node.js (for Vue.js)
- Java JDK (for Spring Boot)
- Maven (for building the Spring Boot application)

## Installation

### Front-End Setup (Vue.js)
1. **Install Dependencies**: Navigate to the front-end directory and run the following command to install Vue.js along with SweetAlert2 for alerting functionalities:
   ```
   npm install
   npm install sweetalert2
   ```
2. **Run the Front-End Server**: Start the Vue.js server by running:
   ```
   npm run serve
   ```
   The Vue.js front-end will now be running on `http://localhost:8080`.

### Back-End Setup (Spring Boot)
1. **Build the Application**: In the back-end directory, build the Spring Boot application using Maven:
   ```
   mvn clean install
   ```
2. **Run the Spring Boot Application**: Start the back -end by running MainApplication.

## UML Documentation
For a detailed understanding of the application architecture, refer to our UML documentation. You can access it [here](https://docs.google.com/document/d/1P_izmAPhWdkx0QjgGeSgiBvzeTGDaReANyReSbdLwf0/edit?usp=sharing).

## Notes
- Ensure that both the front-end and back-end servers are running to use the application fully.
- The default ports are set to 3000 for the front end and 8081 for Spring Boot. If these ports are occupied on your machine, you may need to modify the configuration.

For further assistance, please refer to the respective documentation of Vue.js and Spring Boot.
