#include "Game.hpp"
#include "Font.hpp"
#include "Window.hpp"
#include "Textures.hpp"
#include "Cursor.hpp"
#include "Timer.hpp"
#include "SoundManager.hpp"
#include "ColorIndex.hpp"
#include <cmath>

float dist(sf::Vector2f a, sf::Vector2f b) {
    return std::sqrt((b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y));
}

Game::Game() : Layout() {
	
	// points texts
    _pointsText = std::make_unique<sf::Text>("points",basicFont, 48);
    _pointsText->setFillColor(sf::Color::White);
    _pointsText->setPosition(32, 0);
    _pointsValue = std::make_unique<sf::Text>("0",basicFont, 64);
    _pointsValue->setFillColor(sf::Color::White);
    _pointsValue->setPosition(32, basicFont.getLineSpacing(48));
    
    // combo texts
    _comboText = std::make_unique<sf::Text>("combo",basicFont, 48);
    _comboText->setFillColor(sf::Color::White);
    _comboText->setPosition(window->getSize().x/2-_comboText->getGlobalBounds().width/2, 0);
    _comboValue = std::make_unique<sf::Text>("0",basicFont, 64);
    _comboValue->setFillColor(sf::Color::White);
    _comboValue->setPosition(virtualScreenSize.x/2-_comboValue->getGlobalBounds().width/2, basicFont.getLineSpacing(48));

	// level texts
   _levelText = std::make_unique<sf::Text>("level",basicFont, 48);
    _levelText->setFillColor(sf::Color::White);
    _levelText->setPosition(window->getSize().x-_levelText->getGlobalBounds().width-32, 0);
    _levelValue = std::make_unique<sf::Text>("0",basicFont, 64);
    _levelValue->setFillColor(sf::Color::White);
    _levelValue->setPosition(virtualScreenSize.x-_levelValue->getGlobalBounds().width-32, basicFont.getLineSpacing(48));
     
     // top rect
     _topRect.left= 0;
     _topRect.top = 0;
     _topRect.width = virtualScreenSize.x;
     _topRect.height = 160;
     
    // bottom rect 
    int hgh = 96*2*2;
	_bottomRect.left = 0;
	_bottomRect.top = virtualScreenSize.y-hgh;
	_bottomRect.width = virtualScreenSize.x;
	_bottomRect.height = hgh;
    
    // gameBoardRect
    _gameBoardRect.left = 0;
    _gameBoardRect.top = _topRect.height;
    _gameBoardRect.width = virtualScreenSize.x;
    _gameBoardRect.height = virtualScreenSize.y- _topRect.height - _bottomRect.height;
    
     // next level texts
     _nextLevelText = std::make_unique<sf::Text>("level complete",basicFont, 64);
    _nextLevelText->setFillColor(sf::Color::White);
    _nextLevelText->setPosition(_gameBoardRect.width/2 - _nextLevelText->getGlobalBounds().width/2, _gameBoardRect.top+_gameBoardRect.height/2- 192 - basicFont.getLineSpacing(64)/2);
    
    _gameOverText = std::make_unique<sf::Text>("game over",basicFont, 64);
    _gameOverText->setFillColor(sf::Color::White);
    _gameOverText->setPosition(_gameBoardRect.width/2 - _gameOverText->getGlobalBounds().width/2, _gameBoardRect.top+_gameBoardRect.height/2- 192 - basicFont.getLineSpacing(64)/2);
    
    _cursor = std::make_shared<Cursor>();
    
    _state = GameStates::Start;
    
}

Game::~Game(){
	
}


void Game::selectTargetColor() {
    
    bool anyVisible = false;
    float maxY = -1e9f;

    for (auto& gem : _gems) {
        sf::FloatRect rect;
        rect.left = gem._realPosition.x - Gem::size.x/2.0f;
        rect.top = gem._realPosition.y - Gem::size.y/2.0f;
        rect.width = Gem::size.x;
        rect.height = Gem::size.y;
        
        if (!rect.intersects(_gameBoardRect))
            continue;

        anyVisible = true;
        if (rect.top > maxY) {
            maxY = rect.top;
        }
    }
    
    if (!anyVisible) {
        _targetColor = -1;
        return;
    }

    std::vector<int> colors;
	    for (auto& gem : _gems) {
	        
        sf::FloatRect rect;
        rect.left = gem._realPosition.x - Gem::size.x/2.0f;
        rect.top = gem._realPosition.y - Gem::size.y/2.0f;
        rect.width = Gem::size.x;
        rect.height = Gem::size.y;
        
        if (!rect.intersects(_gameBoardRect))
            continue;

        if (std::abs(rect.top - maxY) < 0.5f) {
            colors.push_back(gem._color);
        }
    }

    if (colors.empty()) {
        _targetColor = -1;
        return;
    }

    _targetColor = colors[std::rand() % colors.size()];
}

