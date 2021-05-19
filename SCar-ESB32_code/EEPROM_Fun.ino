//0  - 39  to store wifi name
//40 - 79  to store wifi pass


void writeToEEPROM(const char* toStore, int address) {
  int i = 0;
  clearAddress(address);
  for (; i < strlen(toStore) + 1; i++) {
    EEPROM.write(address + i, toStore[i]);
  }
  EEPROM.write(address + i, '\0');
  EEPROM.commit();
}

String readFromEEPROM(int address) {
  String stringOut;
  char c;
  int i = 0;
  c = EEPROM.read(address);
  for (; i < 39; i++) {
    c = EEPROM.read(address + i);
    if (c == '\0') break;
    stringOut += c;
  }
  return stringOut;
}

void clearAddress(int startAddress) {
  for (int i = startAddress; i < startAddress + 39; i++) {
    EEPROM.write( i, 0);
  }
  EEPROM.commit();
}
