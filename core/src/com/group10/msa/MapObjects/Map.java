package com.group10.msa.MapObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.io.*;


public class Map {

    public MapObject[][] mapObjects;
    private int[][] mapArray;
    private MapObject Tower;
    private MapObject Target;

    public Map() {
        try {
            mapArray = readFileArray();

            for(int i=0; i<mapArray[0].length;i++){
                for(int j = 0; j<mapArray.length; j++){
                    System.out.print(mapArray[j][i]);
                }
                System.out.println();
            }
        } catch (IOException FileNotFoundException) {

        }

    }
    public int[][] getMapArray(){
        return mapArray;
    }

    public int[][] readFileArray() throws IOException {
        File file = new File("C:\\Users\\Kit\\Documents\\Git\\MSA6\\core\\assets\\Data\\1wall.tmx");
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
                        intArray[u][j] = Character.getNumericValue(line.charAt(i));
                        u++;
                        if(line.charAt(i) == '6'){
                            i++;
                        }
                    }

                }
                u = 0;
                j++;
                array += '\n';
            }



        }

        //System.out.println(array);
        return intArray;
    }
}

//    public int[][] generateArray(){
//
//
//    }



