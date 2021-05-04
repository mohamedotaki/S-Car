 int ultrasonicValue(int PinNumber){
  
   long duration, cm;

  // The PING))) is triggered by a HIGH pulse of 2 or more microseconds.
  // Give a short LOW pulse beforehand to ensure a clean HIGH pulse:
  pinMode(PinNumber, OUTPUT);
  digitalWrite(PinNumber, LOW);
  delayMicroseconds(2);
  digitalWrite(PinNumber, HIGH);
  delayMicroseconds(5);
  digitalWrite(PinNumber, LOW);

  // The same pin is used to read the signal from the PING))): a HIGH pulse
  // whose duration is the time (in microseconds) from the sending of the ping
  // to the reception of its echo off of an object.
  pinMode(PinNumber, INPUT);
  duration = pulseIn(PinNumber, HIGH,5500);
  if(duration == 0){
    cm = 85;
  }else{
     cm = duration / 29 / 2;
  }
  
 
  
  return cm;
  }
