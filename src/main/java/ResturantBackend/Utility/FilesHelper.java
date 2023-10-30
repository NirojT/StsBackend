package ResturantBackend.Utility;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;




@Service
public class FilesHelper{
	// for file
	private final Path fileStorageLocation;
	public static  String configFilePath;


	//for server url
	//get ip address dynamically
	static InetAddress localHost;
	static String ipAddress;
	public  String SERVERURL;


	@Autowired
	public FilesHelper(Environment env) {
		//for server
		try {
			localHost = InetAddress.getLocalHost();
			ipAddress = localHost.getHostAddress();
			// ipAddress=192.168.0.107
			//  SERVERURL = "http://"+ipAddress+":9000/uploads/";
			SERVERURL = "http://localhost:9000/uploads/";

		} catch (Exception e) {

		}

		//for file
		String filePathString = "C:/Server/images";
		this.fileStorageLocation = Paths.get(filePathString).toAbsolutePath().normalize();
		configFilePath="file:///"+fileStorageLocation+"/";


		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new RuntimeException(
					"Could not create the directory where the uploaded files will be stored.", ex);
		}



	}

	private String getFileExtension(String fileName) {
		if (fileName == null) {
			return null;
		}
		String[] fileNameParts = fileName.split("\\.");

		return fileNameParts[fileNameParts.length - 1];
	}

	public String saveFile(MultipartFile file) {
		// Normalize file name
		String fileName =
				new Date().getTime() + "-file." + getFileExtension(file.getOriginalFilename());

		try {
			// Check if the filename contains invalid characters
			if (fileName.contains("..")) {
				throw new RuntimeException(
						"Sorry! Filename contains invalid path sequence " + fileName);
			}

			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);


			return SERVERURL+ fileName;
		} catch (IOException ex) {
			throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}
	public Boolean deleteExistingFile(String fileName) {
		String uploadDir = "C:/Server/images";
		String filePathString = fileName.replace(SERVERURL, "");
		Path filePath = Paths.get(uploadDir, filePathString);
		try {
			Files.deleteIfExists(filePath);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}