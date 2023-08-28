package Kanchanjunga.Config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cloudinary.Cloudinary;

@Configuration
public class ResourceWebConfig implements WebMvcConfigurer {

	final Environment environment;

	public ResourceWebConfig(Environment env) {
		environment = env;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**")
				.addResourceLocations("file:///C:/Users/Acer/Desktop/work/kanchanjunga/sts-backend-images/");
	}

	// upload at cloud file service
	@Bean
	public Cloudinary getCloudinary() {

		Map<String, Object> map = new HashMap<>();
		map.put("cloud_name", "dndmly1jg");
		map.put("api_key", "689366123252814");
		map.put("api_secret", "XhiF2qbC2x0MgcH10sibjc0Tr4g");
		map.put("secure", true);

		return new Cloudinary(map);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://localhost:5173") // Add your client's origin here
				.allowedMethods("GET", "POST", "PUT", "DELETE")
				.allowCredentials(true);
	}

}
