#pragma once
#include <SFML/Graphics.hpp>

extern sf::Clock mainClock;        // main clock

extern sf::Time prevTime;
extern sf::Time currentTime;

float smooth(float t);
float easeOut(float t);