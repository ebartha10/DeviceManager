package com.platform.device.utils;

import java.util.HashMap;
import java.util.Map;

public class ChatRules {
    private static final Map<String, String> rules = new HashMap<>();

    static {
        rules.put("hello", "Hello! I am your virtual assistant. How can I help you?");
        rules.put("hi", "Hi there! How can I assist you today?");
        rules.put("reset password", "To reset your password, go to Settings > Security > Reset Password.");
        rules.put("forgot password", "If you forgot your password, click 'Forgot Password' on the login screen.");
        rules.put("add device", "To add a device, click the 'ADD DEVICE' button on your dashboard.");
        rules.put("delete device", "To delete a device, click the trash icon on the device card.");
        rules.put("monitor usage", "You can monitor usage by clicking the chart icon on any device card.");
        rules.put("contact support", "You can request a human agent by typing 'admin' or clicking the request button.");
        rules.put("billing", "For billing inquiries, please contact sales@nexus.com.");
        rules.put("pricing", "Our basic plan is free for up to 5 devices.");
        rules.put("hours", "Our support team is available 24/7.");
    }

    public static String getResponse(String message) {
        String lowerMsg = message.toLowerCase();
        for (Map.Entry<String, String> entry : rules.entrySet()) {
            if (lowerMsg.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
}

