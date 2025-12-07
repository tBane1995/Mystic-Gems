#include "Cursor.hpp"
#include "Window.hpp"
#include "Timer.hpp"
#include "windows.h"


const float Cursor::minRadius = 32;
const float Cursor::maxRadius = 80;
const float Cursor::grownLimitSeconds = 0.35f;
	
	
	
Cursor::Cursor() {
	_position = sf:: Vector2i(0,0);
	_worldPosition = sf:: Vector2f(0,0);
}

Cursor::~Cursor() {
	
}

void Cursor::update(const sf::Event::TouchEvent &event) {
	
	_position = sf::Vector2i(event.x, event.y);
	_worldPosition = window->mapPixelToCoords(_position);
	
	_startTouchTime = currentTime;
}

void Cursor::update(const sf::Event::MouseButtonEvent &event) {
	
	_position = sf::Vector2i(event.x, event.y);
	_worldPosition = window->mapPixelToCoords(_position, window->getView());
	
	_startTouchTime = currentTime;
}

void Cursor::draw() {
	
	if((currentTime-_startTouchTime).asSeconds() > grownLimitSeconds)
		return;
	
	float t = (currentTime - _startTouchTime).asSeconds()/grownLimitSeconds;
	float radius = easeOut(t)*(maxRadius-minRadius) + minRadius;
	
	
	sf::CircleShape circle(radius);
	circle.setFillColor(sf::Color(255, 47,47,127));
	circle.setOutlineThickness(2.0f);
	circle.setOutlineColor(sf::Color(255, 47,47,255));
	circle.setOrigin(radius, radius);
	circle.setPosition(_worldPosition);
	window->draw(circle);
}
