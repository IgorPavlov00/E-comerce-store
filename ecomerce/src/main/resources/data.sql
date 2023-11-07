-- INSERT INTO jeans (name, description, brand, size, color, price, gender, image_path)
-- VALUES ('Levi''s 30 Khaki Jeans', 'Stylish Levi''s khaki jeans', 'Levi''s', '30', 'Khaki', 80.00, 'male', 'assets/images/levis-30-khaki.jpg');
-- INSERT INTO jeans (name, description, brand, size, color, price, gender, image_path)
-- VALUES ('Levi''s 31 Taper Jeans', 'Levi''s tapered fit jeans', 'Levi''s', '31', 'Blue', 90.00, 'male', 'assets/images/levis-31-taper.jpg');
-- INSERT INTO jeans (name, description, brand, size, color, price, gender, image_path)
-- VALUES ('Levi''s 32 Cargo Jeans', 'Levi''s cargo style jeans', 'Levi''s', '32', 'Green', 95.00, 'male', 'assets/images/levis-32-cargo.jpg');
-- INSERT INTO jeans (name, description, brand, size, color, price, gender, image_path)
-- VALUES ('Levi''s 32 Cargo Taper Jeans', 'Levi''s cargo style tapered fit jeans', 'Levi''s', '32', 'Olive', 100.00, 'male', 'assets/images/levis-32-cargo-taper.jpg');
-- INSERT INTO jeans (name, description, brand, size, color, price, gender, image_path)
-- VALUES ('Levi''s 32 Corduroy Jeans', 'Levi''s corduroy texture jeans', 'Levi''s', '32', 'Brown', 85.00, 'male', 'assets/images/levis-32-corduroy.jpg');
-- INSERT INTO jeans (name, description, brand, size, color, price, gender, image_path)
-- VALUES ('Levi''s 32 Hybrid Cargo Jeans', 'Levi''s hybrid cargo style jeans', 'Levi''s', '32', 'Black', 95.00, 'male', 'assets/images/levis-32-hybrid-cargo.jpg');
-- INSERT INTO jeans (name, description, brand, size, color, price, gender, image_path)
-- VALUES ('Denim 38 Oversize Jeans', 'Oversized denim jeans', 'Denim', '38', 'Blue', 90.00, 'male', 'assets/images/denim-38-oversize.jpg');
-- INSERT INTO jeans (name, description, brand, size, color, price, gender, image_path)
-- VALUES ('Denim 32 Fit Jeans', 'Perfect fit denim jeans', 'Denim', '32', 'Blue', 85.00, 'male', 'assets/images/denim-32-fit.jpg');
-- INSERT INTO jeans (name, description, brand, size, color, price, gender, image_path)
-- VALUES ('Diesel 34 Grey Jeans', 'Stylish Diesel grey jeans', 'Diesel', '34', 'Grey', 95.00, 'male', 'assets/images/diesel_34_grey.jpg');
-- INSERT INTO shoes (name, description, brand, size, color, price, gender, image_path)
-- VALUES ('Air 1 Jordan mid', 'Smokey grey', 'Jordan', '42', 'Grey', 95.00, 'male', 'assets/images/jordan-1.jpg');
-- INSERT INTO shoes (name, description, brand, size, color, price, gender, image_path)
-- VALUES ('Air 1 Jordan high', 'University blue', 'Jordan', '44', 'Blue', 80.00, 'male', 'assets/images/jordan-2-blue.jpg');
-- INSERT INTO shoes (name, description, brand, size, color, price, gender, image_path)
-- VALUES ('Air 1 Jordan high', 'Spiderman:Origin Story', 'Jordan', '43', 'Red', 100.00, 'male', 'assets/images/jordan-3-red.jpg');
--
-- -- Create a sequence generator for auto-generated IDs
-- CREATE SEQUENCE global_sequence;
--
-- -- Create the jeans table
-- CREATE TABLE jeans (
--                        id bigint default nextval('global_sequence') not null,
--                        custom_id varchar(255) not null,
--                        name varchar(255) not null,
--                        description varchar(255),
--                        brand varchar(255),
--                        size varchar(10),
--                        color varchar(50),
--                        price decimal(10,2),
--                        gender varchar(10),
--                        image_path varchar(255),
--                        primary key (id)
-- );
--
-- -- Create the shirts table
-- CREATE TABLE shirts (
--                         id bigint default nextval('global_sequence') not null,
--                         custom_id varchar(255) not null,
--                         name varchar(255) not null,
--                         description varchar(255),
--                         brand varchar(255),
--                         size varchar(10),
--                         color varchar(50),
--                         price decimal(10,2),
--                         gender varchar(10),
--                         image_path varchar(255),
--                         primary key (id)
-- );
--
-- -- Create the shoes table
-- CREATE TABLE shoes (
--                        id bigint default nextval('global_sequence') not null,
--                        custom_id varchar(255) not null,
--                        name varchar(255) not null,
--                        description varchar(255),
--                        brand varchar(255),
--                        size varchar(10),
--                        color varchar(50),
--                        price decimal(10,2),
--                        gender varchar(10),
--                        image_path varchar(255),
--                        primary key (id)
-- );
--
-- -- Insert data into jeans table
-- INSERT INTO jeans (id, custom_id, name, description, brand, size, color, price, gender, image_path)
-- VALUES
--     (1, 'J1000', 'Levi''s 30 Khaki Jeans', 'Stylish Levi''s khaki jeans', 'Levi''s', '30', 'Khaki', 80.00, 'male', 'assets/images/levis-30-khaki.jpg')
-- -- Add more jeans data here...
--
-- -- Insert data into shirts table
-- INSERT INTO shirts (id, custom_id, name, description, brand, size, color, price, gender, image_path)
-- VALUES
--     (2, 'S2000', 'Casual Red Shirt', 'Comfortable red shirt', 'Example Brand', 'M', 'Red', 40.00, 'male', 'assets/images/casual-red-shirt.jpg')
-- -- Add more shirts data here...
--
-- -- Insert data into shoes table
-- INSERT INTO shoes (id, custom_id, name, description, brand, size, color, price, gender, image_path)
-- VALUES
--     (3, 'SH3000', 'Sneakers', 'Comfortable sneakers', 'Nike', '42', 'Black', 90.00, 'male', 'assets/images/sneakers.jpg');
-- -- Add more shoes data here...
-- Insert statements for jeans
INSERT INTO jeans (name, description, brand, size, color, price,type, gender, image_path)
VALUES ('Levi''s 30 Khaki Jeans', 'Stylish Levi''s khaki jeans', 'Levi''s', '30', 'Khaki', 80.00,'jean', 'male', 'assets/images/levis-30-khaki.jpg');

