package com.mygdx.catmario;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class GameScreen implements Screen {
    private final Main game;
    private final SpriteBatch batch;

    private float stateTime;
    private final CharacterController characterController;
    private final Vector2 characterPosition;

    private static final float GROUND_Y = 0;

    private final Texture backgroundTexture;
    private final Texture groundTexture;
    private final Music backgroundMusic;

    private final BitmapFont font;
    private final Texture heartTexture;
    private final int lives = 3;
    private final int hp = 100;

    private final Texture statusBackgroundTexture;
    private final ShapeRenderer shapeRenderer;

    private final int selectedCharacter;
    private final String characterName;

    public GameScreen(Main game, int selectedCharacter, String characterName) {
        this.game = game;
        this.batch = game.batch;
        this.selectedCharacter = selectedCharacter;
        this.characterName = characterName;

        this.characterController = new CharacterController(selectedCharacter);

        stateTime = 0f;

        backgroundTexture = new Texture("gamebackground.png");
        groundTexture = new Texture("ground.png");

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("gamemusic.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);

        font = new BitmapFont();
        heartTexture = new Texture("heart.png");

        characterPosition = new Vector2(100, GROUND_Y + 150);

        statusBackgroundTexture = new Texture("statusbackground.png");
        shapeRenderer = new ShapeRenderer();

        SoundManager.playMusic(backgroundMusic);
    }

    @Override
    public void render(float delta) {
        stateTime += delta;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        characterController.update(delta);
        batch.draw(characterController.getCurrentFrame(stateTime), characterPosition.x, characterPosition.y);
        drawCharacterStatus(batch);
        batch.end();

        drawStatusBoxBorder();

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            pauseGame();
        }
    }

    private void drawCharacterStatus(SpriteBatch batch) {
        float statusYOffset = 50;
        float statusBoxWidth = 400;
        float statusBoxHeight = 200;

        batch.draw(statusBackgroundTexture, 10, Gdx.graphics.getHeight() - statusBoxHeight - 60, statusBoxWidth, statusBoxHeight);
        font.getData().setScale(1.5f);
        font.setColor(Color.BLACK);
        font.draw(batch, "Name: " + characterName, 130, Gdx.graphics.getHeight() - 50 - statusYOffset);

        for (int i = 0; i < lives; i++) {
            batch.draw(heartTexture, 130 + i * 30, Gdx.graphics.getHeight() - 120 - statusYOffset, 60, 40);
        }

        font.draw(batch, "HP: " + hp + "/100", 130, Gdx.graphics.getHeight() - 160 - statusYOffset);
    }

    private void drawStatusBoxBorder() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(10, Gdx.graphics.getHeight() - 237, 400, 205);
        shapeRenderer.end();
    }

    private void pauseGame() {
        SoundManager.stopMusic(backgroundMusic);
        game.setScreen(new PauseMenuScreen(game, selectedCharacter, characterName));
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void show() {
        SoundManager.playMusic(backgroundMusic);
    }

    @Override
    public void hide() {
        SoundManager.stopMusic(backgroundMusic);
    }

    @Override
    public void pause() {
        SoundManager.stopMusic(backgroundMusic);
    }

    @Override
    public void resume() {
        SoundManager.playMusic(backgroundMusic);
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        groundTexture.dispose();
        heartTexture.dispose();
        statusBackgroundTexture.dispose();
        font.dispose();
        shapeRenderer.dispose();
        characterController.dispose();
    }
}




























