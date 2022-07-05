package com.example.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Component
public class WebSocketConfig {

		@Bean
		public ServerEndpointExporter serverEndpointExporter() {
			return new ServerEndpointExporter();
		}
}
