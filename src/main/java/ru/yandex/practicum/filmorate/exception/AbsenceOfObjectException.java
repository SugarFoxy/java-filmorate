package ru.yandex.practicum.filmorate.exception;

public class AbsenceOfObjectException extends RuntimeException {
    public AbsenceOfObjectException(String massager) {
        super(massager);
    }
}
