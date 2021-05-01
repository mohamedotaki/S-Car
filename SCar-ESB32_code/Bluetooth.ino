void bluetooth() {
  if (SerialBT.available()) {
    switch (SerialBT.read()) {
      case 'C': {
          boolean inControlCar = true;
          //set all the bits to high
          vTaskResume( ultrasonicTaskHandle );
          xEventGroupSetBits(xEventGroup, allSensorBits);
          while (inControlCar) {
            // send sensors value to the app using bluetooth
            uint8_t a[9] = {highByte(f), lowByte(f), highByte(b), lowByte(b), highByte(rfs), lowByte(rfs), highByte(r), lowByte(r), 'x'};
            SerialBT.write(a, 9);

            //Listen for button press
            if (SerialBT.available()) {
              switch (SerialBT.read()) {

                // turn left
                case 'L':
                  BTSteering = BTSteering + 100;
                  if (BTSteering >= 1900) {
                    BTSteering = 1900;
                  }
                  rightServo(BTSteering);
                  Serial.println(BTSteering);
                  break;

                //Turn Right
                case 'R':
                  BTSteering = BTSteering - 100;
                  if (BTSteering <= 1100) {
                    BTSteering = 1100;
                  }
                  rightServo(BTSteering);
                  Serial.println(BTSteering);
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
                  Serial.println("parking assist");
                  xEventGroupClearBits(xEventGroup, allSensorBits);
                  parkingAssist();
                  break;

                // go forward or increase the speed
                case 'W':
                  Serial.println("Wifi");
                  //parkingAssist();
                  break;

                // go forward or increase the speed
                case 'E':
                  xEventGroupClearBits(xEventGroup, allSensorBits);
                  vTaskSuspend(ultrasonicTaskHandle);
                  inControlCar = false;
                  Serial.println("left in control car");
                  break;
              }
            }
          }
          break;
        }

      case 'W': {
          scanNetworks();
          break;
        }
      case 'O': {
          wifiName = "";
          wifiPass = "";
          byte b;
          while (true) {
            b = SerialBT.read();
            if (b != 3) {
              wifiName += (char) b;
            }
            else {
              while (true) {
                b = SerialBT.read();
                if (b != 4) {
                  wifiPass += (char) b;
                }
                else {
                  break;
                }
              }
              break;
            }
          }
          ConnectToWiFi(wifiName.c_str(), wifiPass.c_str());
          if (WiFi.status() == WL_CONNECTED) {
            writeToEEPROM(wifiName.c_str(), 0);
            writeToEEPROM(wifiPass.c_str(), 40);
            Serial.println("WIFI was Saved");
          }
          break;
        }

    }
  }




}
