package com.picklegames.levelStates;

import static com.picklegames.handlers.Box2D.B2DVars.PPM;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.picklegames.TweenAccessor.EntityTweenAccessor;
import com.picklegames.entities.Entity;
import com.picklegames.entities.Fire;
import com.picklegames.entities.Lamp;
import com.picklegames.game.FireFighterGame;
import com.picklegames.handlers.Box2D.B2DVars;
import com.picklegames.handlers.Box2D.CreateBox2D;
import com.picklegames.managers.LevelStateManager;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;

public class Level0 extends LevelState {

	private OrthogonalTiledMapRenderer tmr;
	private TiledMap tileMap;
	private Box2DDebugRenderer b2dr;
	private ArrayList<Fire> fires;


	private Lamp player;

	public Level0(LevelStateManager lsm) {
		super(lsm);

	}

	@Override
	public void init() {
		Tween.registerAccessor(Entity.class, new EntityTweenAccessor());

		tileMap = new TmxMapLoader().load("map/introlevel.tmx");
		tmr = new OrthogonalTiledMapRenderer(tileMap);

		cam.viewportWidth = tmr.getMap().getProperties().get("width", Integer.class) * 32;
		cam.viewportHeight = tmr.getMap().getProperties().get("height", Integer.class) * 32;
		cam.position.x = cam.viewportWidth / 2;
		cam.position.y = cam.viewportHeight / 2;

		b2dr = new Box2DDebugRenderer();
		fires = new ArrayList<Fire>();
		createDebrisBox2D();


		player = lsm.getPlayer();
		player.scl(8f);
		player.setBody(CreateBox2D.createBox(FireFighterGame.world, 100, 0, player.getWidth() / 3.5f,
				player.getHeight() / 9, new Vector2(0, -player.getHeight() / 2.5f), BodyType.DynamicBody, "lamp",
				B2DVars.BIT_PLAYER, B2DVars.BIT_GROUND));

		Tween.to(player, EntityTweenAccessor.VEL, 2f).target(2f, 1f).ease(TweenEquations.easeNone).delay(2f)
				.start(lsm.getTweenManager());
		Tween.to(player, EntityTweenAccessor.DIMENSION, 7f).target(player.getWidth() * .5f, player.getHeight() * .5f)
				.ease(TweenEquations.easeInOutQuad).delay(3f).start(lsm.getTweenManager());
	}

	@Override
	public void handleInput() {
		if (Gdx.input.isKeyPressed(Keys.Q)) {
			cam.viewportHeight += 10;
			cam.viewportWidth += 10;
		} else if (Gdx.input.isKeyPressed(Keys.E)) {
			cam.viewportHeight -= 10;
			cam.viewportWidth -= 10;
		}

	}

	@Override
	public void update(float dt) {
		handleInput();

		player.update(dt);

		for (Fire f : fires) {
			f.update(dt);
		}

		System.out.println(player.getVelocity());
	}

	@Override
	public void render() {
		batch.setProjectionMatrix(cam.combined);

		tmr.setView(cam);
		batch.begin();
		tmr.render();
		b2dr.render(game.getWorld(), cam.combined.scl(PPM));
		batch.end();
		cam.update();

		player.render(batch);
		batch.begin();
		
		for (Fire f : fires) {
			f.render(batch);
		}


		batch.end();
		
	}

	public void createDebrisBox2D() {

		MapLayer layer = tileMap.getLayers().get("fire");
		if (layer == null)
			return;

		for (MapObject mo : layer.getObjects()) {

			// get fire position from tile map object layer
			float x = (float) mo.getProperties().get("x", Float.class);
			float y = (float) mo.getProperties().get("y", Float.class);

			// create new fire and add to fires list

			Fire f = new Fire(CreateBox2D.createCircle(game.getWorld(), x, y, 15, false, 1, BodyType.StaticBody, "fire",
					B2DVars.BIT_PLAYER, B2DVars.BIT_PLAYER));
			f.scl((float) Math.random() * 500);
			fires.add(f);

		}

	}

	@Override
	public void dispose() {
		for (Fire f : fires) {
			f.dispose();
		}

	}

}
