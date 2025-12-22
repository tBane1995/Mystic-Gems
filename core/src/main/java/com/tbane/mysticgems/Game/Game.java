package com.tbane.mysticgems.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.tbane.mysticgems.GUI.ButtonStates;
import com.tbane.mysticgems.GUI.ButtonWithText;
import com.tbane.mysticgems.GUI.Font;
import com.tbane.mysticgems.GUI.TextInput;
import com.tbane.mysticgems.MyInput.MyInput;
import com.tbane.mysticgems.Renderer;
import com.tbane.mysticgems.SoundManager;
import com.tbane.mysticgems.Textures.Tex;
import com.tbane.mysticgems.Textures.TexturesManager;
import com.tbane.mysticgems.Time;
import com.tbane.mysticgems.Views.Layout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tbane.mysticgems.Views.LayoutsManager;

import java.util.ArrayList;
import java.util.List;

public class Game extends Layout {


    private List<Gem> _gems = new ArrayList<Gem>();;
    private List<ComboText> _comboTexts = new ArrayList<ComboText>();
    private List<Fragment> _fragments = new ArrayList<Fragment>();

    /// //////////////////////////////////////////
    private float _animTime = 0;
    private int _targetColor = -1;
    private int _points = 0;
    private int _hittedGems = 0;
    private int _combo = 0;
    private int _comboPoints = 0;
    private int _maxCombo = 0;
    private int _level = 0;
    /// /////////////////////////////////////////
    enum GameStates { Start, Playing, LevelComplete, NextLevelText, FindRankingPosition, EndScreen, End };
    private GameStates _state;
    private float _lastTime = 0;
    /// ////////////////////////////////
    private Rectangle _topRect;
    private Rectangle _bottomRect;
    private Rectangle _gameBoardRect;
    /// ////////////////////////////////
    ArrayList<String> _rankingNames = new ArrayList<>();
    ArrayList<Integer> _rankingPoints = new ArrayList<>();
    ArrayList<Integer> _rankingCombos = new ArrayList<>();
    private int _ranking = 15;
    private TextInput _playerNameInput;
    private ButtonWithText _restartBtn;
    private ButtonWithText _exitBtn;

    private String endScreenComment = "GAME OVER";
    public Game() {
        _topRect = new Rectangle(
            0,
            Renderer.VIRTUAL_HEIGHT - 160,
            720,
            160
        );

        _bottomRect = new Rectangle(
            0,
            0,
            720,
            384
        );

        _gameBoardRect = new Rectangle(
            0,
            Renderer.VIRTUAL_HEIGHT - 160 - 1068,
            720,
            1068
        );

        Rectangle rect = new Rectangle(
            (Renderer.VIRTUAL_WIDTH)/2.0f - 416/2,
            (Renderer.VIRTUAL_HEIGHT)/2.0f + 208.0f,
            416,
            60
            );
        _playerNameInput = new TextInput("type player name...","", "player", rect, 16);

        _restartBtn = new ButtonWithText (
            "restart",
            TexturesManager.getTexture("tex/panelButtonNormal.png"),
            TexturesManager.getTexture("tex/panelButtonHover.png"),
            TexturesManager.getTexture("tex/panelButtonPressed.png"),
            (Renderer.VIRTUAL_WIDTH)/2 - 224 - 8,
            (Renderer.VIRTUAL_HEIGHT-96)/2 - 64,
            224,
            96
        );

        _exitBtn = new ButtonWithText (
            "exit",
            TexturesManager.getTexture("tex/panelButtonNormal.png"),
            TexturesManager.getTexture("tex/panelButtonHover.png"),
            TexturesManager.getTexture("tex/panelButtonPressed.png"),
            (Renderer.VIRTUAL_WIDTH)/2 + 8,
            (Renderer.VIRTUAL_HEIGHT-96)/2 - 64,
            224,
            96
        );

        _restartBtn.onclick_func = () -> {
            Gdx.input.setOnscreenKeyboardVisible(false);
            saveHighscores();
            _state = GameStates.Start;
        };

        _exitBtn.onclick_func = () -> {
            Gdx.input.setOnscreenKeyboardVisible(false);
            saveHighscores();
            _state = GameStates.End;
        };

        _lastTime = Time.currentTime;
        _state = GameStates.Start;

    }

