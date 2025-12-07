#include "Window.hpp"

std::shared_ptr<sf::RenderWindow> window;

sf::Vector2f virtualScreenSize = sf::Vector2f(720,1612);

void setScreenView(sf::View& view)
{
    float windowRatio = float(window->getSize().x) / float(window->getSize().y);
    float viewRatio   = float(view.getSize().x) / float(view.getSize().y);

    float sizeX = 1.f;
    float sizeY = 1.f;
    float posX  = 0.f;
    float posY  = 0.f;

    if (windowRatio > viewRatio) {
        sizeX = viewRatio / windowRatio;
        posX  = (1.f - sizeX) / 2.0f;
    }
    // wyższe niż widok → paski poziome
    else {
        sizeY = windowRatio / viewRatio;
        posY  = (1.f - sizeY) / 2.0f;
    }

    view.setViewport(sf::FloatRect(posX, posY, sizeX, sizeY));
    
    window->setView(view);
}