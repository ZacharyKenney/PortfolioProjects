BEGIN TRANSACTION;

DROP TABLE IF EXISTS maintenance_request;
DROP TABLE IF EXISTS payment;
DROP TABLE IF EXISTS rent_cycle;
DROP TABLE IF EXISTS lease;
DROP TABLE IF EXISTS property_feature;
DROP TABLE IF EXISTS property_landlord;
DROP TABLE IF EXISTS feature;
DROP TABLE IF EXISTS unit;
DROP TABLE IF EXISTS property;
DROP TABLE IF EXISTS users;



CREATE TABLE users (
  user_id serial PRIMARY KEY,
  email varchar(255) NOT NULL UNIQUE,     -- Email
  first_name varchar(255) NOT NULL,
  last_name varchar(255) NOT NULL,
  phone_number varchar(14) NOT NULL,
  password varchar(32) NOT NULL,      -- Password
  salt varchar(256) NOT NULL,          -- Password Salt
  role varchar(255) NOT NULL default('renter'),
  CHECK (role IN ('renter', 'landlord', 'maintenance', 'admin'))
);

CREATE TABLE property (
  property_id serial PRIMARY KEY,
  landlord_id integer NOT NULL,
  street_address varchar(255) NOT NULL UNIQUE,
  city varchar(255) NOT NULL,
  state varchar(255) NOT NULL,
  zip_code varchar(10) NOT NULL,
  property_name varchar(255),
  photo_path varchar(255) NOT NULL,
  location varchar(255) NOT NULL,
  CHECK (location IN ('Downtown', 'East Side', 'West Side', 'North Side', 'NKY')),
  CONSTRAINT property_fk_landlord_id FOREIGN KEY(landlord_id) REFERENCES users(user_id)
);

CREATE TABLE unit ( 
  unit_id serial PRIMARY KEY, 
  unit_number varchar(20) NOT NULL, 
  property_id integer NOT NULL, 
  bed_count integer NOT NULL, 
  bath_count decimal NOT NULL, 
  price decimal NOT NULL, 
  sq_ft integer NOT NULL, 
  is_available boolean NOT NULL,
  CONSTRAINT unit_fk_property_id FOREIGN KEY(property_id) REFERENCES property(property_id) 
);

CREATE TABLE feature (
  feature_id serial PRIMARY KEY,
  feature_name varchar(255) NOT NULL
);

CREATE TABLE property_feature (
  property_id integer,
  feature_id integer,
  CONSTRAINT property_feature_pk PRIMARY KEY(property_id, feature_id),
  CONSTRAINT prop_feature_fk_prop_id FOREIGN KEY(property_id) REFERENCES property(property_id),
  CONSTRAINT prop_feature_fk_feature_id FOREIGN KEY(feature_id) REFERENCES feature(feature_id)
);

CREATE TABLE lease (
  lease_id serial PRIMARY KEY,
  user_id integer NOT NULL,
  unit_id integer NOT NULL,
  signed_date date NOT NULL,
  start_date date NOT NULL,
  rent_length integer NOT NULL,
  rent_amount decimal NOT NULL,
  late_fee decimal NOT NULL,
  overdue_balance decimal NOT NUll default(0),
  status varchar(255) NOT NULL,
  CONSTRAINT lease_fk_user_id FOREIGN KEY(user_id) REFERENCES users(user_id),
  CONSTRAINT lease_fk_unit_id FOREIGN KEY(unit_id) REFERENCES unit(unit_id),
  CHECK (status IN ('Active', 'Expired', 'Terminated'))
);

CREATE TABLE rent_cycle (
  rent_cycle_id serial PRIMARY KEY,
  lease_id integer NOT NULL,
  start_date date NOT NULL,
  balance decimal NOT NULL,
  due_date date NOT NULL,
  rent_status varchar(255) NOT NULL,
  CONSTRAINT rent_cycle_fk_lease_id FOREIGN KEY(lease_id) REFERENCES lease(lease_id)
);

