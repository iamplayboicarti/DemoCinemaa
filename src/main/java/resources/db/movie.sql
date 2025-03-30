CREATE DATABASE movie_db;
USE movie_db;

CREATE TABLE movies (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(255),
                        image VARCHAR(255),
                        show_date DATE,
                        status ENUM('upcoming', 'showing', 'ongoing') DEFAULT 'upcoming'
);

CREATE TABLE seats (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       movie_id INT,
                       seat_number VARCHAR(10),
                       status ENUM('empty', 'booked', 'occupied') DEFAULT 'empty',
                       is_couple BOOLEAN DEFAULT FALSE,
                       FOREIGN KEY (movie_id) REFERENCES movies(id)
);

CREATE TABLE orders (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        movie_id INT,
                        seat_ids VARCHAR(255),
                        snack_items VARCHAR(255),
                        total_price DOUBLE,
                        payment_method ENUM('cash', 'transfer'),
                        order_date DATETIME,
                        FOREIGN KEY (movie_id) REFERENCES movies(id)
);

-- Dữ liệu mẫu (10 dòng mỗi bảng) với ngày linh động
INSERT INTO movies (title, image, show_date, status) VALUES
                                                         ('Phim 1', 'movie1.jpg', CURDATE(), 'showing'),
                                                         ('Phim 2', 'movie2.jpg', CURDATE(), 'showing'),
                                                         ('Phim 3', 'movie3.jpg', CURDATE() + INTERVAL 1 DAY, 'upcoming'),
                                                         ('Phim 4', 'movie4.jpg', CURDATE() + INTERVAL 1 DAY, 'ongoing'),
                                                         ('Phim 5', 'movie5.jpg', CURDATE() + INTERVAL 2 DAY, 'showing'),
                                                         ('Phim 6', 'movie6.jpg', CURDATE() + INTERVAL 2 DAY, 'upcoming'),
                                                         ('Phim 7', 'movie7.jpg', CURDATE() + INTERVAL 3 DAY, 'ongoing'),
                                                         ('Phim 8', 'movie8.jpg', CURDATE() + INTERVAL 3 DAY, 'showing'),
                                                         ('Phim 9', 'movie9.jpg', CURDATE() + INTERVAL 4 DAY, 'upcoming'),
                                                         ('Phim 10', 'movie10.jpg', CURDATE() + INTERVAL 4 DAY, 'showing');

INSERT INTO orders (movie_id, seat_ids, snack_items, total_price, payment_method, order_date) VALUES
                                                                                                  (1, 'A2', 'Coca:1,Phomai:1', 150000, 'cash', CURDATE() + INTERVAL 0 DAY + INTERVAL 10 HOUR),
                                                                                                  (1, 'B3', 'Pepsi:2', 100000, 'transfer', CURDATE() + INTERVAL 0 DAY + INTERVAL 11 HOUR),
                                                                                                  (2, 'A2', 'Mirinda:1,Mix:1', 130000, 'cash', CURDATE() + INTERVAL 0 DAY + INTERVAL 12 HOUR),
                                                                                                  (2, 'B3', 'Combo 1 Bắp 2 Nước:1', 70000, 'transfer', CURDATE() + INTERVAL 0 DAY + INTERVAL 13 HOUR),
                                                                                                  (3, 'A1', 'Coca:1', 50000, 'cash', CURDATE() + INTERVAL 1 DAY + INTERVAL 14 HOUR),
                                                                                                  (4, 'A3', 'Phomai:1', 80000, 'transfer', CURDATE() + INTERVAL 1 DAY + INTERVAL 15 HOUR),
                                                                                                  (5, 'B1', 'Combo 2 Bắp 1 Nước:1', 90000, 'cash', CURDATE() + INTERVAL 2 DAY + INTERVAL 16 HOUR),
                                                                                                  (6, 'A4', 'Bơ:1', 60000, 'transfer', CURDATE() + INTERVAL 2 DAY + INTERVAL 17 HOUR),
                                                                                                  (7, 'B2', 'Dâu:1', 70000, 'cash', CURDATE() + INTERVAL 3 DAY + INTERVAL 18 HOUR),
                                                                                                  (8, 'C1', 'Socola:1', 80000, 'transfer', CURDATE() + INTERVAL 3 DAY + INTERVAL 19 HOUR);

