package KoreatechJinJunGun.Win_SpringProject.service.login;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService implements InitializingBean {

    @Value("${jwt-secret-key}")
    private String SECRET_KEY;
    @Value("${jwt-expire-time}")
    private Long expireTime;
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;   //서명 알고리즘
    private Key signature; //서명
    private Key refreshSignature;

    public String createToken(Authentication authentication){
        log.info("createToken 실행");

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        /*
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .signWith(signature, signatureAlgorithm)
                .setExpiration(new Date(System.currentTimeMillis() + 30*1000*60))
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .signWith(signature, signatureAlgorithm)
                .setExpiration(new Date(System.currentTimeMillis() + 30*1000*60))
                .compact();
         */

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .signWith(signature, signatureAlgorithm)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .compact();
    }

    // 토큰으로 클레임을 만들고 이를 이용해 유저 객체를 만들어서 최종적으로 authentication 객체를 리턴
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(signature)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // 토큰의 유효성 검증을 수행
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signature).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //Base64로 인코딩된 비밀 키를 디코딩하여 바이트 배열로 저장
        byte[] keyByte = Decoders.BASE64.decode(SECRET_KEY);
        //디코딩된 비밀키와 서명 알고리즘을 넣어 Key(=서명) 생성
        //SecretKeySpec : Jwt를 서명하거나 서명을 검증하는 데 사용
        signature = new SecretKeySpec(keyByte, signatureAlgorithm.getJcaName());
    }
}
