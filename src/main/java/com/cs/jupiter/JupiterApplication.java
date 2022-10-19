package com.cs.jupiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;

@SpringBootApplication
@org.springframework.context.annotation.Configuration
@ComponentScan({ "com.cs.jupiter" })
public class JupiterApplication {

	@Bean/*web socket */
	public SocketIOServer socketIOServer() {
		Configuration config = new Configuration();
		config.setHostname("localhost");
		config.setPort(8084);
		return new SocketIOServer(config);
	}

	@Bean
	public SpringAnnotationScanner springannotationscanner(SocketIOServer socketserver) {
		return new SpringAnnotationScanner(socketserver);
	}
	
//	@Bean
//	public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
//		System.out.println("cookie");
//	    return new WebServerFactoryCustomizer<TomcatServletWebServerFactory>() {
//			@Override
//			public void customize(TomcatServletWebServerFactory factory) {
//				factory.addContextCustomizers(new TomcatContextCustomizer() {
//					@Override
//					public void customize(Context context) {
//						context.setCookieProcessor(new LegacyCookieProcessor());
//					}
//				});
//			}
//	    };
//	}
	public static void main(String[] args) {
		SpringApplication.run(JupiterApplication.class, args);
	}

}
