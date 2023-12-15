package team.g3.delicacysearch.Service;

import team.g3.delicacysearch.dao.UserMapper;
import team.g3.delicacysearch.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userDao;

    @Override
    public Map<String, Object> login(String username, String password) {
        User user = userDao.selectByName(username);
        Map<String, Object> result = new HashMap<String, Object>();
        if (user == null) {
            result.put("message", "用户不存在");
        } else if (password.equals(user.getPassword())) {
            System.out.println(username + " login success\n");
            result.put("id", user.getId());
        } else {
            result.put("message", "密码错误");
        }
        return result;
    }

    @Override
    public Map<String, Object> register(String email, String username, String password) {

        Map<String, Object> result = new HashMap<String, Object>();
        User user = userDao.selectByEmail(email);
        if (user != null) {
            result.put("message", "电子邮件已被注册");
            return result;
        }
        user = userDao.selectByName(username);
        if (user != null) {
            result.put("message", "用户名已被注册");
            return result;
        }
        user = new User();
        user.setEmail(email);
        user.setName(username);
        user.setPassword(password);
        userDao.insert(user);
        return result;
    }
}
