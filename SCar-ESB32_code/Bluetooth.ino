void bluetooth(){
 if (SerialBT.available()) {
  switch(SerialBT.read()){
    case 'C':{
    boolean inControlCar = true;
    while(inControlCar){
      uint8_t a[8] ={ highByte(ultrasonicValue(frontSensor)),lowByte(ultrasonicValue(frontSensor)),
                      highByte(ultrasonicValue(backSensor)),lowByte(ultrasonicValue(backSensor)),
                      highByte(ultrasonicValue(rightSensorF)),lowByte(ultrasonicValue(rightSensorF)),
                      highByte(ultrasonicValue(rightSensorF)),lowByte(ultrasonicValue(rightSensorF))};

      SerialBT.write(a,8);

     if (SerialBT.available()) {
       switch(SerialBT.read()){
        case 'L':         // turn left 
        BTSteering = BTSteering+100;
         if(BTSteering >=1900){
          BTSteering=1900;
           }
         rightServo(BTSteering);
         Serial.println(BTSteering);
         break;
      case 'R':         // Turn Right
      BTSteering = BTSteering-100;
      if(BTSteering <=1100){
        BTSteering= 1100;
      }
      rightServo(BTSteering);
      Serial.println(BTSteering);
      break;
      case 'F':         // go forward or increase the speed
      Serial.println("forward");
      break;
      case 'B':         //  reverse or increase the speed
      Serial.println("reverse");
      break;
      case 'S':         // Stop motor
      Serial.println("stop");
      //vTaskResume(wifiTaskHandle);
      break;
      case 'P':         // go forward or increase the speed
      Serial.println("parking assist");
       //parkingAssist();
      break;
      case 'W':         // go forward or increase the speed
      Serial.println("Wifi");
       //parkingAssist();
      break;
      case 'E':         // go forward or increase the speed
      inControlCar = false;
      break;
    }
     }
    }
    }break;
    
    case 'W':{
    scanNetworks();
    }break;
    
  }
 }


   
  
}
