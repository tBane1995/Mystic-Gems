#pragma once
#include <SFML/Audio.hpp>

class SoundManager {
public:
    SoundManager();
    ~SoundManager();
    
    // music
    sf::Music _menuMusic;
    sf::Music _gameMusic;
	
	// effects
    sf::SoundBuffer _hitBuffer;
    sf::SoundBuffer _missBuffer;
    
    std::vector<std::shared_ptr<sf::Sound>> _playingSounds;

	void stopAll();
	void playMenuMusic();
	void playGameMusic();
    void playEffect(const sf::SoundBuffer& buffer, float volume);
    void playHit();
    void playMiss();

    void update(); // clearing ended sounds
};

extern std::shared_ptr<SoundManager> soundManager;