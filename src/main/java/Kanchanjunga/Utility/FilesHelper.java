package Kanchanjunga.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.rmi.server.UID;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.KanchanjungaApplication;
//public String saveFile(MultipartFile file) throws IOException {
//	

@Service
public class FilesHelper {

	private final Path fileStorageLocation;

	@Autowired
	public FilesHelper(Environment env) {
		this.fileStorageLocation = Paths.get("C:/Users/Acer/Desktop/work/kanchanjunga/sts-backend-images")
				.toAbsolutePath().normalize();

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
		String[] fileNameParts = fileName.split("/.");

		return fileNameParts[fileNameParts.length - 1];
	}

	public String saveFile(MultipartFile file) {
		// Normalize file name
		String fileName = new Date().getTime() + "-file." + getFileExtension(file.getOriginalFilename());

		try {
			// Check if the filename contains invalid characters
			if (fileName.contains("..")) {
				throw new RuntimeException(
						"Sorry! Filename contains invalid path sequence " + fileName);
			}

			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return KanchanjungaApplication.SERVERURL + fileName;
		} catch (IOException ex) {
			throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	public Boolean deleteExistingFile(String fileName) {
		String uploadDir = "C:/Users/Acer/Desktop/work/kanchanjunga/sts-backend-images";
		String filePathString = fileName.replace(KanchanjungaApplication.SERVERURL, "");
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