package pers.spl.ziot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pers.spl.ziot.authentication.JwtUitls;
import pers.spl.ziot.dao.UserMapper;
import pers.spl.ziot.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userDao;
    @Autowired
    JwtUitls jwtUitls;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Override
    public Map<String, Object> login(String username, String password) {
        User user = userDao.selectByName(username);
        Map<String, Object> result = new HashMap<String, Object>();
        if (user == null) {
            result.put("status", 1);
        } else if (encoder.matches(password, user.getPassword())) {
            System.out.println(username + " login success\n");
            result.put("status", 0);
            result.put("token", jwtUitls.createToken(user));
        } else {
            result.put("status", 2);
        }
        return result;
    }

    @Override
    public int register(String email, String username, String password) {
        User user = userDao.selectByEmail(email);
        if (user != null) {
            return 1;
        }
        user = userDao.selectByName(username);
        if (user != null) {
            return 2;
        }
        user = new User();
        user.setEmail(email);
        user.setName(username);
        String encodePassword = encoder.encode(password);
        user.setPassword(encodePassword);
        userDao.insert(user);
        return 0;
    }
    @Override
    public Map<String, Object> getInfo(int id) {
        User user = userDao.selectByPrimaryKey(id);
        Map<String, Object> result = new HashMap<String, Object>();
        if (user == null) {
            result.put("status", 1);
        } else{
            result.put("status", 0);
            result.put("email", user.getEmail());
            result.put("username", user.getName());
            result.put("phone", user.getPhone());
            result.put("location", user.getLocation());
            result.put("birthday", user.getBirthday());
        }
        return result;
    }

    @Override
    public int modify(int id, String key, String value) {
        User user = userDao.selectByPrimaryKey(id);
        if (user == null) {
            return 1;
        }
        switch (key) {
            case "email":
                user.setEmail(value);
                break;
            case "username":
                user.setName(value);
                break;
            case "phone":
                user.setPhone(value);
                break;
            case "location":
                user.setLocation(value);
                break;
            case "birthday":
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = dateFormat.parse(value);
                    user.setBirthday(date);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                break;
            default:
                return 2;
        }
        userDao.updateByPrimaryKey(user);
        return 0;
    }
}
