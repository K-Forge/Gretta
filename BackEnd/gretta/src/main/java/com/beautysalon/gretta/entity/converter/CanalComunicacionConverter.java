package com.beautysalon.gretta.entity.converter;

import com.beautysalon.gretta.entity.enums.CanalComunicacion;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CanalComunicacionConverter implements AttributeConverter<CanalComunicacion, String> {

    @Override
    public String convertToDatabaseColumn(CanalComunicacion attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public CanalComunicacion convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return CanalComunicacion.valueOf(dbData);
    }
}
