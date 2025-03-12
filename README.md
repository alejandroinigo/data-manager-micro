# Data Manager Microservice

This is a backend microservice built with Spring Boot, designed to retrieve and process data from Google Cloud Storage, providing filtering, sorting, and pagination capabilities.

## About The Project

This application receives REST API requests, fetches data from Google Cloud Storage, and applies filtering, sorting, and pagination before returning the results.

**Key Features:**

* **Data Retrieval:** Fetches data from Google Cloud Storage.
* **Filtering:** Allows filtering data by name and status.
* **Sorting:** Enables sorting data by ID, name, or creation date (CreatedOn).
* **Pagination:** Supports pagination with a configurable page size.

### Built With

* Java version "21.0.6" LTS
* IntelliJ IDEA
* Spring Boot
* Maven

## Getting Started

Follow these steps to set up the project locally.

### Prerequisites

* Install JDK version "21.0.6".

### Installation

1.  **Clone the Repository (or Unzip):**
    * If you downloaded a zip file, extract it and navigate to the directory in your terminal using `cd <directory_name>`.
    * Alternatively, clone the repository from GitHub:

    ```sh
    git clone [https://github.com/alejandroinigo/data-manager-micro.git](https://github.com/alejandroinigo/data-manager-micro.git)
    ```

2.  **Build the Project:**
    * Navigate to the project directory in your terminal and run:

    ```sh
    mvnw clean install # For Windows
    ```

3.  **Start the Application:**
    * Run the following command to start the local development server:

    ```sh
    mvnw spring-boot:run # For Windows
    ```

## Usage

Send GET requests to:
http://localhost:8080/api/records

**Available Parameters:**

* `name`: Free text search by name.
* `status`: Filter by status (e.g., `completed`, `canceled`, `error`).
* `page`: Page number (starting from 1).
* `pageSize`: Number of records per page.
* `sortBy`: Field to sort by (`id`, `name`, `createdon`).
* `sortOrder`: Sort order (`asc` or `desc`).

### Swagger

* **Swagger (OpenAPI):** Used Swagger/OpenAPI to document the REST API.
    * The API contract is defined in the project file: `src/main/resources/contract.yaml`.

### Sample Requests

* `http://localhost:8080/api/records?page=1&pageSize=20`
* `http://localhost:8080/api/records?name=vibrant_hypatia&status=COMPLETED&page=1&pageSize=20`
* `http://localhost:8080/api/records?page=1&pageSize=20&sortBy=name&sortOrder=asc`