package service.messageservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //For broadcasting messages
        registry.enableSimpleBroker("/chatbox");

        //Location where server accepts messages
        registry.setApplicationDestinationPrefixes("/app");
        //For P2P
        //For sending to user, with current setup of sending to chat rooms, this is redundant
        registry.setUserDestinationPrefix(("/dest"));
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        //Forces message to be JSON
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(APPLICATION_JSON);
        //Used to convert objects to JSON
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        //Add the newly created converter to the list
        messageConverters.add(converter);
        //Allow default converters
        return false;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //Origin should be frontend.com
        //Used to establish connection when user opens the tab for the web, at frontend.com/ws
        registry.addEndpoint("/ws").setAllowedOriginPatterns("http://localhost:3000", "http://sonbui.com");
    }
}