void Game::updatePointsText() {
	_pointsValue->setString(std::to_wstring(_points));
}

void Game::updateComboText() {
	_comboValue->setString(std::to_wstring(_combo));
	_comboValue->setPosition((virtualScreenSize.x-_comboValue->getGlobalBounds().width)/2, basicFont.getLineSpacing(48));
}

void Game::updateLevelText() {
	_levelValue->setString(std::to_wstring(_level));
	_levelValue->setPosition(virtualScreenSize.x-_levelValue->getGlobalBounds().width-32, basicFont.getLineSpacing(48));
}

bool Game::collidedGems(int x, int y) {
	for(auto& gem : _gems){
		sf::FloatRect rect;
		rect.left = gem._realPosition.x - Gem::size.x / 2.f;
		rect.top = gem._realPosition.y - Gem::size.y / 2.f;
		rect.width = Gem::size.x;
		rect.height = Gem::size.y;
        
		if(rect.intersects(sf::FloatRect(x,y,Gem::size.x, Gem::size.y))){
			return true;
		}
	}
	
	return false;
}

void Game::createGems() {
	int pairsCount = 16;
	int gemsCount = pairsCount * 2;
	
	int gemsCols = virtualScreenSize.x/Gem::size.x;
	int gemsRows = ceil(float(gemsCount)/float(gemsCols));
	gemsRows *= 5;
	
	int dx = (virtualScreenSize.x - gemsCols*Gem::size.x)/2;
	
	for(int i=0; i<gemsCount; i+=2){
		
		int color = rand()%6;
		
		int x, y;
		do{
			x = dx + (rand()%gemsCols) * Gem::size.x + Gem::size.x/2.0f;
			y = -(rand()%gemsRows+1) * Gem::size.y + Gem::size.y/2.0f;
		}while(collidedGems(x, y));
		Gem gem1(sf::Vector2f(x,y), color);
		_gems.push_back(gem1);
		
		do{
			x = dx + (rand()%gemsCols) * Gem::size.x + Gem::size.x/2.0f;
			y = -(rand()%gemsRows+1) * Gem::size.y + Gem::size.y/2.0f;
		}while(collidedGems(x, y));
		Gem gem2(sf::Vector2f(x,y), color);
		_gems.push_back(gem2);
	}
}

void Game::updateGems(){
	
	if(_gems.empty())
		return;
	
	if((currentTime-_animTime).asSeconds()> 0.05f){
		
		float dt = (currentTime-_animTime).asSeconds();
		float speedBase = 4.2f + 0.0375f*float(_level);
		float speedDelta = -float(_hittedGems/32/(_level+1))+float(_hittedGems)/160.0f;
		float speed = easeOut(speedBase+speedDelta);
		for(auto& gem : _gems){
			gem._position.y += speed * dt;

			float s = sin(gem._dt + currentTime.asSeconds() * 2.f);
        
	        gem._rotation = s * 15.f;
	        float dx = s * 10.0f;
	        float dy = abs(1.f - s) * 10.f;
			gem._realPosition = gem._position + sf::Vector2f(dx,dy);
		}
		
		if(_targetColor== -1){
			selectTargetColor();
		}
		
	}
	
}

void Game::updateFragments(){
	
	if(_fragments.empty())
			return;
			
	if((currentTime-_animTime).asSeconds()> 0.05f){
		
		float dt = (currentTime-_animTime).asSeconds();
		
		for(auto& fragment : _fragments){
			fragment._rect.left += dt*fragment._velocity.x;
			fragment._rect.top += dt*fragment._velocity.y;
		}
		
		
		
		// delete fragments out of screen or oldest
		for (int i = _fragments.size() - 1; i >= 0; i--) {
	        if ((currentTime - _fragments[i]._createTime).asSeconds() > _fragments[i]._life || 
	        !_fragments[i]._rect.intersects(_gameBoardRect)) {
	            _fragments.erase(_fragments.begin() + i);
	        }
		}
	          
		
	
	}
}

