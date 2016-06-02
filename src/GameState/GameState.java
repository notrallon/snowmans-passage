package GameState;

public abstract class GameState {

	protected GameStateManager gsm;

	public abstract void Init();

	public abstract void Update();

	public abstract void Draw( java.awt.Graphics2D g );

	public abstract void KeyPressed( int k );

	public abstract void KeyReleased( int k );

}
