package com.group10.msa;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;
import com.group10.msa.ScreenManager.MenuScreen;

//TestDraw

public class MAS extends Game {

    public SpriteBatch batch;
    public BitmapFont font;

    public void create(){

        batch = new SpriteBatch();
        font = new BitmapFont();
        this.setScreen(new MenuScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

}


/*public class GdxGame implements ApplicationListener {



    private Map map;



	private Texture towerImg;
	private Texture targetImg;
    private Texture treeImg;

    private OrthographicCamera camera;

	@Override
	public void create() {

		towerImg = new Texture(Gdx.files.internal("badlogic.jpg"));
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.BLUE);

		camera = new OrthographicCamera();
		camera.setToOrtho(false,480,320 );
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}

	@Override
	public void render() {

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);


        Gdx.gl.glClearColor(0, 2, 2, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(towerImg,0,0);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}*/
