package jlstanford.bsu.edu.game;

import android.graphics.Point;

import java.io.Serializable;
import java.util.*;
import java.util.Map;

/**
 * Created by Jessica on 4/22/2016.
 */
public class Player implements Serializable{
    private String name;
    private List<Item> inventory;
    private Point position;
    private Item[] chosenItems = new Item[2];
    private Gameplay game;


    public Player(String playerName, Gameplay game) {
        this.name = playerName;
        this.game = game;
        inventory = new ArrayList<Item>();
        inventory.add(new jlstanford.bsu.edu.game.Map(this));
    }

    public Point getPlayerPostion(){
        return position;
    }

    public void addToInventory(Item item){
        inventory.add(item);
    }

    public void removeFromInventory(Item item){
        int indexOfItem= inventory.indexOf(item);
        inventory.remove(indexOfItem);
    }

    public Gameplay getGame(){
        return game;
    }
    
    private int arrayIndexCounter = 0;
    
    public void useItem(Item item){
        if(arrayIndexCounter == 1){
            item.beUsed();
        }else if(arrayIndexCounter ==2 ){
            combineChosenItems();
        }else{}
    }

    private void combineChosenItems() {
        Item itemOne = chosenItems[0];
        Item itemTwo = chosenItems[1];
        combineItems(itemOne, itemTwo); //builder pattern?
    }

    private void combineItems(Item itemOne, Item itemTwo) {
        Map validCombinationsMap = this.getGame().getValidCombinationsMap();
        Map combinationResultsMap = this.getGame().getCombinationResultsMap();

        if(itemOne == validCombinationsMap.get(itemTwo)){

        }
    }


    public void choose(Item item){
        if(arrayIndexCounter < 2) {
            chosenItems[arrayIndexCounter] = item;
            arrayIndexCounter++;
        }else{}
    }

    public boolean hasInInventory(Item item) {

        if(item instanceof GuitarStrings){
             return checkForGuitarStrings();
        }else if(item instanceof HorseHair){
           return playerHasHorseHair();
        }else {return false;}

    }

    private boolean playerHasHorseHair() {
        boolean hasHorseHair = false;
        for(int i = 0; i<inventory.size();i++){
            if(inventory.get(i) instanceof HorseHair){
                hasHorseHair = true;
            }
        }
        return hasHorseHair;
    }

    private boolean checkForGuitarStrings() {
        boolean hasGuitarString = false;
        for(int i = 0; i<inventory.size();i++){
            if(inventory.get(i) instanceof GuitarStrings){
                hasGuitarString = true;
            }
        }
        return hasGuitarString;
    }
}
