package Kanchanjunga.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import Kanchanjunga.JWT.JwtHelper;
import Kanchanjunga.Services.CloudImageUploadService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/cloud/")
public class CloudController {

	@Autowired
	CloudImageUploadService cloudImageUploadService;

	@Autowired
	private JwtHelper jwtHelper;

	@PostMapping("upload")
	public ResponseEntity<?> uploadImage(@RequestParam MultipartFile file, HttpServletRequest request

	) {
		HashMap<String, Object> response = new HashMap<>();
		try {
			String requestHeader = request.getHeader("Authorization");
			String token = null;
			if (requestHeader != null && requestHeader.startsWith("Bearer")) {
				token = requestHeader.substring(7);

				Boolean isValid = jwtHelper.validateLoginToken(token);

				if (!isValid) {
					response.put("status", 400);
					response.put("message", "invalid token");
					return ResponseEntity.status(200).body(response);
				}

				String username = jwtHelper.extractUsername(token);
				System.out.println(username);
				Map<?, ?> upload = cloudImageUploadService.upload(file, username);

				response.put("status", upload.isEmpty() ? 400 : 200);
				response.put("message", upload.isEmpty() ? "not uploaded" : "uploaded");
				return ResponseEntity.status(200).body(response);

			}

			return ResponseEntity.status(200).body("fail");
		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", 500);
			response.put("message", "something went wrong... ");
			return ResponseEntity.status(200).body(response);
		}

	}
}