-- Thêm 48 ghế cho mỗi phim (movie_id từ 1 đến 10)
INSERT INTO seats (movie_id, seat_number, status, is_couple) VALUES
-- movie_id = 1
(1, 'A1', 'empty', FALSE), (1, 'A2', 'booked', FALSE), (1, 'A3', 'empty', FALSE), (1, 'A4', 'occupied', FALSE),
(1, 'A5', 'empty', FALSE), (1, 'A6', 'booked', FALSE), (1, 'A7', 'empty', FALSE), (1, 'A8', 'occupied', FALSE),
(1, 'B1', 'empty', TRUE), (1, 'B2', 'empty', FALSE), (1, 'B3', 'booked', FALSE), (1, 'B4', 'empty', FALSE),
(1, 'B5', 'occupied', FALSE), (1, 'B6', 'empty', FALSE), (1, 'B7', 'booked', FALSE), (1, 'B8', 'empty', FALSE),
(1, 'C1', 'empty', FALSE), (1, 'C2', 'occupied', FALSE), (1, 'C3', 'empty', FALSE), (1, 'C4', 'booked', FALSE),
(1, 'C5', 'empty', FALSE), (1, 'C6', 'empty', FALSE), (1, 'C7', 'occupied', FALSE), (1, 'C8', 'empty', FALSE),
(1, 'D1', 'booked', FALSE), (1, 'D2', 'empty', FALSE), (1, 'D3', 'empty', TRUE), (1, 'D4', 'occupied', FALSE),
(1, 'D5', 'empty', FALSE), (1, 'D6', 'booked', FALSE), (1, 'D7', 'empty', FALSE), (1, 'D8', 'empty', FALSE),
(1, 'E1', 'occupied', FALSE), (1, 'E2', 'empty', FALSE), (1, 'E3', 'booked', FALSE), (1, 'E4', 'empty', FALSE),
(1, 'E5', 'empty', FALSE), (1, 'E6', 'occupied', FALSE), (1, 'E7', 'empty', FALSE), (1, 'E8', 'booked', FALSE),
(1, 'F1', 'empty', FALSE), (1, 'F2', 'empty', TRUE), (1, 'F3', 'occupied', FALSE), (1, 'F4', 'empty', FALSE),
(1, 'F5', 'booked', FALSE), (1, 'F6', 'empty', FALSE), (1, 'F7', 'empty', FALSE), (1, 'F8', 'occupied', FALSE),
-- movie_id = 2
(2, 'A1', 'empty', FALSE), (2, 'A2', 'booked', FALSE), (2, 'A3', 'empty', FALSE), (2, 'A4', 'occupied', FALSE),
(2, 'A5', 'empty', FALSE), (2, 'A6', 'booked', FALSE), (2, 'A7', 'empty', FALSE), (2, 'A8', 'occupied', FALSE),
(2, 'B1', 'empty', TRUE), (2, 'B2', 'empty', FALSE), (2, 'B3', 'booked', FALSE), (2, 'B4', 'empty', FALSE),
(2, 'B5', 'occupied', FALSE), (2, 'B6', 'empty', FALSE), (2, 'B7', 'booked', FALSE), (2, 'B8', 'empty', FALSE),
(2, 'C1', 'empty', FALSE), (2, 'C2', 'occupied', FALSE), (2, 'C3', 'empty', FALSE), (2, 'C4', 'booked', FALSE),
(2, 'C5', 'empty', FALSE), (2, 'C6', 'empty', FALSE), (2, 'C7', 'occupied', FALSE), (2, 'C8', 'empty', FALSE),
(2, 'D1', 'booked', FALSE), (2, 'D2', 'empty', FALSE), (2, 'D3', 'empty', TRUE), (2, 'D4', 'occupied', FALSE),
(2, 'D5', 'empty', FALSE), (2, 'D6', 'booked', FALSE), (2, 'D7', 'empty', FALSE), (2, 'D8', 'empty', FALSE),
(2, 'E1', 'occupied', FALSE), (2, 'E2', 'empty', FALSE), (2, 'E3', 'booked', FALSE), (2, 'E4', 'empty', FALSE),
(2, 'E5', 'empty', FALSE), (2, 'E6', 'occupied', FALSE), (2, 'E7', 'empty', FALSE), (2, 'E8', 'booked', FALSE),
(2, 'F1', 'empty', FALSE), (2, 'F2', 'empty', TRUE), (2, 'F3', 'occupied', FALSE), (2, 'F4', 'empty', FALSE),
(2, 'F5', 'booked', FALSE), (2, 'F6', 'empty', FALSE), (2, 'F7', 'empty', FALSE), (2, 'F8', 'occupied', FALSE),
-- movie_id = 3
(3, 'A1', 'empty', FALSE), (3, 'A2', 'booked', FALSE), (3, 'A3', 'empty', FALSE), (3, 'A4', 'occupied', FALSE),
(3, 'A5', 'empty', FALSE), (3, 'A6', 'booked', FALSE), (3, 'A7', 'empty', FALSE), (3, 'A8', 'occupied', FALSE),
(3, 'B1', 'empty', TRUE), (3, 'B2', 'empty', FALSE), (3, 'B3', 'booked', FALSE), (3, 'B4', 'empty', FALSE),
(3, 'B5', 'occupied', FALSE), (3, 'B6', 'empty', FALSE), (3, 'B7', 'booked', FALSE), (3, 'B8', 'empty', FALSE),
(3, 'C1', 'empty', FALSE), (3, 'C2', 'occupied', FALSE), (3, 'C3', 'empty', FALSE), (3, 'C4', 'booked', FALSE),
(3, 'C5', 'empty', FALSE), (3, 'C6', 'empty', FALSE), (3, 'C7', 'occupied', FALSE), (3, 'C8', 'empty', FALSE),
(3, 'D1', 'booked', FALSE), (3, 'D2', 'empty', FALSE), (3, 'D3', 'empty', TRUE), (3, 'D4', 'occupied', FALSE),
(3, 'D5', 'empty', FALSE), (3, 'D6', 'booked', FALSE), (3, 'D7', 'empty', FALSE), (3, 'D8', 'empty', FALSE),
(3, 'E1', 'occupied', FALSE), (3, 'E2', 'empty', FALSE), (3, 'E3', 'booked', FALSE), (3, 'E4', 'empty', FALSE),
(3, 'E5', 'empty', FALSE), (3, 'E6', 'occupied', FALSE), (3, 'E7', 'empty', FALSE), (3, 'E8', 'booked', FALSE),
(3, 'F1', 'empty', FALSE), (3, 'F2', 'empty', TRUE), (3, 'F3', 'occupied', FALSE), (3, 'F4', 'empty', FALSE),
(3, 'F5', 'booked', FALSE), (3, 'F6', 'empty', FALSE), (3, 'F7', 'empty', FALSE), (3, 'F8', 'occupied', FALSE),
-- movie_id = 4
(4, 'A1', 'empty', FALSE), (4, 'A2', 'booked', FALSE), (4, 'A3', 'empty', FALSE), (4, 'A4', 'occupied', FALSE),
(4, 'A5', 'empty', FALSE), (4, 'A6', 'booked', FALSE), (4, 'A7', 'empty', FALSE), (4, 'A8', 'occupied', FALSE),
(4, 'B1', 'empty', TRUE), (4, 'B2', 'empty', FALSE), (4, 'B3', 'booked', FALSE), (4, 'B4', 'empty', FALSE),
(4, 'B5', 'occupied', FALSE), (4, 'B6', 'empty', FALSE), (4, 'B7', 'booked', FALSE), (4, 'B8', 'empty', FALSE),
(4, 'C1', 'empty', FALSE), (4, 'C2', 'occupied', FALSE), (4, 'C3', 'empty', FALSE), (4, 'C4', 'booked', FALSE),
(4, 'C5', 'empty', FALSE), (4, 'C6', 'empty', FALSE), (4, 'C7', 'occupied', FALSE), (4, 'C8', 'empty', FALSE),
(4, 'D1', 'booked', FALSE), (4, 'D2', 'empty', FALSE), (4, 'D3', 'empty', TRUE), (4, 'D4', 'occupied', FALSE),
(4, 'D5', 'empty', FALSE), (4, 'D6', 'booked', FALSE), (4, 'D7', 'empty', FALSE), (4, 'D8', 'empty', FALSE),
(4, 'E1', 'occupied', FALSE), (4, 'E2', 'empty', FALSE), (4, 'E3', 'booked', FALSE), (4, 'E4', 'empty', FALSE),
(4, 'E5', 'empty', FALSE), (4, 'E6', 'occupied', FALSE), (4, 'E7', 'empty', FALSE), (4, 'E8', 'booked', FALSE),
(4, 'F1', 'empty', FALSE), (4, 'F2', 'empty', TRUE), (4, 'F3', 'occupied', FALSE), (4, 'F4', 'empty', FALSE),
(4, 'F5', 'booked', FALSE), (4, 'F6', 'empty', FALSE), (4, 'F7', 'empty', FALSE), (4, 'F8', 'occupied', FALSE),
-- movie_id = 5
(5, 'A1', 'empty', FALSE), (5, 'A2', 'booked', FALSE), (5, 'A3', 'empty', FALSE), (5, 'A4', 'occupied', FALSE),
(5, 'A5', 'empty', FALSE), (5, 'A6', 'booked', FALSE), (5, 'A7', 'empty', FALSE), (5, 'A8', 'occupied', FALSE),
(5, 'B1', 'empty', TRUE), (5, 'B2', 'empty', FALSE), (5, 'B3', 'booked', FALSE), (5, 'B4', 'empty', FALSE),
(5, 'B5', 'occupied', FALSE), (5, 'B6', 'empty', FALSE), (5, 'B7', 'booked', FALSE), (5, 'B8', 'empty', FALSE),
(5, 'C1', 'empty', FALSE), (5, 'C2', 'occupied', FALSE), (5, 'C3', 'empty', FALSE), (5, 'C4', 'booked', FALSE),
(5, 'C5', 'empty', FALSE), (5, 'C6', 'empty', FALSE), (5, 'C7', 'occupied', FALSE), (5, 'C8', 'empty', FALSE),
(5, 'D1', 'booked', FALSE), (5, 'D2', 'empty', FALSE), (5, 'D3', 'empty', TRUE), (5, 'D4', 'occupied', FALSE),
(5, 'D5', 'empty', FALSE), (5, 'D6', 'booked', FALSE), (5, 'D7', 'empty', FALSE), (5, 'D8', 'empty', FALSE),
(5, 'E1', 'occupied', FALSE), (5, 'E2', 'empty', FALSE), (5, 'E3', 'booked', FALSE), (5, 'E4', 'empty', FALSE),
(5, 'E5', 'empty', FALSE), (5, 'E6', 'occupied', FALSE), (5, 'E7', 'empty', FALSE), (5, 'E8', 'booked', FALSE),
(5, 'F1', 'empty', FALSE), (5, 'F2', 'empty', TRUE), (5, 'F3', 'occupied', FALSE), (5, 'F4', 'empty', FALSE),
(5, 'F5', 'booked', FALSE), (5, 'F6', 'empty', FALSE), (5, 'F7', 'empty', FALSE), (5, 'F8', 'occupied', FALSE),
-- movie_id = 6
(6, 'A1', 'empty', FALSE), (6, 'A2', 'booked', FALSE), (6, 'A3', 'empty', FALSE), (6, 'A4', 'occupied', FALSE),
(6, 'A5', 'empty', FALSE), (6, 'A6', 'booked', FALSE), (6, 'A7', 'empty', FALSE), (6, 'A8', 'occupied', FALSE),
(6, 'B1', 'empty', TRUE), (6, 'B2', 'empty', FALSE), (6, 'B3', 'booked', FALSE), (6, 'B4', 'empty', FALSE),
(6, 'B5', 'occupied', FALSE), (6, 'B6', 'empty', FALSE), (6, 'B7', 'booked', FALSE), (6, 'B8', 'empty', FALSE),
(6, 'C1', 'empty', FALSE), (6, 'C2', 'occupied', FALSE), (6, 'C3', 'empty', FALSE), (6, 'C4', 'booked', FALSE),
(6, 'C5', 'empty', FALSE), (6, 'C6', 'empty', FALSE), (6, 'C7', 'occupied', FALSE), (6, 'C8', 'empty', FALSE),
(6, 'D1', 'booked', FALSE), (6, 'D2', 'empty', FALSE), (6, 'D3', 'empty', TRUE), (6, 'D4', 'occupied', FALSE),
(6, 'D5', 'empty', FALSE), (6, 'D6', 'booked', FALSE), (6, 'D7', 'empty', FALSE), (6, 'D8', 'empty', FALSE),
(6, 'E1', 'occupied', FALSE), (6, 'E2', 'empty', FALSE), (6, 'E3', 'booked', FALSE), (6, 'E4', 'empty', FALSE),
(6, 'E5', 'empty', FALSE), (6, 'E6', 'occupied', FALSE), (6, 'E7', 'empty', FALSE), (6, 'E8', 'booked', FALSE),
(6, 'F1', 'empty', FALSE), (6, 'F2', 'empty', TRUE), (6, 'F3', 'occupied', FALSE), (6, 'F4', 'empty', FALSE),
(6, 'F5', 'booked', FALSE), (6, 'F6', 'empty', FALSE), (6, 'F7', 'empty', FALSE), (6, 'F8', 'occupied', FALSE),
-- movie_id = 7
(7, 'A1', 'empty', FALSE), (7, 'A2', 'booked', FALSE), (7, 'A3', 'empty', FALSE), (7, 'A4', 'occupied', FALSE),
(7, 'A5', 'empty', FALSE), (7, 'A6', 'booked', FALSE), (7, 'A7', 'empty', FALSE), (7, 'A8', 'occupied', FALSE),
(7, 'B1', 'empty', TRUE), (7, 'B2', 'empty', FALSE), (7, 'B3', 'booked', FALSE), (7, 'B4', 'empty', FALSE),
(7, 'B5', 'occupied', FALSE), (7, 'B6', 'empty', FALSE), (7, 'B7', 'booked', FALSE), (7, 'B8', 'empty', FALSE),
(7, 'C1', 'empty', FALSE), (7, 'C2', 'occupied', FALSE), (7, 'C3', 'empty', FALSE), (7, 'C4', 'booked', FALSE),
(7, 'C5', 'empty', FALSE), (7, 'C6', 'empty', FALSE), (7, 'C7', 'occupied', FALSE), (7, 'C8', 'empty', FALSE),
(7, 'D1', 'booked', FALSE), (7, 'D2', 'empty', FALSE), (7, 'D3', 'empty', TRUE), (7, 'D4', 'occupied', FALSE),
(7, 'D5', 'empty', FALSE), (7, 'D6', 'booked', FALSE), (7, 'D7', 'empty', FALSE), (7, 'D8', 'empty', FALSE),
(7, 'E1', 'occupied', FALSE), (7, 'E2', 'empty', FALSE), (7, 'E3', 'booked', FALSE), (7, 'E4', 'empty', FALSE),
(7, 'E5', 'empty', FALSE), (7, 'E6', 'occupied', FALSE), (7, 'E7', 'empty', FALSE), (7, 'E8', 'booked', FALSE),
(7, 'F1', 'empty', FALSE), (7, 'F2', 'empty', TRUE), (7, 'F3', 'occupied', FALSE), (7, 'F4', 'empty', FALSE),
(7, 'F5', 'booked', FALSE), (7, 'F6', 'empty', FALSE), (7, 'F7', 'empty', FALSE), (7, 'F8', 'occupied', FALSE),
-- movie_id = 8
(8, 'A1', 'empty', FALSE), (8, 'A2', 'booked', FALSE), (8, 'A3', 'empty', FALSE), (8, 'A4', 'occupied', FALSE),
(8, 'A5', 'empty', FALSE), (8, 'A6', 'booked', FALSE), (8, 'A7', 'empty', FALSE), (8, 'A8', 'occupied', FALSE),
(8, 'B1', 'empty', TRUE), (8, 'B2', 'empty', FALSE), (8, 'B3', 'booked', FALSE), (8, 'B4', 'empty', FALSE),
(8, 'B5', 'occupied', FALSE), (8, 'B6', 'empty', FALSE), (8, 'B7', 'booked', FALSE), (8, 'B8', 'empty', FALSE),
(8, 'C1', 'empty', FALSE), (8, 'C2', 'occupied', FALSE), (8, 'C3', 'empty', FALSE), (8, 'C4', 'booked', FALSE),
(8, 'C5', 'empty', FALSE), (8, 'C6', 'empty', FALSE), (8, 'C7', 'occupied', FALSE), (8, 'C8', 'empty', FALSE),
(8, 'D1', 'booked', FALSE), (8, 'D2', 'empty', FALSE), (8, 'D3', 'empty', TRUE), (8, 'D4', 'occupied', FALSE),
(8, 'D5', 'empty', FALSE), (8, 'D6', 'booked', FALSE), (8, 'D7', 'empty', FALSE), (8, 'D8', 'empty', FALSE),
(8, 'E1', 'occupied', FALSE), (8, 'E2', 'empty', FALSE), (8, 'E3', 'booked', FALSE), (8, 'E4', 'empty', FALSE),
(8, 'E5', 'empty', FALSE), (8, 'E6', 'occupied', FALSE), (8, 'E7', 'empty', FALSE), (8, 'E8', 'booked', FALSE),
(8, 'F1', 'empty', FALSE), (8, 'F2', 'empty', TRUE), (8, 'F3', 'occupied', FALSE), (8, 'F4', 'empty', FALSE),
(8, 'F5', 'booked', FALSE), (8, 'F6', 'empty', FALSE), (8, 'F7', 'empty', FALSE), (8, 'F8', 'occupied', FALSE),
-- movie_id = 9
(9, 'A1', 'empty', FALSE), (9, 'A2', 'booked', FALSE), (9, 'A3', 'empty', FALSE), (9, 'A4', 'occupied', FALSE),
(9, 'A5', 'empty', FALSE), (9, 'A6', 'booked', FALSE), (9, 'A7', 'empty', FALSE), (9, 'A8', 'occupied', FALSE),
(9, 'B1', 'empty', TRUE), (9, 'B2', 'empty', FALSE), (9, 'B3', 'booked', FALSE), (9, 'B4', 'empty', FALSE),
(9, 'B5', 'occupied', FALSE), (9, 'B6', 'empty', FALSE), (9, 'B7', 'booked', FALSE), (9, 'B8', 'empty', FALSE),
(9, 'C1', 'empty', FALSE), (9, 'C2', 'occupied', FALSE), (9, 'C3', 'empty', FALSE), (9, 'C4', 'booked', FALSE),
(9, 'C5', 'empty', FALSE), (9, 'C6', 'empty', FALSE), (9, 'C7', 'occupied', FALSE), (9, 'C8', 'empty', FALSE),
(9, 'D1', 'booked', FALSE), (9, 'D2', 'empty', FALSE), (9, 'D3', 'empty', TRUE), (9, 'D4', 'occupied', FALSE),
(9, 'D5', 'empty', FALSE), (9, 'D6', 'booked', FALSE), (9, 'D7', 'empty', FALSE), (9, 'D8', 'empty', FALSE),
(9, 'E1', 'occupied', FALSE), (9, 'E2', 'empty', FALSE), (9, 'E3', 'booked', FALSE), (9, 'E4', 'empty', FALSE),
(9, 'E5', 'empty', FALSE), (9, 'E6', 'occupied', FALSE), (9, 'E7', 'empty', FALSE), (9, 'E8', 'booked', FALSE),
(9, 'F1', 'empty', FALSE), (9, 'F2', 'empty', TRUE), (9, 'F3', 'occupied', FALSE), (9, 'F4', 'empty', FALSE),
(9, 'F5', 'booked', FALSE), (9, 'F6', 'empty', FALSE), (9, 'F7', 'empty', FALSE), (9, 'F8', 'occupied', FALSE),
-- movie_id = 10
(10, 'A1', 'empty', FALSE), (10, 'A2', 'booked', FALSE), (10, 'A3', 'empty', FALSE), (10, 'A4', 'occupied', FALSE),
(10, 'A5', 'empty', FALSE), (10, 'A6', 'booked', FALSE), (10, 'A7', 'empty', FALSE), (10, 'A8', 'occupied', FALSE),
(10, 'B1', 'empty', TRUE), (10, 'B2', 'empty', FALSE), (10, 'B3', 'booked', FALSE), (10, 'B4', 'empty', FALSE),
(10, 'B5', 'occupied', FALSE), (10, 'B6', 'empty', FALSE), (10, 'B7', 'booked', FALSE), (10, 'B8', 'empty', FALSE),
(10, 'C1', 'empty', FALSE), (10, 'C2', 'occupied', FALSE), (10, 'C3', 'empty', FALSE), (10, 'C4', 'booked', FALSE),
(10, 'C5', 'empty', FALSE), (10, 'C6', 'empty', FALSE), (10, 'C7', 'occupied', FALSE), (10, 'C8', 'empty', FALSE),
(10, 'D1', 'booked', FALSE), (10, 'D2', 'empty', FALSE), (10, 'D3', 'empty', TRUE), (10, 'D4', 'occupied', FALSE),
(10, 'D5', 'empty', FALSE), (10, 'D6', 'booked', FALSE), (10, 'D7', 'empty', FALSE), (10, 'D8', 'empty', FALSE),
(10, 'E1', 'occupied', FALSE), (10, 'E2', 'empty', FALSE), (10, 'E3', 'booked', FALSE), (10, 'E4', 'empty', FALSE),
(10, 'E5', 'empty', FALSE), (10, 'E6', 'occupied', FALSE), (10, 'E7', 'empty', FALSE), (10, 'E8', 'booked', FALSE),
(10, 'F1', 'empty', FALSE), (10, 'F2', 'empty', TRUE), (10, 'F3', 'occupied', FALSE), (10, 'F4', 'empty', FALSE),
(10, 'F5', 'booked', FALSE), (10, 'F6', 'empty', FALSE), (10, 'F7', 'empty', FALSE), (10, 'F8', 'occupied', FALSE);

SELECT COUNT(*) FROM seats WHERE movie_id = 9; -- Phải ra 48
SELECT COUNT(*) FROM seats WHERE movie_id = 10; -- Phải ra 48
SELECT * FROM seats WHERE seat_number = 'C5' AND movie_id = 9; -- Phải có ghế C5