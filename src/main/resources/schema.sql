CREATE TABLE IF NOT EXISTS users_model (
    user_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar unique,
    login varchar unique,
    name varchar,
    birthday date
);

CREATE TABLE IF NOT EXISTS users_friends (
    friends_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id int REFERENCES users_model(user_id) ON DELETE CASCADE,
    user_friend_id int REFERENCES users_model(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS genre_dictionary (
    genre_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name varchar,
    genre_description varchar
);

CREATE TABLE IF NOT EXISTS mpa_dictionary (
    mpa_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating varchar,
    rating_description varchar
);

CREATE TABLE IF NOT EXISTS directors_model (
    director_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    director_name varchar
);

CREATE TABLE IF NOT EXISTS films_model (
    film_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title varchar,
    description varchar,
    release_date date,
    duration BIGINT,
    mpa_id int REFERENCES mpa_dictionary(mpa_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS films_genres (
    film_genre_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id int REFERENCES films_model(film_id) ON DELETE CASCADE,
    genre_id int REFERENCES genre_dictionary(genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS films_directors (
    films_directors_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id int REFERENCES films_model(film_id) ON DELETE CASCADE,
    director_id int REFERENCES directors_model(director_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS films_likes (
    like_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id int REFERENCES films_model(film_id) ON DELETE CASCADE,
    user_id int REFERENCES users_model(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS films_dislikes (
    dislike_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id int REFERENCES films_model(film_id) ON DELETE CASCADE,
    user_id int REFERENCES users_model(user_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS review_model (
                                            review_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                            content varchar,
                                            is_positive boolean,
                                            useful int
);

CREATE TABLE IF NOT EXISTS film_reviews (
                                            film_id int REFERENCES films_model(film_id),
                                            review_id int REFERENCES review_model(review_id)
);

CREATE TABLE IF NOT EXISTS user_reviews (
                                            user_id int REFERENCES users_model(user_id),
                                            review_id int REFERENCES review_model(review_id)
);

CREATE TABLE IF NOT EXISTS event_types_dictionary (
    event_type_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_type varchar
);

CREATE TABLE IF NOT EXISTS event_operations_dictionary (
    event_operation_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_operation varchar
);

CREATE TABLE IF NOT EXISTS entity_types_dictionary (
    entity_type_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    entity_type varchar
);

CREATE TABLE IF NOT EXISTS users_feed (
    event_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id int REFERENCES users_model(user_id) ON DELETE CASCADE,
    event_type int REFERENCES event_types_dictionary(event_type_id),
    event_operation int REFERENCES event_operations_dictionary(event_operation_id),
    entity_id int,
    event_time BIGINT
);