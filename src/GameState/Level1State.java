package GameState;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Entity.Enemies.E_Fire;
import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;

public class Level1State extends GameState {
	private TileMap 				tileMap;
	private Background 				bg;
	private Player 					player;
	private ArrayList< Enemy > 		enemies;
	private ArrayList< Explosion > 	explosions;
	private int 					bossRoom 	= 3050;
	private boolean 				hasPast 	= false;
	private HUD 					hud;

	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		Init();
	}

	public void Init() {
		tileMap = new TileMap( 30 );
		tileMap.LoadTiles( "/Tilesets/snowfull.gif" );
		tileMap.LoadMap( "/Maps/levelb2.txt" );
		bg 		= new Background( "/Backgrounds/BG.gif", 0.1 );
		player 	= new Player( tileMap );
		player.SetPosition( 100, 320 );

		AddEnemies();

		explosions = new ArrayList< Explosion >();

		hud = new HUD( player );
	}

	private void AddEnemies() {
		enemies = new ArrayList< Enemy >();

		E_Fire s;

		Point[] points = new Point[] {
				new Point( 300, 320 ),
				new Point( 340, 320 ),
				new Point( 380, 320 ),
				new Point( 800, 200 ),
				new Point( 840, 200 ),
				new Point( 880, 200 ),
				new Point( 1525, 200 ),
				new Point( 1560, 200 ),
				new Point( 1600, 200 ),
				new Point( 2220, 200 ),
				new Point( 2260, 200 ),
				new Point( 2300, 200 ) };

		for ( int i = 0; i < points.length; i++ ) {
			s = new E_Fire( tileMap );
			s.SetPosition( points[ i ].x, points[ i ].y );
			enemies.add( s );
		}
	}

	private void GetRoom() {
		if ( player.GetX() >= bossRoom ) {
			hasPast = false;
		}
	}

	public void Update() {
		// Update player
		player.Update();

		if ( !hasPast ) {
			tileMap.SetPosition( GamePanel.WIDTH / 2 - player.GetX(),
								 GamePanel.HEIGHT / 2 - player.GetY() );
			GetRoom();
		} else {
			tileMap.SetPosition( GamePanel.WIDTH / 2 - bossRoom,
								 GamePanel.HEIGHT / 2 - player.GetY() );
		}

		// scroll background
		bg.SetPosition( tileMap.GetX(), tileMap.GetY() );

		// attack enemies
		player.CheckAttack( enemies );

		// Update all enemies
		for ( int i = 0; i < enemies.size(); i++ ) {
			Enemy e = enemies.get( i );
			e.Update();
			if ( e.IsDead() ) {
				enemies.remove( i );
				i--;
				explosions.add( new Explosion( e.GetX(), e.GetY() ) );
			}
		}

		// Update explosions
		for ( int i = 0; i < explosions.size(); i++ ) {
			explosions.get( i ).Update();
			if ( explosions.get( i ).ShouldRemove() ) {
				explosions.remove( i );
				i--;
			}
		}

		if ( player.dead || player.GetY() > GamePanel.HEIGHT * 2 ) {
			gsm.SetState( GameStateManager.MENUSTATE );
		}

	}

	public void Draw(Graphics2D g ) {

		// Draw background
		bg.Draw( g );

		// Draw tilemap
		tileMap.Draw( g );

		// Draw player
		player.Draw( g );

		// Draw enemies
		for ( int i = 0; i < enemies.size(); i++ ) {
			enemies.get( i ).Draw( g );
		}
		// Draw explosions
		for ( int i = 0; i < explosions.size(); i++ ) {
			explosions.get( i ).SetMapPosition( ( int ) tileMap.GetX(),
					( int ) tileMap.GetY() );
			explosions.get( i ).Draw( g );
		}

		// Draw HUD
		hud.Draw( g );
	}

	public void KeyPressed(int k ) {

		if ( k == KeyEvent.VK_LEFT ) {
			player.SetLeft(true);
		}
		if ( k == KeyEvent.VK_RIGHT ) {
			player.SetRight( true );
		}
		if ( k == KeyEvent.VK_UP ) {
			player.SetUp( true );
		}
		if ( k == KeyEvent.VK_DOWN ) {
			player.SetDown( true );
		}
		if ( k == KeyEvent.VK_W ) {
			player.SetJumping( true );
		}
		if ( k == KeyEvent.VK_E ) {
			player.SetGliding( true );
		}
		if ( k == KeyEvent.VK_R ) {
			player.SetScratching();
		}
		if ( k == KeyEvent.VK_F ) {
			player.SetFiring();
		}
	}

	public void KeyReleased(int k ) {
		if (k == KeyEvent.VK_LEFT) {
			player.SetLeft(false);
		}
		if ( k == KeyEvent.VK_RIGHT ) {
			player.SetRight( false );
		}
		if ( k == KeyEvent.VK_UP ) {
			player.SetUp( false );
		}
		if ( k == KeyEvent.VK_DOWN ) {
			player.SetDown( false );
		}
		if ( k == KeyEvent.VK_W ) {
			player.SetJumping( false );
		}
		if ( k == KeyEvent.VK_E ) {
			player.SetGliding( false );
		}
	}
}
