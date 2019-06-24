package com.group10.msa.MapObjects;

import java.util.ArrayList;
public class Intruder extends Agent {
    /*Information needed to Escape */
    public float entrypointx;
    public float entrypointy;
    public float goalX;
    public float goalY;
    private boolean targetFound = false;
    private boolean guardDetetcted = false;
    /*Collection of all nodes and edges */
    private ArrayList<DNode> allNodes = new ArrayList<DNode>();
    private ArrayList<DEdge> allEdges = new ArrayList<DEdge>();
    private DNode startNode;
    private DNode goalNode;
    private DNode oldStartNode;
    private DNode oldGoalNode;

    /* D* */
    private ArrayList<DNode> path; //  Current path
    private ArrayList<DNode> openList; // List of inconsistent Nodes
    private float K = 0;

    /* Guards previous position if seen, so we can delete it */
    private ArrayList<DNode> oldPositions = new ArrayList<DNode>();
    public Intruder(float xStart, float yStart, float startDir, int[][] world) {
        super(xStart, yStart, startDir, world);
        entrypointx = (int) xStart/10;
        entrypointy = (int) yStart/10;
        this.goalX = 10;//(int) ((Math.random() * (78) + 1));
        this.goalY = 20; //(int) ((Math.random() * (78) + 1));
        /*Create the nodes for our map, one for each grid */
        for (int i = 0; i < 80; i++)
            for (int j = 0; j < 80; j++)
                allNodes.add(new DNode(i, j));
        /*Create directed edges for each grid. Thus 8 edges for each node. */
        for(DNode n : allNodes){
            /* up, down, left, right */
            if(n.getX() > 0) allEdges.add(new DEdge(n, getNode(n.getX()-1,n.getY())));
            if(n.getX() < 79) allEdges.add(new DEdge(n, getNode(n.getX()+1,n.getY())));
            if(n.getY() > 0) allEdges.add(new DEdge(n, getNode(n.getX(),n.getY()-1)));
            if(n.getY() < 79) allEdges.add(new DEdge(n, getNode(n.getX(),n.getY()+1)));
            /* Diagonal */
            if(n.getX() > 0 && n.getY() > 0) allEdges.add(new DEdge(n, getNode(n.getX()-1,n.getY()-1)));
            if(n.getX() > 0 && n.getY() < 79) allEdges.add(new DEdge(n, getNode(n.getX()-1,n.getY()+1)));
            if(n.getX() < 79 && n.getY() > 0) allEdges.add(new DEdge(n, getNode(n.getX()+1,n.getY()-1)));
            if(n.getX() < 79 && n.getY() < 79) allEdges.add(new DEdge(n, getNode(n.getX()+1,n.getY()+1)));
        }

        init(); // use for Moving Target D * lite
        computeMinCostPath();
    }
    /*Initializing */
    public void init(){
        this.openList = new ArrayList<DNode>();
        this.K = 0;
        for(DNode n: allNodes){
            n.setRHS(Float.MAX_VALUE);
            n.setG(Float.MAX_VALUE);
            n.setPar(null);
        }
        this.startNode = getNode((int)(this.getX()/10),(int)(this.getY()/10));
        this.goalNode = getNode(goalX, goalY);
        System.out.println("The goal is at " + goalX + " " + goalY);
        this.startNode.setRHS(0);
        this.startNode.calculateKey(K, goalNode);
        openList.add(startNode);
    }

