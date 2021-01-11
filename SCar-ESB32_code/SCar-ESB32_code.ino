#include <WiFi.h>
#include "BluetoothSerial.h"
#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial SerialBT;

TaskHandle_t task1Handle = NULL,wifiTaskHandle = NULL;

//no changes 
// Servo
const int servo = 19;
const int servoChan = 1;

// ESC pin cinfig
const float freq = 62.43756243756244;
const int escChan = 0;
const int res =14;
const int escSignal = 12;
//UltraSonic pin No
const int frontSensor = 2;
const int backSensor = 0;
const int rightSensorB = 16;
const int rightSensorF = 4;
int f,r,l,b;
//Car Settings
int reversingSpeed = 1353, forwardSpeed = 1592;
int beforParkingPos=0;
int carWidth = 35; // car width is 26cm and 10cm for safty
int  carLength = 50; // car length is 41cm and 9cm for safty



void mainTask( void *pvParameters );
//void wifiTask( void *pvParameters );

void setup() {
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
    ledcWrite(escChan,1489);
    delay(2000);
    ledcWrite(escChan,1420);
    delay(500);
    ledcWrite(escChan,1489);
    delay(1000);
  // Tasks 
    xTaskCreatePinnedToCore(mainTask,"mainTask",1024,NULL,3,&task1Handle,0);
//    xTaskCreatePinnedToCore(wifiTask,"wifiTask",2000,NULL,3,&wifiTaskHandle,1);
//    vTaskSuspend(wifiTaskHandle);
/////////////////////////////////////////////////////////
    pinMode(26, INPUT);
     pinMode(25, INPUT);
}

void loop(){

f=ultrasonicValue(frontSensor);
b=ultrasonicValue(backSensor);
l=ultrasonicValue(rightSensorF);
r=ultrasonicValue(rightSensorB);
Serial.print ("\n");
Serial.print (f);
Serial.print ("  -  ");
Serial.print (r);
Serial.print ("  -  ");
Serial.print (l);
Serial.print ("  -  ");
Serial.print (b);
Serial.print ("\n");
//
//delay(2000);
//maxLeftServo();
//delay(2000);
//centerServo();
//delay(2000);
//maxRightServo();
//delay(2000);
//centerServo();








//////motorF(1700);
//Serial.print(pulseIn(26,LOW));
//Serial.print("--");
//Serial.print(pulseIn(26,HIGH));
//Serial.print("--------");
//Serial.print(pulseIn(25,LOW));
//Serial.print("--");
//Serial.println(pulseIn(25,HIGH));


 


  
}
//void wifiTask(void *pvParameters)
//{
//  (void) pvParameters;
//  for (;;)
//  {
//  //  scanNetworks();
//    vTaskSuspend(NULL);
//
//  }
//  }

void mainTask(void *pvParameters)
{
  int BTSteering =1500;
  (void) pvParameters;
  for (;;)
  {
   parkingAssist();
    vTaskSuspend(NULL);
  if (SerialBT.available()) {
    switch(SerialBT.read()){
      case 'L':         // turn left 
      BTSteering = BTSteering+100;
      if(BTSteering >=1900){
        BTSteering=1900;
      }
      rightServo(BTSteering);
      break;
      case 'R':         // Turn Right
      BTSteering = BTSteering-100;
      if(BTSteering <=1100){
        BTSteering= 1100;
      }
      rightServo(BTSteering);
      break;
      case 'F':         // go forward or increase the speed
      Serial.println("forward");
      break;
      case 'B':         //  reverse or increase the speed
      Serial.println("reverse");
      break;
      case 'S':         // Stop motor
      Serial.println("stop");
      vTaskResume(wifiTaskHandle);
      break;
      case 'P':         // go forward or increase the speed
      Serial.println("parking assist");
       parkingAssist();
      break;
    }
     }
     delay(20);



    
    int steering =1500, currentPos=0, emptySpace=0;
    
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
    

   










    

//f=ultrasonicValue(frontSensor);
//b=ultrasonicValue(backSensor);
//l=ultrasonicValue(leftSensor);
//r=ultrasonicValue(rightSensor);
//if(f<=5){
//  Serial.print ("Stoooooooooooooooooop");
// // motorEmergncyStop();
//}
//Serial.print ("\n");
//Serial.print (f);
//Serial.print ("  -  ");
//Serial.print (r);
//Serial.print ("  -  ");
//Serial.print (l);
//Serial.print ("  -  ");
//Serial.print (b);
//Serial.print ("\n");
//
//    vTaskDelay(10);  // one tick delay (15ms) in between reads for stability
     

  }
}
