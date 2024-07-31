package com.app.shwe.utils;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FilesSerializationUtil {
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	public static String serializeImages(List<String> images) throws IOException {
        return objectMapper.writeValueAsString(images);
    }

	public static List<String> deserializeImages(String images) throws IOException {
        return objectMapper.readValue(images, new TypeReference<List<String>>() {});
    }
}
