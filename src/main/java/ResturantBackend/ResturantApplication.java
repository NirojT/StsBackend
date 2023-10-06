package ResturantBackend;

import java.net.InetAddress;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ResturantApplication implements CommandLineRunner {	
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	
	
	public static void main(String[] args) {
		SpringApplication.run(ResturantApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		
		 InetAddress localHost=InetAddress.getLocalHost();
		  String ipAddress =localHost.getHostAddress();
		   System.out.println("Ip address of this net is :"+ipAddress);
	}

}
