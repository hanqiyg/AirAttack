package com.icesoft.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class TMHUD {
	private static final String PREFERENCE_NAME = "HUDConfig";
	public static final String NAME_FLOAT_MAPSCALE = "HUDConfig.mapScale";
	public static final String NAME_BOOLEAN_FULLMAP = "HUDConfig.fullMap";
	public static final String NAME_BOOLEAN_DISPLAY = "HUDConfig.display";
	public static final String NAME_INTEGER_DISPLAYWIDTH = "HUDConfig.displayWidth";
	public static final String NAME_INTEGER_DISPLAYHEIGHT = "HUDConfig.displayHeight";
	public static final String NAME_FLOAT_MOVESTEP = "HUDConfig.moveStep";
	public static final String NAME_FLOAT_ZOOMSTEP = "HUDConfig.zoomStep";
	
	public static final String NAME_FLOAT_ZOOM = "Camera.Zoom";	
	public static final String NAME_FLOAT_POSITIONX = "Camera.Position.x";
	public static final String NAME_FLOAT_POSITIONY = "Camera.Position.y";
	
	public static final float VALUE_FLOAT_MAPSCALE = 1/16f;
	public static final boolean VALUE_BOOLEAN_FULLMAP = true;
	public static final boolean VALUE_BOOLEAN_DISPLAY = false;
	public static final int VALUE_INTEGER_DISPLAYWIDTH = 1;
	public static final int VALUE_INTEGER_DISPLAYHEIGHT = 1;
	public static final float VALUE_FLOAT_MOVESTEP = 1f;
	public static final float VALUE_FLOAT_ZOOMSTEP = 1f;
	
	public static final float VALUE_FLOAT_ZOOM = 1f;
	public static final float VALUE_FLOAT_POSITIONX = 0f;
	public static final float VALUE_FLOAT_POSITIONY = 0f;
	
	private TiledMap map;
	private HUDConfig config;
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer renderer;
	
	private ShapeRenderer shapeRenderer;
	
	public TMHUD(TiledMap map){
		this.map = map;
		this.loadConfigFromPreferences();
	}
	
	public TMHUD(TiledMap map,HUDConfig config){
		this.map = map;
		this.config = config;
		init();
	}
	private void init() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, config.displayWidth, config.displayHeight);	
		renderer = new OrthogonalTiledMapRenderer(map, config.mapScale);
		shapeRenderer = new ShapeRenderer();
	}
	public void render(float delta, Rectangle r){
		if(config.display){
			renderer.setView(camera);
			camera.update();
			renderer.render();	
			if(r != null){
				drawScope(r);
			}
		}		
	}
	public void drawScope(Rectangle r){		
	    shapeRenderer.setProjectionMatrix(camera.combined);
	    
	    shapeRenderer.setColor(Color.BLACK);	    
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.rect(0, 0, camera.viewportWidth,camera.viewportHeight);		
		shapeRenderer.end();
		
	    shapeRenderer.setColor(Color.RED);	    
		shapeRenderer.begin(ShapeType.Line);	
		shapeRenderer.rect(r.x, r.y, r.width, r.height);
		shapeRenderer.end();
		

	}
	public void zoomIn(){
		camera.zoom += config.zoomStep;
		camera.update();
	}
	public void zoomOut(){
		camera.zoom -= config.zoomStep;
		camera.update();
	}
	public void moveLeft(){
		camera.position.x += config.moveStep;
		camera.update();
	}
	public void moveRight(){
		camera.position.x -= config.moveStep;
		camera.update();
	}
	public void moveUp(){
		camera.position.y -= config.moveStep;
		camera.update();
	}
	public void moveDown(){
		camera.position.y += config.moveStep;
		camera.update();
	}
	public void cameraReset(){
		camera.zoom = 1;
		camera.position.set(0,0,0);
	}
	
	public void cameraInfo(){
		System.out.println("isDisplay:" + config.display + 
				"Position:" + camera.position.toString() + 
				" Zoom:" + camera.zoom + 
				" Projection:" + camera.projection.toString());	
	}
	public void changeDisplay(){
		config.display = ! config.display;
	}
	public TiledMap getMap() {
		return map;
	}
	public void setMap(TiledMap map) {
		this.map = map;
	}
	public HUDConfig getConfig() {
		return config;
	}
	public void setConfig(HUDConfig config) {
		this.config = config;
	}
	public void loadDefaultConfig(){
		config = new HUDConfig(VALUE_FLOAT_MAPSCALE, VALUE_BOOLEAN_FULLMAP, VALUE_BOOLEAN_DISPLAY, 
				VALUE_INTEGER_DISPLAYWIDTH, VALUE_INTEGER_DISPLAYHEIGHT, VALUE_FLOAT_MOVESTEP, VALUE_FLOAT_ZOOMSTEP);
		init();
	}
	public void loadConfigFromPreferences(){
		
		Preferences pref = Gdx.app.getPreferences(PREFERENCE_NAME);		
		float mapScale = pref.getFloat(NAME_FLOAT_MAPSCALE,VALUE_FLOAT_MAPSCALE);
		boolean fullMap = pref.getBoolean(NAME_BOOLEAN_FULLMAP,VALUE_BOOLEAN_FULLMAP);
		boolean display = pref.getBoolean(NAME_BOOLEAN_DISPLAY,VALUE_BOOLEAN_DISPLAY);
		int displayWidth = pref.getInteger(NAME_INTEGER_DISPLAYWIDTH,VALUE_INTEGER_DISPLAYWIDTH);
		int displayHeight = pref.getInteger(NAME_INTEGER_DISPLAYHEIGHT,VALUE_INTEGER_DISPLAYHEIGHT);
		float moveStep = pref.getFloat(NAME_FLOAT_MOVESTEP,VALUE_FLOAT_MOVESTEP);
		float zoomStep = pref.getFloat(NAME_FLOAT_ZOOMSTEP,VALUE_FLOAT_ZOOMSTEP);
		config = new HUDConfig(mapScale, fullMap, display, displayWidth, displayHeight, moveStep, zoomStep);		
		init();
		camera.zoom = pref.getFloat(NAME_FLOAT_ZOOM, VALUE_FLOAT_ZOOM);
		camera.position.x = pref.getFloat(NAME_FLOAT_POSITIONX, VALUE_FLOAT_POSITIONX);
		camera.position.y = pref.getFloat(NAME_FLOAT_POSITIONY, VALUE_FLOAT_POSITIONY);
	}
	
	public void saveConfigToPreferences(){
		Preferences pref = Gdx.app.getPreferences(PREFERENCE_NAME);
		
		pref.putFloat(NAME_FLOAT_ZOOM, camera.zoom);
		pref.putFloat(NAME_FLOAT_POSITIONX, camera.position.x);
		pref.putFloat(NAME_FLOAT_POSITIONY, camera.position.y);
		
		pref.putFloat(NAME_FLOAT_MAPSCALE, config.mapScale);
		pref.putBoolean(NAME_BOOLEAN_FULLMAP, config.fullMap);
		pref.putBoolean(NAME_BOOLEAN_DISPLAY, config.display);
		pref.putInteger(NAME_INTEGER_DISPLAYWIDTH, config.displayWidth);
		pref.putInteger(NAME_INTEGER_DISPLAYHEIGHT, config.displayHeight);
		pref.putFloat(NAME_FLOAT_MOVESTEP, config.moveStep);
		pref.putFloat(NAME_FLOAT_ZOOMSTEP, config.zoomStep);
		pref.flush();
	}
	
	public class HUDConfig{
		private float mapScale;
		private boolean fullMap;
		private boolean display;
		private int displayWidth;
		private int displayHeight;
		
		private float moveStep;
		private float zoomStep;
		
		public HUDConfig(float mapScale,boolean fullMap,boolean display, int displayWidth, int displayHeight, float moveStep,float zoomStep){
			this.mapScale = mapScale;
			this.fullMap = fullMap;
			this.display = display;
			this.displayWidth = displayWidth;
			this.displayHeight = displayHeight;
			this.moveStep = moveStep;
			this.zoomStep = zoomStep;

			if(fullMap){
				this.displayWidth  = map.getProperties().get("width",  Integer.class);
				this.displayHeight = map.getProperties().get("height", Integer.class);
				System.out.println(this.displayWidth + "," + this.displayHeight);
			}
		}
	}
}
