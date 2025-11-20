# Photo Album React App

A full-stack web application for managing photo albums, built with **React** (frontend) and **Spring Boot** (backend).

## Table of Contents
1. [Features](#features)
2. [Tech Stack](#tech-stack)
3. [Getting Started](#getting-started)
   - [Prerequisites](#prerequisites)
   - [Backend Setup](#backend-setup)
   - [Frontend Setup](#frontend-setup)
4. [Configuration](#configuration)
5. [Notes](#notes)
6. [Usage](#usage)
7. [Common Issues and Troubleshooting](#common-issues-and-troubleshooting)
8. [Contribution Guidelines](#contribution-guidelines)

## Features

- User authentication (JWT-based)
- Create, view, update, and delete albums
- Upload, download, update, and delete photos
- Automatic thumbnail generation for uploaded images
- Secure file access (users can only access their own albums/photos)
- RESTful API with Swagger/OpenAPI documentation
- Responsive Material UI dashboard
- REST API integration with Spring Boot backend

## Tech Stack

- **Backend:** Java, Spring Boot, Spring Security, Lombok
- **Frontend:** React, Material UI (UI components and styling), Axios (HTTP client for API requests), Redux Toolkit (State management)
- **Database:** H2 Database
- **File Storage:** Local filesystem (static/uploads)
- **API Docs:** Swagger/OpenAPI

## Getting Started

### Prerequisites

- Node.js (v16+ recommended)
- npm or yarn
- Java 11+ (for backend)
- Spring Boot backend running (default: http://localhost:8080)

### Backend Setup

1. **Clone the repository:**
    ```sh
    git clone <your-repo-url>
    cd Backend
    ```

2. **Configure application properties:**
    - Edit `src/main/resources/application.properties` for your DB and JWT settings. Example configuration:
      ```properties
      spring.datasource.url=jdbc:h2:mem:testdb
      spring.datasource.driverClassName=org.h2.Driver
      spring.datasource.username=sa
      spring.datasource.password=
      jwt.secret=your_jwt_secret
      spring.h2.console.enabled=true
      ```

3. **Build and run:**
    ```sh
    mvn clean install
    mvn spring-boot:run
    ```

4. **API Documentation:**
    - Visit [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) for Swagger UI.

### Frontend Setup

1. **Clone the repository:**
    ```sh
    git clone <your-repo-url>
    cd Frontend
    ```

2. **Install dependencies:**
    ```sh
    npm install
    # or
    yarn
    ```

3. **Start the React development server:**
    ```sh
    npm start
    # or
    yarn start
    ```

4. **The app will be available at http://localhost:3000/**

### Configuration

- The frontend is set up to proxy API requests to the backend at `http://localhost:8080`.
- To change the frontend port, create a `.env` file in the Frontend directory and add:
    ```plaintext
    PORT=4000
    ```
### Main API Endpoints
http://localhost:8080/swagger-ui/index.html

- `POST /api/v1/token` — For JWT token generation
- `POST  /api/v1/auth/users/add`— Register a new user
- `POST /api/v1/albums/add` — Create album
- `GET /api/v1/albums` — List albums
- `GET /api/v1/albums/{album_id}` — Album details


## Notes

- Uploaded files and thumbnails are stored in the `static/uploads/photos` and `static/uploads/thumbnails` directories (on the backend).
- Only authenticated users can access their own albums/photos.
- Error handling and logging are implemented for robustness.
- In the frontend, use `/login` for user login or `/register` for registering a new user.

## Usage
- Open http://localhost:3000/ in your browser.
- Register or log in to start creating albums and uploading photos.

## Common Issues and Troubleshooting

- **Backend not starting**: Ensure that the correct Java version is installed and that the application properties are configured properly.
- **API not reachable**: Check if the backend is running on the specified port (default: 8080) and that the frontend is correctly set to proxy requests.
- **JWT errors**: Ensure that the JWT secret is correctly set in the backend configuration.


## Contribution Guidelines

Contributions are welcome! Please follow these steps to contribute:
1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make your changes and commit them with clear messages.
4. Push your branch and create a pull request.
