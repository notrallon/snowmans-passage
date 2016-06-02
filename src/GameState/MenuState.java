package GameState;

import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {

	private Background 	bg;
	private int 		currentChoice 	= 0;
	private String[] 	options 		= { "Start",
											"Help",
											"Quit" };

	private Color 		titleColor;
	private Font 		titleFont;
	private Font 		font;

	public MenuState( GameStateManager gsm ) {
		this.gsm = gsm;

		try {
			bg 			= new Background( "/Backgrounds/snowbg.gif", 1 );
			bg.SetVector( -0.1, 0 );
			titleColor 	= new Color(0, 0, 150);
			titleFont 	= new Font("Century Gothic", Font.PLAIN, 28);
			font 		= new Font("Arial", Font.PLAIN, 12);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public void Init() {

	}

	public void Update() {
		bg.Update();
	}

	public void Draw(Graphics2D g ) {
		// Draw background
		bg.Draw( g );

		// Draw title
		g.setColor( titleColor );
		g.setFont( titleFont );
		g.drawString( "Snowmans Passage", 35, 70 );

		// Draw menu options
		g.setFont( font );
		for ( int i = 0; i < options.length; i++ ) {
			if ( i == currentChoice ) {
				g.setColor( Color.GRAY );
			} else {
				g.setColor( Color.BLACK );
			}
			g.drawString( options[ i ], 145, 140 + i * 15 );
		}
	}

	private void Select() {
		if ( currentChoice == 0 ) {
			// start
			gsm.SetState( GameStateManager.LEVEL1STATE );
		}
		if ( currentChoice == 1 ) {
			// help
		}
		if ( currentChoice == 2 ) {
			System.exit( 0 );
		}
	}

	public void KeyPressed(int k ) {
		if ( k == KeyEvent.VK_ENTER ) {
			Select();
		}
		if ( k == KeyEvent.VK_UP ) {
			currentChoice--;
			if ( currentChoice == -1 ) {
				currentChoice = options.length - 1;
			}
		}
		if ( k == KeyEvent.VK_DOWN ) {
			currentChoice++;
			if ( currentChoice == options.length ) {
				currentChoice = 0;
			}
		}
	}
	public void KeyReleased(int k ) {
	}
}
