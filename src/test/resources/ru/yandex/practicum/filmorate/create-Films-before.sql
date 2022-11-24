delete from FILMORATETEST.PUBLIC.FILM;
delete from FILMORATETEST.PUBLIC.FILM_GENRE;
delete from FILMORATETEST.PUBLIC.USERS;
delete from FILMORATETEST.PUBLIC.LIKE_USER_FILM;

INSERT INTO FILMORATETEST.PUBLIC.FILM ( FILM_ID,NAME,DESCRIPTION,RELEASE_DATE,DURATION,RATING ) values
                                                                                 (1,'testName','testDescription', '2007-03-1',100,1),
                                                                                 (2,'test2Name','test2Description', '2007-03-2',200,2),
                                                                                 (3,'test3Name','test3Description', '2007-03-1',300,3);

insert into FILMORATETEST.PUBLIC.FILM_GENRE (genre_id, film_id) values
                                                                         (1,3),
                                                                         (2,2),
                                                                         (3,1);

INSERT INTO FILMORATETEST.PUBLIC.USERS ( USER_ID,NAME,LOGIN,EMAIL,BIRTHDAY ) values
                                                                                 (1,'testfName','testfLogin', 'test@email.ru','2007-03-1'),
                                                                                 (2,'testfName2','testfLogin2', 'test2@email.ru','2007-03-2');

INSERT INTO  FILMORATETEST.PUBLIC.LIKE_USER_FILM (FILM_ID, USER_ID) VALUES
                                                                        (1,2),
                                                                        (1,1),
                                                                        (3,1);
