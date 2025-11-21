package com.beautysalon.gretta.entity.converter;

import com.beautysalon.gretta.entity.enums.EstadoCita;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EstadoCitaConverter implements AttributeConverter<EstadoCita, String> {

    @Override
    public String convertToDatabaseColumn(EstadoCita attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public EstadoCita convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return EstadoCita.valueOf(dbData);
    }
}
