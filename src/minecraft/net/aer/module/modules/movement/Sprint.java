package net.aer.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.aer.events.world.EventPreUpdate;
import net.aer.module.base.Category;
import net.aer.module.base.Module;


/**
 * Forcefully toggles sprinting on when active
 */
public class Sprint extends Module {

	/**
	 * Constructor for Sprint
	 */
	public Sprint() {
		super("Sprint", Category.MOVEMENT, "Permanantly toggles sprinting");

	}

	@EventTarget
	public void preUpdate(EventPreUpdate event) {
		minecraft.player.setSprinting(true);
	}

	public void onDisable() {
		minecraft.player.setSprinting(false);
	}

}
