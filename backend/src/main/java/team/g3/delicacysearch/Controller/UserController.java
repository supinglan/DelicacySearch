package team.g3.delicacysearch.Controller;

import team.g3.delicacysearch.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String,Object> login(String username, String password) {
        return userService.login(username, password);
    }
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Map<String, Object> register( String email, String username, String password) {
        System.out.println(email);
        System.out.println(username);
        System.out.println(password);
        return userService.register(email,username, password);
    }
}