  void motorF(int speedRequiered){
  ledcWrite(escChan,speedRequiered);
  delay(10);
    
}

void motorR(int speedRequiered){
  if(speedRequiered<1500){
  ledcWrite(escChan,speedRequiered);
  delay(10);
  }
}



void motorS(){
      ledcWrite(escChan,1400);
      delay(500);
    ledcWrite(escChan,1489);
      delay(100);
}

   void motorEmergncyStop(){
    ledcWrite(escChan,1300);
    delay(200);
    ledcWrite(escChan,1495);
      delay(10);
}

//int getRotation(){
//  ADC_VALUE = analogRead(Analog_channel_pin);
//  delay(20);
//while(ADC_VALUE>800){
//  if(count ==0){
//  rotation++;
//  count++;
//  }
//  delay(20);
//  ADC_VALUE = analogRead(Analog_channel_pin);
//}
//count=0;
//return rotation;
//
//
//}
