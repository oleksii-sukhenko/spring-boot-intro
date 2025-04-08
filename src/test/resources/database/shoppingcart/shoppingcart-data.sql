INSERT INTO users (id, email, password, first_name, last_name)
VALUES  (1, 'user1@mail.com', '12345678', 'Firstname1', 'Lastname1'),
        (2, 'user2@mail.com', '12345678', 'Firstname2', 'Lastname2');

INSERT INTO roles (id, role) VALUES (1, 'ROLE_USER');

INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);

INSERT INTO shopping_carts (id, user_id, is_deleted)
VALUES  (1, 1, 0),
        (2, 2, 0);

INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES  (1, 1, 1, 2),
        (2, 1, 2, 1),
        (3, 2, 3, 3);