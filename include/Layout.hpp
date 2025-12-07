#pragma once
#include <SFML/Graphics.hpp>

class Layout {
public:

	Layout();
	~Layout();
	
	virtual void handleEvents(sf::Event& event);
	virtual void update();
	virtual void draw();
};

extern std::vector<std::shared_ptr<Layout>> layouts;