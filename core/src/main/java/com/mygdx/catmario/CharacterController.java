package com.mygdx.catmario;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CharacterController {
    private final Vector2 position;
    private final Vector2 velocity;

    private Animation<TextureRegion> idle1Animation, idle2Animation, walkAnimation, runAnimation, jumpAnimation, attackAnimation, shoot1Animation, shoot2Animation, hurtAnimation, dieAnimation;
    private Animation<TextureRegion> widleAnimation, wwalkAnimation, wrunAnimation, wjumpAnimation, wattack1Animation, wattack2Animation, wattack3Animation, wattack4Animation, whurtAnimation, wdieAnimation;

    private String currentState = "idle1";
    private boolean isIdle2Active = false;
    private boolean isShoot2Active = false;
    private int currentAttack = 1;

    private static final float GRAVITY = -500;
    private static final float JUMP_VELOCITY = 400;
    private static final float MOVE_SPEED = 300;
    private static final float RUN_SPEED = 500;
    private static final float GROUND_LEVEL = 150;

    private int jumpCount = 0;
    private int hp = 100;
    private final int selectedCharacter;

    public CharacterController(int selectedCharacter) {
        this.selectedCharacter = selectedCharacter;
        this.position = new Vector2(100, GROUND_LEVEL);
        this.velocity = new Vector2(0, 0);

        loadAnimations();
    }

    private Animation<TextureRegion> loadAnimation(String name, int frameCount) {
        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i <= frameCount; i++) {
            frames.add(new TextureRegion(new com.badlogic.gdx.graphics.Texture(Gdx.files.internal("animations/" + name + i + ".png"))));
        }
        return new Animation<>(0.1f, frames);
    }

    private void loadAnimations() {
        if (selectedCharacter == 1) {
            idle1Animation = loadAnimation("idle1_", 6);
            idle2Animation = loadAnimation("idle2_", 4);
            walkAnimation = loadAnimation("walk", 8);
            runAnimation = loadAnimation("run", 8);
            jumpAnimation = loadAnimation("jump", 8);
            attackAnimation = loadAnimation("attack", 4);
            shoot1Animation = loadAnimation("shoot1_", 8);
            shoot2Animation = loadAnimation("shoot2_", 8);
            hurtAnimation = loadAnimation("hurt", 2);
            dieAnimation = loadAnimation("die", 4);
        } else {
            widleAnimation = loadAnimation("widle", 5);
            wwalkAnimation = loadAnimation("wwalk", 8);
            wrunAnimation = loadAnimation("wrun", 8);
            wjumpAnimation = loadAnimation("wjump", 7);

            wattack1Animation = loadAnimation("wattack1_", 5);
            wattack2Animation = loadAnimation("wattack2_", 4);
            wattack3Animation = loadAnimation("wattack3_", 6);
            wattack4Animation = loadAnimation("wattack4_", 5);
            
            whurtAnimation = loadAnimation("whurt", 2);
            wdieAnimation = loadAnimation("wdie", 4);
        }
    }

    public void update(float delta) {
        if (hp <= 0) {
            currentState = "die";
            return;
        }

        velocity.x = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                velocity.x = RUN_SPEED * delta;
                currentState = "run";
            } else {
                velocity.x = MOVE_SPEED * delta;
                currentState = "walk";
            }
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                velocity.x = -RUN_SPEED * delta;
                currentState = "run";
            } else {
                velocity.x = -MOVE_SPEED * delta;
                currentState = "walk";
            }
        } else {
            currentState = isIdle2Active ? "idle2" : "idle1";
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (jumpCount < 2) {
                velocity.y = JUMP_VELOCITY;
                jumpCount++;
                currentState = "jump";
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
            currentState = "attack";
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            currentState = isShoot2Active ? "shoot2" : "shoot1";
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            isShoot2Active = !isShoot2Active;
            currentState = isShoot2Active ? "shoot2" : "shoot1";
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            isIdle2Active = !isIdle2Active;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            currentAttack = (currentAttack % 4) + 1;
        }

        if (isDamaged()) {
            hp -= 20;
            currentState = "hurt";
        }

        velocity.y += GRAVITY * delta;
        position.add(velocity.x, velocity.y * delta);

        if (position.y <= GROUND_LEVEL) {
            position.y = GROUND_LEVEL;
            velocity.y = 0;
            jumpCount = 0;
        }
    }

    private boolean isDamaged() {
        return Math.random() < 0.01;
    }

    public TextureRegion getCurrentFrame(float stateTime) {
        switch (currentState) {
            case "walk":
                return selectedCharacter == 1 ? walkAnimation.getKeyFrame(stateTime, true) : wwalkAnimation.getKeyFrame(stateTime, true);
            case "run":
                return selectedCharacter == 1 ? runAnimation.getKeyFrame(stateTime, true) : wrunAnimation.getKeyFrame(stateTime, true);
            case "jump":
                return selectedCharacter == 1 ? jumpAnimation.getKeyFrame(stateTime, true) : wjumpAnimation.getKeyFrame(stateTime, true);
            case "attack":
                if (selectedCharacter == 1) {
                    return attackAnimation.getKeyFrame(stateTime, true);
                } else {
                    switch (currentAttack) {
                        case 1:
                            return wattack1Animation.getKeyFrame(stateTime, true);
                        case 2:
                            return wattack2Animation.getKeyFrame(stateTime, true);
                        case 3:
                            return wattack3Animation.getKeyFrame(stateTime, true);
                        case 4:
                            return wattack4Animation.getKeyFrame(stateTime, true);
                        default:
                            return wattack1Animation.getKeyFrame(stateTime, true);
                    }
                }
            case "shoot1":
                return selectedCharacter == 1 ? shoot1Animation.getKeyFrame(stateTime, true) : wattack1Animation.getKeyFrame(stateTime, true);
            case "shoot2":
                return selectedCharacter == 1 ? shoot2Animation.getKeyFrame(stateTime, true) : wattack2Animation.getKeyFrame(stateTime, true);
            case "hurt":
                return selectedCharacter == 1 ? hurtAnimation.getKeyFrame(stateTime, true) : whurtAnimation.getKeyFrame(stateTime, true);
            case "die":
                return selectedCharacter == 1 ? dieAnimation.getKeyFrame(stateTime, true) : wdieAnimation.getKeyFrame(stateTime, true);
            default:
                return selectedCharacter == 1 ? (isIdle2Active ? idle2Animation.getKeyFrame(stateTime, true) : idle1Animation.getKeyFrame(stateTime, true)) : widleAnimation.getKeyFrame(stateTime, true);
        }
    }

    public void dispose() {
        if (selectedCharacter == 1) {
            idle1Animation.getKeyFrames()[0].getTexture().dispose();
            idle2Animation.getKeyFrames()[0].getTexture().dispose();
            walkAnimation.getKeyFrames()[0].getTexture().dispose();
            runAnimation.getKeyFrames()[0].getTexture().dispose();
            jumpAnimation.getKeyFrames()[0].getTexture().dispose();
            attackAnimation.getKeyFrames()[0].getTexture().dispose();
            shoot1Animation.getKeyFrames()[0].getTexture().dispose();
            shoot2Animation.getKeyFrames()[0].getTexture().dispose();
            hurtAnimation.getKeyFrames()[0].getTexture().dispose();
            dieAnimation.getKeyFrames()[0].getTexture().dispose();
        } else {
            widleAnimation.getKeyFrames()[0].getTexture().dispose();
            wwalkAnimation.getKeyFrames()[0].getTexture().dispose();
            wrunAnimation.getKeyFrames()[0].getTexture().dispose();
            wjumpAnimation.getKeyFrames()[0].getTexture().dispose();
            wattack1Animation.getKeyFrames()[0].getTexture().dispose();
            wattack2Animation.getKeyFrames()[0].getTexture().dispose();
            wattack3Animation.getKeyFrames()[0].getTexture().dispose();
            wattack4Animation.getKeyFrames()[0].getTexture().dispose();
            whurtAnimation.getKeyFrames()[0].getTexture().dispose();
            wdieAnimation.getKeyFrames()[0].getTexture().dispose();
        }
    }
}












