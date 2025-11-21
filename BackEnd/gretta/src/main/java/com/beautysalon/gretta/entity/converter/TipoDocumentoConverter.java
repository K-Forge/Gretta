package com.beautysalon.gretta.entity.converter;

import com.beautysalon.gretta.entity.enums.TipoDocumento;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TipoDocumentoConverter implements AttributeConverter<TipoDocumento, String> {

    @Override
    public String convertToDatabaseColumn(TipoDocumento attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public TipoDocumento convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return TipoDocumento.valueOf(dbData);
    }
}
