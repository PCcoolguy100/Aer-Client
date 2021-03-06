package me.aer.implementation.module.modules.util;

import com.darkmagician6.eventapi.EventTarget;
import me.aer.config.valuesystem.BooleanValue;
import me.aer.config.valuesystem.NumberValue;
import me.aer.implementation.module.base.Category;
import me.aer.implementation.module.base.Module;
import me.aer.implementation.module.base.ModuleManager;
import me.aer.implementation.utils.player.PlayerUtil;
import me.aer.injection.events.render.EventRenderOverlays;
import me.aer.visual.render.Fonts;
import me.aer.visual.render.LengthSorter;
import me.aer.visual.render.RainbowUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class HUD extends Module {

    private DecimalFormat df2 = new DecimalFormat("#");
    private RainbowUtil rbw = new RainbowUtil(3);
    private BooleanValue useGuiColours = new BooleanValue("UseGUIColors", "Whether to use the GUI colours for the HUD", true);
    private NumberValue<Float> red = new NumberValue<>("Red", "Red component for the HUD colour", 0.5f, 0.0f, 1f, true);
    private NumberValue<Float> green = new NumberValue<>("Green", "Green component for the HUD colour", 0.9f, 0.0f, 1f, true);
    private NumberValue<Float> blue = new NumberValue<>("Blue", "Blue component for the HUD colour", 0.6f, 0.0f, 1f, true);
    private BooleanValue coords = new BooleanValue("Coords", "Shows overworld coords", true);
    private BooleanValue netherCoords = new BooleanValue("NetherCoords", "Shows nether coords", true);
    private BooleanValue direction = new BooleanValue("Direction", "Shows which way you're facing", true);
    private BooleanValue showYpos = new BooleanValue("ShowYPos", "Shows your Y position (height)", false);
    private BooleanValue armor = new BooleanValue("Armor", "Shows your armor on your hotbar", true);
    private BooleanValue modList = new BooleanValue("ModList", "Shows the active modules onscreen", false);
    private BooleanValue rainbow = new BooleanValue("Rainbow", "Makes the HUD cycle through rainbow colours!", false);
    private HashMap animations = new HashMap<Module, Integer>();
    private boolean hasReset = false;


    public HUD() {
        super("HUD", Category.UTIL, "Shows useful information onscreen");
    }

    public void resetAnimations() {
        animations.clear();
    }

    public void onDisable() {
        animations.clear();
    }

    @EventTarget
    public void onDraw2D(EventRenderOverlays event) {
        if (minecraft.world == null) {
            return;
        }
        ScaledResolution res = new ScaledResolution(minecraft);
        int col;
        int darkcol;
        int lightcol;
        if (rainbow.getObject()) {
            col = rbw.getRGB();
            darkcol = rbw.getCol().darker().getRGB();
            lightcol = rbw.getCol().brighter().getRGB();
            rbw.cycleColoursCustomSpeed();
            rbw.randomiseDirection();
            rbw.cycleColorsRandom();
            if (!modList.getObject() && !hasReset) {
                animations.clear();
                hasReset = true;
            }
            if (modList.getObject() && hasReset) {
                hasReset = false;
            }
        } else if (useGuiColours.getObject()) {
            Color temp = ((ClickGui) ModuleManager.getModuleByName("ClickGui")).getColNoAlpha();
            col = temp.getRGB();
            darkcol = temp.darker().getRGB();
            lightcol = temp.brighter().getRGB();
        } else {
            Color temp = new Color(red.getValue(), green.getValue(), blue.getValue(), 1f);
            col = temp.getRGB();
            darkcol = temp.darker().getRGB();
            lightcol = temp.brighter().getRGB();
        }
		ScaledResolution scaledRes = new ScaledResolution(minecraft);
		if (coords.getObject()) {
			if (netherCoords.getObject()) {
				if (showYpos.getObject()) {
					if (minecraft.player.dimension == 0) {
                        rend.drawString(
                                Fonts.big, "\u00a7pXYZ: \u00a7r" + df2.format(minecraft.player.posX - 0.5)
                                        + " " + df2.format(minecraft.player.posY) + " " + df2.format(minecraft.player.posZ - 0.5) + " \u00a7p[\u00a7r"
                                        + df2.format(minecraft.player.posX / 8 - 0.5)
                                        + " " + df2.format(minecraft.player.posY) + " " + df2.format(minecraft.player.posZ / 8 - 0.5) + "\u00a7p]"
                                ,
                                5f, (float) scaledRes.getScaledHeight() - 18, col, true
                        );
                    } else {
                        rend.drawString(
                                Fonts.big, "\u00a7pXYZ: \u00a7r" + df2.format(minecraft.player.posX * 8 - 0.5)
                                        + " " + df2.format(minecraft.player.posY) + " " + df2.format(minecraft.player.posZ * 8 - 0.5) + " \u00a7p[\u00a7r"
                                        + df2.format(minecraft.player.posX - 0.5)
                                        + " " + df2.format(minecraft.player.posY) + " " + df2.format(minecraft.player.posZ - 0.5) + "\u00a7p]"
                                ,
                                5f, (float) scaledRes.getScaledHeight() - 18, col, true
                        );
                    }
				} else {
					if (minecraft.player.dimension != -1) {
                        rend.drawString(
                                Fonts.big, "\u00a7pXZ: \u00a7r" + df2.format(minecraft.player.posX - 0.5)
                                        + " " + df2.format(minecraft.player.posZ - 0.5) + " \u00a7p[\u00a7r"
                                        + df2.format(minecraft.player.posX / 8 - 0.5)
                                        + " " + df2.format(minecraft.player.posZ / 8 - 0.5) + "\u00a7p]"
                                ,
                                5f, (float) scaledRes.getScaledHeight() - 18, col, true
                        );
                    } else {
                        rend.drawString(
                                Fonts.big, "\u00a7pXZ: \u00a7r" + df2.format(minecraft.player.posX * 8 - 0.5)
                                        + " " + df2.format(minecraft.player.posZ * 8 - 0.5) + " \u00a7p[\u00a7r"
                                        + df2.format(minecraft.player.posX - 0.5)
                                        + " " + df2.format(minecraft.player.posZ - 0.5) + "\u00a7p]"
                                ,
                                5f, (float) scaledRes.getScaledHeight() - 18, col, true
                        );
                    }
				}
			} else {
				if (showYpos.getObject()) {
					if (minecraft.player.dimension == -1) {
                        rend.drawString(
                                Fonts.big, "\u00a7pXYZ: \u00a7r" + df2.format(minecraft.player.posX * 8 - 0.5)
                                        + " " + df2.format(minecraft.player.posY) + " " + df2.format(minecraft.player.posZ * 8 - 0.5) + " \u00a7p[\u00a7rOverworld\u00a7p]",
                                5f, (float) scaledRes.getScaledHeight() - 18, col, true
                        );
                    } else {
                        rend.drawString(
                                Fonts.big, "\u00a7pXYZ: \u00a7r" + df2.format(minecraft.player.posX - 0.5)
                                        + " " + df2.format(minecraft.player.posY) + " " + df2.format(minecraft.player.posZ - 0.5),
                                5f, (float) scaledRes.getScaledHeight() - 18, col, true
                        );
                    }
				} else {
					if (minecraft.player.dimension == -1) {
                        rend.drawString(
                                Fonts.big, "\u00a7pXZ: \u00a7r" + df2.format(minecraft.player.posX * 8 - 0.5)
                                        + " " + df2.format(minecraft.player.posZ * 8 - 0.5) + " \u00a7p[\u00a7rOverworld\u00a7p]",
                                5f, (float) scaledRes.getScaledHeight() - 18, col, true
                        );
                    } else {
                        rend.drawString(
                                Fonts.big, "\u00a7pXZ: \u00a7r" + df2.format(minecraft.player.posX - 0.5)
                                        + " " + df2.format(minecraft.player.posZ - 0.5),
                                5f, (float) scaledRes.getScaledHeight() - 18, col, true
                        );
                    }
				}
			}
		} else if (netherCoords.getObject()) {
			if (showYpos.getObject()) {
				if (minecraft.player.dimension == -1) {
                    rend.drawString(
                            Fonts.big, "\u00a7pXYZ: \u00a7r" + df2.format(minecraft.player.posX - 0.5)
                                    + " " + df2.format(minecraft.player.posY) + " " + df2.format(minecraft.player.posZ - 0.5),
                            5f, (float) scaledRes.getScaledHeight() - 18, col, true
                    );
                } else {
                    rend.drawString(
                            Fonts.big, "\u00a7pXYZ: \u00a7r" + df2.format(minecraft.player.posX / 8 - 0.5)
                                    + " " + df2.format(minecraft.player.posY) + " " + df2.format(minecraft.player.posZ / 8 - 0.5) + " \u00a7p[\u00a7rNether\u00a7p]",
                            5f, (float) scaledRes.getScaledHeight() - 18, col, true
                    );
                }
			} else {
				if (minecraft.player.dimension == -1) {
                    rend.drawString(
                            Fonts.big, "\u00a7pXZ: \u00a7r" + df2.format(minecraft.player.posX - 0.5)
                                    + " " + df2.format(minecraft.player.posZ - 0.5),
                            5f, (float) scaledRes.getScaledHeight() - 18, col, true
                    );
                } else {
                    rend.drawString(
                            Fonts.big, "\u00a7pXZ: \u00a7r" + df2.format(minecraft.player.posX / 8 - 0.5)
                                    + " " + df2.format(minecraft.player.posZ / 8 - 0.5) + " \u00a7p[\u00a7rNether\u00a7p]",
                            5f, (float) scaledRes.getScaledHeight() - 18, col, true
                    );
                }
			}
		}

		if (direction.getObject()) {
			if (coords.getObject() || netherCoords.getObject()) {
                rend.drawString(Fonts.big, PlayerUtil.direction3(minecraft.player), 5f, (float) scaledRes.getScaledHeight() - 18 - 15, col, true);
            } else {
                rend.drawString(Fonts.big, PlayerUtil.direction3(minecraft.player), 5f, (float) scaledRes.getScaledHeight() - 18, col, true);
            }
		}


		if (armor.getObject()) {
			int offset = -72;
			int x = (res.getScaledWidth() - res.getScaledWidth() / 2);
			int y = res.getScaledHeight() - 40;
			if (minecraft.player.isSurvival() || minecraft.player.isAdventure()) {
				y -= 16;
			}
			for (ItemStack is : minecraft.player.inventory.armorInventory) {
                rend.drawItem(x, y, is);

                offset += 20;
            }

		}


		if (modList.getObject()) {
			int offset = 0;
			int module = 0;
			int y = 0;

			LengthSorter c = new LengthSorter();
			ArrayList<Module> sortedModules = ModuleManager.visibleModuleList;
			sortedModules.sort(c);

			for (Module m : sortedModules) {
                int animateX = (Integer) animations.getOrDefault(m, -1);

                if (m.isActive()) {
                    if (animateX == -1) {
                        animations.put(m, 0);
                        animateX = 0;
                    } else if (animateX < Fonts.mid.getStringWidth(m.getName()) + 1) {
                        animateX++;
                        animations.replace(m, animateX);
                    }
                } else {
                    if (animateX > 0) {
                        animateX--;
                        animations.replace(m, animateX);
                    } else {
                        animations.remove(m);
                        animateX = 0;
                    }
                }


                rend.drawGradHoriz(7, res.getScaledWidth() - animateX + 1, y + offset, (int) (res.getScaledWidth() + Fonts.mid.getStringWidth(m.getName()) - animateX + 2), y + offset + 12, darkcol, 0x99777777);
                rend.drawVerticalLine(res.getScaledWidth() - animateX, y + offset - 1, y + offset + 12, lightcol);
                rend.drawString(Fonts.mid, m.getName(), res.getScaledWidth() - animateX + 1, y + offset + 1, col, true);

                if (animateX != 0) {
                    offset += 12;
                }
            }
		}
        GlStateManager.enableTexture2D();
	}
}
