package com.github.lyd.common.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * JsonWebToken工具类
 * @author liuyadu
 */
public class JwtUtils {


    /**
     * 生成token
     *
     * @param claims 加密内容
     * @param secret 秘钥
     * @param expire 过期时间
     * @return
     */
    public String generateToken(Map<String, Object> claims, String secret, int expire) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.SECOND, expire);
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(c.getTime())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return token;
    }

    /**
     * 验证并解密Token
     *
     * @param token
     * @param secret
     * @return 返回NULL表示验证失败
     */
    public Map<String, Object> validateAndExtractToken(String token, String secret) throws Exception {
        if (token == null) {
            return null;
        }
        try {
            Map<String, Object> body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            return body;
        } catch (Exception e) {
            throw new Exception("token已失效");
        }
    }
}