    private float dist(Vector2 a, Vector2 b) {
        return (float)Math.sqrt((b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y));
    }

    private void selectTargetColor() {

        boolean anyVisible = false;
        float minY = Float.MAX_VALUE;

        // 1️⃣ Szukamy najniższego widocznego gema
        for (Gem gem : _gems) {

            Rectangle rect = new Rectangle(
                gem.realPosition.x - Gem.size.x / 2f,
                gem.realPosition.y - Gem.size.y / 2f,
                Gem.size.x,
                Gem.size.y
            );

            if (!rect.overlaps(_gameBoardRect))
                continue;

            anyVisible = true;

            if (rect.y < minY) {
                minY = rect.y;
            }
        }

        if (!anyVisible) {
            _targetColor = -1;
            return;
        }

        // 2️⃣ Zbieramy kolory z NAJNIŻSZEGO rzędu
        List<Integer> colors = new ArrayList<>();

        for (Gem gem : _gems) {

            Rectangle rect = new Rectangle(
                gem.realPosition.x - Gem.size.x / 2f,
                gem.realPosition.y - Gem.size.y / 2f,
                Gem.size.x,
                Gem.size.y
            );

            if (!rect.overlaps(_gameBoardRect))
                continue;

            if (Math.abs(rect.y - minY) < 1f) {
                colors.add(gem.color);
            }
        }

        if (colors.isEmpty()) {
            _targetColor = -1;
            return;
        }

        _targetColor = colors.get((int)(Math.random() * colors.size()));
    }



    private boolean collidedGems(int x, int y) {
        for(Gem gem : _gems){
            Rectangle rect = new Rectangle(
            gem.realPosition.x - Gem.size.x / 2.f,
            gem.realPosition.y - Gem.size.y / 2.f,
            Gem.size.x,
            Gem.size.y
            );

            if(rect.overlaps(new Rectangle(x,y,Gem.size.x, Gem.size.y))){
                return true;
            }
        }

        return false;
    }

    private boolean gameOverTest(){
        for(Gem gem : _gems){

            Rectangle rect = new Rectangle(
                gem.realPosition.x-Gem.size.x/2.0f,
                gem.realPosition.y-Gem.size.y/2.0f + Gem.size.y,
                Gem.size.x,
                Gem.size.y
            );

            if(rect.overlaps(_bottomRect)){
                _lastTime = Time.currentTime;
                return true;
            }
        }
        return false;
    }

    private boolean nextLevelTest(){
        if(_gems.isEmpty())
            return true;
        else
            return false;
    }

    private void addPointsAndResetCombo(){

        if(_combo>1) {
            _points += _comboPoints;
            _maxCombo = _combo;
        }

        _combo = 0;
        _comboPoints = 0;
    }

    private void addBonus(){
        _points += 50;
    }
    private void createGems() {

        _gems = new ArrayList<Gem>();

        int pairsCount = 16;
        int gemsCount = pairsCount * 2;

        int gemsCols = (int)Renderer.VIRTUAL_WIDTH/(int)Gem.size.x;
        int gemsRows = (int)Math.ceil(gemsCount/gemsCols);
        gemsRows *= 5;

        int dx = ((int)Renderer.VIRTUAL_WIDTH - gemsCols*(int)Gem.size.x)/2;

        for(int i=0; i<gemsCount; i+=2){

            int color = (int)(Math.random()*6.0f);

            int x, y;

            do{
                x = dx + ((int)(Math.random()*(float)gemsCols)) * (int)Gem.size.x + (int)Gem.size.x/2;
                y = Renderer.VIRTUAL_HEIGHT + ((int)(Math.random() * gemsRows) + 1) * (int)Gem.size.y;
            }while(collidedGems(x, y));
            Gem gem1 = new Gem(new Vector2(x,y), color);
            _gems.add(gem1);

            do{
                x = dx + ((int)(Math.random()*(float)gemsCols)) * (int)Gem.size.x + (int)Gem.size.x/2;
                y = Renderer.VIRTUAL_HEIGHT + ((int)(Math.random() * gemsRows) + 1) * (int)Gem.size.y;
            }while(collidedGems(x, y));
            Gem gem2 = new Gem(new Vector2(x,y), color);
            _gems.add(gem2);
        }
    }

