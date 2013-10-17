#include <Servo.h>

#define LEFT_SPEED_PIN 6
#define RIGHT_SPEED_PIN 5 
#define LEFT_DIRECTION_PIN 7
#define RIGHT_DIRECTION_PIN 4
#define LIGHT_PIN 3
#define INCOMING_LIGHT_PIN 0
#define INFRARED_DISTANCE_PIN 1
#define MIN_SPEED 0
#define MAX_SPEED 200
#define BUFFER_LENGTH 20
#define PACKAGE_START_MARKER 255
#define START_SERVO_ANGLE 79

// State variables
int leftSpeed = 0; // 0 - 254
int leftDirection = 0; // 1 - forward, 0 - backward
int rightSpeed = 0; // 0 - 254
int rightDirection = 0; // 1 - forward, 0 - backward
int lightIntensity = 0;
int incomingLight = 0;
int infraredDistance = 0;
int servoAngle = START_SERVO_ANGLE;

Servo servo;

// Command parsing variables
byte buffer[BUFFER_LENGTH];
int readingState = 0; // 0 - waiting for package start marker
                      // 1 - waiting for package type
                      // 2 - waiting for complete body
int commandType = 0;  // 1 - move
                      // 2 - set light
                      // 3 - get incoming light
                      // 4 - get infrared distance
                      // 5 - set servo angle
int bodyLength = 0;
int currentBodyLength = 0;

// Changing state functions
void setWheelsSpeed() {
  analogWrite(LEFT_SPEED_PIN, leftSpeed == 0 ? 0 : map(leftSpeed, 0, 254, MIN_SPEED, MAX_SPEED));
  digitalWrite(LEFT_DIRECTION_PIN, leftDirection == 1 ? LOW : HIGH); 
  analogWrite(RIGHT_SPEED_PIN, rightSpeed == 0 ? 0 : map(rightSpeed, 0, 254, MIN_SPEED, MAX_SPEED));
  digitalWrite(RIGHT_DIRECTION_PIN, rightDirection == 1 ? LOW : HIGH);
}
void setLightIntensity() {
  analogWrite(LIGHT_PIN, lightIntensity);
}
void readIncomingLight() {
  incomingLight = analogRead(INCOMING_LIGHT_PIN);
}
void readInfraredDistance() {
  infraredDistance = analogRead(INFRARED_DISTANCE_PIN);
}

void setServoAngle() {
  servo.write(servoAngle);
}

// Command processing functions
int getBodyLength() {
  if (commandType == 1)
    return 4;
  else if (commandType == 2)
    return 1;
  else if (commandType == 3)
    return 1;
  else if (commandType == 4)
    return 1;
  else if (commandType == 5)
    return 1;
}

boolean runCommand() {
  if (commandType == 1) {
    leftSpeed = buffer[0];
    leftDirection = buffer[1];
    rightSpeed = buffer[2];
    rightDirection = buffer[3];
    setWheelsSpeed();
    Serial.write(0);
    return true;
  }
  else if (commandType == 2) {
    lightIntensity = buffer[0];
    setLightIntensity();
    Serial.write(0);
    return true;
  }
  else if (commandType == 3) {
    readIncomingLight();
    Serial.write(1);
    byte incomingLightByte = map(incomingLight, 0, 1023, 0, 255);
    Serial.write(incomingLightByte);
    return true;
  }
  else if (commandType == 4) {
    readInfraredDistance();
    Serial.write(2);
    byte infraredDistanceByte1 = infraredDistance / 256;
    byte infraredDistanceByte2 = infraredDistance % 256;
    Serial.write(infraredDistanceByte1);
    Serial.write(infraredDistanceByte2);
    return true;
  }
  else if (commandType == 5) {
    servoAngle = buffer[0];
    setServoAngle();
    Serial.write(0);
  }
  
  Serial.write(0);
  return false;
}
 
void setup() 
{
    pinMode(LEFT_SPEED_PIN, OUTPUT);
    pinMode(RIGHT_SPEED_PIN, OUTPUT);
    pinMode(LEFT_DIRECTION_PIN, OUTPUT);
    pinMode(LEFT_DIRECTION_PIN, OUTPUT);
    pinMode(LIGHT_PIN, OUTPUT);
    pinMode(INCOMING_LIGHT_PIN, INPUT);
    servo.attach(9);
    Serial.begin(9600);
    
    setServoAngle();
} 
 
void loop() { 
  while (Serial.available() >= 1) {
    byte value = Serial.read();
    if (value == PACKAGE_START_MARKER) {
      readingState = 1;
      continue;
    }

    if (readingState == 1) {
      commandType = value;
      bodyLength = getBodyLength();
      currentBodyLength = 0;
      readingState = 2;
    }
    else if (readingState == 2) {
      buffer[currentBodyLength] = value;
      ++currentBodyLength;
      if (currentBodyLength == bodyLength) {
        runCommand();
        readingState = 0;
      }
    }
  }
}
