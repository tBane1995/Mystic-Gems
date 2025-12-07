#pragma once
#include "Layout.hpp"
#include "Button.hpp"

class About : public Layout {
public:

	int panelWdt;
	int panelHgh;
	int padding;

	std::shared_ptr<Button> _back;
	std::unique_ptr<sf::Text> _text;
	About();
	~About();
	
	void handleEvents(sf::Event& event);
	void update();
	void draw();
};