package me.aer.implementation.utils.other;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.minecraft.client.resources.SkinManager.SkinAvailableCallback;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

public class Capes {

    private static JsonObject capesJson;
    private static JsonParser jsonParser = new JsonParser();

    public static void getCapes(GameProfile gameprofile, Map skinMap, SkinAvailableCallback callback) {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://raw.githubusercontent.com/Drakyen/Aer-Client/master/capes.json").openConnection();
            connection.connect();
                capesJson = jsonParser.parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
            }catch(Exception e) {
                e.printStackTrace();
                return;
            }
            try {
                if(capesJson.has(gameprofile.getName())) {
                    skinMap.put(Type.CAPE, new MinecraftProfileTexture(capesJson.get(gameprofile.getName()).getAsString(), null));
                }
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
}
