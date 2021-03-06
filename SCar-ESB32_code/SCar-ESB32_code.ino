#define RXD2 16
#define TXD2 17
#define frontSensorBit (1 << 0)
#define rightSensorBit (1<<1)
#define leftSensorBit (1<<2)
#define backSensorBit (1<<3)
#define sendSensorsValueBit (1<<4)
#define parkingAssistBit (1<<5)
#define autonomousDrivingBit (1<<6)
#define allFeatureBits (parkingAssistBit | autonomousDrivingBit)
#define allSensorBits (frontSensorBit | rightSensorBit | leftSensorBit | backSensorBit)
#include <WiFi.h>
#include <HTTPClient.h>
#include <Arduino_JSON.h>
#include "EEPROM.h"
#include "BluetoothSerial.h"
#include <FreeRTOS.h>
#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif


BluetoothSerial SerialBT;
static EventGroupHandle_t  xEventGroup;
TaskHandle_t task1Handle = NULL, ultrasonicTaskHandle = NULL, powerManagementTaskHandle = NULL , featuresTaskHandle = NULL , checkUserHandle = NULL;

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
const int rightSensorB = 4;
const int rightSensorF = 16;
int BTSteering = 1500;
int f, b, rfs, r;

//Car Settings
const int reversingSpeed = 1353, forwardSpeed = 1588;
int beforParkingPos = 0;
const int carWidth = 35; // car width is 26cm and 9cm for safty
const int  carLength = 50; // car length is 41cm and 9cm for safty
const int rightSideDistance = 40;

//wifi
String wifiName, wifiPass;

//Power
int timeToSleep = 0;

// timing
unsigned long myTime = 0;
unsigned long timeout = 0;

//BT

String BTDataInput;

void mainTask( void *pvParameters );
//void checkUser( void *pvParameters );
void ultrasonicTask( void *pvParameters );
//void wifiTask( void *pvParameters );


void setup() {
  EEPROM.begin(255);
  //Initialize serial communication:
  Serial.begin(115200);
  Serial2.begin(9600, SERIAL_8N1, RXD2, TXD2);
  //Bluetooth setup
  SerialBT.begin("S-Car");
  SerialBT.register_callback(callback);
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

  connectToSavedWifi();

  // Tasks
  xEventGroup  =  xEventGroupCreate();
  xTaskCreatePinnedToCore(mainTask, "mainTask", 3000, NULL, 3, &task1Handle, 0);
  xTaskCreatePinnedToCore(ultrasonicTask, "ultrasonicTask", 3000, NULL, 4, &ultrasonicTaskHandle, 0);
  xTaskCreatePinnedToCore(featuresTask, "featuresTask", 2000, NULL, 3, &featuresTaskHandle, 1);
  vTaskSuspend(ultrasonicTaskHandle);
}

void loop() {}

void featuresTask(void *pvParameters)
{
  EventBits_t xEventGroupValue;
  for (;;)
  {
    xEventGroupValue  = xEventGroupWaitBits(xEventGroup, allFeatureBits , pdTRUE, pdFALSE, portMAX_DELAY );
    if ((xEventGroupValue & autonomousDrivingBit) != 0) {
      Serial.println("autonomous driving is on ");
      autonomousDriving();
    }
    if ((xEventGroupValue & parkingAssistBit) != 0) {
      Serial.println("parking assist activated");
      parkingAssist();
    }
    vTaskDelay(10);
  }
}

void ultrasonicTask(void *pvParameters)
{
  EventBits_t xEventGroupValue;
  for (;;)
  {
    xEventGroupValue  = xEventGroupWaitBits(xEventGroup, allSensorBits , pdFALSE, pdFALSE, portMAX_DELAY );
    if ((xEventGroupValue & frontSensorBit) != 0) {
      f = ultrasonicValue(frontSensor);
      if (f < 5) {
        motorS();
      }
    }
    if ((xEventGroupValue & backSensorBit) != 0) {
      b = ultrasonicValue(backSensor);
       if (b < 5) {
        motorS();
      }
    }
    if ((xEventGroupValue & leftSensorBit) != 0) {
      r = ultrasonicValue(rightSensorB);
    }
    if ((xEventGroupValue & rightSensorBit) != 0) {
      rfs = ultrasonicValue(rightSensorF);
    }
    if ((xEventGroupValue & sendSensorsValueBit) != 0) {
      sendSensorsValue();
    }
    vTaskDelay(10);
  }
}

void mainTask(void *pvParameters)
{
  for (;;)
  {
    bluetooth();
    //getLastEvent(1 , 10000);

    vTaskDelay(10);  // one tick delay (15ms) in between reads for stability
  }
}
