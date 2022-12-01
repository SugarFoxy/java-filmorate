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
}
