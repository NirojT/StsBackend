package ResturantBackend.JWT;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtHelper {

	private static final String SECRET = "28472B4B6250655368566D5970337336763979244226452948404D635166546A";

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}



	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = extractExpiration(token);
		return (expiration != null && expiration.before(new Date()));
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public Boolean validateLoginToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String generateToken(String userName) {

		Map<String, Object> claims = new HashMap<>();

		return createToken(claims, userName);

	}

	private String createToken(Map<String, Object> claims, String userName) {

		long nowMillis = System.currentTimeMillis();
		long expMillis = nowMillis + (1L * 24L * 60L * 60L * 1000L); // 1 days in milliseconds
		Date exp = new Date(expMillis);
		  
		return Jwts.builder().setClaims(claims).setSubject(userName).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(exp).signWith(getSignKey(), SignatureAlgorithm.HS256).compact();

	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);

	}

}