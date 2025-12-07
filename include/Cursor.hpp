#pragma once
#include <SFML/Graphics.hpp>

class Cursor {
public:
	sf::Vector2i _position;
	sf::Vector2f _worldPosition;
	
	sf::Time _startTouchTime;
	static const float minRadius;
	static const float maxRadius;
	static const float grownLimitSeconds;
	
	
	Cursor();
	~Cursor();
	
	void update(const sf::Event::TouchEvent &event);
	void update(const sf::Event::MouseButtonEvent &event);
	void draw();
};