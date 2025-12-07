#pragma once
#include <SFML/Graphics.hpp>
#include "Textures.hpp"

class Fragment {
public:
	int _color;
	sf::FloatRect _rect;
	sf::IntRect _textureRect;
	sf::Vector2f _velocity;
	float _life;
	sf::Time _createTime;
	
	Fragment(int color, sf::FloatRect rect, sf::IntRect textureRect, sf::Vector2f velocity, float life);
	~Fragment();
	
	void draw();
};