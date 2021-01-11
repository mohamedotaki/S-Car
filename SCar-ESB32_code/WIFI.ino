void scanNetworks() {
 
  int numberOfNetworks = WiFi.scanNetworks();
 
  Serial.print("Number of networks found: ");
  Serial.println(numberOfNetworks);
 
  for (int i = 0; i < numberOfNetworks; i++) {
    Serial.println( WiFi.SSID(i));
    int bufferSize=WiFi.SSID(i).length()+1;
   byte networkName[bufferSize];
   WiFi.SSID(i).getBytes(networkName,bufferSize);
    SerialBT.write(networkName,bufferSize);
    SerialBT.write('\n');
 
  }
}
