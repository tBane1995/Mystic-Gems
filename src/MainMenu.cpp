#include "MainMenu.hpp"
#include "Window.hpp"
#include "Game.hpp"
#include "Instructions.hpp"
#include "About.hpp"
#include "Timer.hpp"

MainMenu::MainMenu() {
	
	int btnWdt = 512;
	int btnHgh = 96;
	int padding = 32;
	
	std::vector<std::wstring> btnTexts;
	btnTexts.push_back(L"new game");
	btnTexts.push_back(L"highscores");
	btnTexts.push_back(L"instructions");
	btnTexts.push_back(L"settings");
	btnTexts.push_back(L"about");
	btnTexts.push_back(L"exit");
	
	for( int i=0;i<btnTexts.size();i+=1) {
		std::shared_ptr<ButtonWithText> btn = std::make_shared<ButtonWithText>(
			btnTexts[i],
			getTexture("tex/menuButtonNormal.png"),
			getTexture("tex/menuButtonHover.png"),
			getTexture("tex/menuButtonPressed.png"),
			(virtualScreenSize.x-btnWdt)/2,
			(virtualScreenSize.y-(btnTexts.size()*btnHgh + (btnTexts.size()-1)*padding))/2 + i*btnHgh + i*padding, 
			btnWdt,
			btnHgh
		);
		
		_buttons.push_back(btn);
	}
	
	_buttons[0]->_onclick_func = [](){
		soundManager->stopAll();
		layouts.push_back(std::make_shared<Game>());
		};
	_buttons[2]->_onclick_func = [](){
		layouts.push_back(std::make_shared<Instructions>());
		};
	_buttons[4]->_onclick_func = [](){
		layouts.push_back(std::make_shared<About>());
		};
	_buttons[5]->_onclick_func = [](){window->close(); };
}

MainMenu::~MainMenu() {
	
}

void MainMenu::handleEvents(sf::Event& event) {
	for(auto& btn : _buttons)
		btn->handleEvents(event);
}

void MainMenu::update() {
	
	soundManager->playMenuMusic();
	
	for(auto& btn : _buttons)
		btn->update();
}

void MainMenu::draw() {
	
	sf::Sprite bg(getTexture("tex/mainBoard.png")->_texture);
	std::cout<<bg.getTexture()->getSize().x<<", "<<bg.getTexture()->getSize().y;
	window->draw(bg);
	
	for(auto& btn : _buttons)
		btn->draw();
}


