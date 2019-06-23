package com.group10.msa.ScreenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.group10.msa.MAS;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class SetupScreen implements Screen{

    final MAS game;

    private Stage stage;
    private Skin skin;

    OrthographicCamera camera;

    public SetupScreen(final MAS game) {

        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);



    }
    @Override
    public void show() {

        final SelectBox<String> selectBox=new SelectBox<String>(skin);
        selectBox.setItems("XYZ","ABC","PQR","LMN");

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(),1/30f));
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,true);
    }

    @Override
    public void pause(){

    }

    @Override
    public void resume(){
    }

    @Override
    public void dispose(){
        stage.dispose();
    }

    @Override
    public void hide() {

    }
}


