package ru.yandex.practicum.filmorate.controllers.sorts;

import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, QueryBy> {
    @Override
    public QueryBy convert(String source) {
        return QueryBy.valueOf(source.toUpperCase());
    }
}

