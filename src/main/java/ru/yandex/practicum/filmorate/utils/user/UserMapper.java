package ru.yandex.practicum.filmorate.utils.user;

import lombok.experimental.UtilityClass;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.User;

@UtilityClass
public class UserMapper {
    public static User mapUser(SqlRowSet userRow) {
        User user = new User(userRow.getString("email"),
                userRow.getString("login"),
                userRow.getString("name"),
                userRow.getDate("birthday").toLocalDate());
        user.setId(userRow.getInt("user_id"));
        return user;
    }
}
