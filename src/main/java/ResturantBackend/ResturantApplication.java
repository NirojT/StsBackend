package ResturantBackend;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import ResturantBackend.Entity.Table;

@SpringBootApplication
@EnableMongoAuditing
public class ResturantApplication implements CommandLineRunner {
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static final String SERVERURL = "http://localhost:9000/uploads/";
	public static final String FilePath="C:/Users/tmgni/Desktop/SpringBoots/deploy/images";
	public static final String configFilePath="file:///C:/Users/tmgni/Desktop/SpringBoots/deploy/images/";
	

	
	
	
	public static void main(String[] args) {
		SpringApplication.run(ResturantApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
;
	}

}
