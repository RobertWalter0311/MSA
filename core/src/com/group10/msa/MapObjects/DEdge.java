package com.group10.msa.MapObjects;

public class DEdge {
    private DNode u;
    private DNode v;
    private float cost = 1;
    private float newCost = 0;
    public DEdge (DNode start, DNode end, float c){
        this.u = start;
        this.v = end;
        this.cost = c;
    }
    public DEdge (DNode start, DNode end){
        this.u = start;
        this.v = end;
    }
    public void setU(DNode U){this.u = U;}
    public DNode getU(){return u;}

    public void setV(DNode V){this.v = V;}
    public DNode getV(){return v;}

    public void setCost(float c){this.cost = c;}
    public float getCost(){return cost;}

    public void setNewCost(float nC){this.newCost = nC;}
    public float getNewCost(){return newCost;}
    public void changeCost(){
        this.cost = this.newCost;
        this.newCost = 0;
    }
}
