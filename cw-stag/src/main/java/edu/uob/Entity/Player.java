package edu.uob.Entity;

import edu.uob.*;
import java.util.HashSet;

public class Player {
    Location currentlocation;
    HashSet<GameEntity> carryings;
    int Health;
    EntityParser entityParser;
    public Player(){
        currentlocation = entityParser.getBornLocation();

    }
}
