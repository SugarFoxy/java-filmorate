package ru.yandex.practicum.filmorate.utils.director;

import lombok.experimental.UtilityClass;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Director;

@UtilityClass
public class DirectorMapper {
    public static Director mapDirector(SqlRowSet directorRow) {
        return new Director(directorRow.getInt("director_id"),
                directorRow.getString("director_name"));
    }
}
