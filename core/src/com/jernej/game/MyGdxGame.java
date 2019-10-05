package com.jernej.game;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


public class MyGdxGame extends ApplicationAdapter {
	private Texture carImage,truck1Image,truck2Image,backgroundImage,gasImage;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Array<Rectangle> truck1;
	private Array<Rectangle> truck2;
	private Array<Rectangle> gas;
	private long lastTruck1Time;
	private long lastTruck2Time;
	private long lastgasTime;
	private int gasLetf;
	private Rectangle car;
	private int gasCollected;
	private int carHealth;
	private BitmapFont font;

	private int currentLocation=4;
	private int[] spawnLocation = {0,106,217,341,455,568,682,806,919};

	private static int SPEED = 600;
	private static int SPEED_GAS = 200;
	private static int SPEED_TRUCK1= 100;
	private static int SPEED_TRUCK2= 100;
	private static long CREATE_GAS_TIME = 1000000000;
	private static long CREATE_TRUCK1_TIME = 2140000000;
	private static long CREATE_TRUCK2_TIME = 2140000000;

	private static Sprite backgroundSprite;

	private void commandExitGame() {
		Gdx.app.exit();
	}

	private void commandMoveLeft() {
		if(currentLocation != 0 ){
			currentLocation--;
			car.x=spawnLocation[currentLocation];
		}
	}

	private void commandMoveReght() {
		if(currentLocation != 8 ){
			currentLocation++;
			car.x=spawnLocation[currentLocation];
		}
	}

	@Override
	public void create() {

		font = new BitmapFont();
		font.getData().setScale(2);
		gasCollected = 0;
		carHealth = 3;

		carImage = new Texture(Gdx.files.internal("car.png"));
		truck1Image = new Texture(Gdx.files.internal("truck1.png"));
		truck2Image = new Texture(Gdx.files.internal("truck2.png"));
		gasImage = new Texture(Gdx.files.internal("gas.png"));
		backgroundImage=new Texture(Gdx.files.internal("road.png"));
		backgroundSprite =new Sprite(backgroundImage);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();

		car = new Rectangle();
		car.x = spawnLocation[currentLocation]; // center the rocket horizontally
		car.y = 20; // bottom left corner of the rocket is 20 pixels above the bottom screen edge
		car.width = carImage.getWidth();
		car.height = carImage.getHeight();


		gas = new Array<Rectangle>();
		truck1 = new Array<Rectangle>();
		truck2 = new Array<Rectangle>();
		//add first astronoutn and asteroid
		spawnGas();
		spawnTruck1();
		spawnTruck2();
	}

	private void spawnGas() {
		Rectangle tmpGas = new Rectangle();
		tmpGas.x = spawnLocation[MathUtils.random(0, 8)];
		tmpGas.y = Gdx.graphics.getHeight();
		tmpGas.width  = gasImage.getWidth();
		tmpGas.height = gasImage.getHeight();
		gas.add(tmpGas);
		lastgasTime = TimeUtils.nanoTime();
	}

	private void spawnTruck1() {
		Rectangle tmpTruck = new Rectangle();
		tmpTruck.x = spawnLocation[MathUtils.random(0, 8)];
		tmpTruck.y = Gdx.graphics.getHeight();
		tmpTruck.width = truck1Image.getWidth();
		tmpTruck.height = truck1Image.getHeight();
		truck1.add(tmpTruck);
		lastTruck1Time = TimeUtils.nanoTime();
	}
	private void spawnTruck2() {
		Rectangle tmpTruck = new Rectangle();
		tmpTruck.x = spawnLocation[MathUtils.random(0, 8)];
		tmpTruck.y = Gdx.graphics.getHeight();
		tmpTruck.width = truck2Image.getWidth();
		tmpTruck.height = truck2Image.getHeight();
		truck2.add(tmpTruck);
		lastTruck2Time = TimeUtils.nanoTime();
	}

	public void renderBackground() {
		backgroundSprite.draw(batch);
	}

	@Override
	public void render() { //runs every frame
		//clear screen
		Gdx.gl.glClearColor(0, 0, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the rocket, astronauts, asteroids
		batch.begin();
		renderBackground();
		{ //add brackets just for intent
			batch.draw(carImage, car.x, car.y);
			for (Rectangle truck : truck1) {
				batch.draw(truck1Image, truck.x, truck.y);

			}
			for (Rectangle truck : truck2) {
				batch.draw(truck2Image, truck.x, truck.y);

			}
			for (Rectangle gastmp : gas) {
				batch.draw(gasImage, gastmp.x, gastmp.y);
			}
			font.setColor(Color.YELLOW);
			font.draw(batch, "" + gasCollected, Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 20);
			font.setColor(Color.GREEN);
			font.draw(batch, "" + carHealth, 20, Gdx.graphics.getHeight() - 20);
			if(gasCollected<0){
				font.setColor(Color.RED);
				font.draw(batch, "GET GAS!", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
			}
		}
		batch.end();

		// process user input
		if(Gdx.input.isKeyJustPressed(Keys.LEFT)) commandMoveLeft();
		if(Gdx.input.isKeyJustPressed(Keys.RIGHT)) commandMoveReght();
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) commandExitGame();

		// check if we need to create a new
		if(TimeUtils.nanoTime() - lastTruck1Time > CREATE_TRUCK1_TIME) spawnTruck1();
		if(TimeUtils.nanoTime() - lastTruck2Time > CREATE_TRUCK2_TIME) spawnTruck2();
		if(TimeUtils.nanoTime() - lastgasTime > CREATE_GAS_TIME) spawnGas();
		if(TimeUtils.nanoTime()%200==0) gasCollected--;

		if (carHealth > 0 && gasCollected>=-1) { //is game end?
			// move and remove any that are beneath the bottom edge of
			// the screen or that hit the rocket.
			for (Iterator<Rectangle> iter = truck1.iterator(); iter.hasNext(); ) {
				Rectangle tmpTruck = iter.next();
				tmpTruck.y -= SPEED_TRUCK1 * Gdx.graphics.getDeltaTime();
				if (tmpTruck.y + truck1Image.getHeight() < 0) iter.remove();
				if (tmpTruck.overlaps(car)) {
					carHealth--;
					iter.remove();
				}
			}

			for (Iterator<Rectangle> iter = truck2.iterator(); iter.hasNext(); ) {
				Rectangle tmpTruck = iter.next();
				tmpTruck.y -= SPEED_TRUCK2 * Gdx.graphics.getDeltaTime();
				if (tmpTruck.y + truck2Image.getHeight() < 0) iter.remove();
				if (tmpTruck.overlaps(car)) {
					carHealth--;
					iter.remove();
				}
			}

			for (Iterator<Rectangle> iter = gas.iterator(); iter.hasNext(); ) {
				Rectangle tmpGas = iter.next();
				tmpGas.y -= SPEED_GAS * Gdx.graphics.getDeltaTime();
				if (tmpGas.y + gasImage.getHeight() < 0) iter.remove(); //From screen
				if (tmpGas.overlaps(car)) {
					gasCollected++;
					if (gasCollected%10==0){
						SPEED_TRUCK1+=66;
						SPEED_TRUCK2+=66;
					}//speeds up
					iter.remove(); //smart Array enables remove from Array
				}
			}
		} else { //health of rocket is 0 or less
			batch.begin();
			{
				font.setColor(Color.RED);
				font.draw(batch, "The END", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 3);
			}
			batch.end();
		}
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
		carImage.dispose();
		truck1Image.dispose();
		gasImage.dispose();
		batch.dispose();
		font.dispose();
	}

}
