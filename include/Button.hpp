#pragma once
#include <SFML/Graphics.hpp>
#include <iostream>
#include "ElementGUI.hpp"
#include "Textures.hpp"

enum class ButtonState { Idle, Hover, Pressed };

class Button : public ElementGUI {
public:
	sf::IntRect _rect;
	std::shared_ptr<Texture> _normalTexture;
	std::shared_ptr<Texture> _hoverTexture;
	std::shared_ptr<Texture> _pressedTexture;
	
	ButtonState _state;
	sf::Time _clickTime;
	std::function<void()> _onclick_func;
	        
	Button(std::shared_ptr<Texture> normalTexture,std::shared_ptr<Texture> hoverTexture, std::shared_ptr<Texture> pressedTexture, int x, int y, int width, int height);
	~Button();
	
	void unclick();
	void hover();
	void click();
	
	void handleEvents(sf::Event& event);
	void update();
	void draw();
};

class ButtonWithText : public Button {
public:
	std::unique_ptr<sf::Text> _text;
	
	ButtonWithText(std::wstring text, std::shared_ptr<Texture> normalTexture,std::shared_ptr<Texture> hoverTexture, std::shared_ptr<Texture> pressedTexture, int x, int y, int width, int height);
	~ButtonWithText();
	
	void handleEvents(sf::Event& event);
	void update();
	void draw();
};