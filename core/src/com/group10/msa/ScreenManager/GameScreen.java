package com.group10.msa.ScreenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.group10.msa.MapObjects.*;

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
	private Texture grass;
	private Texture dirt;
    private Texture target;
    private Texture wall;
    private Texture sand;
    private Texture water;
    private Texture tower;
    private Texture tree;
    //private Texture agent;
	private Texture texture;
	private BitmapFont font;
	private SpriteBatch batch;
    public float rotationSpeed;
	//private Sprite sprite;
	public int[][] world;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();
	private ArrayList<Agent> agents = new ArrayList<Agent>();
    private TargetArea target1;
    private TargetArea target2;
	private int frameRate = 0;

	public final float WALK = (float)1.4;
    public final float SPRINT = (float)3;

    public GameScreen(final MAS game) {
        this.game = game;

        //load in textures
        target = new Texture(Gdx.files.internal("data/Target.jpg"));
        grass = new Texture(Gdx.files.internal("data/Grass.png"));
        dirt = new Texture(Gdx.files.internal("data/Dirt.png"));
        wall = new Texture(Gdx.files.internal("data/Wall.jpg"));
        sand = new Texture(Gdx.files.internal("data/sand.png"));
        tree = new Texture(Gdx.files.internal("data/Tree.png"));
        tower = new Texture(Gdx.files.internal("data/Tower.jpg"));



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
			//world = defaultWorld();//new int[80][80];
			/*for(int i = 0; i < world.length; i++){
			    for(int j = 0; j < world[0].length; j++){
			        double rando =  Math.random();
			        if(rando < 0.33) world[i][j] =1;
			        else if (rando < 0.66) world[i][j] = 4;
			        else world[i][j] = 6;
                }
            }*/

			//Comment the line below to randomize again
			world = mapA.getMapArray();

			TextureRegion tGrass = new TextureRegion(grass, 10,10);
			TextureRegion tDirt = new TextureRegion(dirt, 10,10);
			TextureRegion tWall = new TextureRegion(wall, 10,10);
            TextureRegion tTarget = new TextureRegion(target, 10,10);
            TextureRegion tSand = new TextureRegion(sand, 10,10);
            TextureRegion tTower = new TextureRegion(tower, 10,10);
            TextureRegion tTree = new TextureRegion(tree, 10,10);


            map = new TiledMap();
			MapLayers layers = map.getLayers();
			TiledMapTileLayer layer = new TiledMapTileLayer(800, 800, 10, 10);
			for (int x = 0; x < 80; x++) {
				for (int y = 79; y >= 0; y--) {
					Cell cell = new TiledMapTileLayer.Cell();
					if(world[x][y] == 1){
						cell.setTile(new StaticTiledMapTile(tTarget));}
					else if (world[x][y] == 2) {
                        cell.setTile(new StaticTiledMapTile(tGrass));
                    }
                    else if (world[x][y] == 3) {
                        cell.setTile(new StaticTiledMapTile(tDirt));
                    }
                    else if (world[x][y] == 4) {
                        cell.setTile(new StaticTiledMapTile(tSand));
                    }
                    else if (world[x][y] == 7) {
                        cell.setTile(new StaticTiledMapTile(tTower));
                    }
                    else if (world[x][y] == 8) {
                        cell.setTile(new StaticTiledMapTile(tTree));
                    }
						else {
							//System.out.println("im here fam");
							cell.setTile(new StaticTiledMapTile(tWall));
						}
						layer.setCell(x, y, cell);
					}
				}
				layers.add(layer);
			Intruder agent1 = new Intruder(15,15, (float)(Math.PI),world);
			Guard agent2 = new Guard(15,250, (float)(Math.PI/2), world);
            Guard agent3 = new Guard(600,250, (float)(Math.PI/2), world);

            agents.add(agent1);
			agents.add(agent2);
            //agents.add(agent3);
			agent1.speed = (1.4f);
            agent2.speed = (1.4f);
            agent3.speed = (1.4f);
			Vector2 v1 = new Vector2(300,600);
            Vector2 v2 = new Vector2(400,400);
            target1 = new TargetArea(Target, v1);
            target2 = new TargetArea(Target, v2);
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
        //System.out.println("FPS" +  Gdx.graphics.getFramesPerSecond());
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
        batch.setProjectionMatrix(camera.combined);

        for(Agent agent: agents) {
            //agent.swerveTo(target1);
            //agent.speed = 1.4f;
            batch.begin();
            batch.setProjectionMatrix(camera.combined);
            agent.plan();
            agent.sprite.translate(-agent.sprite.getX() + agent.getX(), -agent.sprite.getY() + agent.getY());
            agent.sprite.draw(batch);
            batch.end();
            ArrayList newAgentList = new ArrayList();
            for(Agent otherAgents : agents){
                if(agent != otherAgents){
                    newAgentList.add(otherAgents);
                }

                if(agent != otherAgents && radiusDetection(agent, otherAgents)){

                    float noise = agent.normalNoiseDetection(otherAgents.getX(),otherAgents.getY());
                    System.out.println("NOIIIIISE " + noise + " "+ Math.cos(noise)*agent.audioRadius +" "+Math.sin(noise)*agent.audioRadius);

                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

                    shapeRenderer.setProjectionMatrix(camera.combined);
                    shapeRenderer.setColor(1,0,0,0);
                    shapeRenderer.line(agent.getX()+5,agent.getY()+5,(float)Math.cos(noise)*otherAgents.audioRadius+agent.getX()+5,(float)Math.sin(noise)*otherAgents.audioRadius+agent.getY()+5);
                    shapeRenderer.end();
                }
            }
            agent.setAgentList(newAgentList);

        }
       // batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setProjectionMatrix(camera.combined);

        for (Agent agent:agents) {
            shapeRenderer.setColor(new Color(1,1,1,0.5f));
            float[][] vision = agent.vision();
            //System.out.printf(" %s %s%n", vision[0][0], vision[0][1]);
            //degrees not radians
            float start = (float) (Math.toDegrees(agent.getDirection()) - (22.5));
            shapeRenderer.arc(vision[0][0], vision[0][1], agent.visionDistance, start, 45f);
            shapeRenderer.setColor(1, 1, 1, 0.2f);
            //System.out.println( "SPEED " + agent.speed + " and thus the radius " + agent.audioRadius);
            shapeRenderer.circle(vision[0][0], vision[0][1], agent.audioRadius);
            shapeRenderer.setColor(new Color(0, 0, 1, 1));
            ArrayList<float[]> points = agent.visionField(vision);
            for (float[] p : points)
                shapeRenderer.point(p[0], p[1], 0);
        }
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
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
	public boolean radiusDetection(Agent a1, Agent a2){
        float[] vector = {(a2.getX()+5-(a1.getX()+5)), (a2.getY()+5-(a1.getY()+5))};
        float temp = (float) Math.sqrt(vector[0]*vector[0] + vector[1]*vector[1]);
        //System.out.println("vec length " +  temp + " rad " + a2.audioRadius);

        if(temp <= a2.audioRadius) {
            return true;
        }
        else return false;

    }

    public int[][]  defaultWorld(){
        int[][] w= new int[80][80];
        for (int i = 0; i < 80; i++){
            w[i][0] = 8;
            w[0][i] = 8;
            w[79][i] = 8;
            w[i][79] = 8;
        }
        for (int i = 30; i < 50; i++){
            //w[i][30] = 8;
            w[i][40] = 8;
            //w[50][i] = 8;
            //w[i][50] = 8;
        }
        w[3][26] = 1;
        return w;
    }

}

