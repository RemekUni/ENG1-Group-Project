package com.C11.game;

public class HiddenItem extends Hitbox{
    public HiddenItem(float x, float y, float width, float height){
        super(x, y, width, height);
    }

    @Override
    public void onHit(){
        //Collect an item
    }
}
