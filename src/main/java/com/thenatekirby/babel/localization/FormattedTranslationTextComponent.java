package com.thenatekirby.babel.localization;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class FormattedTranslationTextComponent {
    private final TranslationTextComponent base;

    private final Map<String, String> replacements = new HashMap<>();

    public FormattedTranslationTextComponent(@Nonnull String translationKey) {
        this.base = new TranslationTextComponent(translationKey);
    }

    public FormattedTranslationTextComponent withReplacement(@Nonnull String key, @Nonnull String value) {
        this.replacements.put(key, value);
        return this;
    }

    public StringTextComponent makeTextComponent() {
        String rawValue = base.getString();

        for (Map.Entry<String, String> replacement : replacements.entrySet()) {
            rawValue = rawValue.replace("{{" + replacement.getKey() + "}}", replacement.getValue());
        }

        return new StringTextComponent(rawValue);
    }
}
