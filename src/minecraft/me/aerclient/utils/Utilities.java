package me.aerclient.utils;

import me.aerclient.render.render2D.RenderUtils2D;
import me.aerclient.render.render3D.RenderUtils3D;
import me.aerclient.utils.exploits.ClipUtil;
import net.minecraft.client.Minecraft;

public interface Utilities {
    public static final RenderUtils2D rend2D = new RenderUtils2D();
    public static final RenderUtils3D rend3D = new RenderUtils3D();
    public static final ClipUtil clipUtil = new ClipUtil();
    public static final Minecraft minecraft = Minecraft.getMinecraft();
}