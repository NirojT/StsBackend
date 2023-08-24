package Kanchanjunga;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class KanchanjungaApplication implements CommandLineRunner {
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static final String SERVERURL = "http://localhost:9000/uploads/";

	public static void main(String[] args) {
		SpringApplication.run(KanchanjungaApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

	}

}
