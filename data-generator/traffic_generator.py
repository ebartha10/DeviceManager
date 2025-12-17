import time
import json
import random
import logging
import requests
import os
import urllib3

# Suppress insecure request warnings
urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

# Configuration
API_BASE_URL = os.getenv("API_BASE_URL", "http://localhost") # Using HTTP for Swarm/Traefik
MONITORING_BASE_URL = f"{API_BASE_URL}/monitoring"

# Logger setup
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

def get_auth_token():
    # Helper to authenticate against the Auth Microservice (via Traefik)
    login_url = f"{API_BASE_URL}/api/auth/login"
    
    auth_data = {
        "email": "generator@test.com",
        "password": "password"
    }

    try:
        # Try to register first just in case
        register_url = f"{API_BASE_URL}/api/auth/register"
        reg_data = {
            "email": "generator@test.com",
            "password": "password",
            "fullName": "Traffic Generator",
            "role": "ADMIN"
        }
        try:
            requests.post(register_url, json=reg_data, verify=False)
        except: 
            pass

        # Login
        logger.info(f"Logging in to {login_url}...")
        response = requests.post(login_url, json=auth_data, verify=False)
        response.raise_for_status()
        token = response.json().get("token")
        logger.info("Login successful")
        return token
    except Exception as e:
        logger.error(f"Login failed: {e}")
        return None

def get_devices(token):
    # Fetch devices to query data for
    url = f"{API_BASE_URL}/device/get-all"
    headers = {"Authorization": f"Bearer {token}"}
    
    try:
        response = requests.get(url, headers=headers, verify=False)
        response.raise_for_status()
        devices = response.json()
        logger.info(f"Found {len(devices)} devices")
        return devices
    except Exception as e:
        logger.error(f"Failed to fetch devices: {e}")
        return []

def main():
    logger.info("Starting Traffic Generator for Swarm Load Balancer...")
    
    token = get_auth_token()
    if not token:
        return

    devices = get_devices(token)
    if not devices:
        logger.warning("No devices found. Ensure some devices exist (run the original data_generator.py first if needed).")
        # Proceed logic might be limited without devices, but we can try generic queries if any exist
    
    # Traffic Loop
    try:
        request_count = 0
        while request_count < 20:  # Make 20 requests for testing
            request_count += 1
            
            # Simple GET request to test load balancing
            # Using the energy consumption endpoint with valid device if available
            if devices:
                device_id = devices[request_count % len(devices)]['id']
            else:
                # Use a dummy UUID just to test connectivity
                device_id = "00000000-0000-0000-0000-000000000000"
            
            # Construct URL to hit the LOAD BALANCER
            # Path: /monitoring/energy-consumption/daily
            url = f"{MONITORING_BASE_URL}/energy-consumption/daily"
            params = {
                "deviceId": device_id,
                "date": "2024-12-14"
            }
            headers = {"Authorization": f"Bearer {token}"}

            try:
                # This request goes: Client -> Traefik -> Load Balancer -> Monitoring Replica -> DB
                response = requests.get(url, params=params, headers=headers, verify=False)
                
                status = response.status_code
                logger.info(f"Request #{request_count} to {url} - Status: {status}")
                
            except Exception as e:
                logger.error(f"Request #{request_count} failed: {e}")

            # Sleep briefly
            time.sleep(0.5) 

    except KeyboardInterrupt:
        logger.info("Stopping generator...")

if __name__ == "__main__":
    main()
