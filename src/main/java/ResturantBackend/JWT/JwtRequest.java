package ResturantBackend.JWT;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtRequest {

	private String name;
	private String password;
}