    private void createFragments(Gem gem){

        Rectangle rect = new Rectangle(
            gem.realPosition.x,
            gem.realPosition.y,
            Gem.size.x,
            Gem.size.y
        );

        int rows = 10;
        int cols = 10;

        float cx = rect.x;
        float cy = rect.y;

        for(int y=0;y<rows;y+=1) {
            for(int x=0;x<cols;x+=1) {

                float xx = (float) ((int) rect.x + (int) rect.width * x / cols - (int) Gem.size.x / 2);
                float yy = (float) ((int) rect.y + (int) rect.height * y / rows - (int) Gem.size.y / 2);

                if ((xx - cx)*(xx - cx)  + (yy - cy)*(yy - cy) < (Gem.size.x/2) * (Gem.size.x/2)) {

                    Rectangle fragmentRect = new Rectangle(
                        xx,
                        yy,
                        rect.width / cols,
                        rect.height / rows
                    );

                    Rectangle texRect = new Rectangle(
                        (float) (256 * x / cols),
                        (float) (256 * y / rows),
                        (float) (256 / cols),
                        (float) (256 / rows)
                    );


                    float angle = (float) (Math.random() * 360.0f);  // losowy kierunek
                    float speed = 100.f + (float) (Math.random() * 100.0f);                    // baza + losowa wartość

                    Vector2 velocity = new Vector2(
                        (float) Math.cos(Math.toRadians(angle)) * speed,
                        (float) Math.sin(Math.toRadians(angle)) * speed
                    );

                    float life = (float) (Math.random() * 5.0f + 5.0f) / 10.0f;

                    Fragment fragment = new Fragment(gem.color, fragmentRect, texRect, velocity, life);

                    _fragments.add(fragment);
                }


            }
        }
    }

    private void updateGems(){
        if(_gems.isEmpty())
            return;

        if(Time.currentTime-_animTime > 0.05f){

            float dt = Time.currentTime-_animTime;
            float speedBase = 4.2f + 0.0375f*(float)_level;;
            float speedDelta = -(float)(_hittedGems/32/(_level+1))+(float)_hittedGems/160.0f;
            float speed = Time.easeOut(speedBase+speedDelta);
            for(Gem gem : _gems){
                gem.position.y -= speed * dt;

                float degrees = gem.dt + Time.currentTime * 2.f;
                degrees %= 360.0f;
                float s = (float)Math.sin(degrees);

                gem.rotation = -s * 15.f;
                float dx = -s;
                float dy = -Math.abs(1.f - s);
                gem.realPosition.x = gem.position.x + dx;
                gem.realPosition.y = gem.position.y + dy;

            }

            if(_targetColor== -1){
                selectTargetColor();
            }

        }
    }

    private void updateFragments(){

        if(_fragments.isEmpty())
            return;

        if((Time.currentTime-_animTime) > 0.05f){

            float dt = (Time.currentTime-_animTime);

            for(Fragment fragment : _fragments){
                fragment.rect.x += dt*fragment.velocity.x;
                fragment.rect.y += dt*fragment.velocity.y;
            }



            // delete fragments out of screen or oldest
            for (int i = _fragments.size() - 1; i >= 0; i--) {
                if ((Time.currentTime - _fragments.get(i).createTime) > _fragments.get(i).life ||
                    !_fragments.get(i).rect.overlaps(_gameBoardRect)) {
                    _fragments.remove(i);
                }
            }
        }

    }

    private void updateComboTexts(){

        if(_comboTexts.isEmpty())
            return;

        for (int i = _comboTexts.size()  - 1; i >= 0; i--) {
            if ((Time.currentTime - _comboTexts.get(i).createTime) > 1.0f) {
                _comboTexts.remove(i);
            }
        }
    }

    private void destroyAllGems(){
        for (int i = _gems.size() - 1; i >= 0; i--) {
            Rectangle rect = new Rectangle(
                _gems.get(i).realPosition.x - Gem.size.x/2,
                _gems.get(i).realPosition.y - Gem.size.y/2,
                Gem.size.x,
                Gem.size.y
            );

            if(rect.overlaps(_gameBoardRect)){
                if(_gems.get(i).effect == GemEffect.BonusPoints){
                    createFragments(_gems.get(i));
                    _points += 1;
                    _hittedGems += 1;
                    _combo+=1;
                    _comboPoints += _combo;
                    addBonus();
                    _gems.remove(i);
                }else{
                    createFragments(_gems.get(i));
                    _points += 1;
                    _hittedGems += 1;
                    _combo+=1;
                    _comboPoints += _combo;
                    _gems.remove(i);
                }
            }
        }
    }

