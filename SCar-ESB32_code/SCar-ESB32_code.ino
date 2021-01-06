#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

BluetoothSerial SerialBT;

TaskHandle_t task1Handle = NULL;
SemaphoreHandle_t SW2_Semaphore=NULL;

    int s = 0;
const float freq = 62.43756243756244;
const int chan = 0;
const int res =14;

const int escSignal = 12;


int potencia = 0;
int leitura = 0;
int ciclo_A = 0;


const int Analog_channel_pin= 33;
int ADC_VALUE = 0;
int voltage_value = 0; 
int rotation=0;
int count=0;


//no changes 
const int frontSensor = 2;
const int backSensor = 0;
const int rightSensor = 16;
const int leftSensor = 4;
int f,r,l,b;
int reversingSpeed = 1340, forwardSpeed = 1600;
int beforParkingPos=0;
int carWidth = 35; // car width is 26cm and 10cm for safty
int  carLength = 50; // car length is 41cm and 9cm for safty
void TaskAnalogReadA3( void *pvParameters );


void setup() {
  // initialize serial communication:
   Serial.begin(115200);
//pinMode(pingPin,OUTPUT) ;
    //pinMode(in_1,OUTPUT) ; //Logic pins are also set as output
  //  pinMode(in_2,OUTPUT) ;
  //ledcSetup(pwmChannel, freq, resolution);
 //ledcAttachPin(in_2, pwmChannel);
 //ESC.attach(5,1000,2000);

 pinMode(escSignal, OUTPUT);
  pinMode(5, OUTPUT);
   pinMode(26, INPUT);
  
  ledcSetup(chan, freq, res);
  ledcAttachPin(escSignal, chan);
  ledcAttachPin(5, 1);
    ledcWrite(chan,1489);
    delay(4000);

     xTaskCreatePinnedToCore(
    TaskAnalogReadA3
    ,  "AnalogReadA3"
    ,  1024  // Stack size
    ,  NULL
    ,  3  // Priority
    ,  &task1Handle 
    ,  0);

    centerServo();
   //maxRightServo();
 //  maxLeftServo();
    delay(1000);
   // motorF(1590);

  //  parkingAssist(1);
 // vTaskSuspend(task1Handle);

  SerialBT.begin("ESP32test"); //Bluetooth device name
  Serial.println("The device started, now you can pair it with bluetooth!");

}

void loop(){







//
//  //  motorF(1700);
//Serial.print(pulseIn(26,LOW));
//Serial.print("--");
//Serial.println(pulseIn(26,HIGH));




 


  
}


void TaskAnalogReadA3(void *pvParameters)  // This is a task.
{
  (void) pvParameters;
  for (;;)
  {
    f=ultrasonicValue(frontSensor);

    SerialBT.write(f);
    Serial.println(f);
  if (SerialBT.available()) {
    switch(SerialBT.read()){
      case 'L':         // turn left 
      Serial.println("left");
      break;
      case 'R':         // go forward or increase the speed
      Serial.println("right");
      break;
      case 'F':         // go forward or increase the speed
      Serial.println("forward");
      break;
      case 'B':         //  reverse or increase the speed
      Serial.println("reverse");
      break;
      case 'S':         // Stop motor
      Serial.println("stop");
      break;
      case 'P':         // go forward or increase the speed
      Serial.println("parking assist");
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
     parkingAssist();
   vTaskSuspend(NULL);
   










    

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
