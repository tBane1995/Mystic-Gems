#pragma once
#include <SFML/Graphics.hpp>

class Texture {
public:
	std::string _pathfile;
	sf::Texture _texture;
	
	Texture(std::string pathfile);
	~Texture();
};

extern std::vector<std::shared_ptr<Texture>> textures;

void loadTexture(std::string pathfile);
void loadTextures();
std::shared_ptr<Texture> getTexture(std::string pathfile);
std::shared_ptr<Texture> getTexture(int colorIndex);