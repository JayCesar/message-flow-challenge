![banner](https://github.com/JayCesar/message-flow-challenge/assets/44206400/5d6ce1b3-372a-4db3-98f4-143f277e1d7e)

<h4 align="center"> 
  <img alt="GitHub Top Language" src="https://img.shields.io/github/languages/top/JayCesar/message-flow-challenge" />
  <img alt="" src="https://img.shields.io/github/repo-size/JayCesar/message-flow-challenge" />
  <img alt="GitHub Last Commit" src="https://img.shields.io/github/last-commit/JayCesar/message-flow-challenge" />
</h4>

<p align="center">
<img alt="banner" align="center" src="http://img.shields.io/static/v1?label=STATUS&message=%20FINISHED&color=GREEN&style=for-the-badge" />
</p>

---

### 🖥️ About
This is a Spring Boot application that simulates a book reselling system, where resellers can request books from a vendor. The application consists of different components that work together to handle the book request, stock management, and logging processes. It uses IBM MQ to put the book requests into a message queue, which ensures reliable communication between different parts of the system

---

### 🚧 Project Structure
```plaintext
src/main/java/com/ibmmq/messageflow
├── controller
│   └── ResellersController.java
├── dto
│   └── ResellerDTO.java
├── model
│   ├── Book.java
│   ├── LoggerModel.java
│   └── Vendor.java
├── service
│   ├── DataGenerationService.java
│   ├── RequestResellersService.java
│   └── SenderService.java
├── MessageflowApplication.java
└── ResellerApp.java
```

#### com.ibmmq.messageflow.controller 
Contains the ResellersController class, which is a REST controller that provides an endpoint for retrieving resellers' information.

#### com.ibmmq.messageflow.dto
Contains the ResellerDTO class, which is a **Data Transfer Object** used to transfer reseller data between the service and controller layers.

#### com.ibmmq.messageflow.model
Contains the Book class, which represents a book in the system, and the Vendor class, which handles book requests and stock management.
Also includes the LoggerModel classes.

#### com.ibmmq.messageflow.service
Contains the RequestResellersService class, which retrieves resellers' information and their book stocks.
Also includes the SenderService class, which simulates resellers sending book requests to the vendor.
The DataGenerationService class is responsible for generating sample data, such as reseller names and book stocks.
#### com.ibmmq.messageflow
Contains the ResellerApp class, which is the main entry point of the Spring Boot application.

---

### 📝 BookHub Class Diagram
```mermaid
classDiagram
    ResellersController --* RequestResellersService
    RequestResellersService ..> ResellerDTO
    ResellerDTO *-- Book
    Vendor *-- Book
    SenderService ..> Vendor
    SenderService ..> DataGenerationService
    Vendor ..> LoggerModel
    SenderService ..> LoggerModel
    DataGenerationService ..> LoggerModel
    class ResellersController {
        -RequestResellersService
        +list() List~ResellerDTO~
    }
    class RequestResellersService {
        +listAllResellers() List~ResellerDTO~
    }
    class ResellerDTO {
        -String name
        -List~Book~ resellerBookStock
    }
    class Book {
        -String id
        -String name
        -int amount
        -double price
    }
    class Vendor {
        -Map~String, Book~ bookStock
        +onMessage(Message, Session)
        +checkBookInStock(String) boolean
        +verifyRequestedAmount(int, Book) boolean
        +calculateDayProfit(double, int)
        +extractDataFromString(String, String) String
        +sendMessageToAnotherQueue(String)
        +calculateNewBookPrice(String) Double
        +updateStock()
        +printDayProfit()
    }
    class SenderService {
        -JmsTemplate jmsTemplate
        -String[] resellerNames
        -List~Book~ resellerBookStock
        +getRandomBook(List~Book~) Book
        +getRandomName(String[]) String
        +generateRandomAmountOfBooks() int
        +checkMessage(Message)
        +sendAndReceive()
        +run()
    }
    class DataGenerationService {
        +generateResellerNames() String[]
        +generateBookStockReseller(Map~String, Book~) List~Book~
        +generateBookStockVendor() Map~String, Book~
    }
    class LoggerModel {
        +LoggerModel(Level, String)
        +LoggerModel(Level, Exception)
        +LoggerModel(Level, String, LocalDateTime)
    }
```

This diagram shows the relationships between the classes involved in the project:

- **```ResellersController```** uses **```RequestResellersService```** to retrieve a list of **```ResellerDTO```** objects.
- **```RequestResellersService```** interacts with **```ResellerDTO```** and **```Book```** to create the JSON list of resellers and their book stock.
- **```ResellerDTO```** contains a reseller's name and a list of **```Book```** objects representing their book stock.
- **```Book```** is a model class representing a book with properties like id, name, amount, and price.
- **```Vendor```** manages the book stock (`Map<String, Book>`) and performs different operations related to book requests and stock updates.
- **```SenderService```** uses **```Vendor```** to receive messages and **```DataGenerationService```** to generate random data.
- **```DataGenerationService```** is responsible for generating reseller names, book stock for resellers, and book stock for the vendor.
- **```LoggerModel```** is a utility class for logging purposes, used by **```Vendor```**, **```SenderService```**, and **```DataGenerationService```**.

