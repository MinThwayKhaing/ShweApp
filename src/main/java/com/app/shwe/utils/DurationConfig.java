package com.app.shwe.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Configuration
public class DurationConfig {

	@Value("classpath:duration.json")
	private Resource durationResource;

	@Bean
	public List<String> durationList() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		try (InputStream inputStream = durationResource.getInputStream()) {
			JsonNode root = objectMapper.readTree(inputStream);
			JsonNode durationsNode = root.path("durations");
			List<String> durations = new ArrayList<>();
			if (durationsNode.isArray()) {
				for (JsonNode node : durationsNode) {
					durations.add(node.asText());
				}
			}
			return durations;
		}
	}

}
