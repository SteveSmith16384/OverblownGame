package com.scs.splitscreenfps.game.systems;

import java.util.LinkedList;

import com.scs.basicecs.ISystem;
import com.scs.splitscreenfps.BillBoardFPS_Main;
import com.scs.splitscreenfps.game.entities.AvatarFactory;

public class SpeechSystem implements ISystem {

	public LinkedList<String> files;
	public long next_play_time;

	public SpeechSystem() {
		files = new LinkedList<String>();
	}

	@Override
	public void process() {
		synchronized (files) {
			if (this.files.size() > 0) {
				if (this.next_play_time < System.currentTimeMillis()) {
					String file = this.files.remove();
					if (file.length() > 0) {
						BillBoardFPS_Main.audio.play(file);
						this.next_play_time = System.currentTimeMillis() + 4000;
					}
				}
			}
		}
	}


	public void addFile(String s) {
		synchronized (files) {
			this.files.add(s);
		}
	}


	public static String getFileForCharacter(int ch) {
		switch (ch) {
		case AvatarFactory.CHAR_PHARTA:
			return "speech/forgottentraining.wav";
		default:
			return "";
		}
	}

}