bool Game::gameOverTest(){
	for(auto& gem : _gems){
		sf::FloatRect rect;
        rect.left = gem._realPosition.x-Gem::size.x/2.0f;
        rect.top = gem._realPosition.y-Gem::size.y/2.0f;
        rect.width = Gem::size.x;
        rect.height = Gem::size.y;
		if(rect.intersects(_bottomRect)){
			return true;
		}
	}
	return false;
}

bool Game::nextLevel(){
	if(_gems.empty())
		return true;
	else
		return false;
}

void Game::updateCombo(){
	
	if(_combo>1) {
		_points += _comboPoints;
		updatePointsText();
	}
	
	_combo = 0;
	_comboPoints = 0;
	updateComboText();
}

void Game::generateFragments(Gem& gem){
	
	sf::FloatRect rect;
	rect.left = gem._realPosition.x;
	rect.top = gem._realPosition.y;
	rect.width = Gem::size.x;
	rect.height = Gem::size.y;
        
	int rows = 12;
	int cols = 12;
	for(int y=0;y<rows;y+=1) {
		for(int x=0;x<cols;x+=1) {
			sf::FloatRect fragmentRect;
			fragmentRect.left = rect.left + rect.width*x/cols - Gem::size.x/2.0f;
			fragmentRect.top = rect.top + rect.height*y/rows - Gem::size.y/2.0f;
			fragmentRect.width = rect.width/cols;
			fragmentRect.height = rect.height/rows;
			
			sf::IntRect texRect;
			texRect.left = 256*x/cols;
			texRect.top = 256*y/rows;
			texRect.width = 256/cols;
			texRect.height = 256/rows;
			
			float angle = (float)(rand() % 360) * 3.14159f / 180.f;  // losowy kierunek
			float speed = 100.f + (rand() % 100);                    // baza + losowa wartość
			
			sf::Vector2f velocity(
			    std::cos(angle) * speed,
			    std::sin(angle) * speed
			);
			
			float life = float(rand()%5+5)/10.0f;
			
			Fragment fragment( gem._color, fragmentRect, texRect, velocity, life);
			
			_fragments.push_back(fragment);
		}
	}
}

void Game::destroyAllGems(){
	for (int i = _gems.size() - 1; i >= 0; i--) {
		sf::FloatRect rect;
		rect.left = _gems[i]._realPosition.x - Gem::size.x / 2.f;
		rect.top = _gems[i]._realPosition.y - Gem::size.y / 2.f;
		rect.width = Gem::size.x;
		rect.height = Gem::size.y;
        
    	if(rect.intersects(_gameBoardRect)){
    		 if(_gems[i]._effect == GemEffect::BonusPoints){
	    		generateFragments(_gems[i]);
				_points += 1;
				_hittedGems += 1;
				_combo+=1;
				_comboPoints += _combo;
				bonus();
				_gems.erase(_gems.begin() + i);
    		}else{
	    		generateFragments(_gems[i]);
				_points += 1;
				_hittedGems += 1;
				_combo+=1;
				_comboPoints += _combo;
				_gems.erase(_gems.begin() + i);
    		}
    	}
	}
}

void Game::destroyType(int colorType) {
	for (int i = _gems.size() - 1; i >= 0; i--) {
	   	
	   sf::FloatRect rect;
		rect.left = _gems[i]._realPosition.x - Gem::size.x / 2.f;
		rect.top = _gems[i]._realPosition.y - Gem::size.y / 2.f;
		rect.width = Gem::size.x;
		rect.height = Gem::size.y;
        
    	if(rect.intersects(_gameBoardRect) && _gems[i]._color == colorType){
    		
    		if(_gems[i]._effect == GemEffect::DestroyAll){
    			destroyAllGems();
    		}else if(_gems[i]._effect == GemEffect::BonusPoints){
	    		generateFragments(_gems[i]);
				_points += 1;
				_hittedGems += 1;
				_combo+=1;
				_comboPoints += _combo;
				bonus();
				_gems.erase(_gems.begin() + i);
    		}else{
	    		generateFragments(_gems[i]);
				_points += 1;
				_hittedGems += 1;
				_combo+=1;
				_comboPoints += _combo;
				_gems.erase(_gems.begin() + i);
    		}
    	}
	}
}

