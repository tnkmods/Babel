package com.thenatekirby.babel.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class GuiUtil {
    public static void drawHoveringText(PoseStack poseStack, List<? extends FormattedText> textLines, int x, int y, int maxWidth, Font font) {
        Minecraft minecraft = Minecraft.getInstance();
        Screen screen = minecraft.screen;
        if (screen == null) {
            return;
        }

        // text wrapping
        if (maxWidth > 0) {
            boolean needsWrap = textLines.stream()
                    .anyMatch(line -> font.width(line) > maxWidth);

            if (needsWrap) {
                StringSplitter splitter = font.getSplitter();
                textLines = textLines.stream()
                        .flatMap(text -> splitter.splitLines(text, maxWidth, Style.EMPTY).stream())
                        .toList();
            }
        }

        screen.renderComponentTooltip(poseStack, textLines, x, y, font, ItemStack.EMPTY);
    }
}
