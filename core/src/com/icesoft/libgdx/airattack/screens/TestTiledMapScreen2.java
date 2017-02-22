/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.icesoft.libgdx.airattack.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TestTiledMapScreen2 implements Screen{
	private TiledMap map,map2;
	private OrthogonalTiledMapRenderer mapRenderer, hudRenderer;
	private OrthographicCamera mapCamera,hudCamera;
	private Viewport ScreenViewport;
	private Viewport HudViewport;

	private GestureDetector gesture;
	private static final float viewportWidth = 20, viewportHeight = 20;
	SpriteBatch batch;
	private int screenWidth = 0, screenHeight = 0;
	@Override
	public void show() {
		gesture =new GestureDetector(new MyGestureListener());
		InputMultiplexer multiplexer = new InputMultiplexer(); 
		multiplexer.addProcessor(gesture);
		Gdx.input.setInputProcessor(multiplexer);
		
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		
		// load the map, set the unit scale to 1/16 (1 unit == 16 pixels)
		map = new TmxMapLoader().load("maps/plane.tmx");
		mapCamera = new OrthographicCamera();
		mapCamera.setToOrtho(false, viewportWidth, viewportHeight);		
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1/16f);
		
		int width = map.getProperties().get("width",Integer.class);
		int height = map.getProperties().get("height",Integer.class);
		hudCamera  = new OrthographicCamera();
		hudCamera.setToOrtho(false, width, height);	
		hudRenderer = new OrthogonalTiledMapRenderer(map, 1/64f);
		batch = new SpriteBatch();
	}
	public void initHUD(float x,float y,float width,float height){
		
	}
	
	@Override
	public void render(float delta) {
		// clear the screen
		Gdx.gl.glClearColor(0.7f, 0.7f, 1.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(Gdx.input.isKeyPressed(Keys.I)){
			zoom(1f,100f);
		}
		if(Gdx.input.isKeyPressed(Keys.O)){
			zoom(-1f,100f);
		}
		if(Gdx.input.isKeyJustPressed(Keys.B)){
						
		}
		// get the delta time
		float deltaTime = Gdx.graphics.getDeltaTime();
		mapRenderer.setView(mapCamera);
		mapRenderer.render();
		
		
		hudCamera.zoom = 0.1f;
		hudCamera.update();
		hudRenderer.setView(hudCamera);
		hudRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	public void zoom(float originalDistance, float currentDistance){
		float ratio = originalDistance/currentDistance;
        System.out.println(ratio);
        mapCamera.zoom += ZOOM_SPEED * ratio;
        System.out.println(mapCamera.zoom);
       if (mapCamera.zoom < 0.3)
        {
    	   mapCamera.zoom = (float) 0.3;
        }
        else if (mapCamera.zoom > 2)
        {
        	mapCamera.zoom = 2;
        }
       	mapCamera.update();
        System.out.println(mapCamera.zoom);
	}
	public void move(float deltaX, float deltaY){
		System.out.println("deltaX:" + deltaX +", deltaY:" + deltaY);
        float MinX = 0;
        float MinY = 0;
        float MaxX = mapCamera.viewportWidth * mapCamera.zoom;
        float MaxY = mapCamera.viewportHeight * mapCamera.zoom;
        System.out.println("MaxX:" + MaxX +", MaxY:" + MaxY);
        mapCamera.position.x = MathUtils.clamp(mapCamera.position.x, MinX, MaxX);
        mapCamera.position.y = MathUtils.clamp(mapCamera.position.y, MinY, MaxY);
        mapCamera.translate(-deltaX * PAN_RATE, deltaY * PAN_RATE,   0);
        System.out.println("trX:" + -deltaX * PAN_RATE +", trY:" + deltaY * PAN_RATE);
        mapCamera.update();        
	}
	
	public static final float PAN_RATE = (float) 0.01;
	private static final float ZOOM_SPEED = (float) 0.9;
	public class MyGestureListener implements GestureListener{

	    @Override
	    public boolean touchDown(float x, float y, int pointer, int button) {
	        return false;
	    }

	    @Override
	    public boolean tap(float x, float y, int count, int button) {
	        return false;
	    }

	    @Override
	    public boolean longPress(float x, float y) {
	        return false;
	    }

	    @Override
	    public boolean fling(float velocityX, float velocityY, int button) {
	        return false;
	    }

	    @Override
	    public boolean pan(float x, float y, float deltaX, float deltaY) {
	    	move(deltaX, deltaY);
	        return false;
	    }

	    @Override
	    public boolean panStop(float x, float y, int pointer, int button) {
	        //Gdx.app.log("Text", "panstop");
	        return false;
	    }


	    @Override
	    public boolean zoom (float originalDistance, float currentDistance){
	    	zoom(originalDistance,currentDistance);
	    return false;
	}
	@Override
	public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer){
		return false;
	}
	@Override
	public void pinchStop() {
		// TODO Auto-generated method stub
		
	}
	}
}