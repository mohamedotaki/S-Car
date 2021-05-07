int  steering = 1500 , currentPos = 0, emptySpace = 0;


void parkingAssist() {
  vTaskResume(ultrasonicTaskHandle);
  searchForEmptySpace();
  //park();
  vTaskSuspend(ultrasonicTaskHandle);
}

void searchForEmptySpace() {
  int safeDis = 0 , distanceTravelled = 0;
  timeout = 10000;
  boolean readyToPark = false;
  centerServo();
  xEventGroupSetBits(xEventGroup, frontSensorBit | rightSensorBit );
  delay(50);
  distanceTravelled = f;
  motorF(forwardSpeed);
  myTime = millis();
  while (true) {
    // break from the while loop after 10 seconds if the car can't find empty spice 
    unsigned long currentMillis = millis();
    if(currentMillis - myTime > timeout) {
      // nofity user no empty space was found, please try to start the parking assist beside an empty space
      break; 
    }
    fixPosition();
    if (rfs > carWidth) {
      safeDis = + 24; // 24cm as the sensors can detect 24cm of the car length
      if ((distanceTravelled - f) >=  60) {
        readyToPark = true;
        Serial.println("ready to park");
      }
    }
    else {
      distanceTravelled = f;
      safeDis = 0;
    }
    if (readyToPark)break;

  }
  motorS();
  xEventGroupClearBits(xEventGroup, allSensorBits);
}

void park() {
  delay(1000);
  motorS();
  xEventGroupSetBits(xEventGroup, allSensorBits);
  int currentPos = 0, steering = 0;
  boolean parked = false;
  currentPos = rfs;
  while (true) {
    steering = map(rfs, currentPos, currentPos + 10, 1100, 1900);
    if (steering > 1600) {
      steering = 1900;
    }
    if (steering < 1350) {
      steering = 1100;
    }
    writeServo(steering);
    motorR(reversingSpeed);
    if (b < 18) {
      motorS();
      delay(500);
      maxRightServo();
      while (rfs - r > 1 && f > 10) {
        motorF(forwardSpeed);
      }
      while (f > 15 && b < 15) {
        centerServo();
      }
      motorS();
      parked = true;
      centerServo();
    }
    if (parked == true)break;
  }
}

void fixPosition(){
int newSensorValue=0;
// if statement to add 30cm to current sensor value to fix the steering
if(rfs <10){
   newSensorValue =rfs + 30;
}else{
  newSensorValue = rfs;
}
 steering = map(newSensorValue, rightSideDistance-6, rightSideDistance + 10, 1800, 1100);
 writeServo(steering);

}
