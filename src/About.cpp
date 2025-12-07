#include "About.hpp"
#include "Window.hpp"
#include "Font.hpp"

About::About() {
	
	panelWdt = 656;
	panelHgh = 1356;
	padding = 32;
	    
	_back = std::make_shared<Button>(
		getTexture("tex/menuButtonNormal.png"),
		getTexture("tex/menuButtonHover.png"),
		getTexture("tex/menuButtonPressed.png"),
		32,
		32,
		96,
		96
	);
	
	_back->_onclick_func = [](){layouts.pop_back();};	
		int textSize = 32;
		std::wstring text =
			L"Mystic Gems v1.0\n"
			L"\n"
		    L"Game programmed by tBane\n"
		    L"\n"
		    L"Graphics:\n"
		    L"Gems - Indigo Tiger\n"
		    L"Bubbles - Moonflower Carnivore\n"
		    L"Underwater - freedesignfile\n"
		    L"Panels - Sebastian Jaksan\n"
		    L"\n"
		    L"Music & Sound Effects:\n"
		    L"Game - xDeviruchi\n"
		    L"Menu - trapskull3103\n"
		    L"Miss&Hit - leohpaz \n";
		    
		
    
		_text = std::make_unique<sf::Text>(text, basicFont, textSize);
		
		int x = 32 + padding;
		int y = 96+2*32 + padding;
		_text->setPosition(x, y);
}

About::~About() { }
	
void About::handleEvents(sf::Event& event) {
	
	if(event.type == sf::Event::KeyPressed)
	{
	    if(event.key.code == sf::Keyboard::Escape ||
	       event.key.code == sf::Keyboard::BackSpace)
	    {
	        // back btn
	        layouts.pop_back();
	    }
	}
	
	_back->handleEvents(event);
}

void About::update() {
	_back->update();
}

void About::draw() {
	
	sf::Sprite bg(getTexture("tex/mainBoard.png")->_texture);
	window->draw(bg);
	
	int panelWdt = 656;
	int panelHgh = 1420;

	sf::Sprite panel(getTexture("tex/panel.png")->_texture);
	panel.setTextureRect(sf::IntRect(0,0,panelWdt,panelHgh));
	panel.setPosition(32, 96 + 2*32);
	window->draw(panel);
	
	window->draw(*_text);
	
	_back->draw();
}