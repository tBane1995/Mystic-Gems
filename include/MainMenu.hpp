#pragma once
#include "Layout.hpp"
#include "Button.hpp"

class MainMenu : public Layout {
public:
	
	std::vector<std::shared_ptr<ButtonWithText>> _buttons;
	
	MainMenu();
	~MainMenu();
	
	void handleEvents(sf::Event& event);
	void update();
	void draw();
};

