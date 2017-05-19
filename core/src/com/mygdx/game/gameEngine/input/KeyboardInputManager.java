package com.mygdx.game.gameEngine.input;

import com.badlogic.gdx.InputProcessor;

/**
 * Created by rasmus on 2017-05-05.
 */
public class KeyboardInputManager implements InputProcessor {

    private final KeyboardControllerAdapter keyboardControllerAdapter;

    public KeyboardInputManager(KeyboardControllerAdapter adapter){
        this.keyboardControllerAdapter = adapter;
    }

    @Override
    public boolean keyDown(int keycode) {
        keyboardControllerAdapter.keyPressed(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
    	keyboardControllerAdapter.keyReleased(keycode);
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
