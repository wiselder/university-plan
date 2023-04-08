package ru.wiselder.plan.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends RuntimeException {
    public final Entity entity;
    public ObjectNotFoundException(String objectKey, Object objectValue) {
        this.entity = new Entity(objectKey, objectValue);
    }

    public record Entity(String key, Object value) { }
}
