package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Explosion
{
	private int 			x;
	private int 			y;
	private int 			xmap;
	private int 			ymap;
	private int 			width;
	private int 			height;
	private Animation 		animation;
	private BufferedImage[] sprites;
	private boolean 		remove;

	public Explosion( int x, int y ) {

		this.x 	= x;
		this.y 	= y;
		width 	= 30;
		height 	= 30;

		try {
			BufferedImage spritesheet = ImageIO.read( getClass()
					.getResourceAsStream( "/Sprites/Enemies/explosion.gif" ) );

			sprites = new BufferedImage[ 6 ];
			for ( int i = 0; i < sprites.length; i++ ) {
				sprites[ i ] = spritesheet.getSubimage( i * width, 0, width,
														height );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.SetFrames( sprites );
		animation.SetDelay( 70 );

	}

	public void Update() {
		animation.Update();
		if ( animation.HasPlayedOnce() ) {
			remove = true;
		}
	}

	public boolean ShouldRemove() {
		return remove;
	}

	public void SetMapPosition( int x, int y ) {
		xmap = x;
		ymap = y;
	}

	public void Draw( Graphics2D g ) {
		g.drawImage( animation.GetImage(), x + xmap - width / 2,
					y + ymap - height / 2, null );
	}
}
