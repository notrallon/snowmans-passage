package GameState;

import java.util.ArrayList;

public class GameStateManager {
	private ArrayList< GameState > gameStates;
	private int currentState;

	public static final int 	MENUSTATE 		= 0;
	public static final int 	LEVEL1STATE 	= 1;

	public GameStateManager() {
		gameStates 		= new ArrayList< GameState >();

		currentState 	= MENUSTATE;
		gameStates.add( new MenuState( this ) );
		gameStates.add( new Level1State( this ) );
	}

	public void SetState( int state ) {
		currentState = state;
		gameStates.get( currentState ).Init();
	}

	public void Update() {
		gameStates.get( currentState ).Update();
	}

	public void Draw( java.awt.Graphics2D g ) {
		gameStates.get( currentState ).Draw( g );
	}

	public void KeyPressed( int k ) {
		gameStates.get( currentState ).KeyPressed( k );
	}

	public void KeyReleased( int k ) {
		gameStates.get( currentState ).KeyReleased( k );
	}
}
