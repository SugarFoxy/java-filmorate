package ru.yandex.practicum.filmorate.storages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
public class FriendsStorageTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private FriendsStorage friendsStorage;

    @BeforeEach
    public void beforeEach() {
        String firstSqlQuery = "INSERT INTO USERS_MODEL(email, login, name, birthday) " +
                "VALUES ('e5k4p3@gmail.com', 'e5k4p3', 'e5k4p3', DATE '1995-07-11')";
        String secondSqlQuery = "INSERT INTO USERS_MODEL(email, login, name, birthday) " +
                "VALUES ('mulenas@gmail.com', 'Mulenas', 'Mulenas', DATE '1995-07-11')";
        String thirdSqlQuery = "INSERT INTO USERS_MODEL(email, login, name, birthday) " +
                "VALUES ('thius@gmail.com', 'thius', 'thius', DATE '1995-07-11')";
        jdbcTemplate.update(firstSqlQuery);
        jdbcTemplate.update(secondSqlQuery);
        jdbcTemplate.update(thirdSqlQuery);
    }

    @Test
    @Sql("classpath:data.sql")
    public void addToFriends() {
        friendsStorage.addToFriends(1, 2);
        assertEquals(1, friendsStorage.getUserFriends(1).size());
        assertThrows(EntityNotFoundException.class, () -> friendsStorage.addToFriends(1, 9999));
    }

    @Test
    @Sql("classpath:data.sql")
    public void removeFromFriends() {
        friendsStorage.addToFriends(1, 2);
        assertEquals(1, friendsStorage.getUserFriends(1).size());
        friendsStorage.removeFromFriends(1, 2);
        assertEquals(0, friendsStorage.getUserFriends(1).size());
        assertThrows(EntityNotFoundException.class, () -> friendsStorage.removeFromFriends(1, 9999));
    }

    @Test
    @Sql("classpath:data.sql")
    public void getUserFriends() {
        friendsStorage.addToFriends(1, 2);
        friendsStorage.addToFriends(1, 3);
        List<User> userFriends = new ArrayList<>();
        userFriends.add(userStorage.getUserById(2));
        userFriends.add(userStorage.getUserById(3));
        assertEquals(friendsStorage.getUserFriends(1), userFriends);
        assertThrows(EntityNotFoundException.class, () -> friendsStorage.getUserFriends(9999));
    }

    @Test
    @Sql("classpath:data.sql")
    public void getCommonFriends() {
        friendsStorage.addToFriends(1, 2);
        friendsStorage.addToFriends(3, 2);
        List<User> commonFriends = new ArrayList<>();
        commonFriends.add(userStorage.getUserById(2));
        assertEquals(friendsStorage.getCommonFriends(1, 3), commonFriends);
        assertThrows(EntityNotFoundException.class, () -> friendsStorage.getCommonFriends(1, 9999));
    }
}
