package com.mygdx.game.gameEngine.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.utils.Constants;

/**
 * Screen superclass that defines behavior shared by all Unexpected Magic screens.
 * @author rarvid
 * Revised by rastom, soflarb and car0b1nius
 */

public abstract class AbstractScreen extends ScreenAdapter {
	private static Stack<AbstractScreen> prevScreens = new Stack<AbstractScreen>();
	
	protected OrthographicCamera camera;
	protected Viewport viewport;
	protected Stage stage;
	protected SpriteBatch batch;

	protected TextureAtlas atlas;
	protected Skin skin;
	private static final List<ScreenListener> listeners = new ArrayList<>();

	protected AbstractScreen(SpriteBatch batch) {
		this.batch = batch;
		camera = new OrthographicCamera(Constants.VIEWPORT_DIM_X, Constants.VIEWPORT_DIM_Y);
		camera.setToOrtho(false, Constants.VIEWPORT_DIM_X, Constants.VIEWPORT_DIM_Y);
		viewport = new ScalingViewport(Scaling.fit, Constants.VIEWPORT_DIM_X, Constants.VIEWPORT_DIM_Y, camera);
		viewport.apply();
		stage = new Stage(viewport, batch);
		atlas = new TextureAtlas("skins/commodore64/skin/uiskin.atlas");
		skin = new Skin(Gdx.files.internal("skins/commodore64/skin/uiskin.json"), atlas);
	}
	@Override
	public void show(){
		super.show();
		Gdx.input.setInputProcessor(stage);
		System.out.println("abstractscreen. show");
	}
	@Override
	public void dispose() {
		stage.dispose();
		atlas.dispose();
		skin.dispose();
		System.out.println("abstractscreen dispose");
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.26274509f,0.80784313f,0.90588235f,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	
		
	public static void addListener(ScreenListener listener){	
		if (!listeners.contains(listener)){
			listeners.add(listener);
			}
	}

	public static void removeListener(ScreenListener listener){
		listeners.remove(listener);
	}
	protected void changeToPreviousScreen(){
		for(ScreenListener sl : listeners){
			sl.screenChanged(prevScreens.pop());
		}
		dispose();
	}
	protected void changeToScreen(AbstractScreen screen){
		prevScreens.push(this);
		for(ScreenListener sl : listeners){
			sl.screenChanged(screen);
		}
	}
}
