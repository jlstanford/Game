package jlstanford.bsu.edu.game;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Will handle switching stages and code behavior on scene switch
 * is instantiated from start screen
 */
public class Gameplay implements Serializable{

    private Player player;
    private Scene previousScene;
    private Scene currentScene;
    private java.util.Map<Item,Item> navigationalMap;
    private java.util.Map<Item,Item> validCombinationsMap = new HashMap<Item,Item>();
    private java.util.Map<java.util.Map<Item,Item>, Item> combinationResultsMap;

    public Gameplay(String playerName){
        this.player = new Player(playerName,this);
        populateCombinationsMap();
    }

    private void populateCombinationsMap() {
        validCombinationsMap.put(new GuitarStrings(player), new HorseHair(player));
    }

    private void updateScene(Scene scene){
        previousScene = currentScene;
        currentScene = scene;
    }

    public java.util.Map getValidCombinationsMap(){
        return validCombinationsMap;
    }

    public Scene getScene(){
        return currentScene;
    }

    public Scene getPreviousScene(){
        return previousScene;
    }

    public void handleItemClick(Item item){
        if(player.hasInInventory(item)){
            player.choose(item);
        }else{player.addToInventory(item);}
    }

    /*when button is pressed*/
    public void handleUseClick(Item item){
        player.useItem(item);
    }


    public void switchToScene(Scene scene) {
        updateScene(scene);
    }

    public java.util.Map<java.util.Map<Item, Item>, Item> getCombinationResultsMap() {
        return combinationResultsMap;
    }


    public Gameplay getGame(){
        return this;
    }

    public Player getPlayer() {
        return player;
    }
}
