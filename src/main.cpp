#include <iostream>
#include <sstream>
#include <iomanip>
#include <filesystem>
#include <SFML/Graphics.hpp>
#include "Window.hpp"
#include "Timer.hpp"
#include "Font.hpp"
#include "MainMenu.hpp"
#include "Game.hpp"

int main() {
		
    //std::cout << "Current path: " << std::filesystem::current_path() << "\n";
     
        sf::View view;

		window = std::make_shared<sf::RenderWindow>(sf::VideoMode::getDesktopMode()/*(virtualScreenSize.x, virtualScreenSize.y)*/, "Mystic Gems");
		
		std::cout<<window->getSize().x;
		
		// virtual view - for scaling screen
		view.setSize(virtualScreenSize);
		view.setCenter(virtualScreenSize.x / 2.f, virtualScreenSize.y / 2.f);
		setScreenView(view);
		
    	basicFont.loadFromFile("ScienceGothic-VariableFont_CTRS,slnt,wdth,wght.ttf");
    	
    	loadTextures();
    	
        soundManager = std::make_shared<SoundManager>();
        
        layouts.push_back(std::make_shared<MainMenu>());
        
        sf::Clock FPSClock;
        sf::Clock FPSClockUpdate;        // clock for show FPS in main loop of Editor
		
		ElementGUI_hovered = nullptr;
		
        while (window->isOpen()) {

                float FPS = 1.0f / FPSClock.restart().asSeconds();
                if (FPSClockUpdate.getElapsedTime().asSeconds() > 0.5f) {

                        std::ostringstream ss;
                        ss << std::fixed << std::setprecision(2) << FPS << " FPS";
                        window->setTitle("Single Block - " + ss.str());
                        FPSClockUpdate.restart();
                }
                
                prevTime = currentTime;
                currentTime = mainClock.getElapsedTime();
                
                
                // Handle screen resizes
                sf::Event event;
                while (window->pollEvent(event)) {
                 
                    if (event.type == sf::Event::Closed){
                        window->close();
                    }
                    
                    if (event.type == sf::Event::Resized) {
					    setScreenView(view);
					    window->setView(view);
					}
					
					if(layouts.size()<=1) {
						if(event.type == sf::Event::KeyPressed) {
						    if(event.key.code == sf::Keyboard::Escape ||
						       event.key.code == sf::Keyboard::BackSpace)
						    {
						        // back btn
						        window->close();
						    }
						}
					}
					
					if(event.type == sf::Event::TouchEnded)
						ElementGUI_hovered = nullptr;
					
					if( event.type == sf::Event::TouchBegan || event.type == sf::Event::TouchMoved || event.type == sf::Event::TouchEnded || event.type == sf::Event::KeyPressed ) {
						layouts.back()->handleEvents(event);
					}
					
                }

                // update
                layouts.back()->update();
                
                // render
                window->clear();
                layouts.back()->draw();
                window->display();
        }
        return 0;
}