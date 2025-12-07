#include "SoundManager.hpp"
#include <iostream>

SoundManager::SoundManager() {
    // menu music
    if (!_menuMusic.openFromFile("msc/Kesha - your love is my drug  8 bit  (Slowed + bass boosted).ogg")) {
        std::cout << "cant load game music\n";
    } else {
        _menuMusic.setLoop(true);
        _menuMusic.setVolume(100.f);
    }
    
    // game music
    if (!_gameMusic.openFromFile("msc/xDeviruchi - Title Theme.ogg")) {
        std::cout << "cant load game music\n";
    } else {
        _gameMusic.setLoop(true);
        _gameMusic.setVolume(100.f);
    }

	

    // hit effect
    if (!_hitBuffer.loadFromFile("msc/56_Attack_03.ogg")) {
        std::cout << "cant load hit sound effect\n";
    }

    // miss effect
    if (!_missBuffer.loadFromFile("msc/033_Denied_03.ogg")) {
        std::cout << "cant load miss sound effect\n";
    }
}

SoundManager::~SoundManager() {
    stopAll();
}

void SoundManager::stopAll() {
    for (auto& s : _playingSounds) {
        if (s) s->stop();
    }
    _playingSounds.clear();
    
    _menuMusic.stop();
    _gameMusic.stop();
}

void SoundManager::playMenuMusic() {
    if (_menuMusic.getStatus() != sf::Music::Playing) {
        _menuMusic.play();
    }
}

void SoundManager::playGameMusic() {
    if (_gameMusic.getStatus() != sf::Music::Playing) {
        _gameMusic.play();
    }
}

void SoundManager::playEffect(const sf::SoundBuffer& buffer, float volume) {
    if (buffer.getSampleCount() == 0) {
        return;
    }

    auto snd = std::make_shared<sf::Sound>();
    snd->setBuffer(buffer);
    snd->setVolume(volume);
    snd->setLoop(false);
    snd->play();

    _playingSounds.push_back(snd);
}

void SoundManager::playHit() {
    playEffect(_hitBuffer, 100.f);
}

void SoundManager::playMiss() {
    playEffect(_missBuffer, 100.f);
}

void SoundManager::update() {
    // usuń skończone dźwięki
    for (int i = _playingSounds.size() - 1; i >= 0; --i) {
        auto& s = _playingSounds[i];
        if (!s || s->getStatus() == sf::Sound::Stopped) {
            _playingSounds.erase(_playingSounds.begin() + i);
        }
    }
}

std::shared_ptr<SoundManager> soundManager;