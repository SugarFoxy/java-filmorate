CREATE TABLE IF NOT EXISTS rating (
    rating_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating varchar);

CREATE TABLE IF NOT EXISTS film (
    film_id integer PRIMARY KEY AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    description varchar(200),
    release_date date NOT NULL,
    duration long NOT NULL,
    rating INT REFERENCES rating(rating_id) NOT NULL
    );

CREATE TABLE IF NOT EXISTS genre (
    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre varchar);

CREATE TABLE IF NOT EXISTS film_genre (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_id integer REFERENCES genre (genre_id) ON DELETE CASCADE NOT NULL,
    film_id integer REFERENCES film (film_id) ON DELETE CASCADE NOT NULL);

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name varchar,
    login varchar NOT NULL,
    email varchar NOT NULL,
    birthday date NOT NULL,
    CONSTRAINT wrong_birthday_date CHECK (birthday < CURRENT_DATE));

CREATE TABLE IF NOT EXISTS friend (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id integer REFERENCES users (user_id) ON DELETE CASCADE NOT NULL,
    friend_id integer REFERENCES users (user_id) ON DELETE CASCADE NOT NULL,
    status boolean);

CREATE TABLE IF NOT EXISTS like_user_film (
    id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id integer REFERENCES film (film_id) ON DELETE CASCADE NOT NULL,
    user_id integer REFERENCES users (user_id) ON DELETE CASCADE NOT NULL);