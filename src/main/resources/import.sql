INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (1, 'Romario', 'Hernandez', 'fabulosos@gmail.com', '2018-12-22', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (2, 'Manuel', 'Aguilar', 'radiacion@gmail.com', '2013-12-09', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (3, 'Julian', 'Marquina', 'radiacion@gmail.com', '2011-11-09', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (4, 'Esther', 'Cadena', 'casa@gmail.com', '2015-02-28', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (5, 'Esteban', 'Ortega', 'casa@gmail.com', '1999-08-18', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (6, 'Carlos', 'Guzman', 'abanderase@gmail.com', '2000-11-18', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (7, 'Oscar', 'Guzman', 'direccion@gmail.com', '2000-11-18', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (8, 'Camilo', 'Lara', 'direccion@gmail.com', '1999-08-18', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (9, 'Jose', 'Rodriguez', 'abanderase@gmail.com', '2019-01-31', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (10, 'Romario', 'Guzman', 'abanderase@gmail.com', '2015-02-28', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (11, 'Jose', 'Ortega', 'abanderase@gmail.com', '2019-01-31', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (12, 'Romario', 'Aguilar', 'radiacion@gmail.com', '2019-01-31', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (13, 'Luis', 'Sotelo', 'abanderase@gmail.com', '1999-08-18', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (14, 'Esther', 'Diaz', 'facturada@gmail.com', '1999-08-18', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (15, 'Jose', 'Reyna', 'radiacion@gmail.com', '2000-11-18', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (16, 'Julian', 'Ortega', 'abanderase@gmail.com', '2015-02-28', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (17, 'Juan', 'Hernandez', 'facturada@gmail.com', '2013-12-09', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (18, 'Jose', 'Rodriguez', 'fabulosos@gmail.com', '2011-11-09', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (19, 'Roberto', 'Diaz', 'fabulosos@gmail.com', '2015-02-28', '');
INSERT INTO CLIENTS (id, name, last_name, email, create_at, photo) VALUES (20, 'Jose', 'Diaz', 'facturada@gmail.com', '2013-12-09', '');

INSERT INTO PRODUCTS(name, price, create_at) values('Apple iPhone 7' , 10345 , NOW() );
INSERT INTO PRODUCTS(name, price, create_at) values('Apple iPad Pro' , 23987 , NOW());
INSERT INTO PRODUCTS(name, price, create_at) values('Samsung Galaxy s9' , 14000 , NOW() );
INSERT INTO PRODUCTS(name, price, create_at) values('Samsung watch' , 7000 , NOW() );
INSERT INTO PRODUCTS(name, price, create_at) values('Xiaomi miA2' , 4599 , NOW() );
INSERT INTO PRODUCTS(name, price, create_at) values('Dell Computer i9' , 453 , NOW());
INSERT INTO PRODUCTS(name, price, create_at) values('Lenovo Lalptop y77' , 13999 , NOW() );
INSERT INTO PRODUCTS(name, price, create_at) values('Sony TV 4k' , 15999 , NOW() );
INSERT INTO PRODUCTS(name, price, create_at) values('Panasonic TV 4k' , 22995 , NOW() );

INSERT INTO INVOICES (description, observation, client_id, create_at) VALUES ('Invoice for office equipament',null, 1, NOW());

INSERT INTO INVOICES_ITEMS (quantity, invoice_id, product_id) VALUES (1, 1, 1);
INSERT INTO INVOICES_ITEMS (quantity, invoice_id, product_id) VALUES (2, 1, 4);
INSERT INTO INVOICES_ITEMS (quantity, invoice_id, product_id) VALUES (1, 1, 5);
INSERT INTO INVOICES_ITEMS (quantity, invoice_id, product_id) VALUES (1, 1, 7);

INSERT INTO INVOICES (description, observation, client_id, create_at) VALUES ('Bike','Some important note',1,NOW());
INSERT INTO INVOICES_ITEMS (quantity, invoice_id, product_id) VALUES (3, 2, 6);


INSERT INTO users (username, password, enabled) VALUES('jose', '$2a$10$O9wxmH/AeyZZzIS09Wp8YOEMvFnbRVJ8B4dmAMVSGloR62lj.yqXG', 1);
INSERT INTO users (username, password, enabled) VALUES('admin', '$2a$10$DOMDxjYyfZ/e7RcBfUpzqeaCs8pLgcizuiQWXPkU35nOhZlFcE9MS', 1);

INSERT INTO authorities (user_id, authority) VALUES(1, 'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES(2, 'ROLE_USER');
INSERT INTO authorities (user_id, authority) VALUES(2, 'ROLE_ADMIN');
