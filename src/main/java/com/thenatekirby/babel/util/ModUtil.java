package com.thenatekirby.babel.util;

import com.thenatekirby.babel.core.MutableResourceLocation;
import net.minecraftforge.fml.ModList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

// ====---------------------------------------------------------------------------====

@SuppressWarnings("unused")
public class ModUtil {
    public static boolean isModLoaded(@Nonnull String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static boolean isModLoaded(@Nonnull MutableResourceLocation mod) {
        return isModLoaded(mod.getRoot());
    }

    public static boolean isAnyModLoaded(MutableResourceLocation... mods) {
        for (MutableResourceLocation mod : mods) {
            if (isModLoaded(mod.getRoot())) {
                return true;
            }
        }

        return false;
    }

    @Nullable
    public static <T> T callIfModLoaded(@Nonnull String modId, Supplier<Callable<T>> supplier) {
        try {
            if (isModLoaded(modId)) {
                return supplier.get().call();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static void runIfModLoaded(@Nonnull String modId, Supplier<Runnable> supplier) {
        try {
            if (isModLoaded(modId)) {
                supplier.get().run();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
