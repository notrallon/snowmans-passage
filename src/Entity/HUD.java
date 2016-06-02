package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class HUD
{
	private Player 			player;
	private BufferedImage 	image;
	private Font 			font;

	public HUD ( Player p ){
		player = p;

		try {
			image = ImageIO.read( getClass().getResourceAsStream( "/HUD/hud.gif" ) );
			font = new Font( "Arial", Font.PLAIN, 11 );
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}

	public void Draw( Graphics2D g ) {
		g.drawImage( image, 0, 10, null );
		g.setFont( font );
		g.setColor( Color.GRAY );
		g.drawString( player.GetHealth() + "/" + player.GetMaxHealth(),
					  30,
					  21 );
		g.drawString( player.GetSnow() / 100 + "/" + player.GetMaxSnow() / 100,
					  30,
					  47 );
	}
}
