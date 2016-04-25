package jlstanford.bsu.edu.game;

import java.io.Serializable;

/**
 * Created by Jessica on 4/23/2016.
 */
public class HorseHair implements Item,Serializable {

    private final Player player;
    private boolean canBeUsed;

    public HorseHair(Player player){

        this.player = player;
    }

    @Override
    public void beUsed() {
        if(player.getGame().getScene() == Scene.VIENNA_PRACTICE){
            canBeUsed = false;
        }else if(player.getGame().getScene() == Scene.VIENNA_STREET){
            canBeUsed = false;
        }else if (player.getGame().getScene() == Scene.NAVIGATION_VIEW){
            canBeUsed = false;
        }
    }


    public boolean getUsability() {
        return canBeUsed;
    }

}
