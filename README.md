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

____________________________________________________________________________________________

🧰 Tech Stack

Layer                                     Technology
--------------------------------------------------------------------------------------------
Backend                                   Java 21, Spring Boot 3
View                                      Thymeleaf, HTML, CSS, JS
Database                                  MySQL 8
Build                                     Maven
Deployment                                Docker + docker-compose