void parkingAssist(int side){
//            Serial.println("on");
//int emptySpace=0;
//switch (side){
//  
//  //right side
//  case 1:
//  Serial.print("ssssssssss");
//      while(r>8 && r<15){
//        motorF(1580);
//        rightServo(1300); 
//        delay(15);
//      }
//      centerServo();
//      while(emptySpace<10){
//        if(r >14){
//          emptySpace = emptySpace +1;
//          Serial.println(emptySpace);
//        }else emptySpace =0;
//      }
//      motorS();
//      maxRightServo();
//  break;
//  //left side
//    case 2:
//    Serial.println("left");
//    break;
//}
}
void searchForEmptySpace(int r ){
  int safeDis=0;
    if(r>33){
      motorR(1360);
      b=ultrasonicValue(backSensor);
      int back=0;
      if(back ==0) back =b;
      while(r>33 && (back-15)<b ){
         Serial.println("reversing");
         b=ultrasonicValue(backSensor);
         r=ultrasonicValue(rightSensor);
         if(back !=b){
        safeDis =safeDis+1;
         }
        Serial.println(safeDis);
      }
      motorS();
      delay(50);
      motorF(1600);
      f=ultrasonicValue(frontSensor);
      int front=r;
          while(r>30 && (front-15)<f && safeDis <=8){
             Serial.println("forword");
             f=ultrasonicValue(frontSensor);
             r=ultrasonicValue(rightSensor);
             Serial.print(f);
             Serial.println("forword");
             if(front != f){
            safeDis =safeDis+1;
            Serial.println(safeDis);
             }
          }
          motorS();
           Serial.println(safeDis);
   
      }
      motorS();
      
      
    
}
void park(){

  r=ultrasonicValue(rightSensor);
 int currentPos=r,steering=0;
 motorR(1200);
while(true){
  f=ultrasonicValue(frontSensor);
  r=ultrasonicValue(rightSensor);
  b=ultrasonicValue(backSensor);
    Serial.print(f);
   steering = map(r, currentPos, currentPos+20, 1150, 1900);
  if(steering<1500){
    steering =1100;
  }else{
    steering=1900;
  }
      
  if(b<9){
    motorS();
    maxRightServo();
    motorF(1580);
    while(f>6){
      f=ultrasonicValue(frontSensor);
    }
    motorS();
    centerServo();
    break;
}
    Serial.print(" ----- ");
    Serial.print(b);
    Serial.print(" ----- ");
        Serial.print(r);
    Serial.print(" ----- ");
  Serial.println(steering);
  rightServo(steering);
}

//    if(r<17){
//      
//      motorS();
//      delay(500);
//     steering =1100;
//     rightServo(steering);
//     motorF(1600);
//     while(f>6){
//        f=ultrasonicValue(frontSensor);
//     }
//      motorS();
//    }
  }



  