    void destroyType(int colorType) {
        for (int i = _gems.size() - 1; i >= 0; i--) {

            Rectangle rect = new Rectangle(
                _gems.get(i).realPosition.x - Gem.size.x/2,
                _gems.get(i).realPosition.y - Gem.size.y/2,
                Gem.size.x,
                Gem.size.y
            );

            if(rect.overlaps(_gameBoardRect) && _gems.get(i).color == colorType){

                if(_gems.get(i).effect == GemEffect.DestroyAll){
                    destroyAllGems();
                }else if(_gems.get(i).effect == GemEffect.BonusPoints){
                    createFragments(_gems.get(i));
                    _points += 1;
                    _hittedGems += 1;
                    _combo+=1;
                    _comboPoints += _combo;
                    addBonus();
                    _gems.remove(i);

                }else{
                    createFragments(_gems.get(i));
                    _points += 1;
                    _hittedGems += 1;
                    _combo+=1;
                    _comboPoints += _combo;
                    _gems.remove(i);
                }
            }
        }
    }

    private void destroyClickedGems() {

        if (!_gameBoardRect.contains(Cursor.position.x, Cursor.position.y))
            return;

        if (_gems.isEmpty())
            return;

        Gem gemClicked = null;
        // clicked target color
        for (int i = _gems.size() - 1; gemClicked == null && i >= 0; i--) {

            if (dist(new Vector2(Cursor.position.x, Cursor.position.y), _gems.get(i).realPosition) <= Cursor.maxRadius) {
                if (_gems.get(i).color == _targetColor) {
                    gemClicked = _gems.get(i);
                    _gems.remove(i);
                }
            }
        }

        int d = 24;
        for (int i = _gems.size() - 1; gemClicked == null && i >= 0; i--) {
            if (dist(new Vector2(Cursor.position.x, Cursor.position.y), _gems.get(i).realPosition) <= Cursor.maxRadius + d) {
                if (_gems.get(i).color == _targetColor) {
                    gemClicked = _gems.get(i);
                    _gems.remove(i);
                }
            }
        }

        //other clicked (not target color)
        for (int i = _gems.size() - 1; gemClicked == null && i >= 0; i--) {

            if (dist(new Vector2(Cursor.position.x, Cursor.position.y), _gems.get(i).realPosition) <= Cursor.maxRadius) {
                gemClicked = _gems.get(i);
            }
        }

        d = 24;
        for (int i = _gems.size() - 1; gemClicked == null && i >= 0; i--) {
            if (dist(new Vector2(Cursor.position.x, Cursor.position.y), _gems.get(i).realPosition) <= Cursor.maxRadius + d) {
                gemClicked = _gems.get(i);
            }
        }

        if (gemClicked != null) {
            if (gemClicked.color == _targetColor) {
                switch (gemClicked.effect) {
                    case DestroyAll:
                        createFragments(gemClicked);
                        _points += 1;
                        _hittedGems += 1;
                        _combo += 1;
                        _comboPoints += _combo;
                        destroyAllGems();
                        if (nextLevelTest())
                            _state = GameStates.LevelComplete;
                        if(_combo>1)
                            _comboTexts.add(new ComboText(_combo, gemClicked.realPosition.x + 48.0f, gemClicked.realPosition.y+Gem.size.y/2.0f));
                        updateComboTexts();
                        selectTargetColor();
                        SoundManager.playHit();
                        break;

                    case DestroyType:
                        createFragments(gemClicked);
                        _points += 1;
                        _hittedGems += 1;
                        _combo += 1;
                        _comboPoints += _combo;
                        destroyType(gemClicked.color);
                        if (nextLevelTest())
                            _state = GameStates.LevelComplete;
                        if(_combo>1)
                            _comboTexts.add(new ComboText(_combo, gemClicked.realPosition.x+48.0f, gemClicked.realPosition.y+Gem.size.y/2.0f));
                        selectTargetColor();
                        SoundManager.playHit();
                        break;

                    case BonusPoints:
                        createFragments(gemClicked);

                        _points += 1;
                        _hittedGems += 1;
                        _combo += 1;
                        _comboPoints += _combo;
                        addBonus();
                        if (nextLevelTest())
                            _state = GameStates.LevelComplete;
                        if(_combo>1)
                            _comboTexts.add(new ComboText(_combo, gemClicked.realPosition.x+48.0f, gemClicked.realPosition.y+Gem.size.y/2.0f));
                        selectTargetColor();
                        SoundManager.playHit();
                        break;

                    case None:
                        createFragments(gemClicked);

                        _points += 1;
                        _hittedGems += 1;
                        _combo += 1;
                        _comboPoints += _combo;
                        if (nextLevelTest())
                            _state = GameStates.LevelComplete;
                        if(_combo>1)
                            _comboTexts.add(new ComboText(_combo, gemClicked.realPosition.x+48.0f, gemClicked.realPosition.y+Gem.size.y/2.0f));
                        selectTargetColor();
                        SoundManager.playHit();
                        break;
                }
                ;

            } else {
                addPointsAndResetCombo();
                _comboTexts.add(new ComboText(-1, gemClicked.realPosition.x+48.0f, gemClicked.realPosition.y+Gem.size.y/2.0f));
                SoundManager.playMiss();
            }

        } else {
            addPointsAndResetCombo();
            _comboTexts.add(new ComboText(-1, Cursor.position.x+24.0f, Cursor.position.y+96.0f));
            SoundManager.playMiss();
        }
    }
    private void generateEndScreenComment(){

        ArrayList<String> names = new ArrayList<String>();
        ArrayList<Integer> points = new ArrayList<Integer>();
        ArrayList<Integer> combos = new ArrayList<Integer>();

        for (int i = 0; i < 15; i++) {
            if (i >= _rankingPoints.size()) {
                names.add("-");
                points.add(-1);
                combos.add(-1);
            } else {
                names.add(_rankingNames.get(i));
                points.add(_rankingPoints.get(i));
                combos.add(_rankingCombos.get(i));
            }
        }

        endScreenComment = "GAME OVER";

        if (_ranking == 0) {
            endScreenComment = "NEW RECORD";
        }
        else if (_ranking == 1) {
            endScreenComment = "SECOND PLACE";
        }
        else if (_ranking == 2) {
            endScreenComment = "THIRD PLACE";
        }
        else if (_ranking < 5) {
            endScreenComment = "TOP 5";
        }
        else if (_ranking < 10) {
            endScreenComment = "TOP 10";
        }else if (_ranking < 15) {
            endScreenComment = "TOP 15";
        }

    }
    private void findRankingPosition() {

        int newPoints = _points;
        int insertIndex = 15;

        for (int i = 0; i < 15; i++) {
            if (i >= _rankingPoints.size()) {
                insertIndex = i;
                break;
            } else if (newPoints > _rankingPoints.get(i)) {
                insertIndex = i;
                break;
            }
        }

        _ranking = insertIndex;
    }
    private void loadHighscores() {
        Preferences prefs = Gdx.app.getPreferences("highscores");

        _rankingNames = new ArrayList<>();
        _rankingPoints = new ArrayList<>();
        _rankingCombos = new ArrayList<>();

        for (int i = 0; i < 15; i++) {
            _rankingNames.add(prefs.getString("name" + i, "-"));
            _rankingPoints.add(prefs.getInteger("point" + i, -1));
            _rankingCombos.add(prefs.getInteger("combo" + i, -1));
        }
    }


