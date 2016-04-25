package jlstanford.bsu.edu.game;

import java.io.Serializable;

/**
 * Created by Jessica on 4/22/2016.
 */
public class Map implements Item,Serializable{

    private Player player;


    public Map(Player player){
        this.player = player;
    }

    @Override
    public void beUsed() {
        if(player.getGame().getScene() == Scene.VIENNA_STREET){
            player.getGame().switchToScene(Scene.NAVIGATION_VIEW); //to navigation jlstanford.bsu.edu.game.view
        }else if(player.getGame().getScene() == Scene.VIENNA_PRACTICE){
            player.getGame().switchToScene(Scene.NAVIGATION_VIEW); //to navigation jlstanford.bsu.edu.game.view
        }else if(player.getGame().getScene() == Scene.NAVIGATION_VIEW){
            player.getGame().switchToScene(player.getGame().getPreviousScene()); //to previous scene
        }
    }


}
