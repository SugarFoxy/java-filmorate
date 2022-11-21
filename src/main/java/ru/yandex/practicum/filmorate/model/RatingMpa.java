package ru.yandex.practicum.filmorate.model;

public enum RatingMpa {
    G("G"), PG("PG"), PG13("PG-13"), R("R"), NC17("NC-17");
    private final String rating;

    RatingMpa(String rating) {
        this.rating = rating;
    }

    String getRating() {
        return rating;
    }
}
