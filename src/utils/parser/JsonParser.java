package utils.parser;

import com.google.gson.Gson;

public class JsonParser {
    public static <T> String serialize(T sObject){
        String tResult=null;
        Gson tGson = new Gson();
        tResult=tGson.toJson(sObject);
        return tResult;
    }

    public static <T> T unserialize(String sJsonString, Class<T> sClass){
        T tResult=null;
        Gson gson = new Gson();
        tResult = gson.fromJson(sJsonString, sClass);
        return tResult;
    }
}