    private void saveHighscores() {
        Preferences prefs = Gdx.app.getPreferences("highscores");

        // jeśli mamy ranking w którym trzeba wstawić wynik
        if (_ranking <= _rankingPoints.size()) {
            _rankingNames.add(_ranking, _playerNameInput.getText());
            _rankingPoints.add(_ranking, _points);
            _rankingCombos.add(_ranking, _maxCombo);
        }

        // zapisujemy tylko faktyczne wyniki
        for (int i = 0; i < _rankingPoints.size(); i++) {
            prefs.putString("name" + i, _rankingNames.get(i));
            prefs.putInteger("point" + i, _rankingPoints.get(i));
            prefs.putInteger("combo" + i, _rankingCombos.get(i));
        }

        prefs.flush();
    }

    private void drawNextLevelText() {
        if ((int) ((Time.currentTime - _lastTime) * 6.0f) % 2 == 0) {
            GlyphLayout l = new GlyphLayout();
            l.setText(Font.GameCommentFont, "level complete");

            float textWdt = l.width;
            float textHgh = Font.GameCommentFont.getCapHeight();

            Font.GameCommentFont.draw(
                Renderer.batch,
                "level complete",
                ((float) Renderer.VIRTUAL_WIDTH - textWdt) / 2.0f,
                ((float) Renderer.VIRTUAL_HEIGHT + textHgh) / 2.0f + 160
            );
        }
    }

