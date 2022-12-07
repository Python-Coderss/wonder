package com.grg.j3d.game;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.sound.midi.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.grg.j3d.player.Player;
import com.grg.j3d.serial.Serial;
import com.grg.j3d.sound.*;

public class Game extends ApplicationAdapter implements Runnable {
	
	public boolean  flag = true;
	public boolean  rflag = true;
	public boolean disp = false;
	BitmapFont bb;
	PerspectiveCamera camera;
	ModelCache modelBatch;
	ModelBatch b3;
	ModelBuilder modelBuilder;
	Model box;
	ModelInstance modelInstance;
	Environment environment;
	Object3d[][][] obj3d;
	public static Texture t, t2, t3, t4, t5, t6, t7;
	public static int size = 150;
	final int ls = 151;
	public float x = ls,y = ls,z = ls;
	private CameraInputController c;
	private Music m;
	private List<BlockType> bl;
	Iterator<BlockType> BlockIterator;
    boolean gravity = false; 
	
	float yaw,pitch,roll;
	private ShapeRenderer srend;
	private Thread tr;
	BlockType selectedBlock;
	
	boolean doreset = false;
	private SpriteBatch batch;
	private boolean playing;
	private Texture title;
	private int seed;
	private PerlinNoise pn;
	public static Sequencer player;
	public Player p;
	private boolean inv;
	private Texture inven;
    protected boolean isVisible(final Camera cam, int i, int o, int p) {
    	
    	return cam.frustum.pointInFrustum(i, o, p);
    }
    public boolean isIn(Vector3 /*bottomleftcloseblockpos*/ pos, Vector3 point) {
    	boolean comp1 = inRange(pos.x, pos.x + 1, point.x);
    	boolean comp2 = inRange(pos.y, pos.y + 1, point.y);
    	boolean comp3 = inRange(pos.z, pos.z + 1, point.z);
    	
    	return comp1&&comp2&&comp3;
    }
    public Vector3 floor(Vector3 vec) {
    	float x = (float) Math.floor(vec.x);
    	float y = (float) Math.floor(vec.y);
    	float z = (float) Math.floor(vec.z);
    	return new Vector3(x, y, z);
    }
    public Vector3 camPosition() {
    	
    	return floor(camera.position);
    }
	@Override
	public void create () {
		
		
		m = new Music();
		tr = new Thread(this);
		tr.setDaemon(true);
		tr.start();
		batch = new SpriteBatch();
		camera = new PerspectiveCamera(80, 500, 1000);
		//camera = new OrthographicCamera(500, 1000);
		
		camera.position.set(x,y,z);
		bb = new BitmapFont();
		
		
		//camera.lookAt(new Vector3(0,0,0));
		
		camera.near = 0.01F;
		
		camera.far = 30f;
		
		
		
		modelBatch = new ModelCache();
		b3 = new ModelBatch();
		
		
		
		
		
		t = new Texture("stone.png");
		
		t2 = new Texture("coal.png");
		
		t3 = new Texture("emerald.png");
		
		t4 = new Texture("diamond.png");
		
		t5 = new Texture("dirt.png");
		
		t6 = new Texture("ruby.png");
		
		t7 = new Texture("grass.png");
		
		title = new Texture("play.png");
		
		inven = new Texture("inv.png");
		
		environment = new Environment();
		
		
		
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0f, 0f, 0.5f, 1));
		
		obj3d = new Object3d[size][size][size];
		
		Object3d.init();
		
		
		
		Gdx.input.setInputProcessor(c);
		srend = new ShapeRenderer();
        srend.setProjectionMatrix(camera.combined);
        srend.setColor(Color.WHITE);
		
        selectedBlock = BlockType.coal;
        bl = Arrays.asList(BlockType.values());
        BlockIterator = bl.iterator();
        p = new Player(this);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
			
			
			boolean gen = true;
			System.out.print("Read world or create new answer read or anything: ");
			String v = in.readLine().trim();
			gen = !("read".equals(v));
			if (gen) {
				
				System.out.println();
				System.out.print("Seed: ");
				seed = Integer.valueOf(in.readLine());
				System.out.println();
			}
			if (!gen ) {
				FileInputStream i = new FileInputStream("D:\\save.save");
				ObjectInputStream in2 = new ObjectInputStream(i);
				Serial s = (Serial) in2.readObject();
				p.i = s.i;
				obj3d = s.obj3d;
				x = s.loc.p1;
				y = s.loc.p2;
				z = s.loc.p3;
				in2.close();
			} else {
				pn = new PerlinNoise(seed);
				regen();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			seed = 1;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			obj3d = new Object3d[size][size][size];
			regen();
		}
        
        try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	boolean inRange(double min, double max, double val) {
		boolean r = ((val > min) && (val < max)) || val == max || val == min;
		return r;
	}
	
	private void regen() {
		System.out.println("Regen line 1");
				    

				    
		
		for (int i = 0; i < size; i++) {
			for (int o = 0; o < size; o++) {
				double r = 0;
				
				
				
			for (int p = 0; p < size; p++) {
					r = Math.abs(pn.noise((i), (p), (o), size / 4));
					System.out.println(r);
					if (r <= 0.38)
					obj3d[i][p][o] = null;
					else {
						if (r <= 0.45) obj3d[i][p][o] = new Object3d(BlockType.grass);
						else if (r <= 0.5) obj3d[i][p][o] = new Object3d(BlockType.dirt);
						else if (r <= 0.6) obj3d[i][p][o] = new Object3d(BlockType.stone);
						else if (r <= 0.7) obj3d[i][p][o] = new Object3d(BlockType.coal);
						else if (r <= 0.8) obj3d[i][p][o] = new Object3d(BlockType.diamond);
						else if (r <= 0.9) obj3d[i][p][o] = new Object3d(BlockType.ruby);
						else obj3d[i][p][o] = new Object3d(BlockType.emerald);
					}
					
				}
			}
		}
		
	}
	
	public void pause() {
		
		flag = false;
		
	}
	
	
	
	
 
        
	public float restrict(float val, int cap) {
		return (val < 0? 359:val);
	}
	public boolean isInBlock(Vector3 campos) {
		
		return obj3d[(int) campos.x][(int) campos.y][(int) ((int) campos.z)] == null;
	}
	
	
	private void move() {
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
		//music.play();
		Vector3 campos = camPosition();
		try {
			
			} catch (ArrayIndexOutOfBoundsException e) {
				
			} 
		boolean hm = false;
		float mv = 1f;
		
		
		mv = 0.25f;
		Vector3 vec = null;
		if (Gdx.input.isKeyPressed(Keys.W)) {
			
			
			try {
				
				Ray cr = new Ray(new Vector3(x,y,z), camera.direction);
				
				vec = cr.getEndPoint(new Vector3(), mv);
				campos = floor(vec);
				
				if (obj3d[(int) campos.x][(int) campos.y][(int) ((int) campos.z)] == null) {
					x = vec.x;
					y = vec.y;
					z = vec.z;
				}
				} catch (ArrayIndexOutOfBoundsException e) {
					x = vec.x;
					y = vec.y;
					z = vec.z;
				} 
			//hm = true;
		} if (Gdx.input.isKeyPressed(Keys.S)) {
			
			
			try {
				
				Ray cr = new Ray(new Vector3(x,y,z), camera.direction);
				vec = cr.getEndPoint(new Vector3(), -mv);
				campos = floor(vec);
				if (obj3d[(int) campos.x][(int) campos.y][(int) ((int) campos.z)] == null) {
					x = vec.x;
					y = vec.y;
					z = vec.z;
				}
				} catch (ArrayIndexOutOfBoundsException e) {
					x = vec.x;
					y = vec.y;
					z = vec.z;
				} 
			//hm = true;
		} if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
			
			try {
				
				Vector3 d = new Vector3(camera.direction);
				
				d.rotate(new Vector3(1,0,0), 90);
				
				Ray cr = new Ray(new Vector3(x,y,z), d );
				vec = cr.getEndPoint(new Vector3(), -mv);
				campos = floor(vec);
				if (obj3d[(int) campos.x][(int) campos.y][(int) ((int) campos.z)] == null) {
					x = vec.x;
					y = vec.y;
					z = vec.z;
				}
				} catch (ArrayIndexOutOfBoundsException e) {
					x = vec.x;
					y = vec.y;
					z = vec.z;
				} 
			hm = true;
		} if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			
			try {
				
				Vector3 d = new Vector3(camera.direction);
				
				d.rotate(new Vector3(1,0,0), 90);
				
				Ray cr = new Ray(new Vector3(x,y,z), d );
				vec = cr.getEndPoint(new Vector3(), mv);
				campos = floor(vec);
				if (obj3d[(int) campos.x][(int) campos.y][(int) ((int) campos.z)] == null) {
					x = vec.x;
					y = vec.y;
					z = vec.z;
				}
				} catch (ArrayIndexOutOfBoundsException e) {
					x = vec.x;
					y = vec.y;
					z = vec.z;
				} 
			hm = true;
		} if (Gdx.input.isKeyPressed(Keys.A)) {
			try {
				
				Vector3 d = new Vector3(camera.direction);
				
				d.rotate(new Vector3(0,1,0), 90);
				
				Ray cr = new Ray(new Vector3(x,y,z), d );
				vec = cr.getEndPoint(new Vector3(), mv);
				campos = floor(vec);
				if (obj3d[(int) campos.x][(int) campos.y][(int) ((int) campos.z)] == null) {
					x = vec.x;
					y = vec.y;
					z = vec.z;
				}
				} catch (ArrayIndexOutOfBoundsException e) {
					x = vec.x;
					y = vec.y;
					z = vec.z;
				} 
			//hm = true;
			
		} if (Gdx.input.isKeyPressed(Keys.D)) {
			
			try {
				
				Vector3 d = new Vector3(camera.direction);
				
				d.rotate(new Vector3(0,1,0), -90);
				
				Ray cr = new Ray(new Vector3(x,y,z), d );
				vec = cr.getEndPoint(new Vector3(), mv);
				campos = floor(vec);
				if (obj3d[(int) campos.x][(int) campos.y][(int) ((int) campos.z)] == null) {
					x = vec.x;
					y = vec.y;
					z = vec.z;
				}
				} catch (ArrayIndexOutOfBoundsException e) {
					x = vec.x;
					y = vec.y;
					z = vec.z;
				} 
			//hm = true;
		}
		if (!hm && gravity) {
			try {
				campos = camPosition();
				if (obj3d[(int) campos.x][(int) ((int) campos.y - 0.05)][(int) ((int) campos.z)] == null)y -= 0.05;
				} catch (ArrayIndexOutOfBoundsException e) {
					y -= 0.05;
				} 
		}
		//8,2 PITCH
		//4,6 YAW
		//*,- ROLL
		
		float lv = 1f;
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			yaw+=lv;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			yaw-=lv;
		}
		lv = 0.4f;
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			pitch+=lv;
		}
		if (Gdx.input.isKeyPressed(Keys.SLASH)) {
			roll-=lv; //NO ERROR PRONE
		}
		if (Gdx.input.isKeyPressed(Keys.PERIOD)) {
			roll+=lv; //NO ERROR PRONE
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			pitch-=lv;
		}
		try {
			if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) out: {
				Ray cr = new Ray(new Vector3(x,y,z), camera.direction);
				for (int i = 0; i < 5; i++) {
					Vector3 v = cr.getEndPoint(new Vector3(), i + 1);
					Vector3 b = floor(v);
					if ((obj3d[(int) b.x][(int) b.y][(int) b.z] != null)) {
						p.breakBlock(obj3d[(int) b.x][(int) b.y][(int) b.z].type);
						obj3d[(int) b.x][(int) b.y][(int) b.z] = null;
						m.event(Event.breakBlock);
						break out;
					}
				}
			}
			if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) out: {
				Ray cr = new Ray(new Vector3(x,y,z), camera.direction);
			
				
				for (int i = 0; i < 5; i++) {
					Vector3 v = cr.getEndPoint(new Vector3(), i + 1);
					Vector3 b = floor(v);
					if (obj3d[(int) b.x][(int) b.y][(int) b.z] != null) {
						v = cr.getEndPoint(new Vector3(), i);
						b = floor(v);
						if (p.placeBlock(selectedBlock))
						obj3d[(int) b.x][(int) b.y][(int) b.z] = new Object3d(selectedBlock);
						break out;
					}
				}
			
			}
			if (Gdx.input.isKeyJustPressed(Keys.BACKSPACE)) {
				if (BlockIterator.hasNext()) {
					selectedBlock = BlockIterator.next();
				} else {
					BlockIterator = bl.iterator();
					selectedBlock = BlockIterator.next();
				}
			}
			if (Gdx.input.isKeyJustPressed(Keys.G)) {
				if (gravity) gravity = false;
				else gravity = true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			
		}
		
		roll = roll < 0? 359:roll;
		yaw = yaw < 0? 359:yaw;
		pitch = pitch < 0? 359:pitch;
		roll = roll % 360;
		yaw = yaw % 360;
		pitch = pitch % 360;
		camera.up.set(0, 1, 0);
		camera.direction.set(0, 0, -1);
		camera.rotate(new Vector3(0,0,1), (roll - 180));
		camera.rotate(new Vector3(0,1,0), (yaw - 180));
		camera.rotate(new Vector3(1,0,0), (pitch - 180));
		camera.update();
		
	}
	public void gameLoop() {

		
		camera.update();
		
		
		boolean ib = false;
		
		move();
		
		
		if (Gdx.input.isKeyPressed(Keys.L)) {
			x = y = z = ls;
			pitch = yaw = roll = 0;
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.R)) {
			regen();
			x = y = z = ls;
			pitch = yaw = roll = 0;
		}
		if (Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
			System.out.println(p.dump());
		}
		camera.position.set(x,y,z);
		
		camera.update();
		

		modelBatch.begin(camera);
		
		for (int i = 0; i < size; i++) {
			for (int o = 0; o < size; o++) {
				for (int p = 0; p < size; p++) {
					if (!(obj3d[i][o][p] == null)) {
						ModelInstance m = obj3d[i][o][p].getInst(new Float3(i,o,p));
						
						
							
								//BoundingBox b = m.calculateBoundingBox(new BoundingBox());
								
							
							
							
							
						
						if (isVisible(camera, i, o, p)) modelBatch.add(m);
						
					}
					
				}
			}
		}
		modelBatch.end();
		b3.begin(camera);
		b3.render(modelBatch);
		b3.end();
		
		if (Gdx.input.isKeyJustPressed(Keys.E)) {
			inv = !inv;
			
		}
		
		new Ray(new Vector3(x,y,z), camera.direction);
		
		
		
		batch.begin();
		bb.setColor(1, 1, 1, 1);
		Vector3 campos = camPosition();
		String str = "";
		ib = false;
		try {
			ib = obj3d[(int) campos.x][(int) campos.y][(int) campos.z] != null;
			str = "X: " + x + " Y:" + y + " Z: " + z + 
					" FPS: " + Gdx.graphics.getFramesPerSecond() + 
					" Is in block: " + ib + 
					" roll = " + roll + " yaw = " + yaw + " pitch = " + pitch
					+ " Selected block: " + selectedBlock;
		} catch (ArrayIndexOutOfBoundsException e) {
			str = "X: " + x + " Y:" + y + " Z: " + z + " FPS: " + 
					Gdx.graphics.getFramesPerSecond() + 
					" Is in block: Out of bounds" + 
					" roll = " + roll + " yaw = " + yaw + " pitch = " + pitch
					+ " Selected block: " + selectedBlock;
		} 
		
		bb.draw(batch, str, 0, 450 , 100 , 0, true);
		//batch.draw(title, 100,100);
		if (inv) {
			int x,y,x2,y2;
			x = y = 20;
			x2=y2=20;
			// add 60 for new blocks.
			batch.draw(inven, x, y, 400, 300);
			// 1, 0-3
			batch.draw(BlockType.stone.tex, x + 20, y + 20, 40, 40); // stone 0,0
			bb.draw(batch, p.i.query(BlockType.stone)+"", x2 + 20, y2 + 20);
			batch.draw(BlockType.coal.tex, x + 80, y + 20, 40, 40); //coal 0,1
			bb.draw(batch, p.i.query(BlockType.coal)+"", x2 + 80, y2 + 20);
			batch.draw(BlockType.emerald.tex, x + 140, y + 20, 40, 40); //emerald 0,2
			bb.draw(batch, p.i.query(BlockType.emerald)+"", x2 + 140, y2 + 20);
			batch.draw(BlockType.diamond.tex, x + 200, y + 20, 40, 40); //diamond 0,3
			bb.draw(batch, p.i.query(BlockType.diamond)+"", x2 + 200, y2 + 20);
			// 2, 0-2
			batch.draw(BlockType.dirt.tex, x + 20, y + 80, 40, 40); // dirt 1,0
			bb.draw(batch, p.i.query(BlockType.dirt)+"", x2 + 20, y2 + 80);
			batch.draw(BlockType.ruby.tex, x + 80, y + 80, 40, 40); //ruby 1,1
			bb.draw(batch, p.i.query(BlockType.ruby)+"", x2 + 80, y2 + 80);
			batch.draw(BlockType.grass.tex, x + 140, y + 80, 40, 40); //grass 1,2
			bb.draw(batch, p.i.query(BlockType.grass)+"", x2 + 140, y2 + 80);
//			batch.draw(BlockType.diamond.tex, x + 200, y + 80, 40, 40); //diamond 1,3
//			bb.draw(batch, p.i.query(BlockType.diamond)+"", x2 + 200, y2 + 80);





		}
		batch.draw(selectedBlock.tex, 450, 440, 40, 40);
		batch.end();
		
		
		
		
	}
	@Override
	public void render () {
		playing = true;
		Gdx.gl.glClearColor(0.6f,0.8f,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT|GL20.GL_DEPTH_BUFFER_BIT);
		if (playing) {
			gameLoop();
			//y - 100 - 250
			//x - 100 - 300
//			if (Gdx.input.isTouched()) {
//				Vector3 touchPos = new Vector3();
//			    touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
//			    if (inRange(100, 250, touchPos.y) && inRange(100, 300, touchPos.x)) {
//			    	playing = false;
//			    }
//			}
		} else {
			batch.begin();
			batch.draw(title, 200,200);
			batch.end();
			//y - 200 - 350
			//x - 200 - 400
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				Vector3 touchPos = new Vector3();
			    touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			    if (inRange(200, 350, touchPos.y) && inRange(200, 400, touchPos.x)) {
			    	playing = true;
			    }
			}
		}
		
	}
	@Override
	public void dispose () {
		
		FileOutputStream o = null;
		try {
			o = new FileOutputStream("D:\\save.save");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ObjectOutputStream out = new ObjectOutputStream(o);
			Serial s = new Serial(p.i, obj3d, new Float3(x,y,z));
			out.writeObject(s);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bb.dispose();
		b3.dispose();
		modelBatch.dispose();
		t.dispose();
		Object3d.dispose();
		
		flag = false;
		rflag=false;
		disp = true;
		
		m.dispose();
		System.out.println("EXIT DISPOSE");
	}
	@Override
	public void resume() {
		flag = false;
	}
	
	
	
		

	
	public void run() {
		do {
			
			m.event(Event.background);
		} while (rflag);
		System.out.println("EXIT");
	}
	
}
