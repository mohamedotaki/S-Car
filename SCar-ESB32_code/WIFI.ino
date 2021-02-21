void scanNetworks() {
 
  int numberOfNetworks = WiFi.scanNetworks();
 
  Serial.print("Number of networks found: ");
  Serial.println(numberOfNetworks);
  char buff[50];
  for (int i = 0; i < numberOfNetworks; i++) {
    Serial.println( WiFi.SSID(i));
    WiFi.SSID(i).toCharArray(buff,WiFi.SSID(i).length()+1);
     for(int x=0; x<WiFi.SSID(i).length()+1;x++){
     SerialBT.write(buff[x]);
     }
     SerialBT.write(3); //end of text
  }
   SerialBT.write(4);//end of transmission
}

void ConnectToWiFi(const char *wifiName, const char *wifiPass)
{
  WiFi.disconnect();
  WiFi.mode(WIFI_STA);
  WiFi.begin(wifiName, wifiPass);
  Serial.print("Connecting to "); Serial.println(wifiName);
 
  uint8_t i = 0;
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print('.');
    delay(500);
 
    if ((++i % 40) == 0)
    {
      WiFi.disconnect();
      Serial.println("Faild To Connect");
      break;
    }
  }
}
