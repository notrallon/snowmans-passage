package Entity;

import TileMap.TileMap;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class SnowBall extends Player
{
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;

	public SnowBall( TileMap tm, boolean right ) {
		super( tm );

		facingRight 	= right;
		moveSpeed 		= 3.8;
		width 			= 30;
		height 			= 30;
		cwidth 			= 14;
		cwidth 			= 14;

		if (right) {
			dx = moveSpeed;
		} else {
			dx = -moveSpeed;
		}

		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read( getClass()
					.getResourceAsStream( "/Sprites/Player/snowball.gif" ) );
			sprites = new BufferedImage[ 4 ];
			for ( int i = 0; i < sprites.length; i++ ) {
				sprites[ i ] = spritesheet.getSubimage( i * width, 0,
														width, height );
			}

			hitSprites = new BufferedImage[ 3 ];
			for ( int i = 0; i < hitSprites.length; i++ ) {
				hitSprites[i] = spritesheet.getSubimage( i * width, height,
														 width, height );
			}
			animation = new Animation();
			animation.SetFrames( sprites );
			animation.SetDelay( 70 );
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public void SetHit() {
		if ( hit ) {
			return;
		}
		hit = true;
		animation.SetFrames( hitSprites );
		animation.SetDelay( 70 );
		dx = 0;
	}

	public boolean ShouldRemove() {
		return remove;
	}

	public void Update() {
		CheckTileMapCollision();
		SetPosition( xtemp, ytemp );

		if ( dx == 0 && !hit ) {
			SetHit();
		}

		animation.Update();
		if ( hit && animation.HasPlayedOnce() ) {
			remove = true;
		}

	}

	public void Draw(Graphics2D g ) {
		SetMapPosition();
		super.Draw(g);
	}
}
