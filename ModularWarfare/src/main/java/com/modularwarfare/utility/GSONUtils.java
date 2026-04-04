package com.modularwarfare.utility;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
public class GSONUtils {

    @Nullable
    public static <T> T fromJson(Gson gson, JsonReader reader, Type type) {
        try {
            TypeToken<T> token = (TypeToken<T>) TypeToken.get(type);
            return gson.getAdapter(token).read(reader);
        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }
}