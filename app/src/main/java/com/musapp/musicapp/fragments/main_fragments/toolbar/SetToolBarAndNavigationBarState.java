package com.musapp.musicapp.fragments.main_fragments.toolbar;

public interface SetToolBarAndNavigationBarState {
    void setTitle(int titleId);
    void setTitle(String title);
    void hideToolBar();
    void showToolBar();
    void hideNavigationBar();
    void showNavigationBar();
    void showBackArrow(boolean state);
}
