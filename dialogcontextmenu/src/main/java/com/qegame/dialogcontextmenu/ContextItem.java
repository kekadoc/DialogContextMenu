package com.qegame.dialogcontextmenu;

public abstract class ContextItem implements DialogContextItem {

    private String text;

    public ContextItem(String text) {
        this.text = text;
    }

    @Override
    public final String getText() {
        return text;
    }

}
