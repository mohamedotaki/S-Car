

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





//no changes 
const int frontSensor = 2;
const int backSensor = 0;
const int rightSensor = 16;
const int leftSensor = 4;
int f,r,l,b;

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


}

void loop(){



  //  motorF(1700);
//Serial.print(pulseIn(26,LOW));
//Serial.print("--");
//Serial.print(pulseIn(26,HIGH));
//Serial.print("--");
//Serial.println(potValue);
//int speed =1580;

 


  
}


void TaskAnalogReadA3(void *pvParameters)  // This is a task.
{
     int s=0;
  (void) pvParameters;
  for (;;)
  {
    int steering =1500, currentPos=0, emptySpace=0;
    
   // motorF(1650);
   //motorF(1580);
f=ultrasonicValue(frontSensor);
    r=ultrasonicValue(rightSensor);
    b=ultrasonicValue(backSensor);
Serial.print(r);
Serial.print("---");
Serial.println(b);
  
      //searchForEmptySpace(r);
      park();
   vTaskSuspend(NULL);
    if(r<1){
       Serial.print(r);
    if(currentPos ==0)currentPos=r;
    while(r>6){
   r=ultrasonicValue(rightSensor);
   steering = map(r, (80/currentPos)+1, currentPos, 1850, 1100);
        Serial.print(r);
             Serial.print("--");
   Serial.println(steering);
   if(r<=10){
    steering = steering+50;
   }
rightServo(steering);


    }
    centerServo();
    currentPos=0;
    while(r>7){
      emptySpace++;
      Serial.print(emptySpace);
      if(emptySpace > 20){
        r=ultrasonicValue(rightSensor);
        if(currentPos ==0)currentPos=r;
        steering = map(r, (80/currentPos)+1, currentPos, 1100, 1850);
        rightServo(steering);
      }
    }
    
    }else{
     // Serial.print("too far");
      currentPos=0;
    }










    

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