    private void drawEndGameScreen() {

        float x = (Renderer.VIRTUAL_WIDTH)/2.0f;
        float y = (Renderer.VIRTUAL_HEIGHT)/2.0f + 128.0f;

        Tex texture = TexturesManager.getTexture(("tex/smallPanel.png"));
        if(texture != null){
            Sprite sprite = new Sprite(texture.texture);
            sprite.setSize(512, 512);
            sprite.setCenter(x, y);
            sprite.draw(Renderer.batch);
        }

        // dy for movement of everything on the panel
        float dy = 16;

        GlyphLayout layout = new GlyphLayout();
        layout.setText(Font.rankingTitleFont, endScreenComment);
        float textWdt = layout.width;
        float textHgh = Font.rankingTitleFont.getCapHeight();
        Font.rankingTitleFont.draw(Renderer.batch, endScreenComment, (x - textWdt/2.0f), (y + textHgh + 160 + dy));

        // player name text input
        if(_ranking < 15){
            _playerNameInput.draw();
        }

        // points
        layout = new GlyphLayout();
        layout.setText(Font.GameCommentFont, "points: ");
        textWdt = layout.width;
        textHgh = Font.GameCommentFont.getCapHeight();
        Font.GameCommentFont.draw(Renderer.batch, "points:", x - 224 + 16, y + textHgh + dy);

        layout = new GlyphLayout();
        layout.setText(Font.GameCommentFont, Integer.toString(_points));
        textWdt = layout.width;
        textHgh = Font.GameCommentFont.getCapHeight();
        Font.GameCommentFont.draw(Renderer.batch, Integer.toString(_points), x - textWdt + 192, y + textHgh + dy);

        // combo
        layout = new GlyphLayout();
        layout.setText(Font.GameCommentFont, "max combo: ");
        textWdt = layout.width;
        textHgh = Font.GameCommentFont.getCapHeight();
        Font.GameCommentFont.draw(Renderer.batch, "max combo:", x - 224 + 16, y + textHgh - 64 + dy);

        layout = new GlyphLayout();
        layout.setText(Font.GameCommentFont, Integer.toString(_maxCombo));
        textWdt = layout.width;
        textHgh = Font.GameCommentFont.getCapHeight();
        Font.GameCommentFont.draw(Renderer.batch, Integer.toString(_maxCombo), x - textWdt + 192, y + textHgh - 64 + dy);

        // combo
        layout = new GlyphLayout();
        layout.setText(Font.GameCommentFont, "level: ");
        textWdt = layout.width;
        textHgh = Font.GameCommentFont.getCapHeight();
        Font.GameCommentFont.draw(Renderer.batch, "level:", x - 224 + 16, y + textHgh - 128 + dy);

        layout = new GlyphLayout();
        layout.setText(Font.GameCommentFont, Integer.toString(_level));
        textWdt = layout.width;
        textHgh = Font.GameCommentFont.getCapHeight();
        Font.GameCommentFont.draw(Renderer.batch, Integer.toString(_level), x - textWdt + 192, y + textHgh - 128 + dy);

        _restartBtn.draw();
        _exitBtn.draw();
    }

    @Override
    public void handleEvents() {

        if (MyInput.processor.isBackPressed()) {
            // back btn
            SoundManager.stopAll();
            LayoutsManager.pop_back();
            SoundManager.playMenuMusic();
        }else if(_state != GameStates.Start && _state != GameStates.EndScreen){
            if (MyInput.processor.isTouchDown()) {
                Vector2 curPos = MyInput.processor.getTouchPosition();
                if (_gameBoardRect.contains(curPos)) {
                    Cursor.setOn(curPos);
                    if(_state == GameStates.Playing)
                        destroyClickedGems();
                }
            }
        }else if(_state == GameStates.EndScreen){
            if(_ranking < 15){
                _playerNameInput.handleEvents();
            }

            _restartBtn.handleEvents();
            _exitBtn.handleEvents();
        }




    }

