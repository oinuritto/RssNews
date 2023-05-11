package ru.itis.rssnews.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.itis.rssnews.exceptions.UpdateEntityException;
import ru.itis.rssnews.models.Role;

@Component
public class StringToRoleConverter implements Converter<String, Role> {
    @Override
    public Role convert(String source) {
        String strRole = source.trim().toUpperCase();
        try {
            return Role.valueOf(strRole);
        } catch (IllegalArgumentException ex) {
            throw new UpdateEntityException("Role: " + source + " doesn't exist");
        }
    }
}