CREATE TABLE payment (
  payment_id serial PRIMARY KEY,
  rent_cycle_id integer NOT NULL,
  amount_paid decimal NOT NULL,
  date_paid timestamp NOT NULL,
  CONSTRAINT payments_fk_rent_cycle_id FOREIGN KEY(rent_cycle_id) REFERENCES rent_cycle(rent_cycle_id)
);

CREATE TABLE maintenance_request (
  request_id serial NOT NULL PRIMARY KEY,     
  unit_id integer NOT NULL,     
  request_user_id integer NOT NULL,     
  request_desc varchar(1000) NOT NULL,     
  priority integer NOT NULL,
  date_requested timestamp NOT NULL,     
  assigned_user_id integer,     
  is_completed boolean NOT NULL default(false),     
  CONSTRAINT fk_maintenance_requests_unit FOREIGN KEY(unit_id) REFERENCES unit(unit_id), 
  CONSTRAINT fk_maintenance_requests_requested FOREIGN KEY(request_user_id) REFERENCES users(user_id),
  CONSTRAINT fk_maintenance_requests_assigned FOREIGN KEY(assigned_user_id) REFERENCES users(user_id)
);


INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('admin@gmail.com', 'Abode', 'Admin', '(513) 000-0315', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'admin');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('david_pc@gmail.com', 'David', 'Pfaltzgraff-Carlson', '(859) 600-6958', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'maintenance');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('katie_dwyer@gmail.com', 'Katie', 'Dwyer', '(859) 600-6958', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'maintenance');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('joe_riggs@gmail.com', 'Joe', 'Riggs', '(859) 600-6958', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'maintenance');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('angie_auel@gmail.com', 'Angie', 'Auel', '(313) 000-0313', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'landlord');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('evan_white@gmail.com', 'Evan', 'White', '(310) 103-0031', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'landlord');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('rebecca_fox@gmail.com', 'Rebecca', 'Fox', '(702) 277-0720', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'landlord');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('drew_cook@gmail.com', 'Drew', 'Cook', '(949) 494-9944', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'landlord');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('zach_kenney@gmail.com', 'Zach', 'Kenney', '(513) 433-315', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'landlord');

INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('joe_security@gmail.com', 'Joe', 'Melon', '1234567890', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'renter');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('steve_boomer@gmail.com', 'Steve', 'Stier', '1234567890', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'renter');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('sweet_baby_trent@gmail.com', 'Trent', 'Corrill', '1234567890', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'renter');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('rock_dukakis@gmail.com', 'Rock', 'Dukakis', '1234567890', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'renter');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('kristas_bakery@gmail.com', 'Krista', 'Lewis', '1234567890', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'renter');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('isabell_the_dog@gmail.com', 'Doge', 'Isabell', '1234567890', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'renter');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('tom_nook@gmail.com', 'Tom', 'Nook', '1234567890', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'renter');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('rita_stall@gmail.com', 'Rita', 'Stall', '1234567890', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'renter');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('jessica_hall@gmail.com', 'Jessica', 'Hall', '1234567890', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'renter');
INSERT INTO users (email, first_name, last_name, phone_number, password, salt, role) VALUES ('jessica_weatherly@gmail.com', 'J', 'Madd', '1234567890', 'nRNoeCGP0JropBPpDDfIcQ==', 'mbaSqD88vwLEkM/GKKwTOKUWJEcpzZoyvrRz8R2Z1sC1OLLUHdg53/ylzLDq9waiweLaZLigBNDtLYgdKH6kDEEuth5wisTwoS1b/yewubMfnMS+gpQuCj+haoVlJ/zYXM4lEDA73LLkjE6Jp1Em++DD7491T5qJxfwVB4o8nyI=', 'renter');

