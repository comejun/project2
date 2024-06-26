package com.react.project2.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JWTUtil {

    // 인코딩된 키
    private static final String key =
            "c2phbnNqYW5ybmxja3Nncmhna3JsdGxmZ2Rtc3ZtZmh3cHJ4bWRqc3dwUm14c2tmdXNr";

    // 토큰 생성 메서드
    public static String generateToken(Map<String, Object> valueMap, int min) {
        // 암호화된 비밀키
        SecretKey secretKey = null;
        // 인코딩된 키를 '암호화된 비밀키'로 변경
        secretKey = Keys.hmacShaKeyFor(JWTUtil.key.getBytes(StandardCharsets.UTF_8));
        log.info("secretKey : {}", secretKey);

        // JWT 토큰 생성
        String jwtStr = Jwts.builder()
                .setHeader(Map.of("typ", "JWT")) // 헤더 정보
                .setClaims(valueMap) // 페이로드(Claim)에 추가할 (사용자관련)데이터
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(secretKey) // 비밀키로 서명
                .compact(); // 토큰 생성 -> 문자열 리턴
        log.info("jwtStr : {}", jwtStr);

        // 토큰 리턴
        return jwtStr;
    }

    // 토큰 유효성 검증 메서드 : Claim 리턴
    public static Map<String, Object> validateToken(String token) {
        Map<String, Object> claim = null;
        SecretKey secretKey = null;

        try {
            secretKey = Keys.hmacShaKeyFor(JWTUtil.key.getBytes(StandardCharsets.UTF_8));
            claim = Jwts.parserBuilder()
                    .setSigningKey(secretKey) // 비밀키 세팅
                    .build()
                    .parseClaimsJws(token) // 파싱 및 검증 -> 실패하면 에러 발생 -> catch 로 잡기
                    .getBody(); // claim 리턴
        } catch (MalformedJwtException e) { // 잘못된 형식의 토큰 예외
            throw new CustomJWTException("Malformed");
        } catch (ExpiredJwtException e) { // 만료된 토큰 예외
            throw new CustomJWTException("Expired");
        } catch (InvalidClaimException e) { // 유효하지 않는 Claim시 예외
            throw new CustomJWTException("Invalid");
        } catch (JwtException e) { // 그 외 Jwt 관련 예외
            log.info("JwtException : {}", e.getMessage());
            throw new CustomJWTException("JWTError");
        } catch (Exception e) { // 그 외 나머지 예외
            throw new CustomJWTException("Error");
        }
        return claim;
    }

}
