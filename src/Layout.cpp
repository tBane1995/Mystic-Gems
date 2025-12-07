#include "Layout.hpp"

Layout::Layout() { }
Layout::~Layout() { }
	
void Layout::handleEvents(sf::Event& event) { }
void Layout::update() { }
void Layout::draw() { }

std::vector<std::shared_ptr<Layout>> layouts;