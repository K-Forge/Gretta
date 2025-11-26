package com.beautysalon.gretta.entity.converter;

import com.beautysalon.gretta.entity.enums.TipoMensaje;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoMensajeConverter implements AttributeConverter<TipoMensaje, String> {

    @Override
    public String convertToDatabaseColumn(TipoMensaje attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public TipoMensaje convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return TipoMensaje.valueOf(dbData);
    }
}
