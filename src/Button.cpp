#include "Button.hpp"
#include "Font.hpp"
#include "Window.hpp"
#include "Timer.hpp"

Button::Button(std::shared_ptr<Texture> normalTexture,std::shared_ptr<Texture> hoverTexture, std::shared_ptr<Texture> pressedTexture, int x, int y, int width, int height) : ElementGUI() {
	
	_rect.left = x;
	_rect.top = y;
	_rect.width = width;
	_rect.height = height;
	
	_normalTexture = normalTexture;
	_hoverTexture = hoverTexture;
	_pressedTexture = pressedTexture;
	
	_state = ButtonState::Idle;
	
	_onclick_func =  { };
}

Button::~Button() { }


void Button::unclick() {
        _state = ButtonState::Idle;
}

void Button::hover() {
        _state = ButtonState::Hover;
}

void Button::click() {
        _state = ButtonState::Pressed;
        _clickTime = currentTime;
}

void Button::handleEvents(sf::Event& event) {
	sf::Vector2f touch = window->mapPixelToCoords(sf::Vector2i(event.touch.x, event.touch.y));
	if(_rect.contains(touch.x, touch.y)) {
		if(event.type == sf::Event::TouchBegan) {
			ElementGUI_pressed = this->shared_from_this();
		}
		else if(event.type == sf::Event::TouchEnded) {
			if (ElementGUI_pressed.get() == this) {
            	click();
				return;
			}  
		}
	}
	
	if(_state!=ButtonState::Pressed) {
		if(event.type == sf::Event::TouchMoved || event.type == sf::Event::TouchBegan){
			
			if(_rect.contains(touch.x, touch.y)){
				ElementGUI_hovered = this->shared_from_this();
			}else {
					
				if(ElementGUI_hovered.get()== this){
					ElementGUI_hovered = nullptr;
				}
			}
		}
	}
		
		
}

void Button::update() {
	        if (_state == ButtonState::Pressed) {
                if ((currentTime - _clickTime).asSeconds() > 0.05f) {
                        if (_onclick_func) {
                                _onclick_func();
                        }
                        
                        if(ElementGUI_pressed.get() == this)
                        	ElementGUI_pressed = nullptr;
                        unclick();
                }
        }
        else if (ElementGUI_hovered.get() == this) {
                hover();
        }
        else
                unclick();
}

void Button::draw() {
	
	sf::Texture* tex = nullptr;
	switch (_state) {
	    case ButtonState::Idle:
	        tex = &_normalTexture->_texture;
	        break;
	    case ButtonState::Hover:
	        tex = &_hoverTexture->_texture;
	        break;
	    case ButtonState::Pressed:
	        tex = &_pressedTexture->_texture;
	        break;
	}

	sf::Sprite spr(*tex);
	spr.setTextureRect(sf::IntRect(0, 0, _rect.width, _rect.height));
	spr.setPosition(_rect.left, _rect.top);
	window->draw(spr);
}

ButtonWithText::ButtonWithText(std::wstring text, std::shared_ptr<Texture> normalTexture,std::shared_ptr<Texture> hoverTexture, std::shared_ptr<Texture> pressedTexture, int x, int y, int width, int height) : Button(normalTexture, hoverTexture, pressedTexture, x, y, width, height) {
	
	int textSize = 40;
	
	_text = std::make_unique<sf::Text>(text, basicFont, textSize);
	float xx = _rect.left + (_rect.width - _text->getGlobalBounds().width)/2.f;
	float yy = _rect.top + (_rect.height - basicFont.getLineSpacing(textSize))/2.f;
	_text->setPosition(xx, yy);
}

ButtonWithText::~ButtonWithText() { }

void ButtonWithText::handleEvents(sf::Event& event) {
	Button::handleEvents(event);
}

void ButtonWithText::update() {
	Button::update();
}

void ButtonWithText::draw() {
	Button::draw();
	window->draw(*_text);
}