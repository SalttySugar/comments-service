package blog.comments.utils;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class ApplicationConverter {
    private final ConversionService conversionService;

    public ApplicationConverter(@Qualifier("webFluxConversionService") ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public <T> Function<Object, T> convert(Class<T> type) {
        return (source) -> Optional
                .ofNullable(conversionService.convert(source, type))
                .map(type::cast)
                .orElseThrow(RuntimeException::new);
    }

}
