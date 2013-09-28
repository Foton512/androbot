// Моторы подключаются к клеммам M1+,M1-,M2+,M2-  
// Motor shield использует четыре контакта 6,5,7,4 для управления моторами 
 
#define SPEED_LEFT      6
#define SPEED_RIGHT     5 
#define DIR_LEFT        7
#define DIR_RIGHT       4
boolean run = false;
 
void go(int speed, bool reverseLeft, bool reverseRight)
{
    // Для регулировки скорости `speed` может принимать значения от 0 до 255,
    // чем болше, тем быстрее. 
    analogWrite(SPEED_LEFT, speed);
    analogWrite(SPEED_RIGHT, speed);
    digitalWrite(DIR_LEFT, reverseLeft ? LOW : HIGH); 
    digitalWrite(DIR_RIGHT, reverseRight ? LOW : HIGH);
}
 
void setup() 
{
    // Настраивает выводы платы 4,5,6,7 на вывод сигналов 
    for(int i = 5; i <= 8; i++)     
        pinMode(i, OUTPUT);  
        
    //Устанавливаем скорость UART
    Serial.begin(9600); 
} 
 
void loop() 
{ 
    //Если данные пришли
    if (Serial.available() > 0) {
        //Считываем пришедший байт
        byte incomingByte = Serial.read();
        Serial.write(1);
 
        if (incomingByte == 1) {
          run = true;
        }
        else {
          run = false;
        }
      
    }
    
    if (run) {
       go(200, true, true);
    }
    else {
      go(0, false, false);
    }
}
