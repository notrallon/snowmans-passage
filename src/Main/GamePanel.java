package Main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import GameState.GameStateManager;

@SuppressWarnings( "serial" )
public class GamePanel extends JPanel implements Runnable, KeyListener {

	// dimensions
	public static final int 	WIDTH 			= 320;
	public static final int 	HEIGHT 			= 240;
	public static final int 	SCALE 			= 2;

	// fps
	long 						fpsStart;
	long 						fpsElapsed;
	long 						timeFrames 		= 0;
	int 						frames 			= 0;

	// game thread
	private Thread 				thread;
	private boolean 			running;
	private int 				FPS 			= 60;
	private long 				targetTime 		= 1000 / FPS;

	// image
	private BufferedImage 		image;
	private Graphics2D 			g;

	// game state manager
	private GameStateManager 	gsm;

	public GamePanel() {
		super();
		setPreferredSize( new Dimension( WIDTH * SCALE, HEIGHT * SCALE ) );
		setFocusable( true );
		requestFocus();
	}

	public void addNotify() {
		super.addNotify();
		if ( thread == null ) {
			thread = new Thread( this );
			addKeyListener( this );
			thread.start();
		}
	}

	private void init() {
		image 		= new BufferedImage( WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB );
		g 			= ( Graphics2D ) image.getGraphics();
		running 	= true;
		gsm 		= new GameStateManager();
	}

	@Override
	public void keyPressed( KeyEvent key ) {
		gsm.KeyPressed( key.getKeyCode() );

	}

	@Override
	public void keyReleased( KeyEvent key ) {
		gsm.KeyReleased( key.getKeyCode() );

	}

	@Override
	public void keyTyped( KeyEvent key ) { }

	@Override
	public void run() {
		init();
		long start;
		long elapsed;
		long wait;

		// game loop
		while ( running ) {
			start 			= System.nanoTime();
			fpsStart 		= System.currentTimeMillis();

			Update();
			Draw();
			DrawToScreen();

			elapsed 		= System.nanoTime() - start;
			wait 			= targetTime - elapsed / 1000000;

			if ( wait < 0 )
				wait = 5;

			try {
				Thread.sleep( wait );
			} catch ( Exception e ) {
				e.printStackTrace();
			}

			fpsElapsed 		= System.currentTimeMillis();
			timeFrames 		= timeFrames + fpsElapsed - fpsStart;
			frames++;
			if( timeFrames > 1000 ){
				timeFrames 	= 0;
				frames 		= 0;
			}
		}
	}

	private void Update() {
		gsm.Update();

	}

	private void Draw() {
		gsm.Draw(g);
	}

	private void DrawToScreen() {
		Graphics g2 	= getGraphics();
		g2.drawImage( image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null );
		g2.dispose();
	}
}