INSERT INTO property (landlord_id, street_address, city, state, zip_code, property_name, photo_path, location) VALUES (5, '630 Vine St', 'Cincinnati', 'OH', '45202', 'The Provident Apartments' , '1/630VineSt-01.jpg', 'Downtown');
INSERT INTO property (landlord_id, street_address, city, state, zip_code, property_name, photo_path, location) VALUES (5, '2251 Washington Ave', 'Cincinnati', 'OH', '45212', '2251 Washington Ave' , '2/2251WashingtonAve-01.jpg', 'East Side');
INSERT INTO property (landlord_id, street_address, city, state, zip_code, property_name, photo_path, location) VALUES (5, '5601 Viewpointe', 'Cincinnati', 'OH', '45213', 'Hilltop Apartments' , '3/5601Viewpointe-01.jpg', 'North Side');
INSERT INTO property (landlord_id, street_address, city, state, zip_code, property_name, photo_path, location) VALUES (5, '1500 Groesbeck Rd', 'Cincinnati', 'OH', '45224', 'The Bradford Apartments' , '4/1500GroesbeckRd-01.jpg', 'North Side');
INSERT INTO property (landlord_id, street_address, city, state, zip_code, property_name, photo_path, location) VALUES (5, '2496 Queen City Ave', 'Cincinnati', 'OH', '45238', 'Lisa Ridge Apartments' , '5/2496QueenCityAve-01.jpg', 'West Side');
INSERT INTO property (landlord_id, street_address, city, state, zip_code, property_name, photo_path, location) VALUES (5, '631 Vine St', 'Cincinnati', 'OH', '45202', 'The Majestic Apartments' , 'https://m.foolcdn.com/media/millionacres/images/hero__S9fnzmn.width-1200.jpg', 'Downtown');
INSERT INTO property (landlord_id, street_address, city, state, zip_code, property_name, photo_path, location) VALUES (5, '2252 Washington Ave', 'Cincinnati', 'OH', '45212', '2252 Washington Ave' , 'https://homefinderimages.s3.amazonaws.com/cms/articles/1007/1571163764.8921_rental-home.jpg', 'East Side');
INSERT INTO property (landlord_id, street_address, city, state, zip_code, property_name, photo_path, location) VALUES (5, '5608 Viewpointe', 'Cincinnati', 'OH', '45213', 'Valleyway Apartments' , 'https://i2.wp.com/www.buildingsaltlake.com/wp-content/uploads/2019/11/Dixon-View-1_Edit_sm-002.jpg?fit=1941%2C1142&ssl=1', 'North Side');
INSERT INTO property (landlord_id, street_address, city, state, zip_code, property_name, photo_path, location) VALUES (5, '1501 Groesbeck Rd', 'Cincinnati', 'OH', '45224', 'The Elevate Apartments' , 'https://www.nextpittsburgh.com/wp-content/uploads/2020/01/214-Craig-Rendering-copy-scaled-e1578249535877.jpg', 'North Side');
INSERT INTO property (landlord_id, street_address, city, state, zip_code, property_name, photo_path, location) VALUES (5, '2497 Queen City Ave', 'Cincinnati', 'OH', '45238', 'Boulevard Apartments' , 'https://bloximages.chicago2.vip.townnews.com/bozemandailychronicle.com/content/tncms/assets/v3/editorial/e/c2/ec28ac82-d5da-5232-9035-04fff014d3ed/5dc4a6674b0dc.image.jpg?resize=1200%2C801', 'West Side');

INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('0211', 1, 0, 1, 1040, 430, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('0220', 1, 1, 1, 1410, 650, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('0224', 1, 2, 2, 2145, 1052, false);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('HS', 2, 3, 1, 1395, 1400, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('5601C', 3, 2, 2, 1429, 1171, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('5601B', 3, 2, 2, 1279, 1033, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('5602A', 3, 1, 1, 710, 525, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('5602D', 3, 0, 1, 475, 425, false);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('02', 4, 0, 1, 760, 705, false);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('03', 4, 2, 1, 1040, 430, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('04', 4, 1, 1, 1410, 650, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('3', 5, 3, 2, 2145, 1052, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('6', 5, 2, 1, 1395, 1400, false);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('3210C', 6, 2, 2, 1429, 1171, false);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('3211B', 6, 2, 2, 1279, 1033, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('3212B', 6, 1, 1, 710, 525, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('HS', 7, 4, 2, 475, 2425, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('1F', 8, 2, 1, 760, 705, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('1G', 8, 2, 1, 760, 705, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('2A', 8, 3, 2, 1753, 1260, false);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('12', 9, 2, 1, 835, 705, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('13', 9, 2, 1, 950, 870, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('14', 9, 2, 1, 1005, 960, true);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('7A', 10, 0, 1, 999, 500, false);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('7B', 10, 1, 1, 1099, 705, false);
INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) VALUES ('7D', 10, 1, 1, 1099, 705, true);

INSERT INTO lease (user_id, unit_id, signed_date, start_date, rent_length, rent_amount, late_fee, status) VALUES (10, 3, '2020-04-15', '2020-04-15', 3, 525, 10, 'Active');
INSERT INTO lease (user_id, unit_id, signed_date, start_date, rent_length, rent_amount, late_fee, status) VALUES (11, 8, '2019-12-15', '2019-12-15', 12, 475, 5, 'Active');
INSERT INTO lease (user_id, unit_id, signed_date, start_date, rent_length, rent_amount, late_fee, status) VALUES (12, 9, '2020-04-15', '2020-04-15', 12, 1429, 5, 'Active');
INSERT INTO lease (user_id, unit_id, signed_date, start_date, rent_length, rent_amount, late_fee, status) VALUES (13, 13, '2020-01-28', '2020-01-28', 24, 2145, 2, 'Active');
INSERT INTO lease (user_id, unit_id, signed_date, start_date, rent_length, rent_amount, late_fee, status) VALUES (14, 14, '2019-09-15', '2019-09-15', 3, 525, 10, 'Active');
INSERT INTO lease (user_id, unit_id, signed_date, start_date, rent_length, rent_amount, late_fee, status) VALUES (15, 20, '2019-12-15', '2019-12-15', 12, 475, 5, 'Active');
INSERT INTO lease (user_id, unit_id, signed_date, start_date, rent_length, rent_amount, late_fee, status) VALUES (16, 24, '2020-01-20', '2020-01-20', 12, 1429, 5, 'Active');
INSERT INTO lease (user_id, unit_id, signed_date, start_date, rent_length, rent_amount, late_fee, status) VALUES (17, 25, '2020-04-15', '2020-04-15', 24, 2145, 2, 'Active');

INSERT INTO rent_cycle (lease_id, start_date, balance, due_date, rent_status) VALUES (1, '2020-04-15', 0, '2020-05-15', 'Paid');
INSERT INTO rent_cycle (lease_id, start_date, balance, due_date, rent_status) VALUES (2, '2020-04-15', 400, '2020-05-15', 'Unpaid');
INSERT INTO rent_cycle (lease_id, start_date, balance, due_date, rent_status) VALUES (3, '2020-04-15', 0, '2020-05-15', 'Paid');
INSERT INTO rent_cycle (lease_id, start_date, balance, due_date, rent_status) VALUES (4, '2020-04-15', 950, '2020-05-15', 'Unpaid');
INSERT INTO rent_cycle (lease_id, start_date, balance, due_date, rent_status) VALUES (5, '2020-04-15', 0, '2020-05-15', 'Paid');
INSERT INTO rent_cycle (lease_id, start_date, balance, due_date, rent_status) VALUES (6, '2020-04-15', 750, '2020-05-15', 'Unpaid');
INSERT INTO rent_cycle (lease_id, start_date, balance, due_date, rent_status) VALUES (7, '2020-04-15', 0, '2020-05-15', 'Paid');
INSERT INTO rent_cycle (lease_id, start_date, balance, due_date, rent_status) VALUES (8, '2020-04-15', 0, '2020-05-15', 'Paid');


INSERT INTO maintenance_request (unit_id, request_user_id, request_desc, priority, date_requested, assigned_user_id, is_completed) VALUES (3, 10, 'My toilet is clogged.', 1, '2019-10-15', 2, false);
INSERT INTO maintenance_request (unit_id, request_user_id, request_desc, priority, date_requested, assigned_user_id, is_completed) VALUES (8, 11, 'My bedroom door will not close all the way.', 3, '2020-02-15', 2, false);
INSERT INTO maintenance_request (unit_id, request_user_id, request_desc, priority, date_requested, assigned_user_id, is_completed) VALUES (9, 12, 'There are ants coming in through the bedroom window.', 2, '2020-03-03', 3, false);
INSERT INTO maintenance_request (unit_id, request_user_id, request_desc, priority, date_requested, assigned_user_id, is_completed) VALUES (13, 13, 'Lock on main door is broken.', 1, '2020-03-08', 3, false);
INSERT INTO maintenance_request (unit_id, request_user_id, request_desc, priority, date_requested, assigned_user_id, is_completed) VALUES (14, 14, 'My toilet is clogged.', 1, '2020-04-01', 4, false);
INSERT INTO maintenance_request (unit_id, request_user_id, request_desc, priority, date_requested, assigned_user_id, is_completed) VALUES (20, 15, 'Kitchen sink is clogged.', 1, '2020-04-07', 4, false);
INSERT INTO maintenance_request (unit_id, request_user_id, request_desc, priority, date_requested, assigned_user_id, is_completed) VALUES (24, 16, 'Air conditioning not working', 2, '2020-04-08', 4, false);

INSERT INTO feature (feature_name) VALUES ('Controlled Access');
INSERT INTO feature (feature_name) VALUES ('Elevator');
INSERT INTO feature (feature_name) VALUES ('Laundry Facility');
INSERT INTO feature (feature_name) VALUES ('Multi Use Room');
INSERT INTO feature (feature_name) VALUES ('Online Services');
INSERT INTO feature (feature_name) VALUES ('Picnic Area');
INSERT INTO feature (feature_name) VALUES ('Storage Space');
INSERT INTO feature (feature_name) VALUES ('Air Conditioning');
INSERT INTO feature (feature_name) VALUES ('Broadband Internet');
INSERT INTO feature (feature_name) VALUES ('Cable or Satellite');
INSERT INTO feature (feature_name) VALUES ('Cats and Dogs Allowed');
INSERT INTO feature (feature_name) VALUES ('Disability Access');
INSERT INTO feature (feature_name) VALUES ('Heating');
INSERT INTO feature (feature_name) VALUES ('Kitchen');
INSERT INTO feature (feature_name) VALUES ('Linen Closet');
INSERT INTO feature (feature_name) VALUES ('Oven/Range');
INSERT INTO feature (feature_name) VALUES ('Patio or Deck');
INSERT INTO feature (feature_name) VALUES ('Refrigerator/Freezer');
INSERT INTO feature (feature_name) VALUES ('Security System');
INSERT INTO feature (feature_name) VALUES ('W/D Hookup');

INSERT INTO property_feature (property_id, feature_id) VALUES (1, 1);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 2);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 3);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 4);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 5);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 6);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 7);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 8);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 9);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 10);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 11);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 12);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 13);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 14);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 15);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 16);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 17);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 18);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 19);
INSERT INTO property_feature (property_id, feature_id) VALUES (1, 20);

