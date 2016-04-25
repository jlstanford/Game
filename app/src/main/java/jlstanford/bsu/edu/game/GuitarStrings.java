package jlstanford.bsu.edu.game;

import java.io.Serializable;

/**
 * Created by Jessica on 4/22/2016.
 */
public class GuitarStrings implements Item,Serializable{

    private Player player;


    public GuitarStrings(Player player){
        this.player = player;
    }


    @Override
    public void beUsed() {
        if(player.getGame().getScene() == Scene.VIENNA_PRACTICE){
            //cant use this here
        }else if(player.getGame().getScene() == Scene.VIENNA_STREET){
            //cant use this here
        }else if (player.getGame().getScene() == Scene.NAVIGATION_VIEW){
            //cant use this here
        }
    }


}
