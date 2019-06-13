package com.group10.msa.MapObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.group10.msa.ScreenManager.GameScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Agent {

    private float x;
    private float y;
    private int storeX;
    private int storeY;
    private float storeObjX;
    private float storeObjY;
    private float direction;
    public float speed;
    public float visionDistance = 150;
    public float visionDegree = (float)(Math.PI/4);
    private int[][] world;
    private int[][] agentsVision = new int [8][8];
    public Sprite sprite;
    public float audioRadius = 20;
    private float xDir = 0;
    private float yDir = 0;
    private ArrayList currentPath;
    int pathPlace = 0;
    private ArrayList agentList;


    public int[][] agentsworld = new int[80][80];
    public Agent(float xStart, float yStart, float startDir,int[][] world){
        this.x = xStart;
        this.y = yStart;
        this.direction = startDir;
        this.world = world;
        Texture agent = new Texture(Gdx.files.internal("data/CropAgent.jpg"));
        TextureRegion tagent = new TextureRegion(agent,10,10);
        this.sprite = new Sprite(tagent);
        speed = 0;
    }

    //calculates x and y components needed to move (potentially diagonally) at 1.4 metres a second
    public void move(){
        //setAudioRadius();
        //System.out.println(" x " + x + " y " + y);
        //if( x > 790 || x < 0 || y > 790 || y < 0)
          //direction *= Math.PI; // to be deleted, just for testing
        int tempx = (int)(x+5+(metresToCoord(speed)*Math.cos(direction)));
        int tempy = (int)(y+5+(metresToCoord(speed)*Math.sin(direction)));
        tempx *= 0.1;
        tempy *= 0.1;

//        if(world[tempx][tempy] == 9) {
//            System.out.println("TUUUURRRRN" + direction);
//            speed= 1.4f;
//            x+= metresToCoord(speed)*Math.cos(direction);
//            y+= metresToCoord(speed)*Math.sin(direction);
//
////            turn ((float)Math.PI+ direction);
//        }
        //else{
            speed= 1.4f;
            x+= metresToCoord(speed)*Math.cos(direction);
            y+= metresToCoord(speed)*Math.sin(direction);
        //}


    }

    //turns at PI/60 radians every frame
    //capable of determine which way it is fastest to turn, clockwise or anti-clockwise
    public void turn(float newDir){
        if(direction != newDir){
            float turnSpeed = (float)(Math.PI / 60);
//            if(speed <= 1.4f){
//                turnSpeed = (float)(Math.PI / 60);
//            }
//            else{
//                turnSpeed = (float)((1f/18f)*(Math.PI / 60f));
//            }
            if(Math.abs(direction-newDir) > turnSpeed){
                if(direction>Math.PI) {
                    if (direction > newDir && newDir > direction - Math.PI) {
                        direction -= turnSpeed;
                    }
                    else {
                        direction += turnSpeed;
                    }
                }
                else{
                    if (direction < newDir && newDir < direction + Math.PI) {
                        direction += turnSpeed;
                    }
                    else {
                        direction -= turnSpeed;
                    }
                }



            }
            else{
                direction = newDir;
            }
        }

        if(direction > 2.01* Math.PI){
            direction -= 2*Math.PI;

        }
        else if(direction < 0){
            direction += 2* Math.PI;

        }
    }

    public boolean inProximity(float objx, float objy){
        if(Math.abs(x-objx) < 1 && Math.abs(y-objy) < 1){
            return true;
        }
        return false;
    }

    //moves straight to a chosen set of float coordinates
    public void headTo(float objx, float objy){
        //speed = metresToCoord(1.4f);

        if(Math.abs(direction - getAngle(objx,objy)) > 0) {
            turn(getAngle(objx,objy));
        }
        else{
            move();
        }

    }
    //pathfinding way to get around
    public ArrayList aStarTraffic(int startX, int startY, float objx, float objy){
        System.out.println("Start pathfinding>>>>>");
        boolean changeTile = false;
        boolean changeDestination = false;
//        if(objx != storeObjX || objy != storeObjY){
//            System.out.println("New destination" + objx + "  " + objy);
//            currentPath = null;
//            changeTile = true;
//            storeObjX = objx;
//            storeObjY = objy;
//            changeDestination = true;
//            pathPlace = -1;
//        }

        int gridX = startX;
//        if(gridX != storeX){
//            changeTile = true;
//        }
//        storeX = gridX;
        int gridY = startY;
//        if(gridY != storeY){
//            changeTile = true;
//        }
//        storeY = gridY;
        int gridObjX = (int)(objx/10);
        int gridObjY = (int)(objy/10);
//        if(world[gridObjX][gridObjY] == 9){
//            System.out.println("Destination lies within a wall, terminating search");
//            return;
//        }
        ArrayList<Integer> coordListX = new ArrayList();
        ArrayList<Integer> coordListY = new ArrayList();

        ArrayList openlist = new ArrayList<Node>();
        ArrayList closedlist = new ArrayList<Node>();

        ArrayList path = new ArrayList<Node>();

        Node root = new Node(gridX, gridY);
        Node current = root;
        openlist.add(current);
        boolean pathFound = false;

//        if(currentPath != null) {
//            if (currentPath.size() <= 2) {
//                headTo(objx, objy);
//                return;
//            }
//        }

//        if(changeTile) {
//            pathPlace++;
//        }
        //if(changeDestination){
            for (int m = 0; m < 10000; m++) {
                //!(current.getXcoords() == gridObjX && current.getYcoords() == gridObjY)){
//            if(current.getXcoords() == gridObjX && current.getYcoords() == gridObjY){
//                System.out.println("end found");
//            }
                Node child1 = new Node(current.getXcoords() + 1, current.getYcoords(), current);
                Node child2 = new Node(current.getXcoords() + 1, current.getYcoords() + 1, current);
                Node child3 = new Node(current.getXcoords(), current.getYcoords() + 1, current);
                Node child4 = new Node(current.getXcoords() - 1, current.getYcoords() + 1, current);
                Node child5 = new Node(current.getXcoords() - 1, current.getYcoords(), current);
                Node child6 = new Node(current.getXcoords() - 1, current.getYcoords() - 1, current);
                Node child7 = new Node(current.getXcoords(), current.getYcoords() - 1, current);
                Node child8 = new Node(current.getXcoords() + 1, current.getYcoords() - 1, current);
                Node[] tempChildren = {child1, child2, child3, child4, child5, child6, child7, child8};
                boolean one = true;
                boolean two = true;
                boolean three = true;
                boolean four = true;
                boolean five = true;
                boolean six = true;
                boolean seven = true;
                boolean eight = true;


                for (int n = 0; n < coordListX.size(); n++) {
                    if (child1.getXcoords() == coordListX.get(n) && child1.getYcoords() == coordListY.get(n)) {
                        one = false;
                    }
                    if (child2.getXcoords() == coordListX.get(n) && child2.getYcoords() == coordListY.get(n)) {
                        two = false;
                    }
                    if (child3.getXcoords() == coordListX.get(n) && child3.getYcoords() == coordListY.get(n)) {
                        three = false;
                    }
                    if (child4.getXcoords() == coordListX.get(n) && child4.getYcoords() == coordListY.get(n)) {
                        four = false;
                    }
                    if (child5.getXcoords() == coordListX.get(n) && child5.getYcoords() == coordListY.get(n)) {
                        five = false;
                    }
                    if (child6.getXcoords() == coordListX.get(n) && child6.getYcoords() == coordListY.get(n)) {
                        six = false;
                    }
                    if (child7.getXcoords() == coordListX.get(n) && child7.getYcoords() == coordListY.get(n)) {
                        seven = false;
                    }
                    if (child8.getXcoords() == coordListX.get(n) && child8.getYcoords() == coordListY.get(n)) {
                        eight = false;
                    }
                }

                if (world[child1.getXcoords()][child1.getYcoords()] != 9 && one) {
                    current.addChild(child1);
                }
                if (world[child2.getXcoords()][child2.getYcoords()] != 9 && two) {
                    current.addChild(child2);
                }
                if (world[child3.getXcoords()][child3.getYcoords()] != 9 && three) {
                    current.addChild(child3);
                }
                if (world[child4.getXcoords()][child4.getYcoords()] != 9 && four) {
                    current.addChild(child4);
                }
                if (world[child5.getXcoords()][child5.getYcoords()] != 9 && five) {
                    current.addChild(child5);
                }
                if (world[child6.getXcoords()][child6.getYcoords()] != 9 && six) {
                    current.addChild(child6);
                }
                if (world[child7.getXcoords()][child7.getYcoords()] != 9 && seven) {
                    current.addChild(child7);
                }
                if (world[child8.getXcoords()][child8.getYcoords()] != 9 && eight) {
                    current.addChild(child8);
                }

                List children = current.getChildren();
                float min = 100000;
                //System.out.println(current.getXcoords() + "      " + current.getYcoords());
                coordListX.add(current.getXcoords());
                coordListY.add(current.getYcoords());
                closedlist.add(openlist.remove(current));
                for (int i = 0; i < children.size(); i++) {
                    if (((Node) (children.get(i))).getXcoords() == gridObjX && ((Node) (children.get(i))).getYcoords() == gridObjY) {
                        //pathFound = false;
                        System.out.println("end found");
                        Node pathNode = ((Node) (children.get(i)));
                        while (pathNode.getXcoords() != gridX || pathNode.getYcoords() != gridY) {
                            path.add(pathNode);
                            pathNode = pathNode.getParent();
                        }
                        ArrayList inversePath = new ArrayList();
                        for (int k = path.size() - 1; k > 0; k--) {
                            //System.out.println(((Node) (path.get(k))).getXcoords() + " , " + ((Node) (path.get(k))).getYcoords());
                            inversePath.add(path.get(k));
                        }
                        //currentPath = inversePath;
                        //headTo(((Node) (inversePath.get(pathPlace))).getXcoords() * 10, ((Node) (inversePath.get(pathPlace))).getYcoords() * 10);
                        //directLine(inversePath);
                        return inversePath;
                    }
                    ((Node) (children.get(i))).setG(Math.abs((((Node) (children.get(i))).getXcoords() - gridX)) + Math.abs((((Node) (children.get(i))).getYcoords() - gridY)));

                    ((Node) (children.get(i))).setH(Math.abs((((Node) (children.get(i))).getXcoords() - gridObjX)) + Math.abs((((Node) (children.get(i))).getYcoords() - gridObjY)));
                    coordListX.add(((Node) (children.get(i))).getXcoords());
                    coordListY.add(((Node) (children.get(i))).getYcoords());
                    openlist.add(children.get(i));
                }
                for (int i = 0; i < openlist.size(); i++) {
                    if (((Node) (openlist.get(i))).getF() < min) {

                        //System.out.println(((Node) (openlist.get(i))).getXcoords() + "," + ((Node) (openlist.get(i))).getYcoords());
                        //System.out.println(((Node) (openlist.get(i))).getF());
                        current = ((Node) (openlist.get(i)));
                        min = ((Node) (openlist.get(i))).getF();
                    }
                }

            }
            if(!pathFound){
                System.out.println("no path found");
                return null;
            }

        //}


        try {
            //headTo(((Node) (currentPath.get(pathPlace))).getXcoords() * 10, ((Node) (currentPath.get(pathPlace))).getYcoords() * 10);
        }
        catch (IndexOutOfBoundsException exception){
            //headTo(objx, objy);
        }
        return null;
    }


    //Moves agent to float coordinates
    //To have it move until is has reached its destination and then realise it has reached its destination, do the following:
    //in plan method(or whatever constantly called method you're using):
    //
    //if(!inProximity(yourXCoordinate, yourYCoordinate){
    //      aStarHeadTo(yourXCoordinate, yourYCoordinate)
    //}
    //
    //(and then you can change destination or whatever in the else statement)
    //The aStarHeadTo method is a boolean so it can return false if the destination is in a wall or can't be reached.
    public boolean aStarHeadTo(float objx, float objy){
        System.out.println("Start path-finding>>>>>");
        boolean changeTile = false;
        boolean changeDestination = false;
        float[] directCoords = new float[2];

        //When destination changes this happens
        if(objx != storeObjX || objy != storeObjY){
            System.out.println("New destination" + objx + "  " + objy);
            //The path to the last destination is thrown out
            currentPath = null;
            //New coordinates are taken
            storeObjX = objx;
            storeObjY = objy;
            changeDestination = true;
            pathPlace = -1;
        }
        //x "pixel" coordinate is used to find current grid square
        int gridX = (int)((this.getX())/10);

        //When the current grid square changes this happens
        if(gridX != storeX){
            changeTile = true;
        }
        storeX = gridX;

        //y "pixel" coordinate is used to find current grid square
        int gridY = (int)((this.getY())/10);

        //when the current grid square changes this happens
        if(gridY != storeY){
            changeTile = true;
        }
        storeY = gridY;

        //Grid square coordinates of the objective are found
        int gridObjX = (int)(objx/10);
        int gridObjY = (int)(objy/10);

        //If the destination lies within a wall, the algorithm will do nothing and return false

        if(world[gridObjX][gridObjY] == 9){
            System.out.println("Destination lies within a wall, terminating search");

            //(use this boolean to get out of the infinite loop caused)
            return false;
        }

        //Now the real A* begins

        //these coordlists are used to store each grid square discovered so they are not discovered twice (so there are not 2 or more instances of the same gridsquare)
        ArrayList<Integer> coordListX = new ArrayList();
        ArrayList<Integer> coordListY = new ArrayList();

        //openlist for all the grid squares that have been checked but not all children have been checked
        ArrayList openlist = new ArrayList<Node>();
        //closedlist for all the gridsquares that have been checked and also had all their children checked
        ArrayList closedlist = new ArrayList<Node>();

        ArrayList path = new ArrayList<Node>();

        //A* starts by adding the root to the openlist
        //There's a class I made called Node that is suitable for representing a grid square, it holds x, y, G, H, F, the parent, and the children
        Node root = new Node(gridX, gridY);
        Node current = root;
        openlist.add(current);
        boolean pathFound = false;

        //When the agent has reached the next tile in its journey, this happens
        if(changeTile) {
            //pathPlace is iterated by 1, telling the agent to head to the next grid square in the sequence
            pathPlace++;
            //The below if statement just contains some lines that protect from some NullPointer errors and OutOfBounds errors
            if(pathPlace>0){
                for (int i = 0; i < currentPath.size(); i++) {
                    if(((Node)(currentPath.get(i))).getXcoords() == gridX && ((Node)(currentPath.get(i))).getYcoords() == gridY){
                        pathPlace = i + 1;
                        break;
                    }
                }
            }
        }
        //When the destination is changed (For instance, when we first start the algorithm, this happens (So the size 10000 loop doesn't happen every frame)
        if(changeDestination){
            //The 10000 loop below is the search algorithm in A*, checking unchecked neighbours of all the checked grid squares in order of their F value
            for (int m = 0; m < 10000; m++) {

                //gathering all the neighbours/children of the last checked grid square, assigning their coordinates
                Node child1 = new Node(current.getXcoords() + 1, current.getYcoords(), current);
                Node child2 = new Node(current.getXcoords() + 1, current.getYcoords() + 1, current);
                Node child3 = new Node(current.getXcoords(), current.getYcoords() + 1, current);
                Node child4 = new Node(current.getXcoords() - 1, current.getYcoords() + 1, current);
                Node child5 = new Node(current.getXcoords() - 1, current.getYcoords(), current);
                Node child6 = new Node(current.getXcoords() - 1, current.getYcoords() - 1, current);
                Node child7 = new Node(current.getXcoords(), current.getYcoords() - 1, current);
                Node child8 = new Node(current.getXcoords() + 1, current.getYcoords() - 1, current);

                //An ugly amount of local booleans, but it was a last resort, more on them below
                boolean one = true;
                boolean two = true;
                boolean three = true;
                boolean four = true;
                boolean five = true;
                boolean six = true;
                boolean seven = true;
                boolean eight = true;

                //Each of the above 8 nodes is checked to see if its coordinates already exist in the "coordList"s
                for (int n = 0; n < coordListX.size(); n++) {
                    if (child1.getXcoords() == coordListX.get(n) && child1.getYcoords() == coordListY.get(n)) {
                        one = false;
                    }
                    if (child2.getXcoords() == coordListX.get(n) && child2.getYcoords() == coordListY.get(n)) {
                        two = false;
                    }
                    if (child3.getXcoords() == coordListX.get(n) && child3.getYcoords() == coordListY.get(n)) {
                        three = false;
                    }
                    if (child4.getXcoords() == coordListX.get(n) && child4.getYcoords() == coordListY.get(n)) {
                        four = false;
                    }
                    if (child5.getXcoords() == coordListX.get(n) && child5.getYcoords() == coordListY.get(n)) {
                        five = false;
                    }
                    if (child6.getXcoords() == coordListX.get(n) && child6.getYcoords() == coordListY.get(n)) {
                        six = false;
                    }
                    if (child7.getXcoords() == coordListX.get(n) && child7.getYcoords() == coordListY.get(n)) {
                        seven = false;
                    }
                    if (child8.getXcoords() == coordListX.get(n) && child8.getYcoords() == coordListY.get(n)) {
                        eight = false;
                    }
                }

                //if a Node's coordinates already exist in coordList, or the Node is actually a wall, it is not accepted as a child of the current Node
                if (world[child1.getXcoords()][child1.getYcoords()] != 9 && one) {
                    current.addChild(child1);
                }
                if (world[child2.getXcoords()][child2.getYcoords()] != 9 && two) {
                    current.addChild(child2);
                }
                if (world[child3.getXcoords()][child3.getYcoords()] != 9 && three) {
                    current.addChild(child3);
                }
                if (world[child4.getXcoords()][child4.getYcoords()] != 9 && four) {
                    current.addChild(child4);
                }
                if (world[child5.getXcoords()][child5.getYcoords()] != 9 && five) {
                    current.addChild(child5);
                }
                if (world[child6.getXcoords()][child6.getYcoords()] != 9 && six) {
                    current.addChild(child6);
                }
                if (world[child7.getXcoords()][child7.getYcoords()] != 9 && seven) {
                    current.addChild(child7);
                }
                if (world[child8.getXcoords()][child8.getYcoords()] != 9 && eight) {
                    current.addChild(child8);
                }

                List children = current.getChildren();
                float min = 100000;
                coordListX.add(current.getXcoords());
                coordListY.add(current.getYcoords());
                closedlist.add(openlist.remove(current));

                //Each recent accepted child is checked in this loop below:
                for (int i = 0; i < children.size(); i++) {
                    //If the child grid square contains the destination then this happens
                    if (((Node) (children.get(i))).getXcoords() == gridObjX && ((Node) (children.get(i))).getYcoords() == gridObjY) {
                        System.out.println("end found");
                        //The current node is made into pathNode the first element in the "path" arraylist
                        Node pathNode = ((Node) (children.get(i)));

                        //This while loop retrieves the parent of each node, all the way back to the starting coordinates
                        while (pathNode.getXcoords() != gridX || pathNode.getYcoords() != gridY) {
                            path.add(pathNode);
                            pathNode = pathNode.getParent();
                        }
                        //Now we have a path arraylist that starts at the destination, and ends at the current position of the agent/start

                        //Here we just reverse path so that we get a inverse path that starts at the start and ends at the destination
                        ArrayList inversePath = new ArrayList();
                        for (int k = path.size()-1; k > -1; k--) {
                            inversePath.add(path.get(k));

                        }
                        inversePath.add(new Node(gridObjX,gridObjY));

                        //Make inversePath our currentPath
                        currentPath = inversePath;
                        pathPlace = 0;

                        //The Movement, telling the agent to move to the next grid square in the path
                        headTo(((Node) (currentPath.get(pathPlace))).getXcoords() * 10, ((Node) (currentPath.get(pathPlace))).getYcoords() * 10);

//                        directLine(currentPath);
//                        headTo(directCoords[0], directCoords[1]);
                        return true;
                    }
                    //The rest of the algorithm below only takes place if the destination has not been found yet

                    //New G, H, and F values are assigned to the recently accepted children
                    ((Node) (children.get(i))).setG(Math.abs((((Node) (children.get(i))).getXcoords() - gridX)) + Math.abs((((Node) (children.get(i))).getYcoords() - gridY)));

                    ((Node) (children.get(i))).setH(Math.abs((((Node) (children.get(i))).getXcoords() - gridObjX)) + Math.abs((((Node) (children.get(i))).getYcoords() - gridObjY)));


                    coordListX.add(((Node) (children.get(i))).getXcoords());
                    coordListY.add(((Node) (children.get(i))).getYcoords());

                    //the children are added to the openlist
                    openlist.add(children.get(i));
                }

                //The loop below is used to determine which Node in the openlist should have its children investigated next
                for (int i = 0; i < openlist.size(); i++) {

                    //The next best Node is of course the one with the lowest F value
                    if (((Node) (openlist.get(i))).getF() < min) {

                        //System.out.println(((Node) (openlist.get(i))).getXcoords() + "," + ((Node) (openlist.get(i))).getYcoords());
                        //System.out.println(((Node) (openlist.get(i))).getF());

                        //The next best Node is made into current and the 10000 loop starts again with this Node as current
                        current = ((Node) (openlist.get(i)));
                        min = ((Node) (openlist.get(i))).getF();
                    }
                }

            }
            //If the destination has not been found by the end of the 10000 loop, it is likely the destination cannot be reached at all
            if(!pathFound){
                System.out.println("no path found");
                return false;
            }

        }
        //System.out.println(this.getX() + "  " + this.getY() + "     " + storeX + "  " + storeY);
        //System.out.println(pathPlace);
        try {
            for (int i = 0; i < currentPath.size(); i++) {
                //System.out.println(((Node) (currentPath.get(i))).getXcoords() + "  " + ((Node) (currentPath.get(i))).getYcoords());

            }
        }
        catch (NullPointerException exception){
            //If the currentPath still hasn't been instantiated, it is likely the destination cannot be reached at all
            System.out.println("Destination completely isolated, terminating search");
            return false;
        }

        //if the agent is less than a grid square away from the destination, then head straight to it.
        if(Math.abs(this.getX()-objx)<10 && Math.abs(this.getY()-objy)<10){
            headTo(objx,objy);
        }
        else {
            //Some more bug evasion here
            headTo(((Node) (currentPath.get(pathPlace))).getXcoords() * 10, ((Node) (currentPath.get(pathPlace))).getYcoords() * 10);
            //directLine(currentPath);
        }
        return true;
    }




    public float[] directLine(ArrayList givenPath){
        float[] coords = new float[2];
        for (int i = givenPath.size()-1; i > 0; i--) {
            if(probeLine(((Node)(givenPath.get(i))).getXcoords()*10, ((Node)(givenPath.get(i))).getYcoords()*10)){
                xDir = ((Node)(givenPath.get(i))).getXcoords()*10;
                yDir = ((Node)(givenPath.get(i))).getYcoords()*10;
                break;
            }
        }
        System.out.println(xDir + "   " + yDir);
        coords[0] = xDir;
        coords[1] = yDir;
        return coords;
    }

    public boolean probeLine(float tX, float tY){
        ArrayList<Integer> xlist = new ArrayList();
        ArrayList<Integer> ylist = new ArrayList();

        for (int i = 0; i < 100; i++) {
            xlist.add((int)(this.getX() + (tX - this.getX())*i/100)/10);
            ylist.add((int)(this.getY() + (tY - this.getY())*i/100)/10);
            System.out.println((int)(this.getX() + (tX - this.getX())*i/100)/10 + "  " + (int)(this.getY() + (tY - this.getY())*i/100)/10);
        }

        for (int i = 2; i < xlist.size(); i++) {
            if(world[xlist.get(i)][ylist.get(i)] == 9){
                System.out.println("wall found");
                return false;
            }
        }
        System.out.println("no wall found");
        return true;
    }

    public void swerveTo(MapObject object){

        float objectX = object.getPos().x;
        float objectY = object.getPos().y;
        if(direction != getAngle(object)) {
            //speed = 1.4f;
            turn(getAngle(object));
        }
        if(Math.abs(x-objectX) < 1&& Math.abs(y-objectY) <1 ) {
            speed = 0;
        }


    }
    public void swerveTo(int xPos, int yPos){

        Vector2 v1 = new Vector2(xPos,yPos);

        MapObject tempObj = new MapObject(null, v1);
        if(direction != getAngle(tempObj)) {
            //speed = 1.4f;
            turn(getAngle(tempObj));
        }
        if(Math.abs(x-xPos) < 1&& Math.abs(y-yPos) <1 ) {
            speed = 0;
        }


    }
    //finds angle of location relative to agent
    public float getAngle(MapObject object) {
        float[] obj = {(object.getPos().x-x), (object.getPos().y-y)};
        float temp = (float) Math.sqrt(obj[0]*obj[0] + obj[1]*obj[1]);
        obj[0] = obj[0]/temp;
        obj[1] = obj[1]/temp;

        float[] us={(x+5) - x,y -y};
        float temo = (float) Math.sqrt(us[0]*us[0] + us[1]*us[1]);
        us[0] = us[0]/temo;
        us[1] = us[1]/temo;

        float angle= (float)Math.acos((obj[0]*us[0])+(obj[1]*us[1]));
        return angle;
    }


    public float getX(){
        return x;
    }

    public float getY() {
        return y;
    }
    public void newDirection(float newdirection){
        this.direction = newdirection;
    }
    public void changedirection(float change){
        this.direction += change;
    }
    public float getDirection(){ return direction;}
    public float coordToMetres(float coord){
        float metres = (float)0.05*coord;
        return  metres;
    }
    public float metresToCoord(float metres){
        return (float)20*metres/Gdx.graphics.getFramesPerSecond();
    }

    public float[][] vision(){
        float[][] vision = new float[3][2];
        //first point are just our  x and y position
        vision[0][0] = this.x+5;
        vision[0][1] = this.y+5;
        //Next up upper point
        vision[1][0] = (float)(visionDistance*Math.cos((visionDegree/2)+direction)+x+5);
        vision[1][1] = (float)(visionDistance*Math.sin((visionDegree/2)+direction)+y+5);
        // last point
        vision[2][0] = (float)(visionDistance*Math.cos(-(visionDegree/2)+direction)+x+5);
        vision[2][1] = (float)(visionDistance*Math.sin(-(visionDegree/2)+direction)+y+5);
       // visionField(vision);

        return vision;
    }
    public ArrayList<float[]> visionField(float[][] vision){
        //System.out.println(" x " + this.x + " y " + this.y);
        //find biggest and smallest x,y values
        int maxX = 0, maxY = 0;
        int minX = 79, minY = 79;
        for(float[] i : vision) {
            if (((int) i[1] / 10) > maxY) maxY = ((int) i[1] / 10);
            if (((int) i[1] / 10) < minY) minY = ((int) i[1] / 10);
            if (((int) i[0] / 10) > maxX) maxX = ((int) i[0] / 10);
            if (((int) i[0] / 10) < minX) minX = ((int) i[0] / 10);
        }
        ArrayList<float[]> points = new ArrayList<float[]>();
        maxX += 1;
        maxY +=1;
        //System.out.println("minx "+ minX + " miny " + minY + " maxx " + maxX + " maxy " +maxY );
        //starting from the smallest
        agentsVision = new int[maxY-minY+1][maxX-minX+1];
        int count = 0;
        for(int i = maxY; i >= minY;i--){
            for(int j = maxX; j >= minX;j--){
                float[] p= {j*10,i*10};
                //System.out.println(p[0] + " "+ p[1]);
                if(p[0]>=0 &&p[0] < 800 && p[1] >=0 && p[1] < 800 &&
                        isInVisionField(p, vision[0], vision[1],vision[2])){
                    agentsVision[i-minY][j-minX] = world[i][j];
                    agentsworld[i][j] = world[i][j];
                    count++;
                    points.add(p);
                }
            }
        }
        for(int i = 0; i < agentsVision.length; i++){
            for(int j = 0; j < agentsVision[0].length;j++){
                if(agentsVision[i][j] == 0){
                    for(int k = 0; k < 10; k++){
                        for(int l = 0; l < 10; l++){
                            float[] p = {(((minX+j)*10)+k),(((minY+i)*10)+l)};
                            if(p[0]>=0 &&p[0] < 800 && p[1] >=0 && p[1] < 800 &&isInVisionField(p, vision[0],vision[1], vision[2])){
                                agentsVision[i][j] = world[minY+i][minX+j];
                                agentsworld[minY+i][minX+j] = world[minY+i][minX+j];
                                k = 10;
                                l=10;
                                count++;
                                points.add(p);
                            }
                        }
                    }
                }
            }
        }
        //agentsVision[((int)vision[0][1]/10)-minY][((int)vision[0][0]/10)-minX] = 6;
        //System.out.println("Direction" + direction);
        /*for(int i = 0; i < agentsVision.length;i++){
            for(int j = 0; j < agentsVision[0].length; j++){
                System.out.print(agentsVision[i][j]);
            }
            System.out.println();
        }*/
        collisionDetection(agentsVision,minX,minY);
        return points;
    }
    public boolean isInVisionField(float[] point, float[]a,float[]b, float[] c){
        //vector from a to point, normalized to length 1
        float[] a_point = {(point[0]-a[0]),(point[1]-a[1])};
        float temp = (float)Math.sqrt((a_point[0]*a_point[0])+(a_point[1]*a_point[1]));
         if(temp > visionDistance) return false;
        a_point[0] = a_point[0]/temp;
        a_point[1] = a_point[1]/temp;

        //vector from a to b,normalized to
        float[]ab = {(b[0]-a[0]),(b[1]-a[1])};
        temp = (float) Math.sqrt((ab[0]*ab[0])+(ab[1]*ab[1]));
        ab[0] = ab[0]/temp;
        ab[1] = ab[1]/temp;
        //vector from a to c, normalized to length 1
        float[]ac = {(c[0]-a[0]),(c[1]-a[1])};
        temp = (float) Math.sqrt((ac[0]*ac[0])+(ac[1]*ac[1]));
        ac[0] = ac[0]/temp;
        ac[1] = ac[1]/temp;

        float angleAB = (float)Math.acos((a_point[0]*ab[0])+(a_point[1]*ab[1]));
       float angleAC =  (float)Math.acos((a_point[0]*ac[0])+(a_point[1]*ac[1]));
        if(angleAB >= 0 && angleAB <= (Math.PI/4) && angleAC >= 0 && angleAC <= (Math.PI/4)){
            return true;}
        return false;
    }

    public void setAudioRadius(){
        if(speed < 0.5) audioRadius = 20;
        else if (speed < 1) audioRadius = 60;
        else if (speed < 2) audioRadius  = 100;
        else audioRadius = 200;
    }
    public float normalNoiseDetection(float dx,float dy){
        float direc = getAngle(dx,dy);
        float probab = (float) Math.random();
        if(probab < 0.6827 ){
            float rando = (float) (-10 + (Math.random() * ((10 - (-10)) + 1)));
            //System.out.println("RESULT " +(direc +(float)(Math.toRadians(rando))));
            return direc +(float)(Math.toRadians(rando));
        }
        else if (probab < 0.9545){
            float rando = (float) (-20+ (Math.random() * ((20 - (-20)) + 1)));
            return direction +(float)(Math.toRadians(rando));
        }
        else if (probab < 0.9973){
            float rando = (float) (-30 + (Math.random() * ((30 - (-30)) + 1)));
            return direc +(float)(Math.toRadians(rando));
        }
        else{
            float rando = (float) (-40 + (Math.random() * ((40 - (-40)) + 1)));
            return direc +(float)(Math.toRadians(rando));
        }
    }

    public float getAngle(float noisx, float noisy) {
        float[] obj = {(noisx-x), (noisy-y)};
        float temp = (float) Math.sqrt(obj[0]*obj[0] + obj[1]*obj[1]);
        obj[0] = obj[0]/temp;
        obj[1] = obj[1]/temp;

        //horizontal line
        float[] us={1,0};//{(x+5) - x,y -y};
        float temo = (float) Math.sqrt(us[0]*us[0] + us[1]*us[1]);
        us[0] = us[0]/temo;
        us[1] = us[1]/temo;

        float angle= (float)Math.acos((obj[0]*us[0])+(obj[1]*us[1]));
        if(noisy < y) angle = (float) (2*Math.PI) - angle;
        //System.out.println(angle);
        return angle;
    }

    public void collisionDetection(int[][] agentsVision, int minX, int minY){
        for(int i = 0; i < agentsVision.length; i++){
            for (int j = 0; j < agentsVision[0].length; j++){
                if( agentsVision[i][j] == 9){
                    if(getAngle((minX+i)*10,(minY+j)*10)== direction ){
                        //System.out.println("i'm going to hit that wall");
                    }
                }
                if (agentsVision[i][j] == 1){
                    turn(getAngle((minX+i)*10, (minY+j)* 10));
                }
            }
        }
    }
    public void plan (){
//        if(world[(int)x/10][(int)y/10] == 1) speed = 0;
//        else move();
//
//        for(int i = agentsVision.length-1; i >= 0 ; i--){
//            for(int j = agentsVision[i].length-1; j >= 0 ; j--){
//
//                System.out.print(agentsVision[i][j]);
//            }
//            System.out.println();
//        }
//        for(int i = agentsworld.length-1; i >= 0 ; i--){
//            for(int j = 0; j <agentsworld[i].length; j++){
//
//                System.out.print(agentsworld[i][j]);
//            }
//            System.out.println();
//        }
        setAudioRadius();
        //

    }

    public void setAgentList(ArrayList aList){
        this.agentList = aList;
    }

    public boolean radiusDetection(Agent a1, Agent a2){
        float[] vector = {(a2.getX()+5-(a1.getX()+5)), (a2.getY()+5-(a1.getY()+5))};
        float temp = (float) Math.sqrt(vector[0]*vector[0] + vector[1]*vector[1]);
        //System.out.println("vec length " +  temp + " rad " + a2.audioRadius);

        if(temp <= a2.audioRadius) {
            return true;
        }
        else return false;

    }

}
