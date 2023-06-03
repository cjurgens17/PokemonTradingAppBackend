# Project Name: Pokemon API

## Introduction

The Pokemon API project is a custom backend server and API developed using Java and the Spring framework. It provides a platform for interacting with Pokemon data and serves as the backend for a custom client-side application built with Angular. 
The repository link for the client-side application is here [https://github.com/cjurgens17/PokemonTradingApp]

## Technologies Used

- Java
- Spring Framework (Spring Web, Spring Data JPA, Spring Boot)
- JUnit 5 (with Mockito)
- Angular
- Amazon EC2
- PostgreSQL
- Heroku

## Project Overview

The main objective of this project is to create a Pokemon API that allows users to interact with Pokemon data. The API is built using the Spring framework, specifically Spring Web for handling HTTP requests and responses, Spring Data JPA for database interactions, and Spring Boot for easy setup and configuration.

The project includes the following components:

1. Custom Backend Server/API: The core of the project is the backend server, which serves as an API for handling Pokemon-related requests and operations. It is implemented using Java and Spring.

2. Microservices Architecture: The project follows a microservices architecture, enabling modular development and scalability. This allows different components of the API to be developed independently and deployed as separate services.

3. Amazon EC2 Postgres Database: The API is connected to an Amazon EC2 instance running a PostgreSQL database. This ensures reliable and scalable data storage for the Pokemon information.

4. Heroku Deployment: The API is deployed and hosted on Heroku, a cloud platform that provides an easy and convenient way to deploy web applications. This allows the API to be accessible to users over the internet.

5. Test Coverage: The project includes comprehensive test coverage, with all Spring web logic being tested using JUnit 5 and Mockito. This ensures the reliability and correctness of the API's functionality.

6. Custom Client-side Application: Alongside the API, I have also developed a custom client-side application using Angular. This application interacts with the API to provide a user-friendly interface for accessing and manipulating Pokemon data.

## Getting Started

To set up and run the Pokemon API project locally, follow these steps:

1. Clone the project repository from GitHub.

2. Ensure that Java and the necessary dependencies (Spring framework, JUnit 5, Mockito) are installed on your system.

3. Set up an Amazon EC2 instance and configure a PostgreSQL database.

4. Update the API's configuration files (e.g., `application.properties`) with the necessary database connection details.

5. Build the project using a build tool such as Maven or Gradle.

6. Run the project locally using the command-line or an integrated development environment (IDE).

7. Access the API endpoints through a web browser or API testing tool.

## API Endpoints

The Pokemon API exposes various endpoints for interacting with Pokemon data. Here are some example endpoints:

- `GET user/{id}/userPokemon`: Retrieves a list for of a users Pokemon.
- `POST pokemon/addPokemon`: Creates a new Pokemon.
- `POST user/new`: Creates a new User.
- `POST pokemon/{id}/addPokemon`: Updates an existing Pokemon.
- `DELETE user/deleteMessage`: Deletes a Message.

## Conclusion

This Pokemon API project provides a backend solution for managing Pokemon data. With its microservices architecture, comprehensive test coverage, and integration with a custom client-side application, it offers a complete system for interacting with Pokemon information.

Feel free to explore the project's codebase to understand its implementation details and extend its functionality further. Should you have any questions or encounter any issues, please don't hesitate to reach out.

