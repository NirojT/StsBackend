package ResturantBackend.Config;

import java.util.HashMap;
import java.util.Map;

import ResturantBackend.Utility.FilesHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cloudinary.Cloudinary;



@Configuration
public class ResourceWebConfig implements WebMvcConfigurer {

	final Environment environment;
	public ResourceWebConfig(Environment env) {
		environment=env;
	}


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/uploads/**")
		.addResourceLocations(FilesHelper.configFilePath);
	}

	
	
	//upload at cloud file service
	@Bean
	public Cloudinary getCloudinary() {
		
		Map <String,Object>map=new HashMap<>();
		map.put("cloud_name", "dndmly1jg");
		map.put("api_key", "689366123252814");
		map.put("api_secret", "XhiF2qbC2x0MgcH10sibjc0Tr4g");
		map.put("secure", true);
		
		
		
		return new Cloudinary(map);
	}
	
	
	
}
