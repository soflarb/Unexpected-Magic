package com.mygdx.game.model;

import com.mygdx.game.model.song.INote;

/**
 * Class for non-static parts of Notes
 * @author Arvid
 *
 */

public class TrackableNote implements ITrackableNote {
	
	private INote note;
	private boolean isHit;
	
	public TrackableNote(INote note){
		this.note = note;
		this.isHit = false;
	}

	@Override
	public int getPitch() {
		
		return note.getPitch();
	}

	@Override
	public int getDuration() {
		return note.getDuration();
	}

	@Override
	public boolean isHit() {
		return isHit;
	}

	@Override
	public void hit() {
		isHit = true;

	}

	@Override
	public INote getNote() {
		return note;
	}

}
