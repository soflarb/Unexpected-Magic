package com.mygdx.game.gameEngine.managers;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.gameEngine.components.*;
import com.mygdx.game.model.IPlayer;


import com.mygdx.game.model.song.INote;
import com.mygdx.game.model.song.ISong;
import com.mygdx.game.model.song.IVoice;
import com.mygdx.game.utils.Constants;

import java.util.LinkedList;
import java.util.List;

/**
 * A factory class that handles creation of entities.
 * @author soflarb
 * Revised by rastom & car0b1nius
 * 
 */
public class EntityFactory {

	private static final SpriteFactory spriteFactory = new SpriteFactory();
	private static int numberOfPlayers;

	public static List<Entity> createNoteEntities(List<? extends IPlayer> players){
		numberOfPlayers = players.size();
		List<Entity> entities = new LinkedList<>();
		for (int playerIndex = 0; playerIndex < players.size(); playerIndex ++){
			IPlayer player = players.get(playerIndex);
			IVoice voice = player.getVoice();
			for (int tick = 0; tick < voice.length(); tick ++){
				INote note = voice.noteAtTick(tick);
				if (note == null || note.isRest()) continue;
				entities.add(createNoteEntity(note,voice,playerIndex, tick));
			}
		}
		return entities;
	}

	public static List<Entity> createNoteEntities(ISong song) {
		numberOfPlayers = 0;
		List<Entity> entities = new LinkedList<>();
		int iteration = 0;
		for (IVoice voice : song.getVoices()){
			for (int tick = 0; tick < voice.length(); tick++) {
				INote note = voice.noteAtTick(tick);
				if (note == null || note.isRest()) continue;
				entities.add(createNoteEntity(note, voice, iteration % 4, tick));
			}
			iteration++;
		}
		return entities;
	}


	public static Entity createNoteEntity(INote note, IVoice voice, int playerIndex, int tick){

		Entity entity = new Entity();
		int laneIndex = (note.getPitch() % Constants.NUMBER_OF_LANES);
		//int posX = NoteLanes.xCoordinate(lane, playerIndex, numberOfPlayers);
		//int posX = (int) (laneIndex*Constants.LANE_WIDTH+(((Constants.LANE_WIDTH-SpriteFactory.noteSectionWidth)/(numberOfPlayers-1)*playerIndex)));
		int posX;
		if (numberOfPlayers == 0){
			posX = Math.round( (laneIndex*Constants.LANE_WIDTH+(((Constants.LANE_WIDTH-SpriteFactory.noteSectionWidth)/3)*playerIndex)));
		}
		else if (numberOfPlayers == 1){
			posX = Math.round( laneIndex*Constants.LANE_WIDTH);
		}
		else{
			posX = Math.round( (laneIndex*Constants.LANE_WIDTH+(((Constants.LANE_WIDTH-SpriteFactory.noteSectionWidth)/(numberOfPlayers-1))*playerIndex))); 
		}
	
		
		int posY = tick*SpriteFactory.noteSectionHeight;
		PositionComponent positionComponent = new PositionComponent(posX, posY);
		NoteComponent noteComponent = new NoteComponent(note);
		CompositeSpriteComponent spriteComponent = new CompositeSpriteComponent(spriteFactory.createSprites(note.getDuration(),playerIndex,posX,posY));
	
		VoiceComponent voiceComponent = new VoiceComponent(voice);
		entity.add(positionComponent).add(noteComponent).add(voiceComponent);
		entity.add(spriteComponent);
		return entity;
	}


}

