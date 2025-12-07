#include "Timer.hpp"

sf::Clock mainClock;        // main clock

sf::Time prevTime;
sf::Time currentTime;

float smooth(float t) {
    return t * t * (3 - 2 * t);
}

float easeOut(float t) {
    return 1 - powf(1 - t, 3);  // cubic
}