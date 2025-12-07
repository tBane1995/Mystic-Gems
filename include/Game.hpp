#pragma once
#include <iostream>
#include "Layout.hpp"
#include "SoundManager.hpp"
#include "ComboText.hpp"
#include "Fragment.hpp"
#include "Gem.hpp"
#include "Cursor.hpp"

#include <cmath>

float dist(sf::Vector2f a, sf::Vector2f b);

enum class GameStates { Start, Playing, LevelComplete, NextLevelText, EndScreen };

class Game : public Layout {
public:
	std::vector<ComboText> _comboTexts;
	std::vector<Fragment> _fragments;
	std::vector<Gem> _gems;
	sf::Time _animTime;
	int _targetColor;
	int _points;
	int _hittedGems;
	int _combo;
	int _comboLevel;
	int _comboPoints;
	sf::Time _comboTime;
	
	std::shared_ptr<Cursor> _cursor;
	
	std::unique_ptr<sf::Text> _pointsText;
	std::unique_ptr<sf::Text> _pointsValue;
	std::unique_ptr<sf::Text> _comboText;
	std::unique_ptr<sf::Text> _comboValue;
	std::unique_ptr<sf::Text> _levelText;
	std::unique_ptr<sf::Text> _levelValue;
	
	sf::FloatRect _topRect;
	sf::FloatRect _bottomRect;
	sf::FloatRect _gameBoardRect;
	
	std::unique_ptr<sf::Text> _nextLevelText;
	std::unique_ptr<sf::Text> _gameOverText;
	
	GameStates _state;
	sf::Time _lastTime;
	int _level;
	
	Game();
	~Game();
	
	void selectTargetColor();
	void updatePointsText();
	void updateComboText();
	void updateLevelText();
	bool collidedGems(int x, int y);
	void createGems();
	void updateGems();
	void updateFragments();
	bool gameOverTest();
	bool nextLevel();
	void updateCombo();
	void generateFragments(Gem& gem);
	void destroyAllGems();
	void destroyType(int colorType);
	void bonus();
	void destroyClickedGems();
	void deleteComboTexts();
	
	void handleEvents(sf::Event& event);
	void update();
	void draw();
};