// Моторы подключаются к клеммам M1+,M1-,M2+,M2-  
// Motor shield использует четыре контакта 6,5,7,4 для управления моторами 
 
#define SPEED_LEFT      6
#define SPEED_RIGHT     5 
#define DIR_LEFT        7
#define DIR_RIGHT       4
#define MAX_SPEED 200

int speedLeft = 0;
int speedRight = 0;
boolean reverseLeft = 0;
boolean reverseRight = 0;
 
void go()
{
    analogWrite(SPEED_LEFT, speedLeft);
    analogWrite(SPEED_RIGHT, speedRight);
    digitalWrite(DIR_LEFT, reverseLeft ? LOW : HIGH); 
    digitalWrite(DIR_RIGHT, reverseRight ? LOW : HIGH);
}
 
void setup() 
{
    for(int i = 5; i <= 8; i++)     
        pinMode(i, OUTPUT);  
        
    Serial.begin(9600); 
} 
 
void loop() 
{ 
    if (Serial.available() >= 5) {
        byte speed = Serial.read();
        byte direction = Serial.read();
        byte turn = Serial.read();
        byte turnDirection = Serial.read();
        byte turnOnly = Serial.read();
        
        if (turnOnly) {
          speedLeft = MAX_SPEED;
          speedRight = MAX_SPEED;
          reverseLeft = turnDirection == 0;
          reverseRight = turnDirection == 1;
        }
        else {
          speedLeft = speed;
          if (turnDirection == 0)
            speedLeft = constrain(speedLeft - map(turn, 0, MAX_SPEED, 0, speedLeft), 0, MAX_SPEED);
          speedRight = speed;
          if (turnDirection == 1)
            speedRight = constrain(speedRight - map(turn, 0, MAX_SPEED, 0, speedRight), 0, MAX_SPEED);
          if (direction == 0) {
            reverseLeft = false;
            reverseRight = false;
          }
          else {
            reverseLeft = true;
            reverseRight = true;
          }
        }
        
        Serial.write(1);
    }

    go();
}
