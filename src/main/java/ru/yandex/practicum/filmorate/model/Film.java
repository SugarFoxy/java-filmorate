package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Film {

    private int id;
    List<Integer> likes;
    @NotBlank(message = "Название отсутствует")
    private String name;
    @NotNull(message = "Филь не может быть без описания")
    @Size(max = 200, message = "Максимальное коллычество смволов - 200")
    private String description;
    @NotNull(message = "Дата не должна быть пустой")
    private LocalDate releaseDate;
    @NotNull(message = "Продолжительность не может отсутствовать")
    @Positive(message = "Продолжитьльность не может быть отрицательной")
    private long duration;
    @AssertFalse
    boolean releaseDateAfter;

    private boolean isReleaseDateAfter() {
        return releaseDate.isBefore(LocalDate.of(1895, 12, 28));
    }


    public Film(String name, String description, LocalDate releaseDate, long duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public List<Integer> getLikes() {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        return likes;
    }

    public void addLike(Integer id) {
        likes.add(id);
    }

    public void deleteLike(Integer id) {
        likes.remove(id);
    }

    public int getCountLikes(){
        return  likes.size();
    }
}