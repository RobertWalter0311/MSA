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

    //calculates x and y components needed to move (potentially diagonally) at 1.4 metres a second
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

    //turns at PI/60 radians every frame
    //capable of determine which way it is fastest to turn, clockwise or anti-clockwise
    public void turn(float newDir){
        if(direction != newDir){
            if(Math.abs(direction-newDir) > Math.PI/60){
                if(newDir < direction){
                    if(Math.abs(direction-newDir) > Math.PI) {
                        direction += Math.PI / 60;
                    }
                    else{
                        direction -= Math.PI / 60;
                    }
                }
                else if(direction < newDir){
                    if(Math.abs(direction-newDir) > Math.PI) {
                        direction -= Math.PI / 60;
                    }
                    else{
                        direction += Math.PI / 60;
                    }
                }
            }
            else{
                direction = newDir;
            }
        }
        if(direction > 2* Math.PI){
            direction -= 2*Math.PI;
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
