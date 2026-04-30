package master.gard.converter;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class CaseInsensitiveEnumConverterProvider implements ParamConverterProvider {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (!rawType.isEnum()) {
            return null;
        }

        return new ParamConverter<>() {
            @Override
            public T fromString(String value) {
                if (value == null || value.isBlank()) {
                    return null;
                }

                Object[] enumConstants = rawType.getEnumConstants();

                for (Object enumConstant : enumConstants) {
                    Enum enumValue = (Enum) enumConstant;
                    if (enumValue.name().equalsIgnoreCase(value.trim())) {
                        return (T) enumValue;
                    }
                }

                throw new BadRequestException("Valor inválido para o enum " + rawType.getSimpleName() + ": " + value);
            }

            @Override
            public String toString(T value) {
                return value == null ? null : value.toString();
            }
        };
    }
}
