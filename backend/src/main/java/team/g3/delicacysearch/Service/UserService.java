package team.g3.delicacysearch.Service;

import org.springframework.stereotype.Service;

import java.util.Map;

public interface UserService{
    public Map<String, Object> login(String username, String password);
    public Map<String, Object>  register(String email, String username, String password);

}