    @Override
    public void update() {

        switch(_state){

            case Start:
                _level = 0;
                _hittedGems = 0;
                _points = 0;
                _combo = 0;
                _comboPoints = 0;
                _maxCombo = 0;
                _targetColor = -1;

                _ranking = 15;

                _comboTexts.clear();
                _gems.clear();
                _fragments.clear();

                createGems();
                selectTargetColor();
                _state = GameStates.Playing;

                _lastTime = Time.currentTime;
                _animTime = Time.currentTime;

                SoundManager.playGameMusic();
                break;

            case Playing:
                updateGems();
                updateFragments();
                if((Time.currentTime-_animTime) > 0.05f)
                    _animTime = Time.currentTime;

                if(gameOverTest()){
                    addPointsAndResetCombo();
                    findRankingPosition();
                    generateEndScreenComment();
                    _state = GameStates.EndScreen;
                }


                updateComboTexts();
                break;

            case LevelComplete:
                _level += 1;
                createGems();
                selectTargetColor();
                _state = GameStates.NextLevelText;
                _lastTime = Time.currentTime;
                updateComboTexts();
                break;

            case NextLevelText:
                updateFragments();
                if((Time.currentTime-_animTime) > 0.05f)
                    _animTime = Time.currentTime;


                if((Time.currentTime-_lastTime) > 3.0f) {
                    _state = GameStates.Playing;
                    _lastTime = Time.currentTime;
                    _animTime = Time.currentTime;
                }

                updateComboTexts();
                break;
            case FindRankingPosition:
                loadHighscores();
                findRankingPosition();
                _state = GameStates.EndScreen;
                break;

            case EndScreen:
                if(_ranking < 15)
                    _playerNameInput.update();

                _restartBtn.update();
                _exitBtn.update();

                break;
            case End:
                _restartBtn.update();
                _exitBtn.update();

                if(_restartBtn._state == ButtonStates.Idle && _exitBtn._state == ButtonStates.Idle){
                    SoundManager.stopAll();
                    SoundManager.playMenuMusic();
                    LayoutsManager.pop_back();
                }

                break;
        };


    }


