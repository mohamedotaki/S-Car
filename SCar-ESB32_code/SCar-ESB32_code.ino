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
    delay(3000);

     xTaskCreatePinnedToCore(
    TaskAnalogReadA3
    ,  "AnalogReadA3"
    ,  1024  // Stack size
    ,  NULL
    ,  3  // Priority
    ,  &task1Handle 
    ,  0);

    centerServo();
    delay(10);
    motorF(1590);

   // parkingAssist(1);

}

void loop(){


maxRightServo();
delay(3000);
    centerServo();
    delay(3000);
   maxLeftServo();
       delay(3000);
    motorF(1700);
//Serial.print(pulseIn(26,LOW));
//Serial.print("--");
//Serial.print(pulseIn(26,HIGH));
//Serial.print("--");
//Serial.println(potValue);
//int speed =1580;

 



  
}


void TaskAnalogReadA3(void *pvParameters)  // This is a task.
{
  (void) pvParameters;
  for (;;)
  {
f=ultrasonicValue(frontSensor);
//b=ultrasonicValue(backSensor);
//l=ultrasonicValue(leftSensor);
//r=ultrasonicValue(rightSensor);
if(f<=5){
  Serial.print ("Stoooooooooooooooooop");
  motorEmergncyStop();
}
Serial.print (f);
Serial.print ("  -  ");
Serial.print (r);
Serial.print ("  -  ");
Serial.print (l);
Serial.print ("  -  ");
Serial.print (b);
Serial.print ("\n");
    vTaskDelay(10);  // one tick delay (15ms) in between reads for stability
  }
}
