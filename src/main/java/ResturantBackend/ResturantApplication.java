package ResturantBackend;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import ResturantBackend.Utility.EnglishToNepaliDateConverter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableMongoAuditing
@EnableScheduling
public class ResturantApplication implements CommandLineRunner {

	public static String hello;

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Autowired
	static EnglishToNepaliDateConverter englishToNepaliDateConverter;

	public static void main(String[] args) {
		SpringApplication.run(ResturantApplication.class, args);

	}
	@Scheduled(cron = "0 5 22 * * ?")
	public void generateExcelSheetAt10_05PM() {
		System.out.println("Generating Excel sheet at " + LocalTime.now()+"______________________________________________________________________________________");

	}

	public static String CurrentNepaliDate = "";
	public static String CurrentNepaliYearMonth = "";
	public static int CurrentNepaliYear = 0;

	@Override
	public void run(String... args) throws Exception {

		InetAddress localHost=InetAddress.getLocalHost();
		String ipAddress =localHost.getHostAddress();
		System.out.println("Ip address of this net is :"+ipAddress);
		CurrentNepaliDate=englishToNepaliDateConverter.convertToNepaliDate(new
		Date());
		CurrentNepaliYearMonth=englishToNepaliDateConverter.getCurrentNepaliYearMonth();
		CurrentNepaliYear=englishToNepaliDateConverter.getCurrentNepaliYear();

		System.out.println("from 1 "+CurrentNepaliDate);
		System.out.println("from 1 "+CurrentNepaliYearMonth);
		System.out.println("from 1 "+CurrentNepaliYear);

		String relativePath = "Server/images";
		Path normalize = Paths.get(System.getProperty("user.home"), relativePath).toAbsolutePath().normalize();
		System.out.println("_______ "+normalize);


	}

}
