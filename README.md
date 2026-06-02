# Contact Manager

A small Spring Boot REST API for managing contacts. It keeps contact data in memory and exposes endpoints to list, create, update, delete, search, and look up contacts by email.

## Requirements

- Java 17
- Maven
- just, optional, for the shortcut below

## Run

```sh
just run
```

Or run Maven directly:

```sh
mvn spring-boot:run
```

The API starts at `http://localhost:8080/api/contacts`.

## How to use

Start the app in one terminal:

```sh
just run
```

Keep that terminal open while the server is running. Then open another terminal and request contact information with `curl`:

```sh
curl http://localhost:8080/api/contacts
```

Get one contact by ID:

```sh
curl http://localhost:8080/api/contacts/1
```

Search for contacts:

```sh
curl "http://localhost:8080/api/contacts/search?q=alice"
```

Find a contact by email:

```sh
curl "http://localhost:8080/api/contacts/email?email=alice@example.com"
```

If you would like a nice output, pipe the command with `| jq`

```sh
curl http://localhost:8080/api/contacts | jq
```

## Endpoints

- `GET /api/contacts`
- `GET /api/contacts/{id}`
- `POST /api/contacts`
- `PUT /api/contacts/{id}`
- `DELETE /api/contacts/{id}`
- `GET /api/contacts/search?q=alice`
- `GET /api/contacts/email?email=alice@example.com`
