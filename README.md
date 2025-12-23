📚 Buchladen-Online BookStore

A simple online bookstore built with Spring Boot, Thymeleaf, MySQL and Docker.
Users can register, browse books, view authors, read first pages of books and leave feedbacks.
Admin can manage books, authors, categories, and upload images. THe app is bilingual: English
and German. This project simulates how an online bookstore works. It does **not** sell and 
contains **no real payment integration**.

______________________________________________________________________________________________

🚀 Features

👤 User Features (Simulated)
    
  - User registration & login
  - Browse books and authors
  - View books details
  - Read first pages of each book
  - Leave a feedbacks to every books in the shop
  - Read feedbacks of other users
  - Send request to the admin about damaging, changing or returning of book
  - User can read a chat with admin
  - Adding books to cart
  - Creating an order
  - Sending confirmation email
  - Simulated payment selection:
     - PayPal (mock screen, no real payment)
     - Invoice (demo PDF generation)
  - Simulated return/refund process

🛠 Admin Features

  - Add/edit/delete books
  - Add/edit/delete authors
  - Upload book and author images
  - Manage categories
  - Manage users
  - Manage simulated book stock
  - Reply on user's requests
  - Look through orders, sold books

⚙️ Technical Features
  - Spring Boot MVC(Thymeleaf)
  - MySQL with Docker
  - Image uploads stored on host machine
  - REST endpoints for serving images
  - Secure login (Spring Security)

# 🏃Starting the application with Docker
1. **Install Docker Desktop**
   Download and install the Docker from https://www.docker.com/products/docker-desktop.
2. **(Optional) Install MySQL**
   ⚠️ If you run the application **with Docker Compose**, MySQL is already included as a container.
    You only need to install MySQL locally if you want to connect to the database outside Docker.
3. **Download the project**
    - Download the project ZIP from Github
    - Extract it to a folder on your computer
4. **Configure environmental variables**
    The application uses environmental variables defined in a .env file.
    - Copy the template file:
      cp your.env .env
    - Open .env and set all required values (database credentials, API keys, etc)
   The .env file is referenced in docker-compose.yml and will be loaded automatically.
5. **Start Docker Desktop**
    Make sure Docker Desktop is running on your computer.
6. **Start the application**
    Open a terminal in the project root directory and run:
    docker compose up --build
7. **Open the application in your browser**
    Once the container are running, open:
    http://localhost:8080/home

# ✅ Notes
   - Default application port is 8080 (can be changed in .env or docker-compose.yml)
   - Database data is stored in Docker volumes are persists between restarts
   - To stop the application, press Ctrl + C or run:
    docker compose down

# 📚 **Default data(Demo content)**
    After the application starts for the first time, the database is automatically populated
    with default books and stock data.
    This demo content allows you to:
    - Browse available books
    - View book details
    - Test stock management
    - Explore the mail features of the application without manual setup
    
    The default data is intended only for demonstration and learning purposes and
    can be modified or removed at any time.
    💡 This makes it easy for new users to quickly understand how the application works.

____________________________________________________________________________________________

🧰 Tech Stack

Layer                                     Technology
--------------------------------------------------------------------------------------------
Backend                                   Java 21, Spring Boot 3
View                                      Thymeleaf, HTML, CSS, JS
Database                                  MySQL 8
Build                                     Maven
Deployment                                Docker + docker-compose