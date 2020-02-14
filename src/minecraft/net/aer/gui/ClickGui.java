package net.aer.gui;

import net.aer.Aer;
import net.aer.gui.clickgui.elements.Element;
import net.aer.gui.clickgui.elements.ModuleButton;
import net.aer.gui.clickgui.elements.Panel;
import net.aer.module.Category;
import net.aer.utils.config.ConfigHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class ClickGui extends GuiScreen {

	public static ArrayList<Panel> panels;
	public Properties ClickGuiProps;
	private GuiStyle style;

	private ModuleButton hoveredModule;

	public boolean blurMode;

	private boolean isBlurred;

	public ClickGui(GuiStyle styleIn) {

		style = styleIn;

		ClickGuiProps = ConfigHandler.loadSettings("ClickGuiProps", new Properties());

		createPanels(style);

	}


	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		ScaledResolution res = new ScaledResolution(Aer.minecraft);
		this.style.setRes(res);
		this.style.setMouseX(mouseX);
		this.style.setMouseY(mouseY);

		for (Panel p : panels) {
			p.drawScreen(mouseX, mouseY);
		}

		if (blurMode != isBlurred) {
			if (blurMode) {
				if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
					if (mc.entityRenderer.theShaderGroup != null) {
						mc.entityRenderer.theShaderGroup.deleteShaderGroup();
					}
					mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
				}
				isBlurred = true;
			} else {
				if (mc.entityRenderer.theShaderGroup != null) {
					mc.entityRenderer.theShaderGroup.deleteShaderGroup();
					mc.entityRenderer.theShaderGroup = null;
				}
				isBlurred = false;
			}
		}
	}

	private void createPanels(GuiStyle style) {

		panels = new ArrayList<>();

		int px;
		int py;
		int pyo = 10;
		boolean panelExtended = false;

		for (Category c : Category.values()) {
			if (c != Category.HIDDEN) {
				String panelName = c.name().toUpperCase();
				if (ClickGuiProps.containsKey(panelName + "xPos")) {
					px = Integer.parseInt(ClickGuiProps.getProperty(panelName + "xPos"));
				} else {
					px = 10;
				}
				if (ClickGuiProps.containsKey(panelName + "yPos")) {
					py = Integer.parseInt(ClickGuiProps.getProperty(panelName + "yPos"));
				} else {
					py = pyo;
				}
				if (ClickGuiProps.containsKey(panelName + "extended")) {
					panelExtended = Boolean.parseBoolean((ClickGuiProps.getProperty(panelName + "extended")));
				} else {
					panelExtended = false;
				}
				panels.add(new Panel(px, py, style, panelName, panelExtended, c, this));

				pyo += 50;
			}
		}
	}

	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (Panel p : panels) {
			p.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}

	public void mouseReleased(int mouseX, int mouseY, int state) {
		for (Panel p : panels) {
			p.mouseReleased(mouseX, mouseY, state);
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE && !this.keybindListening()) {
			Aer.minecraft.displayGuiScreen((GuiScreen) null);
		}
		for (Panel p : panels) {
			p.keyTyped(typedChar, keyCode);
		}
	}

	private boolean keybindListening() {
		return false;
	}

	@Override
	public void initGui() {
		if (blurMode) {
			if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
				if (mc.entityRenderer.theShaderGroup != null) {
					mc.entityRenderer.theShaderGroup.deleteShaderGroup();
				}
				mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
			}
		}
	}

	public void onGuiClosed() {
		if (mc.entityRenderer.theShaderGroup != null) {
			mc.entityRenderer.theShaderGroup.deleteShaderGroup();
			mc.entityRenderer.theShaderGroup = null;
		}
		for (Panel p : panels) {
			ClickGuiProps.setProperty(p.getName() + "xPos", "" + p.x);
			ClickGuiProps.setProperty(p.getName() + "yPos", "" + p.y);
			ClickGuiProps.setProperty(p.getName() + "extended", "" + p.isExtended());
			for (ModuleButton m : p.getModules()) {
				ClickGuiProps.setProperty(m.getName() + "extended", "" + m.isExtended());
			}
		}
		ConfigHandler.saveSettings("ClickGuiProps", ClickGuiProps);
	}


	public void setHoveredModule(ModuleButton moduleButton) {
		this.hoveredModule = moduleButton;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public void mouseScrolled(int scroll) {
		for (Panel p : panels) {
			p.scroll(scroll);
		}
	}

	public void setCol(Color col) {
		style.setCol(col);
		for (Panel p : panels) {
			for (ModuleButton button : p.getModules()) {
				for (Element e : button.menuElements) {
					e.updateCols();
				}
			}
		}
	}

}