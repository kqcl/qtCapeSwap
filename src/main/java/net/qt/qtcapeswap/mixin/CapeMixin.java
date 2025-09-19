package net.qt.qtcapeswap.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

import static net.qt.qtcapeswap.client.QtcapeswapClient.myCapeTexture;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class CapeMixin {
    @Unique
    private static UUID myUuid;

    @Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
    private void injectLocalCape(CallbackInfoReturnable<SkinTextures> cir) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        GameProfile profile = player.getGameProfile();

        // lazy-init uuid from current session
        if (myUuid == null) {
            myUuid = MinecraftClient.getInstance().getSession().getUuidOrNull();
        }

        if (!profile.getId().equals(myUuid)) return;

        if (myCapeTexture != null) {
            SkinTextures base = cir.getReturnValue();
            cir.setReturnValue(new SkinTextures(
                    base.texture(),
                    base.textureUrl(),
                    myCapeTexture,
                    base.elytraTexture(),
                    base.model(),
                    base.secure()
            ));
        }
    }
}
