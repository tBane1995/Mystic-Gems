#pragma once
#include <SFML/Graphics.hpp>

enum class GemEffect { None, DestroyType, DestroyAll, BonusPoints };

class Gem {
public:
	static const sf::Vector2f size;
	static const int borderWidth;
	GemEffect _effect;
	
	int _color;
	float _dt;
	
	
	sf::Vector2f _position;
	sf::Vector2f _realPosition;
	float _rotation;
	
	Gem(sf::Vector2f position, int color);
	~Gem();
	
	void setPosition(sf::Vector2f position);
	void draw();
};