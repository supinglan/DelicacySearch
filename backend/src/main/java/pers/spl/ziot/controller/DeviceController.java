package pers.spl.ziot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.spl.ziot.authentication.JwtUitls;
import pers.spl.ziot.authentication.PrivateMethod;
import pers.spl.ziot.model.Device;
import pers.spl.ziot.model.Message;
import pers.spl.ziot.service.DeviceService;

import java.util.List;
import java.util.Map;

@RequestMapping("/device")
@RestController
public class DeviceController {
    @Autowired
    DeviceService deviceService;
    @Autowired
    JwtUitls jwtUitls;
    @PrivateMethod
    @PostMapping("/getInfo")
    public Map<String,Object> getInfo(@RequestParam("id") int id) {

        return deviceService.getInfo(id);
    }
    @PrivateMethod
    @PostMapping("/modify")
    public int modify(@RequestParam Map<String,String> info) {
        String id = info.get("id");
        String key = info.get("key");
        String value = info.get("value");
        return deviceService.modify(Integer.parseInt(id),key,value);
    }
    @PrivateMethod
    @PostMapping("/add")
    public int add(@RequestParam Map<String,String> info) {
        String name = info.get("name");
        String own_id = info.get("own_id");
        String category = info.get("category");
        return deviceService.add(name,own_id,category);
    }
    @PrivateMethod
    @PostMapping("/getStat")
    public Map<String, Integer> getStat(@RequestParam("id") int id)
    {
        return deviceService.getStat(id);
    }
    @PrivateMethod
    @PostMapping("/getList")
    public List<Device> getList(@RequestParam("id") int id)
    {
        return deviceService.getList(id);
    }
    @PrivateMethod
    @PostMapping("/getMessages")
    public List<Message> getMessages(@RequestParam("id") int id)
    {
        return deviceService.getMessages(id);
    }
    @PrivateMethod
    @PostMapping("/getMessageStat")
    public int getMessageStat(@RequestParam("id") int id)
    {
        return deviceService.getMessageStat(id);
    }

}