package com.example.AestiqueClothing.Encryption;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EncryptDecryptConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return (attribute == null) ? null : AESUtil.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return (dbData == null) ? null : AESUtil.decrypt(dbData);
    }
}
