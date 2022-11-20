package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.exception.AbsenceOfObjectException;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MPA {

    private Integer id;
    private String name;

    public MPA(Integer id) {
        this.id = id;
    }

    public String getName() {
        switch (id) {
            case 1:
                return RatingMpa.G.getRating();
            case 2:
                return RatingMpa.PG.getRating();
            case 3:
                return RatingMpa.PG13.getRating();
            case 4:
                return RatingMpa.R.getRating();
            case 5:
                return RatingMpa.NC17.getRating();
            default:
                throw new AbsenceOfObjectException("Такого Мпа рейтинга не существует");
        }
    }
}
