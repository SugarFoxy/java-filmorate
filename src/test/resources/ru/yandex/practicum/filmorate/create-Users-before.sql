delete from FILMORATETEST.PUBLIC.USERS;
delete from FILMORATETEST.PUBLIC.FRIEND;

INSERT INTO FILMORATETEST.PUBLIC.USERS ( USER_ID,NAME,LOGIN,EMAIL,BIRTHDAY ) values
    (1,'testName','testLogin', 'test@email.ru','2007-03-1'),
    (2,'testName2','testLogin2', 'test2@email.ru','2007-03-2'),
    (3,'testName3','testLogin3', 'test3@email.ru','2007-03-3');

insert into FILMORATETEST.PUBLIC.FRIEND (USER_ID, FRIEND_ID, STATUS) values
    (1,2,true),
    (2,1,false),
    (3,2,true),
    (2,3,false);