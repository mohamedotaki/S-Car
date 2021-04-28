#define frontSensorBit (1 << 0)
#define rightSensorBit (1<<1)
#define leftSensorBit (1<<2)
#define backSensorBit (1<<3)
#define allSensorBits (frontSensorBit | rightSensorBit | leftSensorBit | backSensorBit)
#include <WiFi.h>
#include "EEPROM.h"
#include "BluetoothSerial.h"
#include <FreeRTOS.h>
#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif


BluetoothSerial SerialBT;
static EventGroupHandle_t  xEventGroup;
TaskHandle_t task1Handle = NULL, ultrasonicTaskHandle = NULL, powerManagementTaskHandle = NULL;

//no changes
// Servo
const int servo = 19;
const int servoChan = 1;

// ESC pin cinfig
const float freq = 62.43756243756244;
const int escChan = 0;
const int res = 14;
const int escSignal = 12;

//UltraSonic pin No
const int frontSensor = 2;
const int backSensor = 0;
const int rightSensorB = 16;
const int rightSensorF = 4;
int BTSteering = 1500;
int f, b, rfs, r;

//Car Settings
int reversingSpeed = 1353, forwardSpeed = 1592;
int beforParkingPos = 0;
int carWidth = 35; // car width is 26cm and 10cm for safty
int  carLength = 50; // car length is 41cm and 9cm for safty

//wifi
String wifiName, wifiPass;

//Power
int timeToSleep=0;

void mainTask( void *pvParameters );
//void powerManagementTask( void *pvParameters );
//void wifiTask( void *pvParameters );

void setup() {
  EEPROM.begin(255);
  //Initialize serial communication:
  Serial.begin(115200);
  //Bluetooth setup
  SerialBT.begin("ESP32test");
  Serial.println("Bluetooth is on..");
  //Servo
  pinMode(servo, OUTPUT);
  ledcSetup(servoChan, 50, 8);
  ledcAttachPin(servo, servoChan);
  //centerServo();
  delay(1000);
  // ESC setup and calebration
  pinMode(escSignal, OUTPUT);
  ledcSetup(escChan, freq, res);
  ledcAttachPin(escSignal, escChan);
  ledcWrite(escChan, 1489);
  delay(2000);
  ledcWrite(escChan, 1420);
  delay(500);
  ledcWrite(escChan, 1489);
  delay(1000);
  // Tasks
  xEventGroup  =  xEventGroupCreate();
  xTaskCreatePinnedToCore(mainTask, "mainTask", 3000, NULL, 3, &task1Handle, 0);
  //xTaskCreatePinnedToCore(ultrasonicTask, "ultrasonicTask", 500, NULL, 3, &ultrasonicTaskHandle, 1);
  //xTaskCreatePinnedToCore(powerManagementTask, "powerManagementTask", 200, NULL, 3, &powerManagementTaskHandle, 1);
  //vTaskSuspend(ultrasonicTaskHandle);
   // vTaskSuspend(task1Handle);
  /////////////////////////////////////////////////////////
//  pinMode(26, INPUT);
//  pinMode(25, INPUT);


  //WIFI
//  wifiName = readFromEEPROM(0);
//  wifiPass = readFromEEPROM(40);
//  Serial.println(wifiName);
//  Serial.println(wifiPass);
//  if (wifiName.length() > 1 && wifiPass.length() > 1) {
//    ConnectToWiFi(wifiName.c_str(), wifiPass.c_str());
//    if (WiFi.status() == WL_CONNECTED) {
//      Serial.println("connected to WIFI");
//    }
//  }


}

void loop() {
  //
  //f=ultrasonicValue(frontSensor);
  //b=ultrasonicValue(backSensor);
  //rfs=ultrasonicValue(rightSensorF);
  //r=ultrasonicValue(rightSensorB);
  //Serial.print ("\n");
  //Serial.print (f);
  //Serial.print ("  -  ");
  //Serial.print (r);
  //Serial.print ("  -  ");
  //Serial.print (rfs);
  //Serial.print ("  -  ");
  //Serial.print (b);
  //Serial.print ("\n");
  //
  delay(2000);
  Serial.print("left");
  maxLeftServo();
  delay(2000);
  Serial.print("center");
  centerServo();
  delay(2000);
  Serial.print("Right");
  maxRightServo();
  delay(2000);
   Serial.print("center");
  centerServo();








  //////motorF(1700);
  //Serial.print(pulseIn(26,LOW));
  //Serial.print("--");
  //Serial.print(pulseIn(26,HIGH));
  //Serial.print("--------");
  //Serial.print(pulseIn(25,LOW));
  //Serial.print("--");
  //Serial.println(pulseIn(25,HIGH));






}
void ultrasonicTask(void *pvParameters)
{

  EventBits_t xEventGroupValue;
  for (;;)
  {
    Serial.print("uuuuuuuuuuuuuuuuuuu");
    xEventGroupValue  = xEventGroupWaitBits(xEventGroup, allSensorBits , pdTRUE, pdFALSE, portMAX_DELAY );
    if ((xEventGroupValue & frontSensorBit) != 0) {
      f = ultrasonicValue(frontSensor);
      Serial.print(f);
    }
    if ((xEventGroupValue & backSensorBit) != 0) {
      b = ultrasonicValue(backSensor);
    }
    if ((xEventGroupValue & leftSensorBit) != 0) {
      rfs = ultrasonicValue(rightSensorF);
    }
    if ((xEventGroupValue & rightSensorBit) != 0) {
      r = ultrasonicValue(rightSensorB);
      Serial.print(r);
    }

    vTaskDelay(10);

  }
}

//void powerManagementTask(void *pvParameters)
//{
//  for (;;)
//  {
//
//
//    vTaskDelay(10);
//  }
//}

void mainTask(void *pvParameters)
{
  for (;;)
  {
    bluetooth();
    // int aa=0;
    // if(aa ==0){
    //   xEventGroupSetBits(xEventGroup, frontSensorBit | rightSensorBit);
    //aa++;
    // }



    // motorF(1650);
    //motorF(1580);
    //f=ultrasonicValue(frontSensor);
    // r=ultrasonicValue(rightSensor);
    // b=ultrasonicValue(backSensor);
    //Serial.print(r);
    //Serial.print("---");
    //Serial.println(b);

    //searchForEmptySpace(r);
    // park();
    vTaskDelay(10);  // one tick delay (15ms) in between reads for stability


  }
}
