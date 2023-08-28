package Kanchanjunga.Services;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface CloudImageUploadService {
	Map<?, ?> upload(MultipartFile file, String username);
}
