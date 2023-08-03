package sport.app.sport_connecting_people.util;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import sport.app.sport_connecting_people.exceptions.jwt.InvalidTokenException;
import sport.app.sport_connecting_people.exceptions.jwt.TokenExpiredException;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @Value("${JWT_PRIVATE_KEY}")
    private String privateKeyValue;

    @Value("${JWT_PUBLIC_KEY}")
    private String publicKeyValue;

    @PostConstruct
    public void init() throws Exception {
        privateKey = readPrivateKey(privateKeyValue);
        publicKey = readPublicKey(publicKeyValue);
    }

    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public boolean validateToken(String jwt) {
        try {
            Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(jwt);
            return true;
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Token has expired");
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            throw new InvalidTokenException("Invalid token");
        } catch (SignatureException e) {
            throw new InvalidTokenException("Invalid token signature");
        }
    }

    public Long getUserId(String jwt) {
        return Long.valueOf(Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject());
    }

    private PrivateKey readPrivateKey(String key) throws Exception {
        byte[] bytes = Base64.getMimeDecoder().decode(key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    private PublicKey readPublicKey(String key) throws Exception {
        byte[] bytes = Base64.getMimeDecoder().decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }
}
