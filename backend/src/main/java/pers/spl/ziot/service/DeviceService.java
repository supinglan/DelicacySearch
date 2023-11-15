package pers.spl.ziot.service;

import pers.spl.ziot.model.Device;
import pers.spl.ziot.model.Message;

import java.util.List;
import java.util.Map;

public interface DeviceService {

    public Map<String, Object> getInfo(int id);
    public int modify(int id, String key, String value);
    public int add(String name, String owner_id, String category);
    public Map<String, Integer> getStat(int id);
    public List<Device> getList(int id);
    public List<Message> getMessages(int id);
    public int getMessageStat(int id);
}
