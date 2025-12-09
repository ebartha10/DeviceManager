package com.platform.device.services;

import com.platform.device.dtos.SupportTopicDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupportTopicService {

    public List<SupportTopicDTO> getSupportTopics() {
        List<SupportTopicDTO> topics = new ArrayList<>();
        
        topics.add(new SupportTopicDTO(
            "connection", 
            "Device not connecting", 
            "Please ensure your device is powered on and within range of the Wi-Fi network. Try restarting the device by holding the power button for 5 seconds."
        ));
        
        topics.add(new SupportTopicDTO(
            "readings", 
            "Strange sensor readings", 
            "Unusual readings often indicate the sensor needs calibration. Please check the device maintenance guide or try a soft reset via the device settings."
        ));
        
        topics.add(new SupportTopicDTO(
            "add-device", 
            "How to add a new device", 
            "Click the 'ADD DEVICE' button in the top right of your dashboard. You can search for available devices and add them to your account instantly."
        ));
        
        topics.add(new SupportTopicDTO(
            "billing", 
            "Billing & Subscription", 
            "NEXUS currently offers free monitoring for up to 5 devices. For enterprise plans, please contact sales@nexus.com."
        ));
        
        topics.add(new SupportTopicDTO(
            "other", 
            "Other issues", 
            "For complex technical issues, please email our support team at support@nexus.com with your device ID and a description of the problem."
        ));
        
        return topics;
    }
}



