drop schema if exists video_rental_schema;
create schema video_rental_schema;

set SCHEMA video_rental_schema;

create table price_type (
  id serial primary key,
  name VARCHAR not null,
  value numeric not null
);

create table film_type (
  id serial primary key,
  name VARCHAR not null,
  number_days_with_same_price integer not null,
  default_bonus_points integer not null default 0,
  price_type_id integer not null references price_type(id)
);

create table film (
  id serial primary key,
  name VARCHAR not null,
  unique(name),
  film_type_id integer not null references film_type(id)
);

create table customer (
  code VARCHAR primary key,
  name VARCHAR not null,
  bonus_points integer not null default 0,
  available_money numeric not null default 0
);

create table customer_film_rent (
  id serial primary key,
  customer_code VARCHAR not null references customer(code),
  film_id integer not null references film(id),
  price numeric not null,
  gained_bonus_points integer not null default 0,
  late_return_charge numeric,
  number_rent_days integer not null,
  rent_date date not null,
  return_date date
);

insert into price_type (name, value) values
  ('basic price', 30),
  ('premium price', 40);

insert into film_type (name, price_type_id, number_days_with_same_price, default_bonus_points) values
  ('New Release', 2, 1, 2),
  ('Regular film', 1, 3, 1),
  ('Old film', 1, 5, 1);
