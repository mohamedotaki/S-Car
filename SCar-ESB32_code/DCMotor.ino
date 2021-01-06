  void motorF(int speedRequiered){
  ledcWrite(chan,speedRequiered);
  delay(10);
    
}

void motorR(int speedRequiered){
  ledcWrite(chan,speedRequiered);
  delay(10);
}



void motorS(){
      ledcWrite(chan,1400);
      delay(100);
    ledcWrite(chan,1489);
      delay(100);
}

   void motorEmergncyStop(){
    ledcWrite(chan,1300);
    delay(10);
    ledcWrite(chan,1495);
      delay(10);
}

int getRotation(){
  ADC_VALUE = analogRead(Analog_channel_pin);
  delay(20);
while(ADC_VALUE>800){
  if(count ==0){
  rotation++;
  count++;
  }
  delay(20);
  ADC_VALUE = analogRead(Analog_channel_pin);
}
count=0;
return rotation;


}
