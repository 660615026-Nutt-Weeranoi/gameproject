package com.mygdx.catmario;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private final Vector2 position;
    private final Vector2 velocity;
    private final Texture texture;
    private final float speed = 500;
    private final float width;
    private final float height;

    public Bullet(float startX, float startY, float width, float height) {
        position = new Vector2(startX, startY);
        velocity = new Vector2(speed, 0);
        texture = new Texture("bullet.png");
        this.width = width;
        this.height = height;
    }

    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));
    }

    public Vector2 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean isOffScreen(float screenWidth) {
        return position.x > screenWidth;
    }

    public void dispose() {
        texture.dispose();
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}