INSERT INTO property_feature (property_id, feature_id) VALUES (2, 3);
INSERT INTO property_feature (property_id, feature_id) VALUES (2, 7);
INSERT INTO property_feature (property_id, feature_id) VALUES (2, 9);
INSERT INTO property_feature (property_id, feature_id) VALUES (2, 13);
INSERT INTO property_feature (property_id, feature_id) VALUES (2, 14);
INSERT INTO property_feature (property_id, feature_id) VALUES (2, 16);
INSERT INTO property_feature (property_id, feature_id) VALUES (2, 17);
INSERT INTO property_feature (property_id, feature_id) VALUES (2, 18);

INSERT INTO property_feature (property_id, feature_id) VALUES (3, 5);
INSERT INTO property_feature (property_id, feature_id) VALUES (3, 6);
INSERT INTO property_feature (property_id, feature_id) VALUES (3, 7);
INSERT INTO property_feature (property_id, feature_id) VALUES (3, 8);
INSERT INTO property_feature (property_id, feature_id) VALUES (3, 11);
INSERT INTO property_feature (property_id, feature_id) VALUES (3, 12);
INSERT INTO property_feature (property_id, feature_id) VALUES (3, 13);
INSERT INTO property_feature (property_id, feature_id) VALUES (3, 14);
INSERT INTO property_feature (property_id, feature_id) VALUES (3, 16);
INSERT INTO property_feature (property_id, feature_id) VALUES (3, 17);
INSERT INTO property_feature (property_id, feature_id) VALUES (3, 18);