    public void computeMinCostPath(){
        // Make sure we have  all inconsistent nodes in  the openList
        //getAllInconsistent();
        sort();
        goalNode.calculateKey(K,goalNode);
        System.out.println("Top key: ["+openList.get(0).getX() + " , "+openList.get(0).getY()+"] "
                +openList.get(0).getKey()+ " Goal  Key: "+ goalNode.getKey() );
        while(openList.get(0).getKey() < goalNode.getKey() //the smallest complete cost of path estimate of an inconsistent node is smaller than the one of goalNode
                || goalNode.getRHS() > goalNode.getG()){ //one step lookahead of goal is smaller than the cost estimate from start to goal

            System.out.println("Recomputing Minimal Cost Path");
            DNode current = openList.get(0);  //Take inconsistent node with smallest cost estimate
            float oldKey = current.getKey();
            float[] newKey = current.calculateKeyval(K,goalNode);
            System.out.println("Considering ["+current.getX()+" , "+current.getY() +"] old key: "+oldKey +  " new Key: "+newKey[0] );
            if(oldKey < newKey[0]) current.setKey(newKey);
            else if(current.getG() > current.getRHS()){
                /* this means there is a better way of reaching "current" by one of its predessesors
                   Since RHS is not infinity, the parent pointer won"t be  null, and we can conclude, which predessesor should be taken to reach current
                   Remove current (since its now consistent) and expand it
                */
                current.setG(current.getRHS());
                openList.remove(current);
                for(DEdge e: getSuccessors(current)){
                    DNode s  =  e.getV();
                    if(s != startNode && current.getPar() != s && (s.getRHS() > current.getG() + e.getCost())){
                        s.setPar(current);
                        s.setRHS(current.getG() + e.getCost());
                        updateState(s);
                    }
                }
            }
            else{
                current.setG(Float.MAX_VALUE);
                for(DEdge e: getSuccessors(current)){
                    DNode s = e.getV();
                    if(s != startNode && s.getPar() == current)
                        // update parent pointer of successors (since g is now infinity)
                        reCalculateRHSPar(s);
                    updateState(s);
                }
                reCalculateRHSPar(current);
                updateState(current);
            }
            sort();
            goalNode.calculateKey(K,goalNode);
            // recalculate the goalNode Key??
        }
        getPath();
    }

    public void plan() {
        /* save old start and  goal nodes, and update them! */
        this.oldStartNode = startNode;
        this.oldGoalNode = goalNode;
        for (DNode n : allNodes) {
            if (n.getX() == (int) this.getX() / 10 && n.getY() == (int) this.getY() / 10)
                this.startNode = n;
            if (n.getX() == goalX && n.getY() == goalY)
                this.goalNode = n;
        }
        if (startNode == goalNode || (Math.abs(this.getX()/10 - goalNode.getX()) < 0.5&& Math.abs(this.getY() / 10 - goalNode.getY()) < 0.5)) {
            System.out.println("Arrived");
            if(!targetFound && !guardDetetcted){
                System.out.println("New random goal");
                goalX = (int) (Math.random() * (780) + 10)/10;
                goalY = (int) (Math.random() * (780) + 10)/10;
                goalNode = getNode(goalX,goalY);
                init();
                computeMinCostPath();
            }


            //return;
        }
        if (startNode != oldStartNode) {
            basicDeletion();
        }
        ArrayList<DEdge> changedEdges = getChangedEdges();
        if (changedEdges.size() == 0 && targetOnPath(goalNode)) {
            if (Math.abs(this.getX() / 10 - path.get(path.size() - 1).getX()) < 0.5&& Math.abs(this.getY() / 10 - path.get(path.size() - 1).getY()) < 0.5)
                path.remove(path.size() - 1);
            if (path.size() > 0)
                headTo(path.get(path.size() - 1).getX() * 10, path.get(path.size() - 1).getY() * 10);
        } else {

            for (DEdge e : changedEdges) {
                float oldCost = e.getCost();
                e.setCost(e.getNewCost());
                float newCost = e.getCost();
                // if the edge has gotten cheaper
                if (oldCost > newCost) {
                    //look if its now a cheaper path
                    if (e.getV() != startNode && e.getV().getRHS() > e.getU().getG() + e.getCost()) {
                        e.getV().setPar(e.getU());
                        e.getV().setRHS(e.getU().getG() + e.getCost());
                        updateState(e.getV());
                    }
                } else {
                    if (e.getV() != startNode && e.getV().getPar() == e.getU()) {
                        System.out.println("Recompute [" + e.getV().getX() + " , " +
                                e.getV().getY() + "]");
                        reCalculateRHSPar(e.getV());
                        updateState(e.getV());
                        // }
                        if(path.contains(e.getV())&& e.getV().getPar() ==null ){
                            int temp = path.indexOf(e.getV());
                            // openList.add(0,startNode);

                            for(int i = temp; i< path.size();i++){ //temp+1 works alrightish//change back to temp in a sec
                                System.out.println("Resetting ["+path.get(i).getX() +" , "+path.get(i).getY()+"]");
                                path.get(i).setPar(null);
                                path.get(i).setG(Float.MAX_VALUE);
                                path.get(i).setRHS(Float.MAX_VALUE);
                                path.get(i).calculateKey(K,goalNode);
                            }
                        }}
                }
            }


            /*for(DNode d : path){
                if(d.getG() == Float.MAX_VALUE) reCalculateRHSPar(d);
            }*/
            computeMinCostPath();
            if (targetOnPath(goalNode)) {
                System.out.println("mooooooving");
                if (Math.abs(this.getX() / 10 - path.get(path.size() - 1).getX()) < 0.5&& Math.abs(this.getY() / 10 - path.get(path.size() - 1).getY()) < 0.5){
                    System.out.println("removing");
                    path.remove(path.size() - 1);}
                if (path.size() > 0){
                    System.out.println("walkin to " + path.get(path.size() - 1).getX() + " " + path.get(path.size() - 1).getY());
                    headTo(path.get(path.size() - 1).getX() * 10, path.get(path.size() - 1).getY() * 10);
                }}
        }

    }
    public ArrayList<DNode> isPointedToBy(DNode n){
        ArrayList<DNode> pointers =  new ArrayList<DNode>();
        for(DNode p: allNodes){
            if(p.getPar()  == n) pointers.add(p);

        }
        return pointers;
    }
    public void getAllInconsistent(){
        for(DNode n: allNodes){
            //if(n.getG() != n.getRHS() && !openList.contains(n)) openList.add(n);
            //else if(n.getG() == n.getRHS() && openList.contains(n)) openList.remove(n);
            updateState(n);
        }
    }

