-- Вставка тестовых данных

-- Пароли захешированы (все: "password123")
-- Используется BCrypt с cost factor 10

-- Администратор
INSERT INTO users (username, password_hash, role, email, phone) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Администратор', 'admin@parik.ru', '+7 (999) 123-45-67');

-- Мастера
INSERT INTO users (username, password_hash, role, email, phone) VALUES
('master1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Мастер', 'master1@parik.ru', '+7 (999) 234-56-78'),
('master2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Мастер', 'master2@parik.ru', '+7 (999) 345-67-89');

-- Клиенты
INSERT INTO users (username, password_hash, role, email, phone) VALUES
('client1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Клиент', 'client1@mail.ru', '+7 (999) 456-78-90'),
('client2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Клиент', 'client2@mail.ru', '+7 (999) 567-89-01');

-- Мастера (связь с пользователями)
INSERT INTO masters (name, specialization, experience, rating, user_id) VALUES
('Иванов Иван Иванович', 'Мужские стрижки, бороды', 5, 4.8, 2),
('Петрова Мария Сергеевна', 'Женские стрижки, окрашивание', 8, 4.9, 3);

-- Услуги
INSERT INTO services (name, description, price, duration) VALUES
('Мужская стрижка', 'Классическая мужская стрижка с укладкой', 800.00, 30),
('Женская стрижка', 'Стрижка и укладка для женщин', 1200.00, 45),
('Окрашивание', 'Полное окрашивание волос', 2500.00, 120),
('Стрижка бороды', 'Подравнивание и оформление бороды', 500.00, 20),
('Укладка', 'Укладка волос с использованием профессиональных средств', 600.00, 30);

-- Записи (примеры)
INSERT INTO appointments (client_id, master_id, service_id, date, time, status) VALUES
(4, 1, 1, CURRENT_DATE + INTERVAL '1 day', '10:00:00', 'Запланирована'),
(4, 1, 4, CURRENT_DATE + INTERVAL '1 day', '10:30:00', 'Запланирована'),
(5, 2, 2, CURRENT_DATE + INTERVAL '2 days', '14:00:00', 'Запланирована'),
(5, 2, 3, CURRENT_DATE + INTERVAL '3 days', '11:00:00', 'Запланирована');

-- Отчёты (примеры)
INSERT INTO reports (report_date, total_clients, total_income) VALUES
(CURRENT_DATE - INTERVAL '1 day', 15, 18000.00),
(CURRENT_DATE - INTERVAL '2 days', 12, 15000.00),
(CURRENT_DATE - INTERVAL '7 days', 45, 54000.00);

