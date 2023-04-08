package ru.wiselder.plan.utils;

import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class SqlUtils {

    public static <T> ResultSetExtractor<Optional<T>> singleExtractor(RowMapper<T> mapper) {
        return rs -> Optional.ofNullable(rs.next() ? mapper.mapRow(rs, 1) : null);
    }

    public static Map<String, String> nameLike(String namePattern) {
        return Map.of("name", namePattern + '%');
    }
}
