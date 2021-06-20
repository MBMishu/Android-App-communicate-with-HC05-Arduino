#include <SoftwareSerial.h>

#include <Servo.h> 
int servoPin = 3; 
  
char turnon = '1';
char turnoff = '2';
Servo Servo1; 
void setup() {
       Serial.begin(9600);
Servo1.attach(servoPin); 
     

}

void loop() {
//  while(Serial.available()==0)
//{
//  }

//byte var = 250;
char var ; 

  while(Serial.available()>0)
{
  var=Serial.read();
   Serial.println("Received: ");


   if(var==turnon){
    Serial.println("turn on: Alo");
     Servo1.write(180); 
    
   }
   if(var==turnoff){
     Serial.println("turn off: Alo");
      Servo1.write(0); 
    
   }
 
Serial.println(var);

}
 digitalWrite(13, LOW);
  

}
