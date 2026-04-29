package com.inventory.store.client.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convierte valores Boolean a CHAR(1) para la base de datos.
 * true -> '1'
 * false -> '0'
 */
@Converter(autoApply = true)
public class BooleanToCharConverter implements AttributeConverter<Boolean, Character> {

    @Override
    public Character convertToDatabaseColumn(Boolean attribute) {
        if (attribute == null) {
            return '1'; // Valor por defecto
        }
        return attribute ? '1' : '0';
    }

    @Override
    public Boolean convertToEntityAttribute(Character dbData) {
        if (dbData == null) {
            return true; // Valor por defecto
        }
        return dbData == '1';
    }
}
