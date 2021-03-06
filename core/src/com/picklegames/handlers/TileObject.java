package com.picklegames.handlers;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.picklegames.handlers.Box2D.B2DVars;

public class TileObject {
	public Body body;
	
	public void parseTiledObjectLayer(World world, MapObjects objects, String userData) {

		for (MapObject object : objects) {
			Shape shape;
			if (object instanceof PolylineMapObject) {
				shape = createPolyline((PolylineMapObject) object);
			} else {
				continue;
			}

			BodyDef bdef = new BodyDef();
			bdef.type = BodyDef.BodyType.StaticBody;
			body = world.createBody(bdef);
			body.setUserData(userData);
			FixtureDef fdef = new FixtureDef();
			fdef.shape = shape;
			fdef.filter.categoryBits = B2DVars.BIT_GROUND;
			fdef.filter.maskBits =  B2DVars.BIT_PLAYER;
			body.createFixture(fdef).setUserData(userData);
			shape.dispose();

		}
	}

	private ChainShape createPolyline(PolylineMapObject polyline) {

		float[] vertices = polyline.getPolyline().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];

		for (int i = 0; i < worldVertices.length; i++) {
			worldVertices[i] = new Vector2(vertices[i * 2] / B2DVars.PPM, vertices[i * 2 + 1] / B2DVars.PPM);
		}
		ChainShape cs = new ChainShape();
		cs.createChain(worldVertices);
		return cs;
	}

}
