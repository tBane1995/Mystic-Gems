#pragma once
#include "Layout.hpp"
#include "Button.hpp"

enum class State{ None, Touch };

class Instructions : public Layout {
public:

	std::shared_ptr<Button> _back;
	int _textSize;
	std::unique_ptr<sf::Text> _text;
	
	int panelWdt;
    int panelHgh;
    int padding;
	int innerW;
    int innerH;
    
	State _state;
	int _dy;
	sf::Vector2f _startTouch;
	sf::Vector2f _endTouch;
	
	Instructions();
	~Instructions();
	
	void handleEvents(sf::Event& event);
	void update();
	void draw();
};