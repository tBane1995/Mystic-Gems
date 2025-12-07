#include "Textures.hpp"

Texture::Texture(std::string pathfile) {
	
	_pathfile = pathfile;
	
	_texture = sf::Texture();
	_texture.loadFromFile(_pathfile);
	
}

Texture::~Texture(){
	
}

std::vector<std::shared_ptr<Texture>> textures;

void loadTexture(std::string pathfile) {
	textures.push_back(std::make_shared<Texture>(pathfile));
	
}

void loadTextures() {
	textures.clear();
	
	loadTexture("tex/topBoard.png");
	loadTexture("tex/bottomBoard.png");
	loadTexture("tex/mainBoard.png");
	loadTexture("tex/panel.png");
	
	loadTexture("tex/menuButtonNormal.png");
	loadTexture("tex/menuButtonHover.png");
	loadTexture("tex/menuButtonPressed.png");
	
	loadTexture("tex/underwater.png");
	
	loadTexture("tex/bubble.png");
	loadTexture("tex/red.png");
	loadTexture("tex/green.png");
	loadTexture("tex/blue.png");
	loadTexture("tex/cyan.png");
	loadTexture("tex/magenta.png");
	loadTexture("tex/yellow.png");
	
}

std::shared_ptr<Texture> getTexture(std::string pathfile){
	for(auto& tex : textures)
		if(tex->_pathfile == pathfile)
			return tex;
			
	return nullptr;
}

std::shared_ptr<Texture> getTexture(int colorIndex) {
	
	std::shared_ptr<Texture> texture;
	
	switch(colorIndex){
		case 0:
			texture = getTexture("tex/red.png");
			break;
		case 1:
			texture = getTexture("tex/green.png");
			break;
		case 2:
			texture = getTexture("tex/blue.png");
			break;
		case 3:
			texture = getTexture("tex/yellow.png");;
			break;
		case 4:
			texture = getTexture("tex/magenta.png");
			break;
		case 5:
			texture = getTexture("tex/cyan.png");
			break;
		default:
			texture = nullptr;
			break;
		};
		
	return texture;

}