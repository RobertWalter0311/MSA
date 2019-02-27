package com.group10.msa.ScreenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.group10.msa.MAS;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
public class GameScreen implements Screen{


    final MAS game;

	private Texture towerImg;
	private TiledMap map;
	private TiledMapRenderer renderer;
	private OrthographicCamera camera;
	private AssetManager assetManager;
	private Texture tiles;
	private Texture tiles2;
	private Texture texture;
	private BitmapFont font;
	private SpriteBatch batch;


    public GameScreen(final MAS game) {
        this.game = game;

        //load in textures
        towerImg = new Texture(Gdx.files.internal("badlogic.jpg"));


        //create Camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        camera.update();
        //creates new batch, we could also use the static public batch from menuscreen
		batch = new SpriteBatch();

		{
			//
			tiles = new Texture(Gdx.files.internal("data/Grass.png"));
			tiles2 = new Texture(Gdx.files.internal("data/Dirt.png"));

			TextureRegion t1 = new TextureRegion(tiles, 10,10);
			TextureRegion t2 = new TextureRegion(tiles2, 10,10);
			map = new TiledMap();
			MapLayers layers = map.getLayers();
			TiledMapTileLayer layer = new TiledMapTileLayer(800, 480, 10, 10);
			for (int x = 0; x < 80; x++) {
				for (int y = 0; y < 48; y++) {
					Cell cell = new TiledMapTileLayer.Cell();
					if( y % 2 == 0 && x % 2 == 0){
						cell.setTile(new StaticTiledMapTile(t2));}
						else {
							//System.out.println("im here fam");
							cell.setTile(new StaticTiledMapTile(t1));
						}
						layer.setCell(x, y, cell);
					}
				}
				layers.add(layer);
		}

		renderer = new OrthogonalTiledMapRenderer(map);
    }

	@Override
	public void dispose() {
		towerImg.dispose();
		batch.dispose();

	}

	@Override
	public void render(float delta) {

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);


		if (Gdx.input.isTouched()) {
			game.setScreen(new MenuScreen(game));
			dispose();
		}
		Gdx.gl.glClearColor(2,2,2,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderer.setView(camera);
		renderer.render();

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

