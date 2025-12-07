#include "ComboText.hpp"
#include <iostream>
#include "Font.hpp"
#include "Timer.hpp"
#include "Window.hpp"

ComboText::ComboText(int value, int x, int y) {
	std::wstring t = (value==-1)? L"miss" : L"x"+std::to_wstring(value);
	_text = std::make_shared<sf::Text>(t, basicFont, 32);
    _text->setFillColor((value==-1)?sf::Color::Red : sf::Color::White);
    _text->setPosition(x,y);
    
    _createTime = currentTime;
}

ComboText::~ComboText() {
	
}

void ComboText::draw(){
	window->draw(*_text);
}