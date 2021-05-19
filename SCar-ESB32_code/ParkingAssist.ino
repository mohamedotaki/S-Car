int  steering = 1500 , currentPos = 0, emptySpace = 0;


void parkingAssist() {
  // vTaskResume(ultrasonicTaskHandle);
  searchForEmptySpace();
  //park();
  //vTaskSuspend(ultrasonicTaskHandle);
}

void searchForEmptySpace() {
  int safeDis = 0 , distanceTravelled = 0;
  timeout = 10000;
  boolean readyToPark = false;
  centerServo();
  //xEventGroupSetBits(xEventGroup, frontSensorBit | rightSensorBit );
  delay(500);
  distanceTravelled = f;
  motorF(forwardSpeed);
  myTime = millis();
  while (true) {
    // break from the while loop after 10 seconds if the car can't find empty spice
    unsigned long currentMillis = millis();
    if (currentMillis - myTime > timeout) {
      // nofity user no empty space was found, please try to start the parking assist beside an empty space
      break;
    }
    fixPosition();
    if (rfs < carWidth) {
      distanceTravelled = f;
      safeDis = 0;
      Serial.println("no space");
    }

    if ((distanceTravelled - f) >=  60) {
      delay(500);

      readyToPark = true;
      Serial.println("ready to park");
    }
    if (readyToPark)break;

  }
  motorS();
  //xEventGroupClearBits(xEventGroup, allSensorBits);
}

void park() {
  delay(1000);
  motorS();
  //xEventGroupSetBits(xEventGroup, allSensorBits);
  int currentPos = 0, steering = 0;
  boolean parked = false;
  currentPos = rfs;
  while (true) {
    maxRightServo();
    motorR(reversingSpeed);
    if (b <= 30) {
      motorS();
      delay(500);
      maxLeftServo();
      motorR(reversingSpeed);
      while (b > 15) {
      }
      motorS();
      maxRightServo();
      motorF(forwardSpeed);
      while (rfs > 20 && f > 20) {
      }
      motorS();
      parked = true;
      centerServo();
    }
    if (parked == true)break;
  }
}

void fixPosition() {
  int newSensorValue = 0;
  // if statement to add 30cm to current sensor value to fix the steering
  if (rfs < 10) {
    newSensorValue = rfs + 30;
  } else {
    newSensorValue = rfs;
  }
  steering = map(newSensorValue, rightSideDistance - 6, rightSideDistance + 10, 1800, 1100);
  writeServo(steering);

}
