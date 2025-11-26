package com.beautysalon.gretta.entity.converter;

import com.beautysalon.gretta.entity.enums.RolUsuario;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RolUsuarioConverter implements AttributeConverter<RolUsuario, String> {

    @Override
    public String convertToDatabaseColumn(RolUsuario attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public RolUsuario convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return RolUsuario.valueOf(dbData);
    }
}
