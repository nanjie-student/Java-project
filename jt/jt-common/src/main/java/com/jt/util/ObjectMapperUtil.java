package com.jt.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static String toJson(Object target){
        try {
            return MAPPER.writeValueAsString(target);
        }catch(JsonProcessingException e){
            e.printStackTrace();
            //将检查异常转化为运行时异常
            throw new RuntimeException(e);
        }
    }
    public static <T> T toObject(String json,Class<T> targetClass){
        try {
            return MAPPER.readValue(json, targetClass);
        }catch(JsonProcessingException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
