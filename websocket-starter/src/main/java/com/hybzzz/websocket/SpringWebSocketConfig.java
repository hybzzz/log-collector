package com.hybzzz.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Configuration
//@EnableWebMvc
@EnableWebSocket
public class SpringWebSocketConfig extends WebMvcConfigurerAdapter
        implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {


        registry.addHandler(webSocketHandler(), "/log-sockjs")
                .setAllowedOrigins("*")
                .addInterceptors(new WebSocketHandshakeInterceptor()).withSockJS();
    }

    @Bean
    public TextWebSocketHandler webSocketHandler() {

        return new TextMessageHandler();
    }
}