import time
import json
import random
import uuid
import logging
import datetime
import requests
import pika
import os

# Configuration
API_BASE_URL = os.getenv("API_BASE_URL", "https://localhost")
RABBITMQ_HOST = os.getenv("RABBITMQ_HOST", "localhost")
RABBITMQ_PORT = int(os.getenv("RABBITMQ_PORT", 5672))
RABBITMQ_USER = os.getenv("RABBITMQ_USER", "kalo")
RABBITMQ_PASS = os.getenv("RABBITMQ_PASS", "kalo")

# RabbitMQ Config
EXCHANGE_NAME = "device.measurements.exchange"
ROUTING_KEY = "device.measurement"

# Simulation Config
REAL_TIME_INTERVAL = 1       # 1 seconds real time delay
SIMULATED_TIME_STEP = 600    # 10 minutes simulated time step

# Logger setup
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

class DeviceSimulator:
    def __init__(self, device_id, device_name):
        self.device_id = device_id
        self.device_name = device_name
        # Random base load between 0.1 and 0.5 kWh per 10-minute interval
        self.base_load = random.uniform(40, 50)
        logger.info(f"Initialized simulator for device {device_name} ({device_id}) with base load {self.base_load:.4f}")

    def generate_measurement(self, timestamp):
        hour = timestamp.hour
        
        # Time of day factor
        if 0 <= hour < 6:
            time_factor = 0.5  # Night
        elif 6 <= hour < 10:
            time_factor = 1.0  # Morning
        elif 10 <= hour < 18:
            time_factor = 1.2  # Day
        elif 18 <= hour < 22:
            time_factor = 1.5  # Evening
        else:
            time_factor = 0.8  # Late night
            
        # Random fluctuation +/- 10%
        fluctuation = random.uniform(0.9, 1.1)
        
        measurement = self.base_load * time_factor * fluctuation
        
        return {
            "timestamp": timestamp.isoformat(),
            "deviceId": self.device_id,
            "measurementValue": measurement
        }



def get_devices_from_file():
    file_path = "./device_ids.txt"
    devices = []
    
    if not os.path.exists(file_path):
        logger.error(f"Device ID file not found: {file_path}")
        return []
        
    try:
        with open(file_path, 'r') as f:
            lines = f.readlines()
            for line in lines:
                device_id = line.strip()
                if device_id:
                    # Validate UUID format optionally, or just trust the file
                    devices.append({
                        "id": device_id,
                        "name": f"Device-{device_id[:8]}" # Placeholder name
                    })
        return devices
    except Exception as e:
        logger.error(f"Failed to read device IDs from file: {e}")
        return []

def setup_rabbitmq():
    credentials = pika.PlainCredentials(RABBITMQ_USER, RABBITMQ_PASS)
    parameters = pika.ConnectionParameters(RABBITMQ_HOST, RABBITMQ_PORT, '/', credentials)
    return pika.BlockingConnection(parameters)

def main():
    logger.info("Starting Data Generator...")
    
    # 1. Get Devices from file
    devices_data = get_devices_from_file()
    
    if not devices_data:
        logger.warning("No devices found in file. Exiting.")
        return

    logger.info(f"Loaded {len(devices_data)} devices from file.")
    
    simulators = [DeviceSimulator(d['id'], d['name']) for d in devices_data]

    # 3. Connect to RabbitMQ
    try:
        connection = setup_rabbitmq()
        channel = connection.channel()
        # Ensure exchange exists (optional, but good practice)
        channel.exchange_declare(exchange=EXCHANGE_NAME, exchange_type='topic', durable=True)
        logger.info("Connected to RabbitMQ")
    except Exception as e:
        logger.error(f"Failed to connect to RabbitMQ: {e}")
        return

    # Start simulation time from now
    current_simulated_time = datetime.datetime.now() - datetime.timedelta(days=1)

    try:
        while True:
            logger.info(f"Generating measurements for time: {current_simulated_time}...")
            for sim in simulators:
                measurement = sim.generate_measurement(current_simulated_time)
                
                # Publish
                try:
                    channel.basic_publish(
                        exchange=EXCHANGE_NAME,
                        routing_key=ROUTING_KEY,
                        body=json.dumps(measurement),
                        properties=pika.BasicProperties(
                            content_type='application/json',
                            delivery_mode=2, # persistent
                        )
                    )
                    logger.info(f"Sent measurement for {sim.device_name}: {measurement['measurementValue']:.4f}")
                except Exception as e:
                    logger.error(f"Failed to send message: {e}")
                    # Reconnect logic could go here
            
            # Advance simulated time
            current_simulated_time += datetime.timedelta(seconds=SIMULATED_TIME_STEP)
            
            logger.info(f"Sleeping for {REAL_TIME_INTERVAL} seconds...")
            time.sleep(REAL_TIME_INTERVAL)

    except KeyboardInterrupt:
        logger.info("Stopping generator...")
    except Exception as e:
        logger.error(f"Unexpected error: {e}")
    finally:
        if connection and not connection.is_closed:
            connection.close()

if __name__ == "__main__":
    main()
