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

## Features

- **User Account Management**
  - **Registration & Login:** Users can register manually or through their Google accounts.
  - **Profile Editing:** Users who have registered can update their name, surname, and email address(only manually registered users).

- **Event Management**
  - **Event Creation:** Users can craft sports events and decide the location through address autocomplete or by marking a spot directly on the interactive map.
  - **Event Deletion:** Event creators have the capability to delete events they've organized.
  - **Join & Leave:** Users can join events hosted by others and have the flexibility to leave whenever they wish.

- **Event Discovery**
  - **Event Search:** Users can browse events and filter them based on criteria such as name, sport type, city, or event date. Additionally, pagination is used to enable easier browsing of search results.
  - **Event Details:** Interested parties can see event participants, available spots, and the exact location on a map.

## Installation Guide
1. Clone this repository.
2. Configure the MySQL database and set up the required credentials.
3. Generate JWT keys using OpenSSL.
4. Set up the OAuth2 configuration for Google sign-in.
5. Launch the application using Spring Boot.
