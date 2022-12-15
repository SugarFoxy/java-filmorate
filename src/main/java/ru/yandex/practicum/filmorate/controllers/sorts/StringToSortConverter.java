package ru.yandex.practicum.filmorate.controllers.sorts;

import org.springframework.core.convert.converter.Converter;

public class StringToSortConverter implements Converter<String, SearchBy> {
    @Override
    public SearchBy convert(String source) {
        return SearchBy.valueOf(source.toUpperCase());
    }
}

