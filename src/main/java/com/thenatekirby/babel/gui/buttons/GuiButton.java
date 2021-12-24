package com.thenatekirby.babel.gui.buttons;

import com.thenatekirby.babel.core.api.IClickableGuiView;
import com.thenatekirby.babel.core.gui.ClickContext;
import com.thenatekirby.babel.gui.views.GuiTextureView;

import javax.annotation.Nonnull;

// ====---------------------------------------------------------------------------====

public class GuiButton extends GuiTextureView implements IClickableGuiView {
    private IOnClickListener listener;

    public GuiButton(int x, int y, int width, int height, IGuiTexture texture) {
        super(x, y, width, height, texture);
    }

    public void setListener(IOnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onClick(@Nonnull ClickContext context) {
        playButtonClick();
        if (this.listener != null) {
            listener.onButtonClick(context);
        }

        return super.onClick(context);
    }

    // ====---------------------------------------------------------------------------====

    public interface IOnClickListener {
        void onButtonClick(@Nonnull ClickContext context);
    }
}
