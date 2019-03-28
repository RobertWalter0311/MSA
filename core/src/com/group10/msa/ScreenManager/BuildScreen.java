
package com.group10.msa.ScreenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.ui.textButtonStyle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.group10.msa.MAS;

    public class BuildScreen implements Screen {


        private MAS game;

        private Texture towerImg;

        private TextButton button;

        private Stage stage;

        private Skin skin;

        private OrthographicCamera camera;


        public BuildScreen(MAS game) {
            this.game = game;

            //load in textures
            //towerImg = new Texture(Gdx.files.internal("badlogic.jpg"));


            //create Camera
            camera = new OrthographicCamera();
            camera.setToOrtho(false, 800, 480);
           // camera.translate(xPos, yPos);
            //Stage stage = new Stage(your wanted stage width, your wanted stage height, false, batch);
            //stage.setCamera(camera);
            stage = new Stage(new ScreenViewport());
            Gdx.input.setInputProcessor(stage);
            for(int y = 0; y < 20; y++){
                for(int x = 0; x < 20; x++){
                    stage.addActor(new TextButton("", skin));
                }
            }
        }

        @Override
        public void dispose() {
            towerImg.dispose();
        }

        @Override
        public void render(float delta) {
            float buttonWidth = 800 / 20;
            float buttonHeight = 480 / 20;
            for(int y = 0; y < 20; y++){
                for(int x = 0; x < 20; x++){
                   // button = stage.getActors().get(x + y * 20);
                    button.setX(x * buttonWidth);
                    button.setY(y * buttonHeight);
                    button.setWidth(buttonWidth);
                    button.setHeight(buttonHeight);
                }
            }

            // tell the camera to update its matrices.
            camera.update();

            // tell the SpriteBatch to render in the
            // coordinate system specified by the camera.
            game.batch.setProjectionMatrix(camera.combined);


            Gdx.gl.glClearColor(2, 2, 2, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            stage.act(delta);
            stage.draw();

            if (Gdx.input.isTouched()) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        }

        @Override
        public void show(){
        }

        @Override
        public void hide(){
        }

        @Override
        public void resize(int width, int height) {
        }

        @Override
        public void pause() {
        }

        @Override
        public void resume() {
        }
    }
