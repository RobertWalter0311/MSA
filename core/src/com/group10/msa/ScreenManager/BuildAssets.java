package com.group10.msa.ScreenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class BuildAssets {


    private static TextureAtlas atlas;

    public static TextureRegion target;
    public static TextureRegion grass;
    public static TextureRegion wall;
    public static TextureRegion dirt;
    public static TextureRegion water;
    public static TextureRegion tree;
    public static TextureRegion sand;
    public static TextureRegion tower;

    public static Texture gridB;

    public static BitmapFont font;
    public static Skin skin;

    //public static AssetManager assetMng;
    //public static Sprite sprite;

    public static void load(){

        /*assetMng.load("packOfImages.pack", TextureAtlas.class);
        assetMng.finishLoading();

        //atlas = new TextureAtlas(Gdx.files.internal("Data/Grass.atlas"));
        atlas=assetMng.get("packOfImages.pack");
        sprite=new Sprite(atlas.findRegion("Ausy1"));
        sprite.setSize(20,40);*/


        atlas = new TextureAtlas(Gdx.files.internal("masAtlasS.atlas"));

        target = atlas.findRegion("targets");
        grass = atlas.findRegion("grasss");
        wall = atlas.findRegion("walls");
        tree = atlas.findRegion("trees");
        dirt = atlas.findRegion("dirts");
        sand = atlas.findRegion("sands");
        water = atlas.findRegion("waters");
        tower = atlas.findRegion("towers");

        skin = new Skin(Gdx.files.internal("skin/neon-ui.json"));

        gridB= new Texture(Gdx.files.internal("data/gridb.PNG"));

    }

    public static void dispose(){
        skin.dispose();
        atlas.dispose();
        gridB.dispose();
    }
}
