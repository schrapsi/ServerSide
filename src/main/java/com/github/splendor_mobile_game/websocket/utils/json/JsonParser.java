package com.github.splendor_mobile_game.websocket.utils.json;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.github.splendor_mobile_game.websocket.utils.json.exceptions.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

/**
 * A utility class for parsing JSON strings into Java objects.
 */
public class JsonParser {

    // TODO: This function can be unit tested
    /**
     * Parses a JSON string into a Java object of the specified class.
     *
     * @param jsonString the JSON string to parse
     * @param clazz      the class of the Java object to create
     * @param <T>        the type of the Java object to create
     * 
     * @return the Java object created from the JSON string
     * 
     * @throws JsonParserException      if there is an error parsing the JSON string
     * @throws JsonIsNotValidJsonObject if the JSON string is not a valid JSON object
     * @throws JsonIsNullException     if the JSON string is null or empty
     * @throws JsonMissingFieldException if a required field is missing from the JSON object
     */
    public static <T> T parseJson(String jsonString, Class<T> clazz) throws JsonParserException {
        Gson gson = new Gson();
    
        // Parse the JSON string into a JsonObject
        JsonObject jsonObject;
        try {
            jsonObject = gson.fromJson(jsonString, JsonObject.class);
        } catch (JsonSyntaxException e) {
            throw new JsonIsNotValidJsonObject("Received string is not valid json object <= " + e.getMessage(), e);
        }
    
        // Check if the JsonObject is null or empty
        if (jsonObject == null)
            throw new JsonIsNullException("Provided json string is null or is empty!");
    
        // Check if all required fields are present in the JsonObject
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();
        
        for (Field field : fields) {
            if (field.getName() == "this$0")
                continue;

            if (field.isAnnotationPresent(Optional.class))
                continue;

            if (!jsonObject.has(field.getName())) {
                stringBuilder.append("Missing required field: " + field.getName() + "\n");
            } 
            else if (!JsonParser.isSimpleType(field.getType()) && !field.getType().isEnum()) {
                JsonParser.parseJson(jsonObject.get(field.getName()).toString(), field.getType());
            }
        }

        if (stringBuilder.length() != 0)
            throw new JsonMissingFieldException(stringBuilder.toString().strip());
    
        // Parse the JsonObject into an object of the specified class
        T object = gson.fromJson(jsonString, clazz);
        return object;
    }

    private static boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive() || JsonParser.simpleTypes.contains(clazz);
    }

    private static Set<Class<?>> simpleTypes = new HashSet<>() {{
        add(String.class);
        add(UUID.class);
    }};

}
