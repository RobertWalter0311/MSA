package com.group10.msa.ScreenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.group10.msa.MAS;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MenuScreen implements Screen {

    final MAS game;

    OrthographicCamera camera;

    private Stage stage;
    private Table table;
    private Skin skin;

    public MenuScreen(final MAS game) {

        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);



    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(),1/30f));
        stage.draw();

        /*game.batch.begin();
        game.font.draw(game.batch, "Welcome to the Multi Agent Surveillance System", 100, 150);
        game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }*/
    }

    @Override
    public void show() {

        TextButton startSim;
        TextButton mapBuild;
        TextButton exit;

        table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);
        stage.addActor(table);
        skin = new Skin(Gdx.files.internal("skin/neon-ui.json"));

        startSim = new TextButton("Start Simulation", skin);
        mapBuild = new TextButton("Map Builder", skin);
        exit = new TextButton("Exit",skin);

        table.add(startSim).fillX().uniformX();
        table.row().pad(10,0,10,0);
        table.add(mapBuild).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();

        startSim.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });

        mapBuild.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new BuildScreen(game));
            }
        });
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });


    }

    @Override
    public void hide() {
    }

    @Override
    public void resize(int width, int height) {


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
}