    public ArrayList<DEdge> getChangedEdges(){
        ArrayList<DEdge> changed = new ArrayList<DEdge>();
        for (int i = 0; i < agentsworld.length; i++) {
            for (int j = 0; j < agentsworld[i].length; j++) {
                if (agentsworld[i][j] == 1 && !targetFound) {
                    System.out.println("Target detected");
                    targetFound = true;
                    goalX = i;
                    goalY = j;
                    init();
                    computeMinCostPath();
                }
                if (agentsworld[i][j] == 9) {
                    /* get corresponding node */
                    DNode current = getNode(i,j);
                    current.setG(Float.MAX_VALUE);
                    current.setRHS(Float.MAX_VALUE);
                    //only check NEWLY SEEN wall nodes
                    if(current.getObject() == 0){
                        current.setObject(9);
                        /* which edges do we need to change*/
                        ArrayList<DEdge> edge =  getSuccessors(current);//getPredessesors(current);
                        edge.addAll(getPredecessors(current));//Successors(current)); // to stay or not to stay
                        for(DEdge e : edge) e.setNewCost(Float.MAX_VALUE);
                        changed.addAll(edge);
                        System.out.println("edge costs change" + i + " "+ j);
                    }
                }

            }

        }
        /*This won't work, since the guard moves, and we can not assume them to be static like walls. so we need to remember where a guard was, and as soon as the guard
        moves make the old edges cheap again. */
        if(oldPositions.size()!= 0){
            for(DNode d: oldPositions){
                ArrayList<DEdge> edge =  getSuccessors(d);
                edge.addAll(getPredecessors(d));
                for(DEdge e : edge) e.setNewCost(1);
                changed.addAll(edge);
            }
            oldPositions = new ArrayList<DNode>();
        }
        for(Agent a: agentList){
            if(a != this){
                float[] position = {a.getX(),a.getY()};
                float[][] vision = vision();
                if(isInVisionField(position, vision[0],vision[1],vision[2])){
                    System.out.println("guard seen");
                    if(!guardDetetcted){
                        guardDetetcted = true;
                        goalX = entrypointx;
                        goalY = entrypointy;
                        goalNode = getNode(goalX,goalY);
                        init();
                        computeMinCostPath();
                    }
                    DNode guard = getNode(((int)position[0]/10),((int)position[1]/10));
                    oldPositions.add(guard);
                    ArrayList<DEdge> edge =  getSuccessors(guard);
                    edge.addAll(getPredecessors(guard));
                    for(DEdge e : edge) e.setNewCost(Float.MAX_VALUE);
                    changed.addAll(edge);
                }
            }
        }
        return changed;
    }

    public void basicDeletion(){
        startNode.setPar(null);
        reCalculateRHSPar(oldStartNode);
        updateState(oldStartNode);
    }

