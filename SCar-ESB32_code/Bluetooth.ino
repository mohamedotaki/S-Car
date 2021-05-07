void bluetooth() {
  if (SerialBT.available()) {
    switch (SerialBT.read()) {
      // user is in control car activity start sending sensors value to the app
      case 'C': {
          //set all the bits to high
          vTaskResume( ultrasonicTaskHandle );
          xEventGroupSetBits(xEventGroup, allSensorBits | sendSensorsValueBit);
        }
      // turn left received from user
      case 'L':
        BTSteering = BTSteering + 100;
        if (BTSteering >= 1900) {
          BTSteering = 1900;
        }
        writeServo(BTSteering);
        break;

      //Turn right received from user
      case 'R':
        BTSteering = BTSteering - 100;
        if (BTSteering <= 1100) {
          BTSteering = 1100;
        }
        writeServo(BTSteering);
        break;

      //go forward or increase the speed
      case 'F':
        Serial.println("forward");
        break;

      //Reverse or increase the speed
      case 'B':
        Serial.println("reverse");
        break;

      // Stop motor
      case 'S':
        Serial.println("stop");
        //vTaskResume(wifiTaskHandle);
        break;

      // Activate parking assist
      case 'P':
        xEventGroupClearBits(xEventGroup, allSensorBits);
        parkingAssist();
        break;

      // go forward or increase the speed
      case 'j':
        Serial.println("Wifi");
        //parkingAssist();
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
    } // end of BT read
  }
} // end of BT function

void sendSensorsValue() {
  // send sensors value to the app using bluetooth
  uint8_t a[9] = {highByte(f), lowByte(f), highByte(b), lowByte(b), highByte(rfs), lowByte(rfs), highByte(r), lowByte(r), 'x'};
  SerialBT.write(a, 9);
}
