-- Заполнение данными таблицы genres
MERGE INTO genre (genre_id, genre) VALUES ( 1, 'Комедия' );
MERGE INTO genre (genre_id, genre) VALUES ( 2, 'Драма' );
MERGE INTO genre (genre_id, genre) VALUES ( 3, 'Мультфильм' );
MERGE INTO genre (genre_id, genre) VALUES ( 4, 'Триллер' );
MERGE INTO genre (genre_id, genre) VALUES ( 5, 'Документальный' );
MERGE INTO genre (genre_id, genre) VALUES ( 6, 'Боевик' );

-- Заполнение данными таблицы ratings
MERGE INTO mpa (id, mpa) VALUES ( 1, 'G' );
MERGE INTO mpa (id, mpa) VALUES ( 2, 'PG' );
MERGE INTO mpa (id, mpa) VALUES ( 3, 'PG-13' );
MERGE INTO mpa (id, mpa) VALUES ( 4, 'R' );
MERGE INTO mpa (id, mpa) VALUES ( 5, 'NC-17' );