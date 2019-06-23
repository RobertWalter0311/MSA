package com.group10.msa.MapObjects;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Explorer extends Agent{

    private float x;
    private float y;
    private float direction;
    //private boolean walking = true;
    public float speed;
    public float visionDistance = 150;
    public float visionDegree = (float)(Math.PI/4);
    private int[][] world;
    private int[][] agentsVision = new int [8][8];
    public Sprite sprite;
    public float audioRadius = 20;
    public int[][] agentsworld = new int[80][80];


    public Explorer(float xStart, float yStart, float startDir,int[][] world) {
        super(xStart, yStart, startDir,world);
    }

   /* public int[][] explore(float xStart, float yStart){

        for(int i=0; i<agentsVision.length; i++)
            for(int j=0; j<agentsVision.length;j++)
                if (agentsVision[i][j]==0)
                    //move to coordinates of empty field

        return int[][] agentsworld;
    }*/
    // exploring
    //this.getx
}
