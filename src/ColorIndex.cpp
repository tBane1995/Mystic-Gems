#include "ColorIndex.hpp"

sf::Color getColorByIndex(int index){
	
	sf::Color color;
	switch(index){
		case 0:
			color = sf::Color(255,47,47,255);
			break;
		case 1:
			color = sf::Color(47,255,47,255);
			break;
		case 2:
			color = sf::Color(47,47,255,255);
			break;
		case 3:
			color = sf::Color(255,255,47,255);
			break;
		case 4:
			color = sf::Color(255,47,255,255);
			break;
		case 5:
			color = sf::Color(47,255, 255,255);
			break;
		default:
			color = sf::Color(127,127,127,255);
			break;
		};
		
	return color;
	};