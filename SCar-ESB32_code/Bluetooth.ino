void bluetooth() {
  if (SerialBT.available()) {
    switch (SerialBT.read()) {
      // user is in control car activity start sending sensors value to the app
      case 'C': {
          //set all the bits to high
          Serial.println("in control car activity start sending sensors vlaue");
          vTaskResume( ultrasonicTaskHandle );
          xEventGroupSetBits(xEventGroup, allSensorBits | sendSensorsValueBit);
        }
      // turn left received from user
      case 'L':
        Serial.println("turning left");
        BTSteering = BTSteering + 100;
        if (BTSteering >= 1900) {
          BTSteering = 1900;
        }
        writeServo(BTSteering);
        break;

      //Turn right received from user
      case 'R':
        Serial.println("turning Right");
        BTSteering = BTSteering - 100;
        if (BTSteering <= 1100) {
          BTSteering = 1100;
        }
        writeServo(BTSteering);
        break;

      //go forward or increase the speed
      case 'F':
        Serial.println("forward");
        motorF(forwardSpeed);
        break;

      //Reverse or increase the speed
      case 'B':
        Serial.println("reverse");
        motorS();
        motorR(reversingSpeed);
        break;

      // Stop motor
      case 'S':
        Serial.println("stop");
        xEventGroupClearBits(xEventGroup, allFeatureBits);
        motorS();
        break;

      // Activate parking assist
      case 'P':
        xEventGroupSetBits(xEventGroup, parkingAssistBit );
        break;

      case 'j':
        Serial.println("Wifi");
        break;

      // Stpe sending sensors value to to app and suspend the ultrasonic task
      case 'E':
        xEventGroupClearBits(xEventGroup, allSensorBits | sendSensorsValueBit);
        vTaskSuspend(ultrasonicTaskHandle);
        break;

      case 'W':
        // scan the available wifi and send to the app
        scanNetworks();
        break;

      case 'O':
        // get user input from the app
        getWifiDetailsFromApp();
        break;
      case 1:
        // get user input from the app
        Serial.println(readBTInput());
        break;

      case 'A':
        xEventGroupSetBits(xEventGroup, autonomousDrivingBit );
        break;

    } // end of BT read
  }
} // end of BT function

void sendSensorsValue() {
  // send sensors value to the app using bluetooth
  uint8_t a[9] = {highByte(f), lowByte(f), highByte(b), lowByte(b), highByte(rfs), lowByte(rfs), highByte(r), lowByte(r), 'x'};
  SerialBT.write(a, 9);
}

void callback(esp_spp_cb_event_t event, esp_spp_cb_param_t *param) {
  if (event == ESP_SPP_SRV_OPEN_EVT) {
    Serial.println("Client Connected");
    // vTaskResume( checkUserHandle);

  }
}

String readBTInput() {
  BTDataInput = "";
  byte b;
  while (true) {
    b = SerialBT.read();
    if (b == 4) {
      break;
    }
    BTDataInput += (char) b;
  }
  return BTDataInput;
}
