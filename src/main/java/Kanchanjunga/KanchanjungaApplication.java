package Kanchanjunga;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import Kanchanjunga.Entity.FoodStock;
import Kanchanjunga.Reposioteries.FoodStockRepo;

@SpringBootApplication
@EnableMongoAuditing
public class KanchanjungaApplication implements CommandLineRunner {
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Autowired
	private FoodStockRepo foodStockRepo;
	
	public static final String SERVERURL = "http://localhost:9000/";
	public static void main(String[] args) {
		SpringApplication.run(KanchanjungaApplication.class, args);
		
		
	
	}
	@Override
	public void run(String... args) throws Exception {
	
	}
	
	
		
	

}
