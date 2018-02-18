#include <Adafruit_NeoPixel.h>
#define PIN 6

Adafruit_NeoPixel strip = Adafruit_NeoPixel(342, PIN, NEO_GRB + NEO_KHZ800);
void setup() {
  strip.begin();
  strip.show();
}

void loop() {
  for(int i = 1; i < 342; i++) {
    strip.setPixelColor(i, strip.Color(255, 0, 0));
    strip.setPixelColor(i-1, strip.Color(0, 0, 0));
    strip.show();
    delay(10);
  }

  
}
