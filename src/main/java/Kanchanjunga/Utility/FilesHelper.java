package Kanchanjunga.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.rmi.server.UID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.KanchanjungaApplication;

@Component
public class FilesHelper {
	public String saveFile(MultipartFile file) throws IOException {
		// uuid for unique name of image
		UID uid = new UID();
		String uidString = uid.toString().replace(':', '_');
		String originalFilename = file.getOriginalFilename();
		String extension = "";
		int dotIndex = originalFilename.lastIndexOf('.');
		if (dotIndex >= 0) {
			extension = originalFilename.substring(dotIndex);
			originalFilename = originalFilename.substring(0, dotIndex);
		}
		String filename = originalFilename + "_" + uidString + extension;
		String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static";
		Path paths = Paths.get(uploadDir);
		if (!Files.exists(paths)) {
			Files.createDirectories(paths);
		}
		Path imagePath = paths.resolve(filename);
		try (InputStream inputStream = file.getInputStream()) {
			Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException("Fail to save file " + filename, e);
		}
		return KanchanjungaApplication.SERVERURL + filename;
	}

	public Boolean deleteExistingFile(String fileName) {
		String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static";
		String filePathString = fileName.replace(KanchanjungaApplication.SERVERURL, "");
		Path filePath = Paths.get(uploadDirectory, filePathString);
		try {
			Files.deleteIfExists(filePath);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
