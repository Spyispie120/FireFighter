package com.picklegames.entities;

import static com.picklegames.handlers.B2DVars.PPM;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.picklegames.game.FireFighterGame;

public class Lamp extends Entity {

	private TextureRegion[] textureR;
	private Texture textureStand;
	private Vector2 velocity;

	public Lamp(){
		super();
		init();
	}
	
	public Lamp(Body body) {
		super(body);
		init();
	}


	public void init() {
		FireFighterGame.res.loadTexture("Character/WalkingSprites.png", "Lamp");
		FireFighterGame.res.loadTexture("Character/Stand_1.png", "Lamp_Stand");
		Texture texture = FireFighterGame.res.getTexture("Lamp");
		textureR = TextureRegion.split(texture, 80, 150)[0]; // 80, 150
		textureStand = FireFighterGame.res.getTexture("Lamp_Stand");
		animation.setFrames(textureR, 1 / 8f);

		width = textureR[0].getRegionWidth() * 1.5f;
		height = textureR[0].getRegionHeight() * 1.5f;

		velocity = new Vector2(0,0);
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		System.out.println("Body linear: "+ body.getLinearVelocity().toString());
//		body.setLinearVelocity(velocity);
	}

	public void render(SpriteBatch spriteBatch) {
		spriteBatch.begin();
		if (velocity.x == 0 && velocity.y == 0) {
			spriteBatch.draw(textureStand, body.getPosition().x * PPM - width / 2,
					body.getPosition().y * PPM - height / 2, width, height);
			// spriteBatch.draw(textureStand, body.getPosition().x * PPM - width
			// / 2, body.getPosition().y * PPM - height / 2, width / 2, height /
			// 2, 80, 150, 1, 1, 0);
		} else {
			if(velocity.x > 0)
			spriteBatch.draw(animation.getFrame(), body.getPosition().x * PPM - width / 2,
					body.getPosition().y * PPM - height / 2, width / 2, height / 2, width, height, -1, 1, 0);
			else if(velocity.x < 0)
				spriteBatch.draw(animation.getFrame(), body.getPosition().x * PPM - width / 2,
						body.getPosition().y * PPM - height / 2, width / 2, height / 2, width, height, 1, 1, 0);
			else {
				spriteBatch.draw(animation.getFrame(), body.getPosition().x * PPM - width / 2,
						body.getPosition().y * PPM - height / 2, width / 2, height / 2, width, height, 1, 1, 0);
			}
		}
		spriteBatch.end();
	}

	public void dispose(){
		body.getFixtureList().clear();
		textureStand.dispose();
	}
	
	
	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public void setVelocity(float x, float y) {
		this.velocity = new Vector2(x, y);
	}

	public void setVelocityX(float x){
		this.velocity.x = x;
	}
	
	public void setVelocityY(float y){
		this.velocity.y = y;
	}
}
