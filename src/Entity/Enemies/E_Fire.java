package Entity.Enemies;

import Entity.*;
import TileMap.TileMap;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;

public class E_Fire extends Enemy
{
	private BufferedImage[] sprites;

	public E_Fire(TileMap tm ) {
		super( tm );

		moveSpeed 		= 0.3;
		maxSpeed 		= 0.3;
		fallSpeed 		= 0.2;
		maxFallSpeed 	= 10.0;

		width 			= 30;
		height 			= 30;
		cwidth 			= 20;
		cheight 		= 20;

		health 			= maxHealth = 2;
		damage 			= 1;

		// load Sprites
		try {
			BufferedImage spritesheet = ImageIO.read( getClass()
					.getResourceAsStream( "/Sprites/Enemies/mobs.gif" ) );

			sprites = new BufferedImage[ 3 ];
			for ( int i = 0; i < sprites.length; i++ ) {
				sprites[ i ] = spritesheet.getSubimage( i * width, 0,
														width, height);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.SetFrames(sprites);
		animation.SetDelay(300);

		right 		= false;
		left 		= true;
		facingRight = false;
	}

	private void GetNextPosition() {

		// movement
		if ( left ) {
			dx -= moveSpeed;
			if ( dx < -maxSpeed ) {
				dx = -maxSpeed;
			}
		} else if ( right ) {
			dx += moveSpeed;
			if ( dx > maxSpeed ) {
				dx = maxSpeed;
			}
		}

		// falling
		if( falling ) {
			dy += fallSpeed;
		}
	}

	public void Update() {
		// Update position
		GetNextPosition();
		CheckTileMapCollision();
		SetPosition( xtemp, ytemp );

		// check flinching
		if( flinching ) {
			long elapsed = ( System.nanoTime() - flinchTimer ) / 1000000;
			if( elapsed > 400 ) {
				flinching = false;
			}
		}

		// if it hits a wall
		if( right && dx == 0 ){
			right = false;
			left = true;
			facingRight = false;
		}
		else if( left && dx == 0 ) {
			right = true;
			left = false;
			facingRight = true;
		}

		// Update animation
		animation.Update();
	}

	public void Draw(Graphics2D g ) {

//		if (NotOnScreen())
//			return;
		SetMapPosition();
		super.Draw( g );
	}
}
