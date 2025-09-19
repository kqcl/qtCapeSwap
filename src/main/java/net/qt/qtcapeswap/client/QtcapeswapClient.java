package net.qt.qtcapeswap.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class QtcapeswapClient implements ClientModInitializer {
    public static final String MOD_ID = "qtcapeswap";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Identifier myCapeTexture;

    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            File capeFile = new File(client.runDirectory, "config/cape.png");
            if (capeFile.exists()) {
                try (FileInputStream fis = new FileInputStream(capeFile)) {
                    NativeImage img = NativeImage.read(fis);
                    myCapeTexture = Identifier.of("qtcapeswap", "mycape");
                    client.getTextureManager()
                            .registerTexture(myCapeTexture, new NativeImageBackedTexture(() -> myCapeTexture.toString(), img));
                } catch (IOException e) {
                    LOGGER.error("Failed to load cape texture from config/cape.png", e);
                }
            }
        });
    }
}
