# ecommerce-wishlist
Wishlist REST API for E-Commerce

# E-Commerce Wishlist Application

## Overview

This Java-based e-commerce wishlist application allows users to manage their wishlist by adding, removing, and checking products.

## Features

- Add a product to the customer's wishlist;
- Remove a product from the customer's wishlist;
- View all products in the customer's wishlist;
- Check if a specific product is on the customer's wishlist.

## Prerequisites

Before running the application, make sure you have the following:

- Java 17
- Maven
- MongoDB instance running

## Getting Started

1. **Clone the repository:**

```bash
git clone https://github.com/yourusername/e-commerce-wishlist.git
cd e-commerce-wishlist
```

2. **Build the project:**

```bash
mvn clean install
```

3. **Run the application:**

```bash
java -jar target/e-commerce-wishlist.jar
```

Make sure to replace e-commerce-wishlist.jar with the actual JAR file generated.

Access the application endpoints at http://localhost:8080/

Configuration
The MongoDB URI can be configured using the MONGO_URI environment variable. Update the value in your system or set it in your IDE's run configuration.

Set the MongoDB URI as an environment variable:

```bash
MONGO_URI=mongodb+srv://<username>:<password>@<cluster-url>/<database>?retryWrites=true&w=majority
```

Contributing
Feel free to contribute to the project by opening issues or submitting pull requests.

License
This project is licensed under the MIT License.   
