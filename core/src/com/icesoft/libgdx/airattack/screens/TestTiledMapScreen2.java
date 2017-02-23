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
import com.icesoft.libgdx.TMHUD;

public class TestTiledMapScreen2 implements Screen{
	private TiledMap map;
	private OrthogonalTiledMapRenderer mapRenderer;
	private OrthographicCamera mapCamera,hudCamera;

	private GestureDetector gesture;
	private static final float viewportWidth = 20, viewportHeight = 20;
	
	private TMHUD hud;
	@Override
	public void show() {
		gesture =new GestureDetector(new MyGestureListener());
		InputMultiplexer multiplexer = new InputMultiplexer(); 
		multiplexer.addProcessor(gesture);
		Gdx.input.setInputProcessor(multiplexer);
		
		// load the map, set the unit scale to 1/16 (1 unit == 16 pixels)
		map = new TmxMapLoader().load("maps/plane.tmx");
		mapCamera = new OrthographicCamera();
		mapCamera.setToOrtho(false, viewportWidth, viewportHeight);		
		mapRenderer = new OrthogonalTiledMapRenderer(map, 1/16f);
		hud = new TMHUD(map);
	}
	@Override
	public void render(float delta) {
		// clear the screen
		Gdx.gl.glClearColor(0.7f, 0.7f, 1.0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		controller();		
		// get the delta time
		float deltaTime = Gdx.graphics.getDeltaTime();
		mapRenderer.setView(mapCamera);
		mapRenderer.render();		
		hud.render(deltaTime,mapRenderer.getViewBounds());
	}
	
	private void controller() {
		if(Gdx.input.isKeyPressed(Keys.I)){
			zoom(1f,100f);
		}
		if(Gdx.input.isKeyPressed(Keys.O)){
			zoom(-1f,100f);
		}
		if(Gdx.input.isKeyJustPressed(Keys.LEFT)){
			hud.moveLeft();
			hud.cameraInfo();
		}
		if(Gdx.input.isKeyJustPressed(Keys.RIGHT)){
			hud.moveRight();
			hud.cameraInfo();
		}
		if(Gdx.input.isKeyJustPressed(Keys.UP)){
			hud.moveUp();
			hud.cameraInfo();
		}
		if(Gdx.input.isKeyJustPressed(Keys.DOWN)){
			hud.moveDown();
			hud.cameraInfo();
		}
		if(Gdx.input.isKeyJustPressed(Keys.M)){
			hud.changeDisplay();
			hud.cameraInfo();
		}
		if(Gdx.input.isKeyJustPressed(Keys.Q)){
			hud.zoomIn();
			hud.cameraInfo();
		}
		if(Gdx.input.isKeyJustPressed(Keys.W)){
			hud.zoomOut();
			hud.cameraInfo();
		}
		if(Gdx.input.isKeyJustPressed(Keys.S)){
			hud.saveConfigToPreferences();
		}
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
        mapCamera.zoom += ZOOM_SPEED * ratio;
       if (mapCamera.zoom < 0.3)
        {
    	   mapCamera.zoom = (float) 0.3;
        }
        else if (mapCamera.zoom > 2)
        {
        	mapCamera.zoom = 2;
        }
       	mapCamera.update();
	}
	public void move(float deltaX, float deltaY){
        float MinX = 0;
        float MinY = 0;
        float MaxX = mapCamera.viewportWidth * mapCamera.zoom;
        float MaxY = mapCamera.viewportHeight * mapCamera.zoom;
        mapCamera.position.x = MathUtils.clamp(mapCamera.position.x, MinX, MaxX);
        mapCamera.position.y = MathUtils.clamp(mapCamera.position.y, MinY, MaxY);
        mapCamera.translate(-deltaX * PAN_RATE, deltaY * PAN_RATE,   0);
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