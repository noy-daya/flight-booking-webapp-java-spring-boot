-- //////////////// user ////////////////
CREATE TABLE IF NOT EXISTS User (
  id bigint primary key auto_increment,
  user_name varchar(30) unique,
  email varchar(50) unique,
  password varchar(255),
  is_admin boolean,
  last_seen datetime default NOW()
);

-- //////////////// country ////////////////
CREATE TABLE IF NOT EXISTS Country (
  id bigint primary key,
  name varchar(50)
);

-- //////////////// plane ////////////////
CREATE TABLE IF NOT EXISTS Plane (
  id bigint primary key,
  description varchar(50),
  num_of_rows int,
  num_of_seats_per_row int,
  total_quantity int,
  available_quantity int
);

-- //////////////// city ////////////////
CREATE TABLE IF NOT EXISTS City (
  id bigint primary key,
  name varchar(50),
  country_id bigint,
  description varchar(1000),
  img_url varchar(500),
  foreign key (country_id) references country(id)
);

-- //////////////// airport ////////////////
CREATE TABLE IF NOT EXISTS Airport (
  id bigint primary key,
  code varchar (3),
  name varchar(50),
  city_id bigint,
  foreign key (city_id) references city(id)
);

-- //////////////// flight_status ////////////////
CREATE TABLE IF NOT EXISTS Flight_Status (
  id bigint primary key,
  description varchar(20)
);

-- //////////////// flight ////////////////
CREATE TABLE IF NOT EXISTS Flight (
  id bigint primary key auto_increment,
  plane_id bigint,
  departure_airport_id bigint,
  arrival_airport_id bigint,
  departure_date date,
  departure_time time,
  arrival_date date,
  arrival_time time,
  ticket_price double,
  duration bigint, -- in seconds
  status_id bigint,
  foreign key (plane_id) references plane(id),
  foreign key (departure_airport_id) references airport(id),
  foreign key (arrival_airport_id) references airport(id),
  foreign key (status_id) references flight_status(id)
);

-- //////////////// route ////////////////
CREATE TABLE IF NOT EXISTS Route (
  route_number bigint,
  flight_id bigint,
  sequence_number bigint,
  is_booking_allowed boolean default false,
  foreign key (flight_id) references flight(id)
);

-- //////////////// luggage ////////////////
CREATE TABLE IF NOT EXISTS Luggage (
  id bigint primary key,
  description varchar(20),
  weight_limit int,
  price double
);

-- //////////////// booking ////////////////
CREATE TABLE IF NOT EXISTS Booking (
  id bigint primary key auto_increment,
  user_id bigint,
  route_number bigint,
  num_of_tickets int,
  total_price double,
  booking_date datetime,
  last_modify_date datetime,
  is_canceled bool,
  foreign key (user_id) references user(id)
);

-- //////////////// passenger ////////////////
CREATE TABLE IF NOT EXISTS Passenger (
  booking_id bigint,
  passport_id VARCHAR(20),
  first_name varchar(255),
  last_name varchar(255),
  gender varchar(20),
  foreign key (booking_id) references booking(id),
  unique (booking_id, passport_id)
);

-- //////////////// passenger_flight ////////////////
CREATE TABLE IF NOT EXISTS Passenger_Flight (
  booking_id bigint,
  passport_id VARCHAR(20),
  flight_id bigint,
  seat_num varchar(20) not null,
  luggage_ids varchar(20) not null,
  foreign key (booking_id) references booking(id),
  foreign key (flight_id)references flight(id)
);

-- //////////////// feedback ////////////////
CREATE TABLE IF NOT EXISTS Feedback (
  user_id bigint,
  route_number bigint,
  cleaning_rating int,
  convenience_rating int,
  service_rating int,
  foreign key (user_id) references user(id),
  unique (user_id, route_number)
);

-- //////////////// reserved_seats ////////////////
CREATE TABLE IF NOT EXISTS Reserved_Seats (
  passport_id VARCHAR(20),
  flight_id bigint,
  seat_num varchar(20) not null,
  reservation_time timestamp default CURRENT_TIMESTAMP, -- automatically store the current timestamp when a new row is inserted.
  foreign key (flight_id) references flight(id),
  unique (flight_id, seat_num)  -- Composite unique constraint
);