INSERT INTO jeans (name, description, brand, size, color, price,type, gender, image_path)
VALUES ('Levi''s 31 Taper Jeans', 'Levi''s tapered fit jeans', 'Levi''s', '31', 'Blue', 90.00,'jean', 'male', 'assets/images/levis-31-taper.jpg');

INSERT INTO jeans (name, description, brand, size, color, price,type, gender, image_path)
VALUES ('Levi''s 32 Cargo Jeans', 'Levi''s cargo style jeans', 'Levi''s', '32', 'Green', 95.00,'jean', 'male', 'assets/images/levis-32-cargo.jpg');

INSERT INTO jeans (name, description, brand, size, color, price,type, gender, image_path)
VALUES ('Levi''s 32 Hybrid Cargo Jeans', 'Levi''s hybrid cargo style jeans', 'Levi''s', '32', 'Black', 95.00,'jean', 'male', 'assets/images/levis-32-hybrid-cargo.jpg');
-- Add more insert statements for jeans as needed

-- Insert statements for shoes
INSERT INTO shoes (name, description, brand, size, color, price,type, gender, image_path)
VALUES ('Air 1 Jordan mid', 'Smokey grey', 'Jordan', '42', 'Grey', 95.00, 'shoe','male', 'assets/images/jordan-1.jpg');

INSERT INTO shoes (name, description, brand, size, color, price,type, gender, image_path)
VALUES ('Air 1 Jordan high', 'University blue', 'Jordan', '44', 'Blue', 80.00,'shoe', 'male', 'assets/images/jordan-2-blue.jpg');

INSERT INTO shoes (name, description, brand, size, color, price,type, gender, image_path)
VALUES ('Air 1 Jordan high', 'Spiderman:Origin Story', 'Jordan', '43', 'Red', 100.00, 'shoe','male', 'assets/images/jordan-3-red.jpg');

-- Add more insert statements for shoes as needed
