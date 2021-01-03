void parkingAssist(int side){
            Serial.println("on");
int emptySpace=0;
switch (side){
  
  //right side
  case 1:
  Serial.print("ssssssssss");
      while(r>8 && r<15){
        motorF(1580);
        rightServo(1300); 
        delay(15);
      }
      centerServo();
      while(emptySpace<10){
        if(r >14){
          emptySpace = emptySpace +1;
          Serial.println(emptySpace);
        }else emptySpace =0;
      }
      motorS();
      maxRightServo();
  break;
  //left side
    case 2:
    Serial.println("left");
    break;
}

}
