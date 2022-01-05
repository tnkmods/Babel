package com.thenatekirby.babel.core.localization;


import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

// ====---------------------------------------------------------------------------====

public class FormattedTranslatableComponent {
    private final TranslatableComponent base;

    private final Map<String, String> replacements = new HashMap<>();

    public FormattedTranslatableComponent(@Nonnull String translationKey) {
        this.base = new TranslatableComponent(translationKey);
    }

    public FormattedTranslatableComponent withReplacement(@Nonnull String key, @Nonnull String value) {
        this.replacements.put(key, value);
        return this;
    }

    public TextComponent makeTextComponent() {
        String rawValue = base.getString();

        for (Map.Entry<String, String> replacement : replacements.entrySet()) {
            rawValue = rawValue.replace("{{" + replacement.getKey() + "}}", replacement.getValue());
        }

        return new TextComponent(rawValue);
    }
}
