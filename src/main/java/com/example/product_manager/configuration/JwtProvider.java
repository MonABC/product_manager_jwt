package com.example.product_manager.configuration;

import com.example.product_manager.model.entity.User;
import com.example.product_manager.service.user.UserServiceImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {
    private static final String SECRET_KEY = "123456789"; //mã số bí mật
    private static final Long EXPIRE_TIME = 86400000000L;
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Autowired
    private UserServiceImpl userService;

    public String generateTokenLogin(String username) { // tạo ra jwt từ thông tin user
        return Jwts.builder()
                .setSubject((username))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + EXPIRE_TIME * 1000))
                .signWith(SignatureAlgorithm.ES256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {}", e); //kiểm tra phần chữ kí điện tử
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT Token -> Message: {}", e); // kiểm tra định dạng
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT Token -> Message: {}", e); // kiểm thời gian sống
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT Token -> Message: {}", e); // kiểm tra hỗ trợ
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string empty -> Message: {}", e); // kiểm tra rỗng
        }
        return false;
    }

    public String getUserNameFromJwtToken(String token) { //lấy thông tin từ chuỗi Jwt
        String userName = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return userName;
    }

    public User getUserByUsername(String username) {
        return userService.findByUserName(username);
    }
}
