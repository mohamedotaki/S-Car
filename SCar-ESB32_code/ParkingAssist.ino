int steering =1500, currentPos=0, emptySpace=0;


void parkingAssist(){
  
    searchForEmptySpace();
    //fixPosition();
   // park();
}
void searchForEmptySpace( ){
  
  int safeDis=0;
    r=ultrasonicValue(rightSensor);
    if(r>carWidth){
      b=ultrasonicValue(backSensor);
      int back=0;
      back =b;
      motorR(reversingSpeed);
      while(r>carWidth && (back-25)<=b){
        motorR(map(b,back-25,back,1400,reversingSpeed));
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
      f=ultrasonicValue(frontSensor);  
      safeDis = front-f +15;  
      posBesideObject = posBesideObject - f; 
      front=f;  
      while(f > (front - 20-posBesideObject)){
              Serial.print("1:");
          Serial.println(front);
      //  motorF(map(f,(front-20-posBesideObject),front,1580,forwardSpeed));
        f=ultrasonicValue(frontSensor); 
     break;
      }
      motorS();
      if(safeDis>50 && r >carWidth){
        
      }
//             if(r<10){
//              beforParkingPos++;
//              if(beforParkingPos >=4){
//                  Serial.print("beside car");
//             Serial.println(beforParkingPos);
//              break;
//              }
//             }
          
         // Serial.print("after");
         //  Serial.println(safeDis);
          
   
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
  r=ultrasonicValue(rightSensor);
 int currentPos=r,steering=0;
 motorR(reversingSpeed);
while(true){
  f=ultrasonicValue(frontSensor);
  r=ultrasonicValue(rightSensor);
  b=ultrasonicValue(backSensor);
    Serial.print(f);
   steering = map(r, currentPos, currentPos+20, 1100, 1900);
  if(steering<1500){
    steering =1100;
  }else{
    steering=1900;
  }
      
  if(b<9){
    motorS();
    maxRightServo();
    motorF(forwardSpeed);
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





  
