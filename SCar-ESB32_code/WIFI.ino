void scanNetworks()
{
  int numberOfNetworks = WiFi.scanNetworks();

  Serial.print("Number of networks found: ");
  Serial.println(numberOfNetworks);
  char buff[50];
  for (int i = 0; i < numberOfNetworks; i++) {
    Serial.println( WiFi.SSID(i));
    WiFi.SSID(i).toCharArray(buff, WiFi.SSID(i).length() + 1);
    for (int x = 0; x < WiFi.SSID(i).length() + 1; x++) {
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
  Serial.print("Connecting to ");
  Serial.print(wifiName);

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
  if (WiFi.status() == WL_CONNECTED) {
    Serial.print("\nConnected to ");
    Serial.println(wifiName);
  }
}

// function to read the wifi details from the EEPROM and connect to the saved Wifi
void connectToSavedWifi() {
  // WIFI
  wifiName = readFromEEPROM(0);
  wifiPass = readFromEEPROM(40);
  if (wifiName.length() > 1 && wifiPass.length() > 1) {
    ConnectToWiFi(wifiName.c_str(), wifiPass.c_str());
  }
}

void getWifiDetailsFromApp() {
  wifiName = "";
  wifiPass = "";
  byte b;
  while (true) {
    b = SerialBT.read();
    if (b != 3) {
      wifiName += (char) b;
    }
    else {
      while (true) {
        b = SerialBT.read();
        if (b != 4) {
          wifiPass += (char) b;
        }
        else {
          break;
        }
      }
      break;
    }
  }
  ConnectToWiFi(wifiName.c_str(), wifiPass.c_str());
  if (WiFi.status() == WL_CONNECTED) {
    writeToEEPROM(wifiName.c_str(), 0);
    writeToEEPROM(wifiPass.c_str(), 40);
    Serial.println("WIFI was Saved");
  }
}

void getLastEvent(int ownerID, int updateTime) {
  unsigned long lastRun = 0;
  //Send an HTTP POST request every 10 minutes
  if ((millis() - lastRun) > updateTime) {
    if (WiFi.status() == WL_CONNECTED && ownerID > 0) {
      String dataToSend = "{\"EventID\":\"";
      dataToSend += ownerID;
      dataToSend += "\"}";
      HTTPClient http;
      // Your IP address with path or Domain name with URL path
      http.begin("http://192.168.1.3:8080/S_Car_Server_war_exploded/GetEventsESP32");
      http.addHeader("Content-Type", "application/json");
      int httpResponseCode = http.POST(dataToSend);
      String dataReceived = "";
      if (httpResponseCode > 0) {
        dataReceived = http.getString();
        dataReceived.remove(0, 7);
        JSONVar myObject = JSON.parse(dataReceived);
        // JSON.typeof(jsonVar) can be used to get the type of the var
        if (JSON.typeof(myObject) == "undefined") {
          Serial.println("Parsing input failed!");
        }
        Serial.print("Event Data= ");
        Serial.println(myObject);
      }
      // Free resources
      http.end();
    }
    else {
      Serial.println("WiFi Disconnected");
    }
    lastRun = millis();
  }

}

void listenForChanges() {
  while (WiFi.status() == WL_CONNECTED) {


  }

}
