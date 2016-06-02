package GameState;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class BufferedImageLoader {
	private BufferedImage image;

	public BufferedImage LoadImage( String path ) {
		try {
			image = ImageIO.read( getClass().getResource( path ) );
		} catch( Exception e ) {
			e.printStackTrace();
		}
		return image;
	}
}
