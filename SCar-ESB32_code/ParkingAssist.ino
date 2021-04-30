int  steering=1500 ,currentPos=0, emptySpace=0;


void parkingAssist(){
  vTaskResume(ultrasonicTaskHandle);
    searchForEmptySpace();
    //fixPosition();
    park();
  vTaskSuspend(ultrasonicTaskHandle);
}

void searchForEmptySpace(){
  int safeDis=0;
  boolean readyToPark = false;
  centerServo();
  xEventGroupSetBits(xEventGroup, frontSensorBit | rightSensorBit | leftSensorBit);
      safeDis=0;
      motorF(forwardSpeed);
      int back=b , front = f;
      while(f >20){
    while(r>carWidth && rfs > carWidth && f >20){
      Serial.println("Clauclating empty space");
      if(r>carWidth && rfs > carWidth) {
        safeDis =+ 24; // 24cm as the sensors can detect 24cm of the car length
        if(safeDis > 50){
        readyToPark =true;
        Serial.println("ready to park");
        }
      } 
      else {
        safeDis=0;
        }
        delay(100);         
  if(readyToPark)break;

      }
       if(readyToPark)break;
}
 
 
 motorS();     
    
}
//void fixPosition(){
//     r=ultrasonicValue(rightSensorB);
//     rfs=ultrasonicValue(rightSensorF);
//     currentPos=rfs;
//   while((rfs-r)>0){
//       Serial.print(r);
//       Serial.print("---");
//       Serial.println(l); 
//     b=ultrasonicValue(backSensor);
//    while(b>10 && (l-r)>0){
//    r=ultrasonicValue(rightSensorB);
//    l=ultrasonicValue(rightSensorF);
//    b=ultrasonicValue(backSensor); 
//    maxLeftServo();
//    motorR(map(l,currentPos, currentPos - (l-r)-3, reversingSpeed, 1360));
//    }
//    Serial.println("Done");
//        r=ultrasonicValue(rightSensorB);
//    l=ultrasonicValue(rightSensorF);
//    Serial.println(l-r);
//    motorS();
//    centerServo();
//   }
////    while(r>6){
////      motorF(forwardSpeed);
////   r=ultrasonicValue(rightSensorB);
////   steering = map(r, (80/currentPos)+1, currentPos, 1850, 1100);
////        Serial.print(r);
////             Serial.print("--");
////   Serial.println(steering);
////   if(r<=10){
////    steering = steering+50;
////   }
////rightServo(steering);
////
////
////    }
////    centerServo();
////    motorS();
////    currentPos=0;
////}
//}

void park(){
  delay(1000);
  motorS(); 
 int currentPos=0,steering=0;
 boolean parked=false;
      currentPos = rfs;
while(true){
   steering = map(rfs, currentPos, currentPos+10, 1100, 1900);
   if(steering>1600){
    steering = 1900;
   }
   if(steering<1350){
    steering = 1100;
   }
   rightServo(steering);
    motorR(reversingSpeed); 
    b=ultrasonicValue(backSensor);
    if(b<18){
      motorS();
        delay(500);
         maxRightServo();
         f=ultrasonicValue(frontSensor);
           r=ultrasonicValue(rightSensorB);
           rfs=ultrasonicValue(rightSensorF);
         while(rfs-r>1 && f>10){
          motorF(forwardSpeed);
           r=ultrasonicValue(rightSensorB);
           rfs=ultrasonicValue(rightSensorF);
           f=ultrasonicValue(frontSensor);
         }
          b=ultrasonicValue(backSensor);
         while(f>15 && b <15){
          centerServo();
          b=ultrasonicValue(backSensor);
          f=ultrasonicValue(frontSensor);
         }
         motorS();
          parked = true;
         centerServo();
       }
         if (parked== true)break;
      } 
        

}





  
