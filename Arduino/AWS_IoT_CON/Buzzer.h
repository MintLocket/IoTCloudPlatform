#include <Arduino.h>

#define BUZZER_OFF 0
#define BUZZER_ON 1

class Buzzer {
  private:
    int pin;
    byte state;

  public:
    Buzzer(int pin);
    void tone(unsigned int freq);
    void noTone();
    byte getState();
};