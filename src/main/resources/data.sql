INSERT INTO author (biography, name, photo_url) VALUES
('Hier sollte die Biografie von Fjodor Dostojewski stehen.','Fedor Dostojewski','/uploads/authors/dostoevski.jpeg'),
('Hier sollte die Biografie von Wilhelm Hauff stehen.','Wilhelm Hauff','/uploads/authors/hauff.jpeg'),
('Hier sollte die Biografie von Robert A. Heinlein stehen.','Robert A. Heinlein','/uploads/authors/heinlein.jpeg'),
('Hier sollte die Biografie von Arthur Conan Doyle stehen.','Arthur Conan Doyle','/uploads/authors/conandoyle.jpeg');

INSERT INTO book (genre, image_book, isbn, price, slug, title, toggled_book, author_id) VALUES
('Klassik','/uploads/books/Schuld und Sühne.jpeg',23567,25.5,'schuld-und-suehne','Schuld und Sühne',true,1),
('Kinderbuch','/uploads/books/Der Zwerg Nase.jpeg',654549,12.23,'der-zwerg-nase','Der Zwerg Nase',true,2),
('Science-Fiction', '/uploads/books/Bewohner der Milchstraße.jpeg', 583372, 13.23, 'bewohner-der-milchstrasse', 'Bewohner der Milchstraße', true, 3),
('Detektivroman', '/uploads/books/Der Hund von Baskervill.jpeg', 45576, 10.38, 'der-hund-von-baskervill', 'Der Hund von Baskervill', true, 4);

INSERT INTO book_description(description, book_id) VALUES
('Hier sollte die Beschreibung des Buchs Schuld und Sühne.',1),
('Hier sollte die Beschreibung des Buchs Der Zwerg Nase.',2),
('Hier sollte die Beschreibung des Buchs Bewohner der Milchstraße.',3),
('Hier sollte die Beschreibung des Buchs Der Hund von Baskervill.',4);

INSERT INTO stock(last_update, one_book_price, quantity, stock_status, total_price, book_id) VALUES
('2025-12-12 00:00:00', 25.5, 10, 'NEW', 255.0, 1),
('2025-12-12 00:00:00', 12.23, 10, 'NEW', 122.3, 2),
('2025-12-12 00:00:00', 13.23, 10, 'NEW', 132.3, 3),
('2025-12-12 00:00:00', 10.38, 10, 'NEW', 103.8, 4);