INSERT INTO property_feature (property_id, feature_id) VALUES (4, 3);
INSERT INTO property_feature (property_id, feature_id) VALUES (4, 4);
INSERT INTO property_feature (property_id, feature_id) VALUES (4, 5);
INSERT INTO property_feature (property_id, feature_id) VALUES (4, 6);
INSERT INTO property_feature (property_id, feature_id) VALUES (4, 7);
INSERT INTO property_feature (property_id, feature_id) VALUES (4, 8);
INSERT INTO property_feature (property_id, feature_id) VALUES (4, 9);
INSERT INTO property_feature (property_id, feature_id) VALUES (4, 10);
INSERT INTO property_feature (property_id, feature_id) VALUES (4, 11);
INSERT INTO property_feature (property_id, feature_id) VALUES (4, 12);
INSERT INTO property_feature (property_id, feature_id) VALUES (4, 13);
INSERT INTO property_feature (property_id, feature_id) VALUES (4, 14);
INSERT INTO property_feature (property_id, feature_id) VALUES (4, 15);
INSERT INTO property_feature (property_id, feature_id) VALUES (4, 16);
INSERT INTO property_feature (property_id, feature_id) VALUES (4, 18);
INSERT INTO property_feature (property_id, feature_id) VALUES (4, 19);

INSERT INTO property_feature (property_id, feature_id) VALUES (5, 1);
INSERT INTO property_feature (property_id, feature_id) VALUES (5, 3);
INSERT INTO property_feature (property_id, feature_id) VALUES (5, 5);
INSERT INTO property_feature (property_id, feature_id) VALUES (5, 6);
INSERT INTO property_feature (property_id, feature_id) VALUES (5, 7);
INSERT INTO property_feature (property_id, feature_id) VALUES (5, 8);
INSERT INTO property_feature (property_id, feature_id) VALUES (5, 9);
INSERT INTO property_feature (property_id, feature_id) VALUES (5, 10);
INSERT INTO property_feature (property_id, feature_id) VALUES (5, 11);
INSERT INTO property_feature (property_id, feature_id) VALUES (5, 12);
INSERT INTO property_feature (property_id, feature_id) VALUES (5, 13);
INSERT INTO property_feature (property_id, feature_id) VALUES (5, 14);
INSERT INTO property_feature (property_id, feature_id) VALUES (5, 15);
INSERT INTO property_feature (property_id, feature_id) VALUES (5, 16);
INSERT INTO property_feature (property_id, feature_id) VALUES (5, 17);
INSERT INTO property_feature (property_id, feature_id) VALUES (5, 18);
INSERT INTO property_feature (property_id, feature_id) VALUES (5, 19);

INSERT INTO property_feature (property_id, feature_id) VALUES (6, 3);
INSERT INTO property_feature (property_id, feature_id) VALUES (6, 5);
INSERT INTO property_feature (property_id, feature_id) VALUES (6, 6);
INSERT INTO property_feature (property_id, feature_id) VALUES (6, 7);
INSERT INTO property_feature (property_id, feature_id) VALUES (6, 8);
INSERT INTO property_feature (property_id, feature_id) VALUES (6, 9);
INSERT INTO property_feature (property_id, feature_id) VALUES (6, 10);
INSERT INTO property_feature (property_id, feature_id) VALUES (6, 11);
INSERT INTO property_feature (property_id, feature_id) VALUES (6, 17);
INSERT INTO property_feature (property_id, feature_id) VALUES (6, 18);
INSERT INTO property_feature (property_id, feature_id) VALUES (6, 19);

