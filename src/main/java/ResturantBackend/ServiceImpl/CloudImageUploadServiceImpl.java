package ResturantBackend.ServiceImpl;

import java.io.IOException;
import java.util.Map;

import org.modelmapper.internal.bytebuddy.asm.Advice.This;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import ResturantBackend.Entity.Users;
import ResturantBackend.Reposioteries.UserRepo;
import ResturantBackend.Services.CloudImageUploadService;


@Service
public class CloudImageUploadServiceImpl implements CloudImageUploadService {

	@Autowired
	UserRepo repo;
	
	@Autowired
	final Cloudinary cloudinary;
	public CloudImageUploadServiceImpl(Cloudinary cloudinary) {
		this.cloudinary=cloudinary;
	}
	
	@Override
	public Map upload(MultipartFile file,String username) {
	
		try {
			
			Users user = repo.findByName(username).get();
			
			System.out.println(user.toString());
			Map upload = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
			
		String urlString=(String)upload.get("url");
			user.setImage(urlString);
			repo.save(user);
			System.out.println(user.toString());
		return upload;
		
		} catch (IOException e) {
			throw new RuntimeException("upload fail"+e.getMessage());
		}
		
	}

}