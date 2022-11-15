CREATE TABLE IF NOT EXISTS rating (
    rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating varchar);


CREATE TABLE IF NOT EXISTS film (
    film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL,
    description varchar(200) NOT NULL,
    release_date date NOT NULL,
    duration integer NOT NULL,
    rating_id integer REFERENCES rating (rating_id) ON DELETE CASCADE,
    CONSTRAINT wrong_release_date CHECK (release_data >= '28.12.1895'));


CREATE TABLE IF NOT EXISTS genre (
    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre varchar);

CREATE TABLE IF NOT EXISTS film_genre (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_id integer REFERENCES genre (genre_id) ON DELETE CASCADE,
    film_id integer REFERENCES film (film_id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar,
    login varchar NOT NULL,
    email varchar NOT NULL,
    birthday date NOT NULL,
    CONSTRAINT wrong_birthday_date CHECK (birthday < CURRENT_DATE));

CREATE TABLE IF NOT EXISTS friend (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id integer REFERENCES users (user_id) ON DELETE CASCADE,
    friend_id integer REFERENCES users (user_id) ON DELETE CASCADE,
    status boolean);

CREATE TABLE IF NOT EXISTS like_user_film (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id integer REFERENCES film (film_id) ON DELETE CASCADE,
    user_id integer REFERENCES users (user_id) ON DELETE CASCADE
    );