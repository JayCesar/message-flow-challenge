![banner](https://github.com/JayCesar/message-flow-challenge/assets/44206400/5d6ce1b3-372a-4db3-98f4-143f277e1d7e)

### 🖥️ About
This is a Spring Boot application that simulates a book reselling system, where resellers can request books from a vendor. The application consists of different components that work together to handle the book request, stock management, and logging processes. It uses IBM MQ to put the book requests into a message queue, which ensures reliable communication between different parts of the system

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