void Game::bonus(){
	_points += 50;
}

void Game::destroyClickedGems() {
    
    if(!_gameBoardRect.contains(_cursor->_worldPosition.x, _cursor->_worldPosition.y))
    	return;
    	
    if(_gems.empty())
    	return;
    
    std::shared_ptr<Gem> gemClicked = nullptr;
    
    // clicked target color
    for (int i = _gems.size() - 1; gemClicked== nullptr && i >= 0; i--) {
            
            if(dist(_cursor->_worldPosition, _gems[i]._realPosition) <= Cursor::maxRadius) {
			    if(_gems[i]._color == _targetColor) {
			        gemClicked= std::make_shared<Gem>(_gems[i]);
			        _gems.erase(_gems.begin() + i);
			    }
			}
    }
        
    int d = 24;
    for (int i = _gems.size() - 1; gemClicked== nullptr && i >= 0; i--) {
        if(dist(_cursor->_worldPosition, _gems[i]._realPosition) <= Cursor::maxRadius + d){
            
			if(_gems[i]._color == _targetColor){
			    gemClicked= std::make_shared<Gem>(_gems[i]);
			    _gems.erase(_gems.begin() + i);
			}
        }
    }
    
    //other clicked (not target color)
    for (int i = _gems.size() - 1; gemClicked== nullptr && i >= 0; i--) {
            
            if(dist(_cursor->_worldPosition, _gems[i]._realPosition) <= Cursor::maxRadius) {
			    gemClicked= std::make_shared<Gem>(_gems[i]);
			}
    }
        
    d = 24;
    for (int i = _gems.size() - 1; gemClicked== nullptr && i >= 0; i--) {
        if(dist(_cursor->_worldPosition, _gems[i]._realPosition) <= Cursor::maxRadius+d){
			gemClicked = std::make_shared<Gem>(_gems[i]);
        }
    }
    
    if(gemClicked!=nullptr){
    	if(gemClicked->_color == _targetColor){
    		 switch(gemClicked->_effect){
    		 	case GemEffect::DestroyAll:
    		 		generateFragments(*gemClicked);
    		 		_points += 1;
					_hittedGems += 1;
					_combo+=1;
					_comboPoints += _combo;
    		 		destroyAllGems();
					_comboTime = currentTime;
    		 		updatePointsText();
    		 		if(nextLevel())
    		 			_state = GameStates::LevelComplete;
    		 		if(_combo>1)
    		 			_comboTexts.push_back(ComboText(_combo, gemClicked->_realPosition.x+48, gemClicked->_realPosition.y-Gem::size.y/2.0f));
    		 		updateComboText();
    		 		selectTargetColor();
    		 		soundManager->playHit();
    		 		break;
    		 		
    		 	case GemEffect::DestroyType:
    		 		generateFragments(*gemClicked);
    		 		_points += 1;
					_hittedGems += 1;
					_combo+=1;
					_comboPoints += _combo;
    		 		destroyType(gemClicked->_color);
    		 		_comboTime = currentTime;
    		 		updatePointsText();
    		 		if(nextLevel())
    		 			_state = GameStates::LevelComplete;
    		 		if(_combo>1)
    		 			_comboTexts.push_back(ComboText(_combo, gemClicked->_realPosition.x+48, gemClicked->_realPosition.y-Gem::size.y/2.0f));
    		 		updateComboText();
    		 		selectTargetColor();
    		 		soundManager->playHit();
    		 		break;
    		 	
    		 	case GemEffect::BonusPoints:
		    		generateFragments(*gemClicked);
		            			
					_points += 1;
					_hittedGems += 1;
					_combo+=1;
					_comboPoints += _combo;
					_comboTime = currentTime;
					bonus();
					updatePointsText();
					if(nextLevel())
						_state = GameStates::LevelComplete;
					if(_combo>1)
						_comboTexts.push_back(ComboText(_combo, gemClicked->_realPosition.x+48, gemClicked->_realPosition.y-Gem::size.y/2.0f));
					
					updateComboText();
					selectTargetColor();
					soundManager->playHit();
					break;
					
    		 	case GemEffect::None:
		    		generateFragments(*gemClicked);
		            			
					_points += 1;
					_hittedGems += 1;
					_combo+=1;
					_comboPoints += _combo;
					_comboTime = currentTime;
					updatePointsText();
					if(nextLevel())
						_state = GameStates::LevelComplete;
					if(_combo>1)
						_comboTexts.push_back(ComboText(_combo, gemClicked->_realPosition.x+48, gemClicked->_realPosition.y-Gem::size.y/2.0f));
					
					updateComboText();
					selectTargetColor();
					soundManager->playHit();
					break;
			};

    	}else{
    		updateCombo();
    		_comboTexts.push_back(ComboText(-1, gemClicked->_realPosition.x+48, gemClicked->_realPosition.y-Gem::size.y/2.0f));
    		soundManager->playMiss();
    	}
    	
    }else{
    	updateCombo();
    	_comboTexts.push_back(ComboText(-1, _cursor->_worldPosition.x+24, _cursor->_worldPosition.y-96));
    	soundManager->playMiss();
    }
    	
    	
}

