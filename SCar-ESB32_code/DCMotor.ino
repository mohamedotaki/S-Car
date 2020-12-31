  void motorF(int speedRequiered){
  ledcWrite(chan,speedRequiered);
  delay(100);
    
}

void motorR(int speedRequiered){
    ledcWrite(chan,1420);
    ledcWrite(chan,1495);
          delay(20);
  ledcWrite(chan,speedRequiered);
  delay(100);
}



void motorS(){
    ledcWrite(chan,1495);
      delay(100);
}

   void motorEmergncyStop(){
    ledcWrite(chan,1300);
    delay(10);
    ledcWrite(chan,1495);
      delay(100);
}
