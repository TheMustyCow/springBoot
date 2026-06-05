# Spring Boot Project Writeup

## Overview

For this project, we built a small Contact Manager API using Spring Boot. The goal was to create a backend application that can store and manage contact information, including a person's first name, last name, email, phone number, and address. Instead of building a full frontend, we focused on the backend logic and used REST API endpoints that can be tested with tools like `curl`, Postman, or a browser for simple GET requests.

Spring Boot was a good choice for this project because it gives us a lot of setup automatically. It starts an embedded web server, handles routing, supports JSON request and response bodies, and lets us organize the project into clear layers.

## How We Used Spring Boot

We used Spring Boot to create a REST API for managing contacts. The main application starts in `ContactManagerApplication`, where the `@SpringBootApplication` annotation tells Spring Boot to configure the app, scan for components, and run the embedded server.

The controller layer is handled by `ContactController`. This class uses annotations like `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, and `@DeleteMapping` to connect HTTP requests to Java methods. For example, when someone sends a `GET` request to `/api/contacts`, Spring Boot calls the method that returns all contacts. When someone sends a `POST` request, Spring Boot reads the JSON body and turns it into a `Contact` object.

The service layer is handled by `ContactService`. This is where most of the project logic lives. It stores contacts in memory using a map, generates new IDs, searches contacts, updates existing contacts, deletes contacts, and saves changes back to `contacts.json`. Marking this class with `@Service` lets Spring manage it and inject it into the controller automatically.

The model layer is represented by the `Contact` class. This class defines the data structure for each contact. It includes fields like `id`, `firstName`, `lastName`, `email`, `phone`, and `address`. We also used validation annotations such as `@NotBlank` and `@Email` to show how Spring Boot can help enforce rules for valid data.

## Features We Built

The API supports the main CRUD operations:

- Create a new contact
- Read all contacts
- Read one contact by ID
- Update an existing contact
- Delete a contact
- Search contacts by name or email
- Find a contact by exact email address

The project also uses a `contacts.json` file as simple storage. When the application starts, it loads the contacts from that file. When contacts are added, updated, or deleted, the service writes the new list back to the file. This helped us practice persistence without needing to set up a full database.

## What We Learned

One important thing we learned is how Spring Boot organizes an application. Separating the project into a controller, service, and model made the code easier to understand. The controller focuses on HTTP requests and responses, the service focuses on business logic, and the model defines the shape of the data.

We also learned how useful Spring annotations are. Instead of manually setting up a web server and routing system, Spring Boot lets us describe what each class or method should do with annotations. This made it easier to create endpoints and connect them to Java methods.

Another major lesson was how JSON works with Java objects. Spring Boot can automatically convert incoming JSON into a `Contact` object and convert Java objects back into JSON responses. We also used Jackson's `ObjectMapper` directly in the service to read from and write to the `contacts.json` file.

This project also showed why HTTP status codes matter. Returning `200 OK`, `201 Created`, `204 No Content`, and `404 Not Found` makes the API more understandable and more consistent for anyone using it.

## Challenges

One challenge was deciding how to store the contacts. A database would be more realistic, but using a JSON file kept the project simple and helped us focus on Spring Boot fundamentals. Another challenge was making sure each contact had a unique ID, which we handled with an `AtomicLong`.

We also had to think about what should happen when a contact is not found. Using `Optional` in the service helped make those cases clearer, and the controller could then return a proper `404 Not Found` response.

## How We Could Expand It

In the future, this project could be expanded by replacing the JSON file with a real database such as PostgreSQL, MySQL, or H2. We could use Spring Data JPA to create a repository layer, which would make storing and querying contacts more powerful.

We could also add stronger validation, duplicate email checking, automated tests, pagination, sorting, and a simple frontend. Another useful improvement would be adding authentication so each user could have their own private contact list.

Overall, this project gave us a practical introduction to Spring Boot and showed how Java can be used to build a clean, working REST API.
