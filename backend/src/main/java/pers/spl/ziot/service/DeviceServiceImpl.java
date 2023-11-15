package pers.spl.ziot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.spl.ziot.dao.DeviceMapper;
import pers.spl.ziot.dao.MessageMapper;
import pers.spl.ziot.dao.UserMapper;
import pers.spl.ziot.model.Device;
import pers.spl.ziot.model.Message;
import pers.spl.ziot.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeviceServiceImpl implements DeviceService{
    @Autowired
    DeviceMapper deviceDao;
    @Autowired
    UserMapper userDao;
    @Autowired
    MessageMapper messageDao;
    @Override
    public Map<String, Object> getInfo(int id) {
        Device device = deviceDao.selectByPrimaryKey(id);
        Map<String, Object> result = new HashMap<String, Object>();
        if (device == null) {
            result.put("status", 1);
        } else{
            result.put("status", 0);
            result.put("name", device.getName());
            result.put("activateTime", device.getActivateTime());
            result.put("category", device.getCategory());
            result.put("description", device.getDescription());
            result.put("state", device.getState());
            result.put("owner", userDao.selectByPrimaryKey(device.getOwnerId()).getName());
        }
        return result;
    }
    @Override
    public int modify(int id, String key, String value) {
        Device device = deviceDao.selectByPrimaryKey(id);
        if (device == null) {
            return 1;
        }
        switch (key) {
            case "name":
                device.setName(value);
                break;
            case "activateTime":
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = sdf.parse(value);
                    device.setActivateTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 2;
                }
                break;
            case "category":
                device.setCategory(Integer.parseInt(value));
                break;
            case "description":
                device.setDescription(value);
                break;
            case "state":
                device.setState(Integer.parseInt(value));
                break;
            case "owner":
                User user = userDao.selectByName(value);
                if (user == null) {
                    return 3;
                }
                device.setOwnerId(user.getId());
                break;
            default:
                return 4;
        }
        return 0;
    }
    @Override
    public int add(String name, String owner_id, String category) {
        Device device = new Device();
        device.setName(name);
        device.setOwnerId(Integer.parseInt(owner_id));
        device.setCategory(Integer.parseInt(category));
        deviceDao.insert(device);
        return 0;
    }

    @Override
    public Map<String, Integer> getStat(int id) {
        List<Device> list = deviceDao.selectByOwner(id);
        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put("total", list.size());
        int online = 0, car = 0, home = 0, wearableDevice = 0, other = 0;
        for (Device device : list) {
            if (device.getState() == 1) {
                online++;
            }
            switch (device.getCategory()) {
                case 1:
                    car++;
                    break;
                case 2:
                    home++;
                    break;
                case 3:
                    wearableDevice++;
                    break;
                default:
                    other++;
                    break;
            }
        }
        result.put("online", online);
        result.put("car", car);
        result.put("home", home);
        result.put("wearableDevice", wearableDevice);
        result.put("other", other);
        return result;
    }
    @Override
    public List<Device> getList(int id) {
        List<Device> list = deviceDao.selectByOwner(id);
        return list;
    }
    @Override
    public List<Message> getMessages(int id) {
        List<Message> list = messageDao.selectByDevice(id);
        return list;
    }
    @Override
    public int getMessageStat(int id) {
        List<Message> list = messageDao.selectByDevice(id);
        return list.size();
    }
}
