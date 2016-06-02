package Entity;

import TileMap.*;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends MapObject
{
	// player
	private int 							health;
	private int 							maxHealth;
	private int snow;
	private int maxSnow;
	public boolean 							dead;
	private boolean 						flinching;
	private long 							flinchTimer;

	// fireball
	private boolean 						firing;
	private int snowCost;
	private int snowBallDamage;
	private ArrayList<SnowBall> snowBalls;

	// scratch
	private boolean 						scratching;
	private int 							scratchDamage;
	private int 							scratchRange;

	// gliding
	private boolean 						gliding;

	// animations
	private ArrayList< BufferedImage[] > 	sprites;
	private final int[] 					numFrames 	= { 2, 8, 1, 2, 4, 4, 5 };

	// animation actions
	private static final int 				IDLE 		= 0;
	private static final int 				WALKING 	= 1;
	private static final int 				JUMPING 	= 2;
	private static final int 				FALLING 	= 3;
	private static final int 				GLIDING 	= 4;
	private static final int 				FIREBALL 	= 5;
	private static final int 				SCRATCHING 	= 6;

	public Player( TileMap tm ) {

		super( tm );

		width 				= 30;
		height 				= 30;
		cwidth 				= 15;
		cheight 			= 20;
		canJump 			= true;
		moveSpeed 			= 0.3;
		maxSpeed 			= 1.6;
		stopSpeed 			= 0.4;
		fallSpeed 			= 0.15;
		maxFallSpeed 		= 4.0;
		jumpStart 			= -5.3;
		stopJumpSpeed 		= 0.3;
		facingRight 		= true;
		health 				= maxHealth
							= 5;
		snow 				= maxSnow
							= 2500;

		snowCost 			= 200;
		snowBallDamage 		= 5;
		snowBalls 			= new ArrayList<>();

		scratchDamage 		= 8;
		scratchRange 		= 40;

		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read( getClass()
					.getResourceAsStream( "/Sprites/Player/snowman.gif" ) );

			sprites = new ArrayList<>();
			for ( int i = 0; i < 7; i++ ) {
				BufferedImage[] bi = new BufferedImage[ numFrames[ i ] ];
				for ( int j = 0; j < numFrames[i]; j++ ) {
					if ( i != SCRATCHING ) {
						bi[ j ] = spritesheet.getSubimage( j * width, i * height,
														   width, height );
					} else {
						bi[ j ] = spritesheet.getSubimage( j * width * 2, i * height,
														   width * 2, height);
					}
				}
				sprites.add( bi );
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		animation = new Animation();
		currentAction = IDLE;
		animation.SetFrames(sprites.get( IDLE ));
		animation.SetDelay( 400 );

	}

	public int GetHealth() {
		return health;
	}

	public int GetMaxHealth() {
		return maxHealth;
	}

	public int GetSnow() {
		return snow;
	}

	public int GetMaxSnow() {
		return maxSnow;
	}

	public void SetFiring() {
		if ( !scratching ) {
			firing = true;
		}
	}

	public void SetScratching() {
		if(!firing) {
			scratching = true;
		}
	}

	public void SetGliding( boolean b ) {
		gliding = b;
	}

	public void CheckAttack( ArrayList< Enemy > enemies ) {
		// loop through the enemies
		for ( int i = 0; i < enemies.size(); i++ ) {
			Enemy e = enemies.get(i);

			// scratch attack
			if ( scratching ) {
				if ( facingRight ) {
					if ( e.GetX() > x && e.GetX() < x + scratchRange &&
						 e.GetY() > y - height / 2 && e.GetY() < y + height / 2) {
						e.Hit( scratchDamage );
					}
				} else {
					if (e.GetX() < x && e.GetX() > x - scratchRange &&
						e.GetY() > y - height / 2 && e.GetY() < y + height / 2) {
						e.Hit( scratchDamage );
					}
				}
			}
			// fireballs
			for(int j = 0; j < snowBalls.size(); j++ ) {
				if( snowBalls.get( j ).Intersects( e ) ){
					e.Hit(snowBallDamage);
					snowBalls.get( j ).SetHit();
					break;
				}
			}

			// check for enemy collision
			if( Intersects( e ) ) {
				Hit( e.GetDamage() );
			}
		}
	}

	public void Hit( int damage ) {
		if( flinching ) {
			return;
		}
		health 		-= damage;
		if(health < 0) {
			health 	= 0;
		}
		if(health == 0) {
			dead 	= true;
		}
		flinching 	= true;
		flinchTimer = System.nanoTime();
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
		} else {
			if ( dx > 0 ) {
				dx -= stopSpeed;
				if ( dx < 0 ) {
					dx = 0;
				}
			} else if ( dx < 0 ) {
				dx += stopSpeed;
				if ( dx > 0 ) {
					dx = 0;
				}
			}
		}

		// cannot move while attacking unless you're in air
		if ( ( currentAction == SCRATCHING ||
			   currentAction == FIREBALL ) &&
			   !( jumping || falling ) ) {
			dx = 0;
		}

		// jumping
		if ( jumping && !falling ) {
			dy 		= jumpStart;
			falling = true;
		}

		// falling
		if ( falling ) {
			if ( dy > 0 && gliding ) {
				dy += fallSpeed * 0.1;
			} else {
				dy += fallSpeed;
			}

			if ( dy > 0 ) {
				jumping = false;
			}

			if ( dy < 0 && !jumping ) {
				dy += stopJumpSpeed;
			}

			if ( dy > maxFallSpeed ) {
				dy = maxFallSpeed;
			}
		}
	}

	public void Update() {
		// Update position
		GetNextPosition();
		CheckTileMapCollision();
		SetPosition( xtemp, ytemp );

		// check if attack has stopped
		if ( currentAction == SCRATCHING ) {
			if ( animation.HasPlayedOnce() ) {
				scratching = false;
			}
		}

		if ( currentAction == FIREBALL ) {
			if ( animation.HasPlayedOnce() ) {
				firing = false;
			}
		}

		// fireball attack
		snow += 1;
		if ( snow > maxSnow) {
			snow = maxSnow;
		}
		if ( firing && currentAction != FIREBALL ) {
			if ( snow > snowCost) {
				snow -= snowCost;
				SnowBall fb = new SnowBall( tileMap, facingRight );
				fb.SetPosition( x, y );
				snowBalls.add( fb );
			}
		}

		// Update fireballs
		for (int i = 0; i < snowBalls.size(); i++ ) {
			snowBalls.get( i ).Update();
			if ( snowBalls.get( i ).ShouldRemove() ) {
				snowBalls.remove( i );
				i--;
			}
		}

		// check done flinching
		if( flinching ) {
			long elapsed = ( System.nanoTime() - flinchTimer ) / 1000000;
			if( elapsed > 1000 ) {
				flinching = false;
			}
		}

		// set animation
		if ( scratching ) {
			if ( currentAction != SCRATCHING ) {
				width 			= 60;
				currentAction 	= SCRATCHING;
				animation.SetFrames(sprites.get( SCRATCHING ));
				animation.SetDelay( 50 );
			}
		} else if ( firing ) {
			if (currentAction != FIREBALL) {
				currentAction = FIREBALL;
				animation.SetFrames(sprites.get( FIREBALL ) );
				animation.SetDelay( 70 );
			}
		} else if ( dy > 0 ) {
			if ( gliding ) {
				if ( currentAction != GLIDING ) {
					width 			= 30;
					currentAction 	= GLIDING;
					animation.SetFrames( sprites.get( GLIDING ) );
					animation.SetDelay( 100 );
				}
			} else if ( currentAction != FALLING ) {
				width 			= 30;
				currentAction 	= FALLING;
				animation.SetFrames( sprites.get( FALLING ) );
				animation.SetDelay( 100 );
			}
		} else if ( dy < 0 ) {
			if ( currentAction != JUMPING ) {
				width 			= 30;
				currentAction 	= JUMPING;
				animation.SetFrames( sprites.get( JUMPING ) );
				animation.SetDelay( -1 );
			}
		} else if ( left || right ) {
			if ( currentAction != WALKING ) {
				width 			= 30;
				currentAction 	= WALKING;
				animation.SetFrames( sprites.get( WALKING ) );
				animation.SetDelay( 40 );
			}
		} else {
			if ( currentAction != IDLE ) {
				width 			= 30;
				currentAction 	= IDLE;
				animation.SetFrames( sprites.get( IDLE ) );
				animation.SetDelay( 400 );
			}
		}

		animation.Update();

		// set direction
		if ( currentAction != SCRATCHING && currentAction != FIREBALL ) {
			if ( right ) {
				facingRight = true;
			}
			if ( left ) {
				facingRight = false;
			}
		}
	}

	public void Draw( Graphics2D g ) {
		SetMapPosition();

		// Draw fireballs
		for (int i = 0; i < snowBalls.size(); i++ ) {
			snowBalls.get( i ).Draw( g );
		}

		// Draw player
		if ( flinching ) {
			long elapsed = ( System.nanoTime() - flinchTimer ) / 1000000;
			if ( elapsed / 100 % 2 == 0 ) {
				return;
			}
		}
		super.Draw( g );
	}
}
