-- Заполнение данными таблицы genres
MERGE INTO genre (genre_id, genre) VALUES ( 1, 'Комедия' );
MERGE INTO genre (genre_id, genre) VALUES ( 2, 'Драма' );
MERGE INTO genre (genre_id, genre) VALUES ( 3, 'Мультфильм' );
MERGE INTO genre (genre_id, genre) VALUES ( 4, 'Триллер' );
MERGE INTO genre (genre_id, genre) VALUES ( 5, 'Документальный' );
MERGE INTO genre (genre_id, genre) VALUES ( 6, 'Боевик' );

-- Заполнение данными таблицы ratings
MERGE INTO rating (rating_id, rating) VALUES ( 1, 'G' );
MERGE INTO rating (rating_id, rating) VALUES ( 2, 'PG' );
MERGE INTO rating (rating_id, rating) VALUES ( 3, 'PG-13' );
MERGE INTO rating (rating_id, rating) VALUES ( 4, 'R' );
MERGE INTO rating (rating_id, rating) VALUES ( 5, 'NC-17' );