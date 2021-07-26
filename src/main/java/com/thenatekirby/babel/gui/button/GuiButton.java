package com.thenatekirby.babel.gui.button;

import com.thenatekirby.babel.api.IClickableGuiView;
import com.thenatekirby.babel.gui.GuiView;
import com.thenatekirby.babel.gui.core.ClickContext;
import com.thenatekirby.babel.gui.core.GuiTextureView;

import javax.annotation.Nonnull;

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

    public interface IOnClickListener {
        void onButtonClick(@Nonnull ClickContext context);
    }
}
