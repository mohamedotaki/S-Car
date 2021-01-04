  void motorF(int speedRequiered){
  ledcWrite(chan,speedRequiered);
  delay(10);
    
}

void motorR(int speedRequiered){
  ledcWrite(chan,speedRequiered);
  delay(10);
}



void motorS(){
    ledcWrite(chan,1489);
      delay(100);
}

   void motorEmergncyStop(){
    ledcWrite(chan,1300);
    delay(10);
    ledcWrite(chan,1495);
      delay(10);
}
