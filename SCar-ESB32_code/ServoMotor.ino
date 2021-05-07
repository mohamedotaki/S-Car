// servo values 1100 to 1900

void centerServo(){
  ledcWrite(servoChan,1500);
delay(10);
}

void maxLeftServo(){
  ledcWrite(servoChan,1900);
delay(10);  
}

void maxRightServo(){
  ledcWrite(servoChan,1100);
delay(10);
}

void writeServo(int steeringValue){
if(steeringValue>=1100 && steeringValue <= 1900){
   ledcWrite(servoChan,steeringValue);
}
delay(10);

}
