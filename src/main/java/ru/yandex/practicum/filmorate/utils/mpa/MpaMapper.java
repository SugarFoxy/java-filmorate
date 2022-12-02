package ru.yandex.practicum.filmorate.utils.mpa;

import lombok.experimental.UtilityClass;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Mpa;

@UtilityClass
public class MpaMapper {
    public static Mpa mapMpa(SqlRowSet mpaRow) {
        return new Mpa(mpaRow.getInt("mpa_id"),
                mpaRow.getString("rating"),
                mpaRow.getString("rating_description"));
    }
}
