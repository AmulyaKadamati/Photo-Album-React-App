# Photo Album React App

This project is a photo album management web application built with React (frontend) and Spring Boot (backend). Users can create albums, upload photos, view, download, and delete images, all with secure authentication.

## Features
- User authentication (JWT-based)
- Create, edit, and delete albums
- Upload, view, download, and delete photos
- Responsive Material UI dashboard
- REST API integration with Spring Boot backend

## Technology Stack
- React (Frontend library)
- Material UI (UI components and styling)
- Redux Toolkit (State management)
- Axios (HTTP client for API requests)
- Spring Boot (Backend REST API)
- JWT (Authentication)

## Getting Started

### Backend (Spring Boot)
1. Make sure you have Java 11+ and Maven or Gradle installed.
2. Clone or download the backend project.
3. In the backend directory, run:
   ```sh
   ./mvnw spring-boot:run
   ```
   or
   ```sh
   ./gradlew bootRun
   ```
4. The backend will start on [http://localhost:8080](http://localhost:8080) by default.

### Frontend (React)
1. Make sure you have Node.js and npm (or yarn) installed.
2. In the `Frontend` directory, run:
   ```sh
   npm install
   # or
   yarn
   ```
3. Start the React development server:
   ```sh
   npm start
   # or
   yarn start
   ```
4. The app will be available at [http://localhost:3000](http://localhost:3000).

### Configuration
- The frontend is set up to proxy API requests to the backend at `http://localhost:8080`.
- If you want to change the frontend port, create a `.env` file in the `Frontend` directory and add:
  ```
  PORT=4000
  ```

### Usage
- Open [http://localhost:3000](http://localhost:3000) in your browser.
- Register or log in to start creating albums and uploading photos.

## Credits
- Built using the Mantis Free React Material UI Dashboard Template.

## License
MIT License