INSERT INTO property_feature (property_id, feature_id) VALUES (7, 5);
INSERT INTO property_feature (property_id, feature_id) VALUES (7, 6);
INSERT INTO property_feature (property_id, feature_id) VALUES (7, 7);
INSERT INTO property_feature (property_id, feature_id) VALUES (7, 8);
INSERT INTO property_feature (property_id, feature_id) VALUES (7, 9);
INSERT INTO property_feature (property_id, feature_id) VALUES (7, 10);
INSERT INTO property_feature (property_id, feature_id) VALUES (7, 11);
INSERT INTO property_feature (property_id, feature_id) VALUES (7, 12);
INSERT INTO property_feature (property_id, feature_id) VALUES (7, 13);
INSERT INTO property_feature (property_id, feature_id) VALUES (7, 16);
INSERT INTO property_feature (property_id, feature_id) VALUES (7, 17);
INSERT INTO property_feature (property_id, feature_id) VALUES (7, 18);
INSERT INTO property_feature (property_id, feature_id) VALUES (7, 19);

INSERT INTO property_feature (property_id, feature_id) VALUES (8, 13);
INSERT INTO property_feature (property_id, feature_id) VALUES (8, 14);
INSERT INTO property_feature (property_id, feature_id) VALUES (8, 15);
INSERT INTO property_feature (property_id, feature_id) VALUES (8, 16);
INSERT INTO property_feature (property_id, feature_id) VALUES (8, 17);
INSERT INTO property_feature (property_id, feature_id) VALUES (8, 18);
INSERT INTO property_feature (property_id, feature_id) VALUES (8, 19);

INSERT INTO property_feature (property_id, feature_id) VALUES (9, 1);
INSERT INTO property_feature (property_id, feature_id) VALUES (9, 3);
INSERT INTO property_feature (property_id, feature_id) VALUES (9, 5);
INSERT INTO property_feature (property_id, feature_id) VALUES (9, 6);
INSERT INTO property_feature (property_id, feature_id) VALUES (9, 7);
INSERT INTO property_feature (property_id, feature_id) VALUES (9, 8);
INSERT INTO property_feature (property_id, feature_id) VALUES (9, 9);
INSERT INTO property_feature (property_id, feature_id) VALUES (9, 10);
INSERT INTO property_feature (property_id, feature_id) VALUES (9, 11);
INSERT INTO property_feature (property_id, feature_id) VALUES (9, 12);
INSERT INTO property_feature (property_id, feature_id) VALUES (9, 13);

INSERT INTO property_feature (property_id, feature_id) VALUES (10, 1);
INSERT INTO property_feature (property_id, feature_id) VALUES (10, 3);
INSERT INTO property_feature (property_id, feature_id) VALUES (10, 5);
INSERT INTO property_feature (property_id, feature_id) VALUES (10, 6);
INSERT INTO property_feature (property_id, feature_id) VALUES (10, 7);
INSERT INTO property_feature (property_id, feature_id) VALUES (10, 8);
INSERT INTO property_feature (property_id, feature_id) VALUES (10, 9);
INSERT INTO property_feature (property_id, feature_id) VALUES (10, 10);

COMMIT TRANSACTION;

SELECT * FROM property;

SELECT * FROM maintenance_request;

SELECT * FROM users WHERE role = 'maintenance';

SELECT * FROM property;

SELECT * FROM unit;

Update unit Set is_available = false WHERE unit_id = 12;

SELECT * FROM lease;

SELECT * FROM rent_cycle;

SELECT rent_cycle_id, rent_status, balance FROM rent_cycle WHERE lease_id = 1;

UPDATE rent_cycle SET rent_status = 'Overdue' WHERE rent_cycle_id = 1 AND due_date < '04/13/2021' RETURNING balance;

UPDATE lease SET overdue_balance = 2145 WHERE lease_id = 4;

SELECT request_id, maintenance_request.unit_id, request_user_id, request_desc, priority, date_requested, assigned_user_id, is_completed, unit_number, property_name, street_address, city, state, property.zip_code 

SELECT feature_name FROM feature JOIN property_feature ON property_feature.feature_id = feature.feature_id JOIN property ON property_feature.property_id = property.property_id WHERE property.property_id = 3