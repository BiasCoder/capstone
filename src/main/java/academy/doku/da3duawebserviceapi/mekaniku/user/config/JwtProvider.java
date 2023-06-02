//package academy.doku.da3duawebserviceapi.mekaniku.user.config;
//
//
//import io.jsonwebtoken.*;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.List;
//
//@Component
//public class JwtProvider {
//
//    private final String jwtSecret = "myJwtSecretKey"; // ganti dengan secret key yang sesuai
//    private final long jwtExpirationMs = 86400000; // 24 jam
//
//    public String generateToken(String username, List<String> roles) {
//        Date now = new Date();
//        Date expiration = new Date(now.getTime() + jwtExpirationMs);
//
//        return Jwts.builder()
//                .setSubject(username)
//                .claim("roles", roles)
//                .setIssuedAt(now)
//                .setExpiration(expiration)
//                .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                .compact();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
//            return true;
//        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException ex) {
//            return false;
//        }
//    }
//
//    public String getUsernameFromToken(String token) {
//        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
//        return claims.getSubject();
//    }
//
//    public List<String> getRolesFromToken(String token) {
//        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
//        List<String> roles = (List<String>) claims.get("roles");
//        return roles;
//    }
//}
//
