package ru.yandex.practicum.filmorate.utils.genre;

import lombok.experimental.UtilityClass;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Genre;

@UtilityClass
public class GenreMapper {
    public static Genre mapGenre(SqlRowSet genreRow) {
        return new Genre(genreRow.getInt("genre_id"),
                genreRow.getString("genre_name"),
                genreRow.getString("genre_description"));
    }
}
