package com.group10.msa.MapObjects;

public class Radio {

    public static float xPos;
    public static float yPos;
    public static boolean alert = false;

    public Radio(){

    }

    public static void changePos(float newx, float newy){
        xPos = newx;
        yPos = newy;
    }

    public float[] getPos(){
        float[] xyPos = {xPos,yPos};
        return xyPos;
    }

    public void setPos(float[] xyPos){
        xPos = xyPos[0];
        yPos = xyPos[1];
    }

    public void setAlert(boolean isAlerted){
        alert = isAlerted;
    }

    public boolean getAlert(){
        return alert;
    }
}
