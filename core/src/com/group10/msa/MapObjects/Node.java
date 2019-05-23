package com.group10.msa.MapObjects;

import java.util.ArrayList;
import java.util.List;

public class Node
{
    private List<Node> children = null;
    private int xcoords;
    private int ycoords;
    private float f;
    private int g;
    private float h;
    private Node parent;

    public Node(int xcoords, int ycoords){
        this.children = new ArrayList<Node>();
        this.xcoords = xcoords;
        this.ycoords = ycoords;
    }
    public Node(int xcoords, int ycoords, Node thisParent){
        this.children = new ArrayList<Node>();
        this.xcoords = xcoords;
        this.ycoords = ycoords;
        this.parent = thisParent;
    }

    public void addChild(Node child)
    {

        children.add(child);
    }

    public void findNeighbors(){

    }

    public List getChildren(){
        return children;
    }

    public void setG(int gee){
        this.g = gee;
        this.f = this.getG() + this.getH();
    }

    public void setH(float haa){
        this.h = haa;
        this.f = this.getG() + this.getH();
    }

    public void setF(float eff){
        this.f = eff;
    }

    public int getG(){
        return g;
    }

    public float getH() {
        return h;
    }

    public float getF() {
        return f;
    }

    public int getXcoords() {
        return xcoords;
    }

    public int getYcoords() {
        return ycoords;
    }

    public Node getParent() {
        return parent;
    }
}
