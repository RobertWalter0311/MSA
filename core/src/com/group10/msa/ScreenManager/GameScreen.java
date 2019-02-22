package com.group10.msa.ScreenManager;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.group10.msa.MAS;
import com.group10.msa.MapObjects.Map;

public class GameScreen implements Screen{


    final MAS game;

	private Texture towerImg;

    private OrthographicCamera camera;


    public GameScreen(final MAS game) {
        this.game = game;

        //load in textures
        towerImg = new Texture(Gdx.files.internal("badlogic.jpg"));


        //create Camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

	@Override
	public void dispose() {
		towerImg.dispose();
	}

	@Override
	public void render(float delta) {

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);


        Gdx.gl.glClearColor(2, 2, 2, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		game.batch.draw(towerImg,0,0);
		game.batch.end();

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

