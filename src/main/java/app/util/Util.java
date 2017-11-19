package app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Util {

    public static Util getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final Util instance = new Util();
    }

    private static final ObjectMapper mapper = new ObjectMapper();


    private Util() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public String toJson(Object object) {
        String json = null;
        try {
            json = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public <T> T fromJson(String stringJson, Class<T> type) {
        T object = null;
        try {
            object = mapper.readValue(stringJson, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    public String generateCode() {
        return CodeGenerator.getInstance().generateCode();
    }

}
