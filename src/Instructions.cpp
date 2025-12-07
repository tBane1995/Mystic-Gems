#include "Instructions.hpp"
#include "Window.hpp"
#include "Font.hpp"
#include<iostream>

Instructions::Instructions() {
	
	panelWdt = 656;
    panelHgh = 1356;
    padding = 32;
    
    innerW = panelWdt - 2 * padding;
    innerH = panelHgh - 2 * padding;
    
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
	_textSize = 32;
	std::wstring text =
	    L"Mystic Gems - Instructions\n"
	    L"\n"
	    L"Your goal is to stop the gems\nfrom reaching the bottom\nof the screen.\n"
	    L"\n"
	    L"- Gems keep falling from the top.\n"
	    L"- Click on the gems to destroy\nthem and gain points.\n"
	    L"- If any gem reaches the bottom\nof the screen,\nyou lose.\n"
	    L"- Some gems are special and\ngive bonuses, for example:\n"
	    L"  * Destroying all visible gems\nof a specific type.\n"
	    L"  * Additional score points.\n"
	    L"\n"
	    L"Combos:\n"
	    L"- Destroy gems quickly,\none after another, to build\na combo.\n"
	    L"- Higher combo chains grant\nextra bonus points.\n"
	    L"\n"
	    L"React quickly, aim carefully,\nand try to get as many\npoints as possible!";
		
	_text = std::make_unique<sf::Text>(text, basicFont, _textSize);
	
	_dy = 0;
	
	_state = State::None;
	
	
}

Instructions::~Instructions() { }
	
void Instructions::handleEvents(sf::Event& event) {
	_back->handleEvents(event);
	
	if(event.type == sf::Event::KeyPressed)
	{
	    if(event.key.code == sf::Keyboard::Escape ||
	       event.key.code == sf::Keyboard::BackSpace)
	    {
	        // back btn
	        layouts.pop_back();
	    }
	}
	
	if(ElementGUI_pressed == nullptr){
		if(event.type == sf::Event::TouchBegan) {
			_startTouch.x = event.touch.x;
			_startTouch.y = event.touch.y;
			_state = State::Touch;
		}
	}
	
	if(_state == State::Touch){
		_endTouch.x = event.touch.x;
		_endTouch.y = event.touch.y;
	}
	
	if(event.type == sf::Event::TouchEnded) {
	    float textHeight = _text->getGlobalBounds().height;
  	  int maxDy = std::max(0, int(textHeight - innerH + padding));
	
	    _dy += _startTouch.y - _endTouch.y;
	    _dy = std::clamp(_dy, 0, maxDy);
	
	    _startTouch = _endTouch;
	    _state = State::None;
	}
}

void Instructions::update() {
	_back->update();
}

void Instructions::draw() {
    sf::Sprite bg(getTexture("tex/mainBoard.png")->_texture);
    window->draw(bg);

    

    sf::Sprite panel(getTexture("tex/panel.png")->_texture);
    panel.setTextureRect(sf::IntRect(0, 0, panelWdt, panelHgh));
    panel.setPosition(32, 96 + 2 * padding);
    window->draw(panel);

    sf::RenderTexture rtex;
    rtex.create(innerW, innerH);
    rtex.clear(sf::Color::Transparent);

    int drag = (_state == State::Touch) ? int(_startTouch.y - _endTouch.y) : 0;

    float textHeight = _text->getGlobalBounds().height;
    
    int maxDy = std::max(0, int(textHeight - innerH + padding));
    int dy = std::clamp(_dy + drag, 0, maxDy);

    // Tekst w środku obszaru, BEZ paddingu – padding zrobimy pozycją sprite'a
    _text->setPosition(0.f, -dy);

    rtex.draw(*_text);
    rtex.display();

    sf::Sprite textSprite(rtex.getTexture());

    // TERAZ dodajemy padding względem panelu
    textSprite.setPosition(
        panel.getPosition().x + padding,
        panel.getPosition().y + padding
    );

    window->draw(textSprite);

    _back->draw();
}