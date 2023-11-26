package ResturantBackend.Controller;

import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ResturantBackend.JWT.JwtHelper;
import ResturantBackend.Services.CloudImageUploadService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/cloud/")
@CrossOrigin(origins = { "http://127.0.0.1:5173/", "http://localhost:5173/",
		"https://cute-taiyaki-355152.netlify.app",
		"http://192.168.0.102:5173/" }, allowCredentials = "true")
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
				Map upload = cloudImageUploadService.upload(file, username);

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
