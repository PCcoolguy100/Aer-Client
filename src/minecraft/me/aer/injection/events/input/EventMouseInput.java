package me.aer.injection.events.input;

import com.darkmagician6.eventapi.events.Event;

public class EventMouseInput implements Event {

	public int button;

	public EventMouseInput(int button) {
		this.button = button;
	}
}