    @Override
    public void draw() {
        Sprite background = new Sprite(TexturesManager.getTexture("tex/mainBoard.png").texture);
        background.setPosition(0, 0);
        background.draw(Renderer.batch);

        Sprite gameBoard = new Sprite(TexturesManager.getTexture("tex/underwater.png").texture);
        gameBoard.setPosition(_gameBoardRect.x, _gameBoardRect.y);
        gameBoard.draw(Renderer.batch);

        for (Gem gem : _gems) {
            Rectangle rect = new Rectangle(
                gem.realPosition.x - Gem.size.x / 2f,
                gem.realPosition.y - Gem.size.y / 2f,
                Gem.size.x,
                Gem.size.y
            );

            if (rect.overlaps(_gameBoardRect))
                gem.drawBubble();
        }

        Renderer.batch.end();
        Renderer.shapeRenderer.setProjectionMatrix(Renderer.batch.getProjectionMatrix());
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        for (Gem gem : _gems) {
            Rectangle rect = new Rectangle(
                gem.realPosition.x - Gem.size.x / 2f,
                gem.realPosition.y - Gem.size.y / 2f,
                Gem.size.x,
                Gem.size.y
            );

            if (rect.overlaps(_gameBoardRect))
                gem.drawColor();
        }

        Gdx.gl.glDisable(GL20.GL_BLEND);
        Renderer.batch.begin();

        for (Gem gem : _gems) {
            Rectangle rect = new Rectangle(
                gem.realPosition.x - Gem.size.x / 2f,
                gem.realPosition.y - Gem.size.y / 2f,
                Gem.size.x,
                Gem.size.y
            );

            if (rect.overlaps(_gameBoardRect))
                gem.drawGem();

        }

        Renderer.batch.end();
        Renderer.shapeRenderer.setProjectionMatrix(Renderer.batch.getProjectionMatrix());
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        for (Fragment fragment : _fragments) {
            fragment.draw();
        }

        Gdx.gl.glDisable(GL20.GL_BLEND);
        Renderer.batch.begin();

        for (ComboText text : _comboTexts) {
            text.draw();
        }

        Sprite topBoard = new Sprite(TexturesManager.getTexture("tex/topBoard.png").texture);
        topBoard.setPosition(_topRect.x, _topRect.y);
        topBoard.draw(Renderer.batch);

        Sprite bottomBoard = new Sprite(TexturesManager.getTexture("tex/bottomBoard.png").texture);
        bottomBoard.setPosition(_bottomRect.x, _bottomRect.y);
        bottomBoard.draw(Renderer.batch);

        // text - "points"
        Font.gameTopTextFont.draw(Renderer.batch, "points", 32, Renderer.VIRTUAL_HEIGHT - 16);

        // text - "combo"
        GlyphLayout layout = new GlyphLayout();
        layout.setText(Font.gameTopTextFont, "combo");
        float textWidth = layout.width;
        Font.gameTopTextFont.draw(Renderer.batch, "combo", Renderer.VIRTUAL_WIDTH / 2 - textWidth / 2, Renderer.VIRTUAL_HEIGHT - 16);

        // text - "level"
        layout = new GlyphLayout();
        layout.setText(Font.gameTopTextFont, "level");
        textWidth = layout.width;
        Font.gameTopTextFont.draw(Renderer.batch, "level", Renderer.VIRTUAL_WIDTH - 32 - textWidth, Renderer.VIRTUAL_HEIGHT - 16);

        //
        int y = (int) Font.gameTopTextFont.getLineHeight();

        // value = points
        Font.gameTopTextFont.draw(Renderer.batch, Integer.toString(_points), 32, Renderer.VIRTUAL_HEIGHT - 16 - y);

        // value - combo
        layout = new GlyphLayout();
        layout.setText(Font.gameTopTextFont, Integer.toString(_combo));
        textWidth = layout.width;
        Font.gameTopTextFont.draw(Renderer.batch, Integer.toString(_combo), Renderer.VIRTUAL_WIDTH / 2 - textWidth / 2, Renderer.VIRTUAL_HEIGHT - 16 - y);

        // value - level
        layout = new GlyphLayout();
        layout.setText(Font.gameTopTextFont, Integer.toString(_level));
        textWidth = layout.width;
        Font.gameTopTextFont.draw(Renderer.batch, Integer.toString(_level), Renderer.VIRTUAL_WIDTH - 32 - textWidth, Renderer.VIRTUAL_HEIGHT - 16 - y);

        /// ////////////////////////////////////////////////

        Vector2 size = new Vector2(96.0f * 3.0f, 96.0f * 3.0f);
        float dy = 16;

        //draw shape of bubble
        Renderer.batch.end();
        Renderer.shapeRenderer.setProjectionMatrix(Renderer.batch.getProjectionMatrix());

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Renderer.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Renderer.shapeRenderer.setColor(47.0f / 255.0f, 47.0f / 255.0f, 47.0f / 255.0f, 0.5f);
        Renderer.shapeRenderer.circle(_bottomRect.width / 2.0f, _bottomRect.height / 2.0f + dy, Math.max(size.x / 2, size.y / 2));
        Renderer.shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
        Renderer.batch.begin();

        // draw the bubble
        Tex bubbleTex = TexturesManager.getTexture("tex/bubble.png");
        Sprite bubbleSprite = new Sprite(bubbleTex.texture);
        bubbleSprite.setOriginCenter();
        bubbleSprite.setSize(size.x, size.y);
        bubbleSprite.setCenter(_bottomRect.width / 2.0f, _bottomRect.height / 2.0f + dy);
        bubbleSprite.draw(Renderer.batch);

        // draw the big Gem
        Tex gemTex = TexturesManager.getTexture(_targetColor);
        if (gemTex != null) {
            Sprite gemSprite = new Sprite(gemTex.texture);
            gemSprite.setOriginCenter();
            gemSprite.setSize(size.x, size.y);
            gemSprite.setCenter(_bottomRect.width / 2.0f, _bottomRect.height / 2.0f + dy);
            gemSprite.draw(Renderer.batch);
        }

        Cursor.draw();

        if (_state == GameStates.NextLevelText) {
            drawNextLevelText();
        }

        if (_state == GameStates.EndScreen) {
            drawEndGameScreen();
        }
    }
}
