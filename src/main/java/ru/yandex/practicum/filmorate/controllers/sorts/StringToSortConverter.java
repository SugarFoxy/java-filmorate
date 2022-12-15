package ru.yandex.practicum.filmorate.controllers.sorts;

import org.springframework.core.convert.converter.Converter;

public class StringToSortConverter implements Converter<String, SortBy> {
    @Override
    public SortBy convert(String source) {
        return SortBy.valueOf(source.toUpperCase());
    }
}

