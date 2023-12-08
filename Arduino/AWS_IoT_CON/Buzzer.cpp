#include "Buzzer.h"

Buzzer::Buzzer(int pin) {
  // Use 'this->' to make the difference between the
  // 'pin' attribute of the class and the 
  // local variable 'pin' created from the parameter.
  this->pin = pin;
  this->state = false;
  pinMode(pin, OUTPUT);
}

void Buzzer::tone(unsigned int freq){
  ::tone(pin, freq);
  this->state = BUZZER_ON;
}
void Buzzer::noTone() {
  ::noTone(pin);
  this->state = BUZZER_OFF;
}

byte Buzzer::getState() {
  return state;
}
