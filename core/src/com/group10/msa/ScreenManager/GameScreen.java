package com.group10.msa.ScreenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.group10.msa.MAS;
public class GameScreen implements Screen{


    final MAS game;

	private Texture towerImg;
	private TiledMap map;
	private TiledMapRenderer renderer;
	private OrthographicCamera camera;
	private AssetManager assetManager;
	private Texture tiles;
	private Texture tiles2;
    private Texture wall;
    private Texture agent;
	private Texture texture;
	private BitmapFont font;
	private SpriteBatch batch;
	private Sprite sprite;
	private int[][] world;


    public GameScreen(final MAS game) {
        this.game = game;

        //load in textures
        tiles = new Texture(Gdx.files.internal("data/Grass.png"));
        tiles2 = new Texture(Gdx.files.internal("data/Dirt.png"));
        wall = new Texture(Gdx.files.internal("data/Wall.jpg"));
        agent = new Texture(Gdx.files.internal("data/CropAgent.jpg"));



        //create Camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        camera.update();
        //creates new batch, we could also use the static public batch from menuscreen
		batch = new SpriteBatch();

		{
			//

			world = new int[80][48];
			for(int i = 0; i < world.length; i++){
			    for(int j = 0; j < world[0].length; j++){
			        double rando =  Math.random();
			        if(rando < 0.33) world[i][j] =1;
			        else if (rando < 0.66) world[i][j] = 3;
			        else world[i][j] = 2;
                }
            }

			TextureRegion t1 = new TextureRegion(tiles, 10,10);
			TextureRegion t2 = new TextureRegion(tiles2, 10,10);
			TextureRegion twall = new TextureRegion(wall, 10,10);
			map = new TiledMap();
			MapLayers layers = map.getLayers();
			TiledMapTileLayer layer = new TiledMapTileLayer(800, 480, 10, 10);
			for (int x = 0; x < 80; x++) {
				for (int y = 47; y >= 0; y--) {
					Cell cell = new TiledMapTileLayer.Cell();
					if(world[x][y] == 1){
						cell.setTile(new StaticTiledMapTile(t2));}
						else if ( world[x][y] == 3)
						    cell.setTile(new StaticTiledMapTile(twall));
						else {
							//System.out.println("im here fam");
							cell.setTile(new StaticTiledMapTile(t1));
						}
						layer.setCell(x, y, cell);
					}
				}
				layers.add(layer);
            TextureRegion tagent = new TextureRegion(agent,10,10);
            sprite = new Sprite(tagent);

		}

		renderer = new OrthogonalTiledMapRenderer(map);
    }

	@Override
	public void dispose() {
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
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)&& sprite.getX() > 0){
            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                sprite.translateX(-0.1f);
            else
                sprite.translateX(-10.0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)&& sprite.getX() < 790){
            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                sprite.translateX(10f);
            else
                sprite.translateX(10.0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)&& sprite.getY() > 0){
            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                sprite.translateY(-10f);
            else
                sprite.translateY(-10.0f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)&& sprite.getY() < 470){
            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                sprite.translateY(10f);
            else
                sprite.translateY(10.0f);
        }
        batch.begin();
        sprite.draw(batch);
        batch.end();
        int agentx = (int) sprite.getX()/10;
        int agenty = (int) sprite.getY()/10;
        System.out.println("agent is on tile x: " + (agentx) + " y: " + (agenty));
        if(agentx < 80 && agenty < 48)
        System.out.println("thus hes on tile " + world[agentx][agenty]);
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

