create user rest_api_user with password 'rest_api_user';

create database video_rental_db with template = template0 encoding = 'utf8' lc_collate = 'C' lc_ctype = 'C';

alter database video_rental_db owner to rest_api_user;

\connect video_rental_db

create schema video_rental_schema;

alter schema video_rental_schema owner to rest_api_user;

set search_path = video_rental_schema, pg_catalog;
set role rest_api_user;

create table price_type (
  id serial primary key,
  name text not null,
  value numeric not null
);

create table film_type (
  id serial primary key,
  name text not null,
  number_days_with_same_price integer not null,
  default_bonus_points integer not null default 0,
  price_type_id integer not null references price_type(id)
);

create table film (
  id serial primary key,
  name text not null,
  unique(name),
  film_type_id integer not null references film_type(id)
);

create table customer (
  code text primary key,
  name text not null,
  bonus_points integer not null default 0,
  available_money numeric not null default 0
);

create table customer_film_rent (
  id serial primary key,
  customer_code text not null references customer(code),
  film_id integer not null references film(id),
  price numeric not null,
  gained_bonus_points integer not null default 0,
  late_return_charge numeric,
  number_rent_days integer not null,
  rent_date date not null,
  return_date date
);

alter table film owner to rest_api_user;
alter table film_type owner to rest_api_user;
alter table price_type owner to rest_api_user;
alter table customer owner to rest_api_user;
alter table customer_film_rent owner to rest_api_user;

insert into price_type (name, value) values
  ('basic price', 30),
  ('premium price', 40);

insert into film_type (name, price_type_id, number_days_with_same_price, default_bonus_points) values
  ('New Release', 2, 1, 2),
  ('Regular film', 1, 3, 1),
  ('Old film', 1, 5, 1);

insert into customer (code, name, available_money) values
  ('6d72c220-d2bc-43d4-a36b-dda070120031', 'Alice', 1000),
  ('948cba97-0533-486b-9244-f5459ff0232e', 'Bob', 500);

insert into film (name, film_type_id) values
  ('Matrix 11', 1),
  ('Spiderman', 2),
  ('Spiderman 2', 2),
  ('Out of Africa', 3);
