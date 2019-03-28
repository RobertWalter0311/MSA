package com.group10.msa.MapObjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.group10.msa.ScreenManager.BuildAssets;

public class MapObject {
    public enum MapType{
        Grass, Dirt, Sand, Water, Wall, Tree , Tower, Target,Null
    }

    private MapType type;

    private Vector2 pos;

    public MapObject(MapType type, Vector2 pos){
        this.type = type;
        this.pos = pos;

    }

    public void draw(SpriteBatch batch){
        //draw the different sprites

        if(type == MapType.Grass){
            batch.draw(BuildAssets.grass, pos.x , pos.y );
        }else if(type == MapType.Dirt){
            batch.draw(BuildAssets.dirt, pos.x , pos.y );
        }else if(type == MapType.Sand){
            batch.draw(BuildAssets.sand, pos.x , pos.y );
        }else if(type == MapType.Water){
            batch.draw(BuildAssets.water, pos.x , pos.y );
        }else if(type == MapType.Wall){
            batch.draw(BuildAssets.wall, pos.x-1 , pos.y );
        }else if(type == MapType.Tree){
            batch.draw(BuildAssets.tree, pos.x , pos.y );
        }else if(type == MapType.Target){
            batch.draw(BuildAssets.target, pos.x , pos.y );
        } else if(type == MapType.Tower){
            batch.draw(BuildAssets.tower, pos.x , pos.y );
        }

    }



    public Vector2 getPos(){
        return pos;
    }

    public void setPos(Vector2 pos){
        this.pos=pos;
    }

    public MapType getType(){
        return type;
    }

}

