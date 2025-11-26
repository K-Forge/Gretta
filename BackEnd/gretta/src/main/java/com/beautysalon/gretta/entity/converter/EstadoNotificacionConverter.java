package com.beautysalon.gretta.entity.converter;

import com.beautysalon.gretta.entity.enums.EstadoNotificacion;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstadoNotificacionConverter implements AttributeConverter<EstadoNotificacion, String> {

    @Override
    public String convertToDatabaseColumn(EstadoNotificacion attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public EstadoNotificacion convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return EstadoNotificacion.valueOf(dbData);
    }
}
