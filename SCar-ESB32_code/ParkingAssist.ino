int  steering=1500 ,currentPos=0, emptySpace=0;


void parkingAssist(){
  
    searchForEmptySpace();
    //fixPosition();
    park();
}
void searchForEmptySpace(){
  
  int safeDis=0;
  boolean readyToPark = false;
  centerServo();
  r=ultrasonicValue(rightSensorB);
  l=ultrasonicValue(rightSensorF);
  f=ultrasonicValue(frontSensor);
  b=ultrasonicValue(backSensor);
    Serial.print(r);
    Serial.print("-r");
    Serial.print(l);
    Serial.print("-rf");
    Serial.print(f);
    Serial.print("-f");
    Serial.print(b);
    Serial.println("-b");
    while(r<carWidth || l <carWidth || r>carWidth || l > carWidth && f > 20){
        if(f<15){
        break;
        }
       Serial.println("11111 while");
      r=ultrasonicValue(rightSensorB);
      l=ultrasonicValue(rightSensorF);
      f=ultrasonicValue(frontSensor);
      b=ultrasonicValue(backSensor);
      int back=b , front = f;
      safeDis=0;
      motorF(forwardSpeed);
    while(r>carWidth && l > carWidth){
      Serial.println("firstwhile");
      if(r>carWidth && l > carWidth) {
        safeDis = 24; // 24cm as the sensors can detect 24cm of the car length
      }else safeDis=0;
        b=ultrasonicValue(backSensor);
        f=ultrasonicValue(frontSensor);

        if(f<15){
          Serial.println("f<10");
          break;
        }
        if(front-f >45 || b-back>45){
            safeDis = safeDis +36;              // park without fix the car pos
            readyToPark =true;
            Serial.println("ready to park");
            break;
        }
         b=ultrasonicValue(backSensor);
         f=ultrasonicValue(frontSensor);
        r=ultrasonicValue(rightSensorB);
        l=ultrasonicValue(rightSensorF);
        if(r<carWidth || l < carWidth && front-f >26 || b-back>26){
          Serial.println("not ready prep");
          b=ultrasonicValue(backSensor);
          f=ultrasonicValue(frontSensor);
          front =f;   back = b;
          while(front -f <35 || b - back <35){
            Serial.println("bbbbbbbbb");
          b=ultrasonicValue(backSensor);
          f=ultrasonicValue(frontSensor);
          readyToPark =true;
          }
          
        }
 
            
  if(readyToPark)break;

      }
  if(readyToPark)break;
 }
 motorS();     
    
}
//void fixPosition(){
//     r=ultrasonicValue(rightSensorB);
//     l=ultrasonicValue(rightSensorF);
//     currentPos=l;
//   while((l-r)>0){
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
     l=ultrasonicValue(rightSensorF);
      currentPos = l;
while(true){
  f=ultrasonicValue(frontSensor);
  l=ultrasonicValue(rightSensorF);
  b=ultrasonicValue(backSensor);
   steering = map(l, currentPos, currentPos+10, 1100, 1900);
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
           l=ultrasonicValue(rightSensorF);
         while(l-r>1 && f>10){
          motorF(forwardSpeed);
           r=ultrasonicValue(rightSensorB);
           l=ultrasonicValue(rightSensorF);
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





  
