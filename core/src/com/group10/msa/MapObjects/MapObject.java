package com.group10.msa.MapObjects;

import com.badlogic.gdx.math.Vector2;

public class MapObject {
    public enum MapType{
        Grass, Dirt, Sand, Water, Wall, Tree , Tower, Target,
    }

    private MapType type;

    private Vector2 pos;

    public MapObject(MapType type, Vector2 pos){

        this.type = type;
        this.pos = pos;

    }

    public Vector2 getPos(){
        return pos;
    }

    public MapType getType(){
        return type;
    }

}

