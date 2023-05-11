package ru.itis.rssnews.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.itis.rssnews.models.PageParam;

@Component
public class StringToPageParam implements Converter<String, PageParam> {
    @Override
    public PageParam convert(String source) {
        String strPage = source.trim();
        int page;
        try {
            page = Integer.parseInt(strPage);
        } catch (NumberFormatException ex) {
            page = 1;
        }

        return PageParam.builder()
                .page(page)
                .build();
    }
}
