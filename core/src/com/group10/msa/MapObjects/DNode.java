package com.group10.msa.MapObjects;

public class DNode {

    private float keyL;
    private float keyR;

    private float g; // cost estimate from start to this node
    private float h; // cost estimate from this node to goal
    private float f; // g + h
    private float rhs = Float.MAX_VALUE; // one step lookahead of g
    private DNode par; // parent pointer: points to parent state in search tree
    // parent pointers are used to find the cost minimal path
    private float[] key = new float[2];

    private float xPos;
    private float yPos;

    private int object = 0;
    public DNode(float x, float y){
        this.xPos = x;
        this.yPos = y;
    }

    public float getX(){return xPos;}
    public float getY(){return yPos;}

    public float getG(){return g;}
    public void setG(float G){this.g = G;}

    public float getH(){return h;}
    public void setH(float H){this.h = H;}

    public float getF(){return f;}
    public void setF(float F){this.f = F;}

    public float getRHS(){return rhs;}
    public void setRHS(float RHS){this.rhs = RHS;}

    public DNode getPar(){return par;}
    public void setPar(DNode PAR){this.par = PAR;}

    public float getKey(){return key[0];}
    public void setKey(float[] ke){this.key = ke;}

    public void setObject(int obj){this.object = obj;}
    public int getObject(){return this.object;}

    public void calculateKey( float k, DNode goal){
        float min2 = this.getG();
        if(this.getRHS()< min2) min2 =this.getRHS(); //
        float min = min2 + k;
        min += heuristic(goal);
        key[0] = min;
        key[1] = min2;
    }

    public float [] calculateKeyval( float k, DNode goal){
        float min2 = this.getG();
        if(this.getRHS()< min2) min2 =this.getRHS(); //
        float min = min2 + k;
        min += heuristic(goal);
        float[] result = {min, min2};
        return result;
    }

    public float heuristic (DNode node){
        float h = (this.xPos-node.getX())*(this.xPos-node.getX()) + (this.yPos-node.getY())*(this.yPos-node.getY());
        return (float) Math.sqrt(h);
        //float h = (float) Math.abs(this.xPos-node.getX()) + Math.abs(this.yPos-node.getY());
        //return h;
    }
}
