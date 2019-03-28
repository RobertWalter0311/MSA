package com.group10.msa.ScreenManager;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.group10.msa.MapObjects.Map;
import com.group10.msa.MapObjects.MapObject;
import com.group10.msa.MapObjects.MapObject.MapType;


public class BuildScreen extends ApplicationAdapter implements InputProcessor,Screen{

    //final MAS game;

    SpriteBatch batch;
    OrthographicCamera cam;
    Viewport viewport;
    ShapeRenderer render;
    Stage stage;
    Skin skin;

    MapType terrainSelect = MapType.Target;

    MapObject[][] arrayObject;

    MapObject target;
    boolean targetPlaced = false;

    private BitmapFont font;



    public BuildScreen() {



        BuildAssets.load();

        skin = new Skin(Gdx.files.internal("skin/neon-ui.json"));

        cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        cam.position.set(cam.viewportWidth/2, cam.viewportHeight/2,0);
        cam.update();

        viewport = new FitViewport(cam.viewportWidth, cam.viewportHeight);

        render = new ShapeRenderer();

        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.BLACK);

        arrayObject = new MapObject[80][80];

        //fill the outer fields with wall
        for(int i=0; i<arrayObject.length; i++){
            for(int j=0; j<arrayObject[0].length; j++){
                if(i==0|| i== arrayObject.length-1 || j==0 || j== arrayObject[0].length-1) {
                    arrayObject[i][j] = new MapObject(MapObject.MapType.Wall, new Vector2(i * 16, 1080 - j * 16));
                }else{
                    arrayObject[i][j] = new MapObject(MapType.Grass, new Vector2(i * 16, 1080 - j * 16));

                }
            }
        }

        stage = new Stage(new ScreenViewport());
        Table table = new Table().debug();

        final Dialog dialog = new Dialog("Warning",skin){};
        dialog.text("Be sure that the target is placed and all boxes are filled");
        dialog.button("OK",true);

        TextButton save = new TextButton("Save Map",skin);
        save.addListener(new InputListener(){
            public boolean touchDown(InputEvent event,float x, float y,int pointer,int button){

                int cntr = 0;

                for (int i = 0; i<arrayObject.length; i++){
                    for (int j = 0; j<arrayObject[0].length; j++){

                        if(arrayObject[i][j].getType()== MapType.Null)
                            cntr++;
                    }
                }

                if (cntr == 0) Map.createMap(arrayObject, target);
                else dialog.show(stage);
                return  true;
            }
        });

        TextButton resetTargetArea = new TextButton("Reset Target Area",skin);
        resetTargetArea.addListener(new InputListener(){
            public boolean touchDown(InputEvent event,float x, float y, int pointer, int button){
                target = null;
                targetPlaced = false;
                return true;
            }
        });



        table.add(resetTargetArea);
        table.row();
        table.add(save);
        table.row();


        table.pack();
        table.setPosition(1280,240);
        stage.addActor(table);

        InputProcessor inputProcessorOne = this;
        InputProcessor inputProcessorTwo = stage;

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(inputProcessorOne);
        multiplexer.addProcessor(inputProcessorTwo);
        Gdx.input.setInputProcessor(multiplexer);



    }


    public void render(float delta){
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        render.setProjectionMatrix(cam.combined);
        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        batch.draw(BuildAssets.gridB,0,8,1280,1280);

        for (int i=0;i<arrayObject.length;i++){
            for (int j=0;j<arrayObject[0].length;j++){
                arrayObject[i][j].draw(batch);
            }
        }

        if(targetPlaced)target.draw(batch);

        batch.draw(BuildAssets.tower,1380,820);
        batch.draw(BuildAssets.target,1295,820);
        batch.draw(BuildAssets.grass,1295,740);
        batch.draw(BuildAssets.dirt,1380,740);
        batch.draw(BuildAssets.sand,1295,660);
        batch.draw(BuildAssets.wall,1380,660);
        batch.draw(BuildAssets.water,1295,580);
        batch.draw(BuildAssets.tree,1380,580);
        batch.end();

        render.begin(ShapeRenderer.ShapeType.Filled);
        render.setColor(Color.RED);

        if(terrainSelect == MapType.Target)
            render.circle(1315, 840, 8);
        else if(terrainSelect == MapType.Grass)
            render.circle(1295, 740, 8);
        else if(terrainSelect == MapType.Dirt)
            render.circle(1380, 740, 8);
        else if(terrainSelect == MapType.Sand)
            render.circle(1295, 660, 8);
        else if(terrainSelect == MapType.Water)
            render.circle(1380, 660, 8);
        else if(terrainSelect == MapType.Wall)
            render.circle(1295, 580, 8);
        else if(terrainSelect == MapType.Tree)
            render.circle(1380, 580, 8);




        render.end();

        stage.act();
        stage.draw();

    }

    @Override
    public void dispose(){
        batch.dispose();
        render.dispose();
        BuildAssets.dispose();
    }
    @Override
    public void show(){
    }

    @Override
    public void hide(){
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public boolean keyTyped(char character){
        return false;
    }

    @Override
    public boolean keyDown(int keycode){

        return false;
    }

    @Override
    public boolean keyUp(int keycode){

        if (keycode== Input.Keys.NUM_1)
            terrainSelect = MapType.Grass;
        else if (keycode== Input.Keys.NUM_2)
            terrainSelect = MapType.Dirt;
        else if (keycode== Input.Keys.NUM_3)
            terrainSelect = MapType.Sand;
        else if (keycode== Input.Keys.NUM_4)
            terrainSelect = MapType.Target;
        else if (keycode== Input.Keys.NUM_5)
            terrainSelect = MapType.Wall;
        else if (keycode== Input.Keys.NUM_9)
            terrainSelect = MapType.Tower;
        else if (keycode== Input.Keys.NUM_7)
            terrainSelect = MapType.Tree;
        else if (keycode== Input.Keys.NUM_8)
            terrainSelect = MapType.Water;
        return false;

    }

    @Override
    public boolean touchDown(int screenX,int screenY,int pointer, int button){
        touchDraw(screenX,screenY);
        return false;
    }

    @Override
    public boolean touchUp(int screenX,int screenY,int pointer, int button){
        return false;
    }

    @Override
    public boolean touchDragged(int screenX,int screenY,int pointer){
        touchDraw(screenX,screenY);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX,int screenY){
        return false;
    }

    @Override
    public boolean scrolled(int amount){
        return false;
    }

    private void touchDraw(int screenX,int screenY){

        Vector3 mouse = new Vector3(screenX,screenY,0);
        cam.unproject(mouse);

        for (int i=1; i<arrayObject.length; i++){
            for (int j=1; j<arrayObject[0].length-1;j++){

                Rectangle rec = new Rectangle(arrayObject[i][j].getPos().x,arrayObject[i][j].getPos().y,16,16);
                if(rec.contains(mouse.x,mouse.y)){
                    if(terrainSelect == MapType.Target && !targetPlaced){// target placing
                    target = new MapObject(terrainSelect, new Vector2(i*16 , 1080 - 16*j));
                    targetPlaced = true;

                }else if(terrainSelect != MapType.Target){
                        arrayObject[i][j]= new MapObject(terrainSelect,new Vector2(i*16,1080-16*j));
                    }
                }
            }
        }
    }
}
