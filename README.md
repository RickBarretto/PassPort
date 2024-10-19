# Events Management

This project is a PBL (Project-Based Learning) initiative for my university's Programming and Algorithms II course. 
It focuses on applying OOP principles in Java through practical, real-world scenarios.

The requirements are:
- Must be written in Java
- Must follow some project requirements
- Must not use database, but store data into Json files

## Features

- **Event Creation:** Administrators can create new events. 
- **User Management:** The system manages administrators and regular users.
- **Ticket Sales:** Includes a ticketing system where users can buy tickets for events.
- **Event Listings:** Users can browse through a list of available events.
- **Event Evaluation:** After attending events, users can evaluate them.
- **Refund Management:** The project also supports ticket refunds.
- **Database Integration:** All the event and user data is stored in a Json database.

## Project's Structure

This project uses Clean Architecture and Domain-Driven Design (DDD) for learning purposes. 
The choosen Architecture ensures modularity and testability by separating the code into layers. 
With this in mind, the project's structure aligns perfectly with this approach. 
Here's a look at the src/main structure:

- `domain`: The business logic
    - `context`: Provides all context needed for the user's stories
    - `exceptions`: Contains all domain-specific exceptions
    - `models`: Includes all models (entities and value objects)
- `infra`: The infrastructure code
    Handles database interactions and external services
- `roles`: All interfaces that define the logic
    Ensures flexibility between the infra and domain layers

## Workspace's Structure

The workspace contains three main folders:
- `src/main`: the source code
- `src/test`: the test files
- `lib`: the necessary `.jar` dependencies
