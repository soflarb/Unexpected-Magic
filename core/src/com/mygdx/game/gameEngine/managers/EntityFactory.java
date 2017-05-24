package com.mygdx.game.gameEngine.managers;

import com.badlogic.ashley.core.Entity;
import com.mygdx.game.gameEngine.components.*;
import com.mygdx.game.model.NoteLanes;
import com.mygdx.game.model.Player;
import com.mygdx.game.model.song.INote;
import com.mygdx.game.model.song.ISong;
import com.mygdx.game.model.song.IVoice;
import com.mygdx.game.utilities.file.Constants;
import java.util.LinkedList;
import java.util.List;

/**
 * A factory class that handles creation of entities.
 * @author soflarb
 * Revised by rastom & car0b1nius
 */
public class EntityFactory {

	private static final SpriteFactory spriteFactory = new SpriteFactory();
	private static int numberOfPlayers;

	public static List<Entity> createNoteEntities(List<Player> players){
		numberOfPlayers = players.size();
		List<Entity> entities = new LinkedList<>();
		for (int playerIndex = 0; playerIndex < players.size(); playerIndex ++){
			Player player = players.get(playerIndex);
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
		List<Entity> entities = new LinkedList<>();
		for (IVoice voice : song.getVoices()){
			for (int tick = 0; tick < voice.length(); tick++) {
				INote note = voice.noteAtTick(tick);
				if (note == null || note.isRest()) continue;
				entities.add(createNoteEntity(note, voice, 0, tick));
			}
		}
		return entities;
	}


	public static Entity createNoteEntity(INote note, IVoice voice, int playerIndex, int tick){

		Entity entity = new Entity();
		int laneIndex = (note.getPitch() % Constants.NUMBER_OF_LANES);
		//int posX = NoteLanes.xCoordinate(lane, playerIndex, numberOfPlayers);
		//int posX = (int) (laneIndex*Constants.LANE_WIDTH+(((Constants.LANE_WIDTH-SpriteFactory.noteSectionWidth)/(numberOfPlayers-1)*playerIndex)));
		int posX = (int) (laneIndex*Constants.LANE_WIDTH+(((Constants.LANE_WIDTH-SpriteFactory.noteSectionWidth)/(numberOfPlayers)*playerIndex))); //TODO fix x coord
		
		int posY = tick*SpriteFactory.noteSectionHeight;
		PositionComponent positionComponent = new PositionComponent(posX, posY);
		NoteComponent noteComponent = new NoteComponent(note);
		CompositeSpriteComponent spriteComponent = new CompositeSpriteComponent(spriteFactory.createSprites(note.getDuration(),playerIndex,posX,posY));
		HitComponent hitComponent = new HitComponent();
		VoiceComponent voiceComponent = new VoiceComponent(voice);
		entity.add(positionComponent).add(noteComponent).add(hitComponent).add(voiceComponent);
		entity.add(spriteComponent);
		return entity;
	}


}

