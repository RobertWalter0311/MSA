package com.group10.msa.ScreenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.group10.msa.MAS;
import com.group10.msa.MapObjects.Agent;
import com.group10.msa.MapObjects.Map;
import com.group10.msa.MapObjects.MapObject;
import com.group10.msa.MapObjects.TargetArea;

import java.util.ArrayList;

import static com.group10.msa.MapObjects.MapObject.MapType.Target;
//import com.group10.msa.MapObjects.Map;

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
    private float rotationSpeed;
	private Sprite sprite;
	public int[][] world;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private Agent agent1;
	private TargetArea target1;
	private TargetArea target2;
	private int frameRate = 0;

	public final float WALK = (float)1.4;
    public final float SPRINT = (float)3;

    public GameScreen(final MAS game) {
        this.game = game;

        //load in textures
        tiles = new Texture(Gdx.files.internal("data/Grass.png"));
        tiles2 = new Texture(Gdx.files.internal("data/Dirt.png"));
        wall = new Texture(Gdx.files.internal("data/Wall.jpg"));
        agent = new Texture(Gdx.files.internal("data/CropAgent.jpg"));



        //create Camera
        rotationSpeed = 0.5f;
        camera = new OrthographicCamera(800,800);
        camera.setToOrtho(false, 800, 800);
        camera.update();
        //creates new batch, we could also use the static public batch from menuscreen
		batch = new SpriteBatch();

		{
			//
            Map mapA = new Map();
			world = new int[80][80];
			for(int i = 0; i < world.length; i++){
			    for(int j = 0; j < world[0].length; j++){
			        double rando =  Math.random();
			        if(rando < 0.33) world[i][j] =1;
			        else if (rando < 0.66) world[i][j] = 4;
			        else world[i][j] = 6;
                }
            }

			//Comment the line below to randomize again
			world = mapA.getMapArray();

			TextureRegion t1 = new TextureRegion(tiles, 10,10);
			TextureRegion t2 = new TextureRegion(tiles2, 10,10);
			TextureRegion twall = new TextureRegion(wall, 10,10);
			map = new TiledMap();
			MapLayers layers = map.getLayers();
			TiledMapTileLayer layer = new TiledMapTileLayer(800, 800, 10, 10);
			for (int x = 0; x < 80; x++) {
				for (int y = 79; y >= 0; y--) {
					Cell cell = new TiledMapTileLayer.Cell();
					if(world[x][y] == 4){
						cell.setTile(new StaticTiledMapTile(t2));}
						else if (world[x][y] == 6)
						    cell.setTile(new StaticTiledMapTile(twall));
						else {
							//System.out.println("im here fam");
							cell.setTile(new StaticTiledMapTile(t1));
						}
						layer.setCell(x, y, cell);
					}
				}
				//layer.setOpacity(0.f);
				layers.add(layer);
			agent1 = new Agent(400,400, (float)((0*Math.PI/4)),world);
			Vector2 v1 = new Vector2(400,600);
            Vector2 v2 = new Vector2(400,400);
            target1 = new TargetArea(Target, v1);
            target2 = new TargetArea(Target, v2);
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
        //manipulate camera eg. zoom
        handleInput();
        // tell the camera to update its matrices.
        //camera.position.set(sprite.getX(), sprite.getY(), 0);
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
                sprite.translateX(0.1f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)&& sprite.getY() > 0){
            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                sprite.translateY(-10f);
            else
                sprite.translateY(-0.1f);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)&& sprite.getY() < 790){
            if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
                sprite.translateY(10f);
            else
                sprite.translateY(0.1f);
        }

        agent1.swerveTo(target1, WALK);
        sprite.translate(-sprite.getX()+agent1.getX(), -sprite.getY()+agent1.getY());
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.setProjectionMatrix(camera.combined);
        sprite.draw(batch);
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setColor(new Color(1,1,1,0.5f));
        int agentx = (int) (sprite.getX()+5);
        int agenty = (int) (sprite.getY()+5);
        //float x2 = (float) Math.cos(45);

        float[][] vision =  agent1.vision();
        System.out.printf(" %s %s%n", vision[0][0], vision[0][1]);
        //degrees not radians
        float start = (float)(Math.toDegrees(agent1.getDirection())-(22.5));//(float)(agent1.getDirection()+(Math.PI/8));
        shapeRenderer.arc(vision[0][0], vision[0][1], 75f,start,45f );



                //shapeRenderer.cone(vision[0][0],vision[0][1], 0, 75f,75f);
        //shapeRenderer.triangle(vision[0][0],vision[0][1],vision[1][0],vision[1][1],vision[2][0],vision[2][1]);
        //shapeRenderer.triangle(agentx,agenty,(float)(50*Math.cos(-0.3926991)+agentx),(float)(50*Math.sin(-0.3926991)+agenty),(float)(50*Math.cos(0.3926991)+agentx),(float)(50*Math.sin(0.3926991)+agenty) );

        shapeRenderer.setColor(new Color(1,0,0,1));
        shapeRenderer. point(80,80,0);
        shapeRenderer. point(vision[1][0],vision[1][1],0);
        shapeRenderer. point(vision[2][0],vision[2][1],0);

        shapeRenderer.setColor(new Color(0,0,1,1));
        ArrayList<float[]> points = agent1.visionField(vision);
        for(float[] p : points)
            shapeRenderer.point(p[0],p[1],0);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);



       /* agentx = agentx/10;
        agenty = agenty/10;
        System.out.println("agent is on tile x: " + (agentx) + " y: " + (agenty));
        if(agentx < 80 && agenty < 80){
            if(world[agentx][agenty] == 1 ) System.out.println("thus hes on tile Grass ");
            if(world[agentx][agenty] == 6) System.out.println("thus hes on tile Wall ");
            if(world[agentx][agenty] == 4) System.out.println("thus hes on tile Tower");
        }*/

//        System.out.println("agent is on tile x: " + (agentx) + " y: " + (agenty));
//        if(agentx < 80 && agenty < 48)
//            System.out.println("thus hes on tile " + world[agentx][agenty]);
        System.out.println("DIRECTIONNN" + agent1.getDirection());
    }


    private void handleInput() {
        //key ordering is botched for now
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            camera.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            camera.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.translate(-3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.translate(3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.translate(0, -3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.translate(0, 3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.rotate(-rotationSpeed, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.rotate(rotationSpeed, 0, 0, 1);
        }

        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 800 / camera.viewportWidth);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

      //  camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, 800 - effectiveViewportWidth);
        // camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, 480 - effectiveViewportHeight);
    }


    @Override
    public void show(){
    }

    @Override
    public void hide(){
    }

    @Override
	public void resize(int width, int height) {
       camera.viewportWidth = 800;
       camera.viewportHeight = 480;
       camera.update();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}

