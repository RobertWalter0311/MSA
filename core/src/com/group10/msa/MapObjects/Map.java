package com.group10.msa.MapObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;



public class Map {

    public MapObject[][] mapObjects;
    private int[][] mapArray;
    private MapObject Tower;
    private MapObject Target;

    public Map() {
        try {
            mapArray = readFileArray();

        } catch (IOException FileNotFoundException) {
            System.out.println("ERROR");
        }

    }
    public int[][] getMapArray(){
        return mapArray;
    }


    public int[][] readFileArray() throws IOException {
        File file = new File("C:\\Users\\Notebook\\git\\MSA\\MSA\\core\\assets\\MapInfo.txt");
        FileInputStream fileStream = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(fileStream);
        BufferedReader reader = new BufferedReader(input);

        String line;
        String array = "";

        int length = 0;
        int height = 0;
//        while ((line = reader.readLine()) != null) {
//            if (line.contains(",")) {
//                length = line.length();
//                height++;
//            }
//        }
        int[][] intArray = new int[80][80];
        int j = 0;
        int u = 0;
        while ((line = reader.readLine()) != null) {
            if(line.contains(",")){
                for(int i = 0; i<line.length(); i++) {
                    if (line.charAt(i) != ',') {
                        array += line.charAt(i);
                        //System.out.println("u " + u + " j " + j + " i " + i);
                        intArray[u][j] = Character.getNumericValue(line.charAt(i));
                        u++;
                        if(u == 80) {
                            u= 0;
                            j++;
                        }
                       // if(line.charAt(i) == '6'){
                         //   i++;
                        //}
                    }

                }
                u = 0;
                j++;
                array += '\n';
            }



        }
        /*for(int i = 0; i < intArray.length;i++){
            for(int k = 0; k < intArray[0].length;k++){
                System.out.print(intArray[i][k]);
            }
            System.out.println();
        }*/
        //System.out.println(array);
        return intArray;
    }

    public float coordToMetres(float coord){
        float metres = (float)0.05*coord;
        return  metres;
    }
    public float metresToCoord(float metres){
        float coord = (float)20*metres;
        return  metres;
    }

    public static void createMap(MapObject[][] arrayObject, MapObject target){

        //create the map txt file with the coordinates
        mapTxt("MapInfo.txt", arrayObject,target);


        //generate a texture map
        //pngMap("MapTexture.png", arrayObject);
    }
    static void mapTxt(String filename, MapObject[][] arrayObject, MapObject target) {
        //create the filemap
        FileHandle file = Gdx.files.local(filename);

        String finalMap = "";

        for(int i=0; i<arrayObject.length; i++) {
            for (int j = 0; j < arrayObject[0].length; j++) {

                if(arrayObject[i][j].getType() == MapObject.MapType.Grass){
                    finalMap += "2"+",";
                }else if(arrayObject[i][j].getType() == MapObject.MapType.Dirt){
                    finalMap += "3"+",";
                }else if(arrayObject[i][j].getType() == MapObject.MapType.Sand){
                    finalMap += "4"+",";
                }else if(arrayObject[i][j].getType() == MapObject.MapType.Water){
                    finalMap += "5"+",";
                }else if(arrayObject[i][j].getType() == MapObject.MapType.Wall){
                    finalMap += "9"+",";
                }else if(arrayObject[i][j].getType() == MapObject.MapType.Tree){
                    finalMap += "7"+",";
                }else if(arrayObject[i][j].getType() == MapObject.MapType.Tower){
                    finalMap += "8"+",";
                }else if(arrayObject[i][j].getType() == MapObject.MapType.Target){
                    finalMap += "1"+",";
                }
            }

            file.writeString(finalMap, false);
        }

    /*static void pngMap(String filename, MapObject[][] arrayObject){
        int width = 1152;
        int height = 768;

        BufferedImage bufferedImage2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB );
        Graphics g = bufferedImage2.getGraphics();

        try {

            BufferedImage grass;
            BufferedImage dirt;
            BufferedImage sand;
            BufferedImage water;

            try{
                //this is for the output .jar file
                grass = ImageIO.read(Map.class.getResource("/texture/grass.png"));
                dirt = ImageIO.read(Map.class.getResource("/texture/dirt.png"));
                sand = ImageIO.read(Map.class.getResource("/texture/sand.png"));
                water = ImageIO.read(Map.class.getResource("/texture/water.jpg"));

            }catch(IllegalArgumentException e) {

                grass = ImageIO.read(new File("texture/grass.png"));
                dirt = ImageIO.read(new File("texture/dirt.png"));
                sand = ImageIO.read(new File("texture/sand.png"));
                water = ImageIO.read(new File("texture/water.jpg"));
            }

            int xOff = 0, yOff =0;

            for(int i=1; i<arrayObject.length-1; i++) {
                for (int j = 1; j<arrayObject[0].length-1; j++) {

                    if(yOff>=(height))yOff = 0;

                    if (arrayObject[i][j].getType() == MapObject.MapType.Grass) {
                        g.drawImage(grass, xOff, yOff, null);
                    } else if (arrayObject[i][j].getType() == MapObject.MapType.Dirt) {
                        g.drawImage(dirt, xOff, yOff, null);
                    } else if (arrayObject[i][j].getType() == MapObject.MapType.Sand) {
                        g.drawImage(sand, xOff, yOff, null);
                    } else if (arrayObject[i][j].getType() == MapObject.MapType.Water) {
                        g.drawImage(water, xOff, yOff, null);
                    }else if (arrayObject[i][j].getType() == MapObject.MapType.Tree) {
                        g.drawImage(grass, xOff, yOff, null);
                    }
                    yOff +=64;
                }
                xOff += 64;
            }

            ImageIO.write(bufferedImage2,"png",new File(filename));
        }catch (IOException ioex){ ioex.printStackTrace(); }*/

    }
}







