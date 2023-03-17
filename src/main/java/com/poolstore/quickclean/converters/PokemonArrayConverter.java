package com.poolstore.quickclean.converters;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poolstore.quickclean.models.Pokemon;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;



import java.util.List;



@Converter
public class PokemonArrayConverter implements AttributeConverter<List<Pokemon>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();



    @Override
    public String convertToDatabaseColumn(List<Pokemon> pokemon) {
        try{


            return mapper.writeValueAsString(pokemon);
        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Pokemon> convertToEntityAttribute(String json) {
        try{

            TypeReference<List<Pokemon>> typeRef = new TypeReference<>() {};

            return mapper.readValue(json,typeRef);
        } catch(JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
