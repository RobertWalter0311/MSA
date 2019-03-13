package com.group10.msa.MapObjects;

public class Agent {

    private float x;
    private float y;
    private float direction;

    public Agent(float xStart, float yStart, float startDir){
        this.x = xStart;
        this.y = yStart;
        this.direction = startDir;
    }

    public void move(float speed){
        if(direction < Math.PI/2){
            x += speed*Math.sin(direction);
            y += speed*Math.cos(direction);
        }
        if(direction >= Math.PI/2 && direction < Math.PI){
            x += speed*Math.cos(direction - Math.PI/2);
            y -= speed*Math.sin(direction - Math.PI/2);
        }
        if(direction >= Math.PI && direction < (3*Math.PI)/2){
            x -= speed*Math.sin(direction - Math.PI);
            y -= speed*Math.cos(direction - Math.PI);
        }
        if(direction >= (3*Math.PI)/2){
            x -= speed*Math.cos(direction - (3*Math.PI)/2);
            y += speed*Math.sin(direction - (3*Math.PI)/2);
        }
    }

    public float getX(){
        return x;
    }

    public float getY() {
        return y;
    }

    public float coordToMetres(float coord){
        float metres = (float)0.05*coord;
        return  metres;
    }
    public float metresToCoord(float metres){
        float coord = (float)20*metres;
        return  metres;
    }
}