void Game::deleteComboTexts(){
	
	if(_comboTexts.empty())
		return;
	
	for (int i = _comboTexts.size()  - 1; i >= 0; i--) {
	        if ((currentTime - _comboTexts[i]._createTime).asSeconds() > 1.0f) {
	            _comboTexts.erase(_comboTexts.begin() + i);
	        }
		}
}

void Game::handleEvents(sf::Event& event){
	
	if(event.type == sf::Event::KeyPressed)
	{
	    if(event.key.code == sf::Keyboard::Escape ||
	       event.key.code == sf::Keyboard::BackSpace)
	    {
	        // back btn
	        soundManager->stopAll();
	        layouts.pop_back();
	    }
	}
	
	if (event.type == sf::Event::Closed){
    	layouts.pop_back();
    }
	
	if (event.type == sf::Event::TouchBegan) {
		sf::Vector2i pos = sf::Vector2i(window->mapPixelToCoords(sf::Vector2i(event.touch.x, event.touch.y)));
		if(_gameBoardRect.contains(pos.x, pos.y)){
			_cursor->update(event.touch);
			if(_state == GameStates::Playing)
				destroyClickedGems();
		}
	}
	else if (event.type == sf::Event::MouseButtonPressed) {
		
		sf::Vector2i pos = sf::Vector2i(window->mapPixelToCoords(sf::Vector2i(event.mouseButton.x, event.mouseButton.y)));
		if(_gameBoardRect.contains(pos.x, pos.y)){
			_cursor->update(event.mouseButton);
			if(_state == GameStates::Playing)
				destroyClickedGems();
		}
	}
}

void Game::update() {
	switch(_state){
		
		case GameStates::Start:
		    _level = 0;
		    _hittedGems = 0;
		    _points = 0;
		    _combo = 0;
		    _comboPoints = 0;
		    
		    updatePointsText();
	    	updateComboText();
	    	updateLevelText();
	    	
	    	_comboTexts.clear();
	    	_gems.clear();
	    	_fragments.clear();
	    	
		    createGems();
		    selectTargetColor();
	    	_state = GameStates::Playing;
	    	
	    	_lastTime = currentTime;
	    	_animTime = currentTime;
	    	_comboTime = currentTime;
	    	
	    	soundManager->playGameMusic();
	    	break;
	    	
		case GameStates::Playing:
			updateGems();
			updateFragments();
			if((currentTime-_animTime).asSeconds()> 0.05f)
	    		_animTime = currentTime;
	    		
	    	if(gameOverTest())
	    		_state = GameStates::EndScreen;
	    	
	    	/* // reset combo with time
	    	if((currentTime-_comboTime).asSeconds()>1.0f){
		    	updateCombo();
	    	}*/
	    	
	    	deleteComboTexts();
	    	soundManager->update();
	    		
	    	break;
	    	
	    case GameStates::LevelComplete:
	    	_level += 1;
	    	createGems();
			updateLevelText();
			selectTargetColor();
			_state = GameStates::NextLevelText;
			_lastTime = currentTime;
			deleteComboTexts();
			break;
			
		case GameStates::NextLevelText:
			updateFragments();
			if((currentTime-_animTime).asSeconds()> 0.05f)
	    		_animTime = currentTime;
	    	/* // combo time limiting 
	    	if((currentTime-_comboTime).asSeconds()>1.0f){
		    	updateCombo();
	    	}
	    	*/
	    	
			if((currentTime-_lastTime).asSeconds()> 3.0f) {
				_state = GameStates::Playing;
				_lastTime = currentTime;
				_animTime = currentTime;
				}
				
			deleteComboTexts();
			break;
			
		case GameStates::EndScreen:
			soundManager->stopAll();
			layouts.pop_back();
			break;
	};
    
}

