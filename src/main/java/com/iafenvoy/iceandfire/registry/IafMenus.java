package com.iafenvoy.iceandfire.registry;

import com.iafenvoy.iceandfire.IceAndFire;
import com.iafenvoy.iceandfire.screen.gui.*;
import com.iafenvoy.iceandfire.screen.gui.bestiary.BestiaryScreen;
import com.iafenvoy.iceandfire.screen.menu.*;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import com.iafenvoy.iceandfire.platform.DeferredHolder;
import com.iafenvoy.iceandfire.platform.DeferredRegister;

import java.util.function.Supplier;

public final class IafMenus {
    /**
     * Pass-through payload codec: whatever the server writes into the opening buffer is
     * replayed verbatim to the client menu constructor (mirrors NeoForge's
     * IMenuTypeExtension.create(IContainerFactory) behaviour).
     */
    private static final StreamCodec<RegistryFriendlyByteBuf, FriendlyByteBuf> BUF_CODEC = StreamCodec.of(
            (out, value) -> out.writeBytes(value),
            in -> new FriendlyByteBuf(in.readBytes(in.readableBytes()))
    );

    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(Registries.MENU, IceAndFire.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<DragonMenu>> DRAGON_SCREEN = register("dragon", () -> extended(DragonMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<HippogryphMenu>> HIPPOGRYPH_SCREEN = register("hippogryph", () -> extended(HippogryphMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<HippocampusMenu>> HIPPOCAMPUS_SCREEN = register("hippocampus", () -> extended(HippocampusMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<DragonForgeMenu>> DRAGON_FORGE_SCREEN = register("dragon_forge", () -> extended(DragonForgeMenu::new));
    public static final DeferredHolder<MenuType<?>, MenuType<PodiumMenu>> PODIUM_SCREEN = register("podium", () -> new MenuType<>(PodiumMenu::new, FeatureFlags.VANILLA_SET));
    public static final DeferredHolder<MenuType<?>, MenuType<LecternMenu>> IAF_LECTERN_SCREEN = register("iaf_lectern", () -> new MenuType<>(LecternMenu::new, FeatureFlags.VANILLA_SET));
    public static final DeferredHolder<MenuType<?>, MenuType<BestiaryMenu>> BESTIARY_SCREEN = register("bestiary", () -> extended(BestiaryMenu::new));

    private static <C extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<C>> register(String name, Supplier<MenuType<C>> type) {
        return REGISTRY.register(name, type);
    }

    private static <T extends AbstractContainerMenu> ExtendedMenuType<T, FriendlyByteBuf> extended(ExtendedMenuType.ExtendedFactory<T, FriendlyByteBuf> factory) {
        return new ExtendedMenuType<>(factory, BUF_CODEC);
    }

    /**
     * Registers menu screens. Replaces NeoForge's RegisterMenuScreensEvent handler;
     * uses vanilla MenuScreens.register (opened by the access widener).
     */
    public static void initClient() {
        MenuScreens.register(IAF_LECTERN_SCREEN.get(), LecternScreen::new);
        MenuScreens.register(PODIUM_SCREEN.get(), PodiumScreen::new);
        MenuScreens.register(DRAGON_SCREEN.get(), DragonScreen::new);
        MenuScreens.register(HIPPOGRYPH_SCREEN.get(), HippogryphScreen::new);
        MenuScreens.register(HIPPOCAMPUS_SCREEN.get(), HippocampusScreen::new);
        MenuScreens.register(DRAGON_FORGE_SCREEN.get(), DragonForgeScreen::new);
        MenuScreens.register(BESTIARY_SCREEN.get(), BestiaryScreen::new);
    }
}
