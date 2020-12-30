// servo values 1100 to 1850


void centerServo(){
  ledcWrite(1,1500);
delay(10);
}

void maxLeftServo(){
  ledcWrite(1,1850);
delay(10);  
}

void maxRightServo(){
  ledcWrite(1,1100);
delay(10);
}

void rightServo(int ang){
if(ang>=1100){
   ledcWrite(1,ang);
delay(10);
}
}
