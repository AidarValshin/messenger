package RU.MEPHI.ICIS.C17501.messenger.config.websocket;

/*import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;*/

/*
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    public static class WebSocketProperties {

        */
/**
 * Префикс приложения
 * <p>
 * Префикс STOMP-endpoint-а
 *//*

        private static final String MESSENGER = "/messenger";

        */
/**
 * Префикс STOMP-endpoint-а
 *//*

        private static final String WS_STOMP_ENDPOINT_PREFIX = "/ws";
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WebSocketProperties.WS_STOMP_ENDPOINT_PREFIX);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //.setApplicationDestinationPrefixes(WebSocketProperties.MESSENGER);
        registry.enableSimpleBroker("/topic");
    }


}
*/