void Game::draw(){
	
	// game board
	
    sf::Sprite gameBoard(getTexture("tex/underwater.png")->_texture);
    gameBoard.setPosition(_gameBoardRect.left, _gameBoardRect.top);
    window->draw(gameBoard);
    
	// gameobjects
	for(auto& gem : _gems){
		gem.draw();
	}
	
	for(auto& fragment : _fragments){
		fragment.draw();
	}
	
	for(auto& combo : _comboTexts){
		combo.draw();
	}
	
	
	// top rect
	sf::Color panelColor(63,63,63,255);
	
    sf::RectangleShape tRect(sf::Vector2f(_topRect.width, _topRect.height));
    tRect.setFillColor(panelColor);
    tRect.setPosition(_topRect.left, _topRect.top);
    window->draw(tRect);
    
    sf::Sprite topBoard(getTexture("tex/topBoard.png")->_texture);
    topBoard.setPosition(_topRect.left, _topRect.top);
    window->draw(topBoard);
    
    
    window->draw(*_pointsText);
	window->draw(*_pointsValue);
	
	window->draw(*_comboText);
	window->draw(*_comboValue);
	
	window->draw(*_levelText);
	window->draw(*_levelValue);
	
	// bottom rect
    sf::RectangleShape bRect(sf::Vector2f(_bottomRect.width, _bottomRect.height));
    bRect.setFillColor(panelColor);
    bRect.setPosition(_bottomRect.left, _bottomRect.top);
    window->draw(bRect);
    
    sf::Sprite bottomBoard(getTexture("tex/bottomBoard.png")->_texture);
    bottomBoard.setPosition(_bottomRect.left, _bottomRect.top);
    window->draw(bottomBoard);
    
    // target gem
    
    std::shared_ptr<Texture> bubbleTex = getTexture("tex/bubble.png");
    std::shared_ptr<Texture> gemTex = getTexture(_targetColor);
	
	sf::Vector2f size(96.0f * 3.0f, 96.0f * 3.0f);
	
	sf::CircleShape circle(size.x/2);
	circle.setFillColor(sf::Color(47, 47, 47, 127));
	circle.setOutlineColor(sf::Color(47,47,47,255))
;
	circle.setOutlineThickness(6);
	circle.setOrigin(size.x/2, size.y/2);
	circle.setPosition(_bottomRect.left + _bottomRect.width/2, _bottomRect.top+_bottomRect.height/2 -24);
	window->draw(circle);
	
	if(bubbleTex!= nullptr){
        sf::Sprite bubble(bubbleTex->_texture);
    	sf::Vector2f scale;
        scale.x = size.x / float(bubbleTex->_texture.getSize().x);
        scale.y = size.y / float(bubbleTex->_texture.getSize().y);
        bubble.setScale(scale);
        
        bubble.setOrigin(
            bubbleTex->_texture.getSize().x / 2.f,
            bubbleTex->_texture.getSize().y / 2.f
        );
       
		bubble.setPosition(_bottomRect.left + _bottomRect.width / 2.f, _bottomRect.top+ _bottomRect.height  / 2.f - 24.0f);
		
        window->draw(bubble);
    }
	
	if(gemTex != nullptr) {
		sf::Sprite gem(gemTex->_texture);
		sf::Vector2f scale;
		scale.x =  size.x/ float(gemTex->_texture.getSize().x);
		scale.y = size.y / float(gemTex->_texture.getSize().y);
		gem.setScale(scale.x, scale.y);
		gem.setOrigin(
            gemTex->_texture.getSize().x / 2.f,
            gemTex->_texture.getSize().y / 2.f
        );
       
		gem.setPosition(_bottomRect.left + _bottomRect.width / 2.f, _bottomRect.top+ _bottomRect.height  / 2.f - 24.0f);
		window->draw(gem);
	}
    
    if(_state == GameStates::NextLevelText) {
    	if(int((currentTime-_lastTime).asSeconds()*6.0f)%2==0)
    		window->draw(*_nextLevelText);
    }
    
    if(_state == GameStates::EndScreen) {
    	window->draw(*_gameOverText);
    }
    
    _cursor->draw();
}