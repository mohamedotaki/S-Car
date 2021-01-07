int  steering=1500 ,currentPos=0, emptySpace=0;


void parkingAssist(){
  
   // searchForEmptySpace();
    //fixPosition();
    park();
}
void searchForEmptySpace( ){
  
  int safeDis=0;
    r=ultrasonicValue(rightSensor);
    if(r>carWidth){
      b=ultrasonicValue(backSensor);
      int back=0;
      back =b;
      while(r>carWidth && (back-25)<=b){
        motorR(map(b,back-25,back,1420,reversingSpeed));
         b=ultrasonicValue(backSensor);
         r=ultrasonicValue(rightSensor);
      }
      motorS();
      safeDis = back - b;
      delay(500);
      f=ultrasonicValue(frontSensor);
      r=ultrasonicValue(rightSensor);
      int front=f, right =r, posBesideObject=0;
          while(r>carWidth && (front-50)<f ){
             motorF(map(f,front-50,front,1580,forwardSpeed));
             f=ultrasonicValue(frontSensor);
             r=ultrasonicValue(rightSensor);
             if(r<carWidth-10) posBesideObject=f;
          } 
          motorS();
          delay(500);
      f=ultrasonicValue(frontSensor);  
      safeDis = front-f +15;  
      posBesideObject = posBesideObject - f; 
       Serial.print("pos:");
          Serial.println(posBesideObject);
      front=f;  
      while(f > (front - 30+posBesideObject)){                   
        motorF(map(f,(front-30+posBesideObject),front,1580,forwardSpeed));
        f=ultrasonicValue(frontSensor); 
      }
      motorS();
      }
      motorS();
      
      
    
}
void fixPosition(){
     r=ultrasonicValue(rightSensor);
   if(r<20){
       Serial.print(r);
    currentPos=r;
    while(r>6){
      motorF(forwardSpeed);
   r=ultrasonicValue(rightSensor);
   steering = map(r, (80/currentPos)+1, currentPos, 1850, 1100);
        Serial.print(r);
             Serial.print("--");
   Serial.println(steering);
   if(r<=10){
    steering = steering+50;
   }
rightServo(steering);


    }
    centerServo();
    motorS();
    currentPos=0;
}
}

void park(){
  motorS();
 int currentPos=0,steering=0;
while(true){
  f=ultrasonicValue(frontSensor);
  r=ultrasonicValue(rightSensor);
  b=ultrasonicValue(backSensor);
  currentPos = r;
      Serial.print("steering: ");
      Serial.print (steering);
         Serial.print("   RightSen:");
      Serial.println(r);
   steering = map(r, currentPos, currentPos+30, 1100, 1900);
   rightServo(steering);
    //motorR(reversingSpeed);   
//  if(b<9){
//    motorS();
//    maxRightServo();
//    motorF(forwardSpeed);
//    while(f>6){
//      f=ultrasonicValue(frontSensor);
//    }
//    motorS();
//    centerServo();
//    break;
//}
  if(r ==1)break;
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





  
