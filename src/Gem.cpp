#include "Gem.hpp"
#include "Window.hpp"
#include "ColorIndex.hpp"
#include "Textures.hpp"
#include "Timer.hpp"

const sf::Vector2f Gem::size = sf::Vector2f(96,96);
const int Gem::borderWidth = 8;

Gem::Gem(sf::Vector2f position, int color) {
	
	_position = position;
	_color = color;
	
	_rotation = 0;
	_realPosition = _position; // +dx, +dy
	
	int effect = rand()%200;
	if(effect > 190) _effect = GemEffect::DestroyAll;
	else if(effect > 170) _effect = GemEffect::DestroyType;
	else if(effect > 145) _effect = GemEffect::BonusPoints;
	else _effect = GemEffect::None;
	
	_dt = float(rand() % 360) * M_PI / 180.f;
	
}

Gem::~Gem() {
	
}

void Gem::setPosition(sf::Vector2f position) {
	_position = position;
}
void Gem::draw() {
    
    std::shared_ptr<Texture> bubbleTex = getTexture("tex/bubble.png");
    std::shared_ptr<Texture> gemTex = getTexture(_color);
    
    
    switch (_effect) {
      case GemEffect::DestroyAll:
      {
		    int radius = std::max(Gem::size.x/2, Gem::size.y/2);
		    sf::CircleShape circle(radius);
		
		    float t = currentTime.asSeconds() * 2.f;
		    sf::Uint8 r = sf::Uint8(127 + 127 * sin(t));
		    sf::Uint8 g = sf::Uint8(127 + 127 * sin(t + 2.f));
		    sf::Uint8 b = sf::Uint8(127 + 127 * sin(t + 4.f));
		
		    circle.setFillColor(sf::Color(r, g, b, 127));
		    circle.setOutlineThickness(3);
		    circle.setOutlineColor(sf::Color(r, g, b, 255));
		    circle.setOrigin(radius, radius);
		    circle.setPosition(_realPosition);
		    window->draw(circle);
		}
		break;
			
		case GemEffect::DestroyType:
		{
		    int radius = std::max(Gem::size.x/2, Gem::size.y/2);
		    sf::CircleShape circle(radius);
		
		    float t = currentTime.asSeconds() * 2.f;
		    sf::Uint8 r = sf::Uint8(191 + 64 * sin(t));
		    sf::Uint8 g = sf::Uint8(47);
		    sf::Uint8 b = sf::Uint8(47);
		
		    circle.setFillColor(sf::Color(r, g, b, 127));
		    circle.setOutlineThickness(3);
		    circle.setOutlineColor(sf::Color(r, g, b, 255));
		    circle.setOrigin(radius, radius);
		    circle.setPosition(_realPosition);
		    window->draw(circle);
		}
		break;
		
	case GemEffect::BonusPoints:
		{
		    int radius = std::max(Gem::size.x/2, Gem::size.y/2);
		    sf::CircleShape circle(radius);
		
		    float t = currentTime.asSeconds() * 2.f;
		    sf::Uint8 r = sf::Uint8(191 + 64 * sin(t));
		    sf::Uint8 g = sf::Uint8(127 + 64 * sin(t));
		    sf::Uint8 b = sf::Uint8(47);
		
		    circle.setFillColor(sf::Color(r, g, b, 127));
		    circle.setOutlineThickness(3);
		    circle.setOutlineColor(sf::Color(r, g, b, 255));
		    circle.setOrigin(radius, radius);
		    circle.setPosition(_realPosition);
		    window->draw(circle);
		}
		break;
		
		default:
		{
		    int radius = std::max(Gem::size.x/2, Gem::size.y/2);
		    sf::CircleShape circle(radius);
		
		    float t = currentTime.asSeconds() * 2.f;
		    sf::Uint8 r = sf::Uint8(47);
		    sf::Uint8 g = sf::Uint8(47);
		    sf::Uint8 b = sf::Uint8(47);
		
		    circle.setFillColor(sf::Color(r, g, b, 127));
		    circle.setOutlineThickness(3);
		    circle.setOutlineColor(sf::Color(r, g, b, 255));
		    circle.setOrigin(radius, radius);
		    circle.setPosition(_realPosition);
		    window->draw(circle);
		}
		break;
	      
	};
    
    
    if(bubbleTex!= nullptr){
        sf::Sprite bubble(bubbleTex->_texture);
    	sf::Vector2f scale;
        scale.x = float(size.x) / float(bubbleTex->_texture.getSize().x);
        scale.y = float(size.y) / float(bubbleTex->_texture.getSize().y);
        bubble.setScale(scale);
        
        bubble.setOrigin(
            bubbleTex->_texture.getSize().x / 2.f,
            bubbleTex->_texture.getSize().y / 2.f
        );
        
        bubble.setRotation(_rotation);
		bubble.setPosition(_realPosition);
		
        window->draw(bubble);
    }
        
    if(gemTex != nullptr) {
        sf::Sprite spr(gemTex->_texture);

        sf::Vector2f scale;
        scale.x = float(size.x) / float(gemTex->_texture.getSize().x);
        scale.y = float(size.y) / float(gemTex->_texture.getSize().y);
        spr.setScale(scale);

        spr.setOrigin(
            gemTex->_texture.getSize().x / 2.f,
            gemTex->_texture.getSize().y / 2.f
        );
        
        spr.setRotation(_rotation);
		spr.setPosition(_realPosition);
        
        window->draw(spr);
    }
}

