#pragma once
#include <SFML/Graphics.hpp>


extern sf::Vector2f virtualScreenSize;

void setScreenView(sf::View& view);

extern std::shared_ptr<sf::RenderWindow> window;