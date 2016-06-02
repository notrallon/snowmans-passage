package Entity;

import TileMap.TileMap;

public class Enemy extends MapObject{

	protected int 		health;
	protected int 		maxHealth;
	protected int 		damage;
	protected boolean 	dead;
	protected boolean 	flinching;
	protected long 		flinchTimer;

	public Enemy( TileMap tm ){
		super( tm );
	}

	public boolean IsDead(){ return dead; }

	public int GetDamage() { return damage; }

	public void Hit( int damage ){
		if(dead || flinching) {
			return;
		}

		health -= damage;

		if(health < 0) {
			health 	= 0;
		}

		if(health == 0) {
			dead 	= true;
		}

		flinching 	= true;
		flinchTimer = System.nanoTime();
	}

	public void Update(){

	}
}
