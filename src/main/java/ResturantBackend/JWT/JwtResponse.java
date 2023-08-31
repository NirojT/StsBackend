package ResturantBackend.JWT;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {

	private String token;
	private String userRole;
}
