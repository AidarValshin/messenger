package RU.MEPHI.ICIS.C17501.messenger.service;

import RU.MEPHI.ICIS.C17501.messenger.db.repo.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    public List<String> getAllDevicesIdsByTelNumber(String telNumber) {
        return deviceRepository.findAllUserDevices(telNumber);
    }
}
