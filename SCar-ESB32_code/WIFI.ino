void scanNetworks() {
 
  int numberOfNetworks = WiFi.scanNetworks();
 
  Serial.print("Number of networks found: ");
  Serial.println(numberOfNetworks);
  char buff[50];
  for (int i = 0; i < numberOfNetworks; i++) {
    Serial.println( WiFi.SSID(i));
    WiFi.SSID(i).toCharArray(buff,WiFi.SSID(i).length()+1);
     for(int x=0; x<WiFi.SSID(i).length()+1;x++){
     SerialBT.write((char)buff[x]);
     }
     SerialBT.write(3); //end of text
  }
   SerialBT.write(4);//end of transmission
}
