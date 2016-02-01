package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class MyGdxGame extends ApplicationAdapter {
	OrthographicCamera cam;
	SpriteBatch batch;
	Texture img;
	float screenHeight;
	float screenWidth;
	World world;
	float midY;
	float midX;
	Box2DDebugRenderer b2dr;
	Body body2;
	int num = 0;
	float velX;
	float velY;
	Vector2 pos;
	Vector2 oldPos;
	float cityX;
	float cityY;
	@Override
	public void create () {
		pos = new Vector2(0,0);
		img = new Texture("city v7c.png");
		cam = new  OrthographicCamera(100,100);
		screenHeight = Gdx.graphics.getHeight();
		screenWidth = Gdx.graphics.getWidth();
		midY = screenHeight / 2;
		midX = screenWidth / 2;
		world = new World(new Vector2(0,0), true);
		world.setContactListener(new MyContactListener());
		b2dr =  new Box2DDebugRenderer();
		batch = new SpriteBatch();
		//batch.setProjectionMatrix(cam.combined);
		BodyDef bdef = new BodyDef();
		bdef.position.set((float)21.8,-5);
		bdef.type = BodyType.StaticBody;
		Body body = world.createBody(bdef);
		PolygonShape shape = new PolygonShape();
		//Vector2[] vertices = {new Vector2(0,30), new Vector2 (0,0), new Vector2 (60,0), new Vector2 (60,30)};
		shape.setAsBox((float)9.3,12);
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = 2;
		body.createFixture(fdef).setUserData("platform");

		bdef.position.set(0,0);
		bdef.type = BodyType.DynamicBody;
		body2 = world.createBody(bdef);
		PolygonShape shape2 = new PolygonShape();
		Vector2[] vertices2 = {new Vector2(0,10), new Vector2 (0,0), new Vector2 (6,0),new Vector2 (6,10)};
		shape2.set(vertices2);
		fdef.shape = shape2;
		fdef.filter.categoryBits = 4;
		fdef.filter.maskBits = 2;
		body2.createFixture(fdef).setUserData("triangle");
		cam.position.y = 20;

	}

	public void update(float dt) {
		world.step(dt, 6, 2);
		velX= 0;
		body2.setLinearVelocity(new Vector2(0,0));
		if(Gdx.input.isKeyPressed(Keys.D)){
			body2.setLinearVelocity(new Vector2(5,0));
			velX = 5;}
		if(Gdx.input.isKeyPressed(Keys.A)){
			body2.setLinearVelocity(new Vector2(-5,0));
			velX = -5;}
		if(Gdx.input.isKeyPressed(Keys.W)){
			body2.setLinearVelocity(new Vector2(velX,5));}
		if(Gdx.input.isKeyPressed(Keys.S)){
			body2.setLinearVelocity(new Vector2(velX,-5));}

		updatePos();
		cam.position.x = pos.x;
		cam.position.y = pos.y;
		
		//screenHeight = Gdx.graphics.getHeight();
		//screenWidth = Gdx.graphics.getWidth();
		//midY = screenHeight / 2;
		//midX = screenWidth / 2;
	}
	public void updatePos(){
		oldPos = new Vector2(pos);
		pos = body2.getPosition();
		if(Math.abs(pos.x-oldPos.x)>.2)
			cityX-=6.41666666666*(pos.x-oldPos.x);
		if(Math.abs(pos.y-oldPos.y)>.2)
			cityY-=4.8*(pos.y-oldPos.y);
			
		//System.out.println(midX);
		System.out.println(pos);
	}
	
	@Override
	public void render () {
		update(1);
		cam.update();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, cityX, cityY);
		batch.draw(new TextureRegion(img), midX, midY, (float)38.5, 48);
		batch.end();
		//b2dr.render(world, cam.combined);
	}
}
