package pers.spl.ziot.authentication;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Component;
import pers.spl.ziot.dao.UserMapper;
import pers.spl.ziot.model.User;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUitls {

    @Autowired
    UserMapper userDao;

    //token过期时间
    private static final long EXPIRE_TIME=60*60*1000;

    //token秘钥
    private static final String KEY = "ziot2023";

    //创建token
    public String createToken(User user){
        Map<String,Object> header = new HashMap();
        header.put("typ","JWT");
        header.put("alg","HS256");
        JwtBuilder builder = Jwts.builder().setHeader(header)
                .claim("id",user.getId())
                .claim("username",user.getName())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256,KEY.getBytes(Charset.forName("UTF-8")));
        String token = builder.compact();
        return token;
    }


    //验证token
    public int verify(String token){
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(KEY.getBytes(Charset.forName("UTF-8"))).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            //token过期
            return 2;
        }
        //检查token中保存的用户id是否存在
        Integer id = (Integer) claims.get("id");
        User user = userDao.selectByPrimaryKey(id);
        if(user != null){
            return 0;
        }else{
            return 1;
        }
    }
}
