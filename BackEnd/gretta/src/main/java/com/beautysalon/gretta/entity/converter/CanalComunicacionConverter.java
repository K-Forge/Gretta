package com.beautysalon.gretta.entity.converter;

import com.beautysalon.gretta.entity.enums.CanalComunicacion;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

@Converter(autoApply = false)
public class CanalComunicacionConverter implements AttributeConverter<CanalComunicacion, Object> {

    @Override
    public Object convertToDatabaseColumn(CanalComunicacion attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("canal_comunicacion");
            pgObject.setValue(attribute.name());
            return pgObject;
        } catch (SQLException e) {
            throw new RuntimeException("Error converting enum to PostgreSQL type", e);
        }
    }

    @Override
    public CanalComunicacion convertToEntityAttribute(Object dbData) {
        if (dbData == null) {
            return null;
        }
        String value = dbData.toString();
        if (value.isEmpty()) {
            return null;
        }
        return CanalComunicacion.valueOf(value);
    }
}
