package Entity;

import Main.GamePanel;
import TileMap.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class MapObject
{
	// tiles
	protected TileMap 		tileMap;
	protected int 			tileSize;
	protected double 		xmap;
	protected double 		ymap;

	// position and vector
	protected double 		x;
	protected double 		y;
	protected double 		dx;
	protected double 		dy;

	// dimensions
	protected int 			width;
	protected int 			height;

	// collision box
	protected int 			cwidth;
	protected int 			cheight;

	// collision
	protected int 			currRow;
	protected int 			currCol;
	protected double 		xdest;
	protected double 		ydest;
	protected double 		xtemp;
	protected double 		ytemp;
	protected boolean 		topLeft;
	protected boolean 		topRight;
	protected boolean 		bottomLeft;
	protected boolean 		bottomRight;

	// animations
	protected Animation 	animation;
	protected int 			currentAction;
	protected int 			previousAction;
	protected boolean 		facingRight;

	// movement
	protected boolean 		left;
	protected boolean 		right;
	protected boolean 		up;
	protected boolean 		down;
	protected boolean 		jumping;
	protected boolean 		falling;
	protected boolean 		canJump;

	// physics
	protected double 		moveSpeed;
	protected double 		maxSpeed;
	protected double 		stopSpeed;
	protected double 		fallSpeed;
	protected double 		maxFallSpeed;
	protected double 		jumpStart;
	protected double 		stopJumpSpeed;

	// constructor
	public MapObject( TileMap tm ) {
		tileMap 	= tm;
		tileSize 	= tm.GetTileSize();
	}

	public boolean Intersects( MapObject o ) {
		Rectangle r1 = GetRectangle();
		Rectangle r2 = o.GetRectangle();
		return r1.intersects( r2 );
	}

	public Rectangle GetRectangle() {
		return new Rectangle( ( int ) x - cwidth / 2, ( int ) y - cheight / 2,
								cwidth, cheight );
	}

	public void CalculateCorners( double x, double y ) {
		int leftTile 	= ( int ) ( x - cwidth / 2 ) / tileSize;
		int rightTile 	= ( int ) ( x + cwidth / 2 - 1 ) / tileSize;
		int topTile 	= ( int ) ( y - cheight / 2 ) / tileSize;
		int bottomTile 	= ( int ) ( y + cheight / 2 - 1 ) / tileSize;

		if ( topTile < 0 || bottomTile >= tileMap.GetNumRows() ||
			 leftTile < 0 || rightTile >= tileMap.GetNumCols() ) {
			topLeft = topRight = bottomLeft = bottomRight = false;
			return;
		}

		int tl 		= tileMap.GetType(topTile, leftTile);
		int tr 		= tileMap.GetType(topTile, rightTile);
		int bl 		= tileMap.GetType(bottomTile, leftTile);
		int br 		= tileMap.GetType(bottomTile, rightTile);
		topLeft 	= tl == Tile.BLOCKED;
		topRight 	= tr == Tile.BLOCKED;
		bottomLeft 	= bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
	}

	public void CheckTileMapCollision() {
		currCol = (int) x / tileSize;
		currRow = (int) y / tileSize;

		xdest 	= x + dx;
		ydest 	= y + dy;

		xtemp 	= x;
		ytemp 	= y;

		CalculateCorners( x, ydest );
		if ( dy < 0 ) {
			if ( topLeft || topRight ) {
				dy 		= 0;
				ytemp 	= currRow * tileSize + cheight / 2;
			} else {
				ytemp += dy;
			}
		}
		if ( dy > 0 ) {
			if ( bottomLeft || bottomRight ) {
				dy 		= 0;
				falling = false;
				ytemp 	= ( currRow + 1 ) * tileSize - cheight / 2;
			} else {
				ytemp 	+= dy;
			}
		}

		CalculateCorners( xdest, y );
		if ( dx < 0 ) {
			if ( topLeft || bottomLeft ) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			} else {
				xtemp += dx;
			}
		}
		if ( dx > 0 ) {
			if ( topRight || bottomRight ) {
				dx 		= 0;
				xtemp 	= (currCol + 1) * tileSize - cwidth / 2;
			} else {
				xtemp 	+= dx;
			}
		}

		if ( !falling ) {
			CalculateCorners( x, ydest + 1 );
			if ( !bottomLeft && !bottomRight ) {
				falling = true;
			}
		}
	}

	public int GetX() {
		return ( int ) x;
	}

	public int GetY() {
		return ( int ) y;
	}

	public int GetWidth() {
		return width;
	}

	public int GetHeight() {
		return height;
	}

	public int GetCWidth() {
		return cwidth;
	}

	public int GetCHeight() {
		return cheight;
	}

	public void SetPosition( double x, double y ) {
		this.x = x;
		this.y = y;
	}

	public void SetVector( double dx, double dy ) {
		this.dx = dx;
		this.dy = dy;
	}

	public void SetMapPosition() {
		xmap = tileMap.GetX();
		ymap = tileMap.GetY();
	}

	public void SetLeft( boolean b ) {
		left = b;
	}

	public void SetRight( boolean b ) {
		right = b;
	}

	public void SetUp( boolean b ) {
		up = b;
	}

	public void SetDown( boolean b ) {
		down = b;
	}

	public void SetJumping( boolean b ) {
		jumping = b;
	}

	public void SetCanJump(boolean b ) {
		canJump = b;
	}

	public boolean NotOnScreen() {
		return 	x + xmap + width < 0 ||
				x + xmap - width > GamePanel.WIDTH ||
				y + ymap + height < 0 ||
				y + ymap - height > GamePanel.HEIGHT;
	}

	public void Draw(Graphics2D g ){
		if ( facingRight ) {
			g.drawImage( animation.GetImage(), ( int ) ( x + xmap - width / 2 ),
					( int ) ( y + ymap - height / 2 ), null );
		} else {
			g.drawImage( animation.GetImage(), ( int ) ( x + xmap - width / 2 + width ),
					   ( int ) ( y + ymap - height / 2 ), -width, height, null );
		}

		// Draw collision box
//		Rectangle r = GetRectangle();
//		r.x += xmap;
//		r.y += ymap;
//		g.Draw(r);
	}
}
