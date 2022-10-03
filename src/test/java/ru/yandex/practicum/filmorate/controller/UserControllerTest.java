package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController;

    @BeforeEach
    public void init(){
        userController = new UserController();
    }

    @Test
    void getUsers() {
        List<User> usersBefore = userController.getUsers();
        userController.postUsers( User.builder()
                .email("ok@ya.ru")
                .login("Oks")
                .birthday(LocalDate.of(1994,5,21))
                .build()
        );
        List<User> usersAfter = userController.getUsers();
        assertAll(
                () -> assertEquals(0, usersBefore.size(), "список пуст"),
                () -> assertEquals(1, usersAfter.size(), "имеется оди элемент")
        );

    }

    @Test
    void postUsers() {
    }

    @Test
    void updateUsers() {
    }
}