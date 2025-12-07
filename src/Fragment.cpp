#include "Fragment.hpp"
#include "ColorIndex.hpp"
#include "Timer.hpp"
#include "Window.hpp"
#include "Gem.hpp"

Fragment::Fragment(int color, sf::FloatRect rect, sf::IntRect textureRect, sf::Vector2f velocity, float life) {
	_color = color;
	_rect = rect;
	_textureRect = textureRect;
	_velocity = velocity;
	_life = life;
	_createTime = currentTime;
}

Fragment::~Fragment() {
	
}

void Fragment::draw() {
    int borderWidth = 1;
    sf::RectangleShape rect(sf::Vector2f(_rect.width, _rect.height));

    sf::Color color = getColorByIndex(_color);
    float k = 1.0f - ((currentTime - _createTime).asSeconds() / _life);   // 1 to 0
    if (k < 0.0f) k = 0.0f;
    color.a = 255.0f * k;

    rect.setFillColor(color);
    rect.setOutlineThickness(borderWidth);
    rect.setOutlineColor(sf::Color(47,47,47,255.0f*k));
    rect.setPosition(_rect.left + borderWidth, _rect.top + borderWidth);

    window->draw(rect);
    
    sf::Sprite spr(getTexture(_color)->_texture);
    spr.setTextureRect(_textureRect);
    spr.setScale(Gem::size.x/256.0f, Gem::size.y/256.0f);
    spr.setPosition(_rect.left, _rect.top);
    
    window->draw(spr);
}
