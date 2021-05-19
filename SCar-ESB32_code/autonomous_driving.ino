void autonomousDriving() {
  String datareceived = "";
  byte steeringByte;
  while (Serial2.available() > 0) {
    steeringByte = Serial2.read();
    if (char(steeringByte) == 'E') {
      break;
    }
    datareceived += char(steeringByte);
  }
  if (datareceived.equals("Autopilot is Disabled")) {

  }
  if (datareceived != "") {
    Serial.println(datareceived);
  }
}
