package ru.yandex.practicum.filmorate.storages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dao.user.UserStorage;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception_handler.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.user.UserMapper;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
public class UserStorageTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserStorage userStorage;

    @BeforeEach
    public void beforeEach() {
        String firstSqlQuery = "INSERT INTO USERS_MODEL(email, login, name, birthday) " +
                "VALUES ('e5k4p3@gmail.com', 'e5k4p3', 'e5k4p3', DATE '1995-07-11')";
        String secondSqlQuery = "INSERT INTO USERS_MODEL(email, login, name, birthday) " +
                "VALUES ('mulenas@gmail.com', 'Mulenas', 'Mulenas', DATE '1995-07-11')";
        jdbcTemplate.update(firstSqlQuery);
        jdbcTemplate.update(secondSqlQuery);
    }

    @Test
    @Sql("classpath:data.sql")
    public void addUser() {
        User correctUser = new User("new@gmail.com", "User", "User",
                LocalDate.of(1995, 7, 11));
        userStorage.addUser(correctUser);
        String sqlQuery = "SELECT * FROM users_model WHERE email = 'new@gmail.com'";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sqlQuery);
        userRow.next();
        User newUser = UserMapper.mapUser(userRow);
        assertEquals(correctUser.getEmail(), newUser.getEmail());
        assertEquals(correctUser.getLogin(), newUser.getLogin());
        assertEquals(correctUser.getName(), newUser.getName());
        assertEquals(correctUser.getBirthday(), newUser.getBirthday());
        User duplicateUser = new User("e5k4p3@gmail.com", "e5k4p3", "e5k4p3",
                LocalDate.of(1995, 7, 11));
        assertThrows(EntityAlreadyExistsException.class, () -> userStorage.addUser(duplicateUser));
    }

    @Test
    @Sql("classpath:data.sql")
    public void deleteUser() {
        userStorage.deleteUser(1);
        String sqlQuery = "SELECT * FROM users_model WHERE user_id = 1";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sqlQuery);
        assertFalse(userRow.next());
        assertThrows(EntityNotFoundException.class, () -> userStorage.deleteUser(9999));
    }

    @Test
    @Sql("classpath:data.sql")
    public void updateUser() {
        User newUser = new User("newuser@gmail.com", "NewUser", "NewUser",
                LocalDate.of(2000, 8, 20));
        newUser.setId(2);
        userStorage.updateUser(newUser);
        String sqlQuery = "SELECT * FROM users_model WHERE user_id = 2";
        SqlRowSet userRow = jdbcTemplate.queryForRowSet(sqlQuery);
        userRow.next();
        User user = UserMapper.mapUser(userRow);
        assertEquals(newUser.getEmail(), user.getEmail());
        assertEquals(newUser.getLogin(), user.getLogin());
        assertEquals(newUser.getName(), user.getName());
        assertEquals(newUser.getBirthday(), user.getBirthday());
        newUser.setId(9999);
        assertThrows(EntityNotFoundException.class, () -> userStorage.updateUser(newUser));
    }

    @Test
    @Sql("classpath:data.sql")
    public void getUserById() {
        User user = userStorage.getUserById(1);
        assertEquals(1, user.getId());
        assertThrows(EntityNotFoundException.class, () -> userStorage.getUserById(9999));
    }

    @Test
    @Sql("classpath:data.sql")
    public void getAllUsers() {
        List<User> allUsers = userStorage.getAllUsers();
        assertEquals(2, allUsers.size());
    }
}