    public void updateState(DNode n){
        /*Inconsistent and already in openlist, update key */
        //internet told me to add a tolerance
        if(n.getG() != n.getRHS() && openList.contains(n))
            n.calculateKey(K,goalNode);
        else if(n.getG() != n.getRHS() && !openList.contains(n)){
            n.calculateKey(K,goalNode);
            openList.add(n);
        }
        else if(n.getG() == n.getRHS() && openList.contains(n)) {
            n.calculateKey(K,goalNode);
            openList.remove(n);
        }
    }

    public void reCalculateRHSPar(DNode s){
        //ADDED: size 2 loop avoidance
        float best = Float.MAX_VALUE;
        DNode bestNode = null;
        for(DEdge e: getPredecessors(s)){
            DNode pre = e.getU();
            if (pre.getG() > pre.getRHS()) {
                pre.setG(pre.getRHS());
                openList.remove(pre);
                // expand p
                for (DEdge te : getSuccessors(pre)) {
                    DNode n = te.getV();
                    if (n != startNode && pre.getPar() != n && (n.getRHS() > pre.getG() + te.getCost())) {
                        n.setPar(pre);
                        n.setRHS(pre.getG() + te.getCost());
                        updateState(n);
                    }
                }
            }
            if(pre.getPar() != s) {
                System.out.println("Considering ["+pre.getX()+" , "+pre.getY()+"]");
                System.out.println(pre.getG()+" + "+e.getCost()+" < " +best );
                if (pre.getG() + e.getCost() < best) {
                    best = pre.getG() + e.getCost();
                    bestNode = pre;
                }
            }
        }
        System.out.println("New: ["+s.getX()+" , "+s.getY()+" ] RHS: "+best);
        if(bestNode !=null) System.out.println("PAR: ["+bestNode.getX()+" , "+bestNode.getY()+"]" );
        s.setRHS(best);
        s.setPar(bestNode);
    }
    /*Helper Method: Retrieve a Node from the collection of nodes.
      We never create more Nodes.
     */
    public DNode getNode(float nX, float nY){
        for(DNode n : allNodes){
            if(n.getX() == nX && n.getY() == nY) return n;
        }
        return null;
    }
    /*Helper Method: Retrieve all outgoing edges from a node.
      --> returns edges to all its successors
     */
    public ArrayList<DEdge> getSuccessors(DNode n){
        ArrayList<DEdge> succ =  new ArrayList<DEdge>();
        for(DEdge e : allEdges){
            if(e.getU() == n) succ.add(e);
        }
        return succ;
    }
    /*Helper Method: Retrieve all incoming edges from a node.
    --> returns edges to all its predecessors
    */
    public ArrayList<DEdge> getPredecessors(DNode n){
        ArrayList<DEdge> pred =  new ArrayList<DEdge>();
        for(DEdge e : allEdges){
            if(e.getV() == n) pred.add(e);
        }
        return pred;
    }
    /*Helper Method: Sort the Openlist according to the first key values
     */
    public void sort(){
        boolean swaps = true;
        while(swaps){
            swaps = false;
            for(int i = 0; i < openList.size()-1; i++){
                if(openList.get(i).getKey() > openList.get(i+1).getKey()){
                    swaps = true;
                    DNode temp = openList.get(i);
                    openList.set(i,openList.get(i+1));
                    openList.set(i+1,temp);
                }
            }
        }
    }
    /* Use Parent Pointers to read off a path from goal to start. */
    public void getPath(){
        ArrayList<DNode> newpath = new ArrayList<DNode>();
        DNode current = goalNode;
        newpath.add(goalNode);
        while(current != startNode){
            System.out.println("path [" + current.getX() +" , " + current.getY()+ "] key: " + current.getKey()
                    + " g: "+ current.getG()+ " rhs: " + current.getRHS());
            if(current.getPar()== null || newpath.contains(current.getPar())){
                System.out.println("RECOMPUTING PATH");
                init();
                computeMinCostPath();
                break;
            }
            current = current.getPar();
            newpath.add(current);
        }
        this.path = newpath;
        for(DNode n : path) {
            System.out.println(" NODE " + n.getX() + " " + n.getY());
        }
    }

    /*Helper Method: Determine if goal is still on our path */
    public boolean targetOnPath(DNode g) {
        for(DNode n : path){
            if(n.getX() == g.getX() && n.getY() == g.getY()) return true;
        }
        return false;
    }
}