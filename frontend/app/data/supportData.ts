export interface SupportTopic {
  id: string;
  label: string;
  response: string;
}

export const supportTopics: SupportTopic[] = [
  {
    id: "connection",
    label: "Device not connecting",
    response: "Please ensure your device is powered on and within range of the Wi-Fi network. Try restarting the device by holding the power button for 5 seconds."
  },
  {
    id: "readings",
    label: "Strange sensor readings",
    response: "Unusual readings often indicate the sensor needs calibration. Please check the device maintenance guide or try a soft reset via the device settings."
  },
  {
    id: "add-device",
    label: "How to add a new device",
    response: "Click the 'ADD DEVICE' button in the top right of your dashboard. You can search for available devices and add them to your account instantly."
  },
  {
    id: "billing",
    label: "Billing & Subscription",
    response: "NEXUS currently offers free monitoring for up to 5 devices. For enterprise plans, please contact sales@nexus.com."
  },
  {
    id: "other",
    label: "Other issues",
    response: "For complex technical issues, please email our support team at support@nexus.com with your device ID and a description of the problem."
  }
];

