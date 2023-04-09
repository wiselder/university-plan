package ru.wiselder.plan.exception;

import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class MyCustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        var errorAttributes = super.getErrorAttributes(webRequest, options);
        var error = super.getError(webRequest);
        if (error instanceof ObjectNotFoundException e) {
            errorAttributes.put("entity", e.entity);
        } else if (error instanceof LessonIntersectionException e) {
            errorAttributes.put("collision_type", e.type);
            errorAttributes.put("collision_lesson", e.lesson);
        }
        return errorAttributes;
    }
}
