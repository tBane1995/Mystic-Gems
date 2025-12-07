#pragma once
#include <SFML/Graphics.hpp>

class ComboText{
public:
	sf::Time _createTime;
	std::shared_ptr<sf::Text> _text;
	
	ComboText(int value, int x, int y);
	~ComboText();
	void draw();
};