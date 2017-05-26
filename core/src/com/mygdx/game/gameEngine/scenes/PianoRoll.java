package com.mygdx.game.gameEngine.scenes;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.gameEngine.components.CompositeSpriteComponent;
import com.mygdx.game.gameEngine.components.PositionComponent;
import com.mygdx.game.gameEngine.managers.EntityFactory;
import com.mygdx.game.model.IPlayer;
import com.mygdx.game.model.Ticker;
import com.mygdx.game.model.song.ISong;
import com.mygdx.game.utils.Constants;

import java.util.List;

/**
 * A class that defines the properties of the piano roll (the area where the notes are drawn).
 * @author soflarb
 * 
 */
public class PianoRoll {
	private final Engine engine;
	private final SpriteBatch batch;
	public final OrthographicCamera camera;
	private final Viewport viewport;
	private final ComponentMapper<PositionComponent> positionComponentMapper;
	private final ComponentMapper<CompositeSpriteComponent> spriteComponentMapper;
	private Ticker ticker;

	
	public PianoRoll(Engine engine, SpriteBatch spriteBatch, List<? extends IPlayer> players, ISong song, Ticker ticker) {
        this.engine = engine;
        batch = spriteBatch;
        this.ticker = ticker;
		createNoteEntities(players,song);
        camera = new OrthographicCamera();
        viewport = new ScalingViewport(Scaling.fit, Constants.PIANOROLL_DIM_X, Constants.PIANOROLL_DIM_Y, camera);
        viewport.apply(true);
        viewport.setScreenBounds((int) Constants.PIANOROLL_POS_X, (int) Constants.PIANOROLL_POS_Y, (int) Constants.PIANOROLL_DIM_X, (int) Constants.PIANOROLL_DIM_Y);
        spriteComponentMapper = ComponentMapper.getFor(CompositeSpriteComponent.class);
        positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);
    }

    private void createNoteEntities(List<? extends IPlayer> players, ISong song){
		if(players.isEmpty()){
			for(Entity entity : EntityFactory.createNoteEntities(song)){
				engine.addEntity(entity);
			}
		}
		else {
			for (Entity entity : EntityFactory.createNoteEntities(players)) {
				engine.addEntity(entity);
			}
		}
	}

	public void placeCamera(){
		camera.position.y = (float)ticker.tickWithDecimals()*Constants.NOTESPRITE_HEIGHT + camera.viewportHeight/2;
		camera.update();
	}

	public void draw() {
		viewport.apply(false);//TODO centercamera thing is not applied
		ImmutableArray<Entity> entities = engine.getEntitiesFor(Family.all(CompositeSpriteComponent.class,PositionComponent.class).get());
		for (Entity entity : entities) {
			PositionComponent pos = positionComponentMapper.get(entity);
			CompositeSpriteComponent spr = spriteComponentMapper.get(entity);
			if (pos.y > camera.position.y - (viewport.getScreenHeight()/2) - spr.getCompositeSprite().getLength() && pos.y < camera.position.y + (viewport.getScreenHeight()/2)) {
				spr.getCompositeSprite().draw(batch);
			}
		}
	}
	
	public void resize(int width, int height, int screenX, int screenY){
		viewport.update(width, height, false);//TODO centercamera thing is not applied
		viewport.setScreenPosition(screenX, (int)(screenY + Constants.PIANOROLL_BOT_PADDING * (height/Constants.VIEWPORT_DIM_Y)));
	}
}
