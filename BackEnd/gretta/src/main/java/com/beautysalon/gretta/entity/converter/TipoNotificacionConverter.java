package com.beautysalon.gretta.entity.converter;

import com.beautysalon.gretta.entity.enums.TipoNotificacion;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoNotificacionConverter implements AttributeConverter<TipoNotificacion, String> {

    @Override
    public String convertToDatabaseColumn(TipoNotificacion attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public TipoNotificacion convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return TipoNotificacion.valueOf(dbData);
    }
}
