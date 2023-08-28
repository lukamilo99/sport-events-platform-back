# Sport Events Platform - backend

This is a RESTful API and part of a full-stack application ([frontend](https://github.com/lukamilo99/sport-connecting-people-front)), designed to help users to find sports activities across different cities. It enables users to discover, create, and join sports events across various cities. 

## Technologies

- **Spring Boot**: The foundation for creating stand-alone applications.
- **Hibernate**: ORM tool for database interaction.
- **MySQL**: Used as the primary database.
- **Spring Security & JWT**: Securing and authorizing user data with JWT tokens.
- **OAuth2**: Allows users to sign in/register through Google.
- **Spring WebFlux**: Asynchronous calls, especially for interacting with the GeoApify API.
- **GeoApify**: Fetching location information such as map display and address autocomplete.
- **JUnit, Mockito**: Written a few unit tests for web and service layer - work in progress.

## Features

- **User Account Management**
  - **Registration & Login:** Users can register manually or through their Google accounts.
  - **Profile Editing:** Users who have registered can update their name, surname, and email address(only manually registered users).
  - **Role-Based Access Control:** The platform uses roles to determine user permissions, restricting actions based on the user's role.
 
- **Friendship Management**
  - **Add Friends:** Users can send and receive friend requests to connect with others on the platform.

- **Event Management**
  - **Event Creation:** Users can craft sports events and decide the location through address autocomplete or by marking a spot directly on the interactive map.
  - **Event Modification:** Event creators have the capability to delete and update events they've organized.
  - **Join, Invite, Leave:** Users can join events hosted by others or invite friends to them and have the flexibility to leave whenever they wish.

- **Request Management**
  - **Friendship Requests:** Users can send, receive, and manage friendship requests.
  - **Event Invitation Requests:** Users can send and receive invitations for sports events. This enables users to invite others to the events they are organizing or get invited by others.

- **Notification Management**
  - **Request Notifications:** Users receive notifications for friendship and event invitation requests. This helps in keeping users updated about the interactions from other users.

## Installation Guide
1. Clone this repository.
2. Configure the MySQL database and set up the required credentials.
3. Generate JWT keys using OpenSSL.
4. Set up the OAuth2 configuration for Google sign-in.
5. Launch the application using Spring Boot.
