package Entity;

import java.awt.image.BufferedImage;

public class Animation
{
	private BufferedImage[] 	frames;
	private int 				currentFrame;
	private long 				startTime;
	private long 				delay;
	private boolean 			playedOnce;

	public Animation() {
		playedOnce = false;
	}

	public void SetFrames( BufferedImage[] frames ) {
		this.frames 	= frames;
		currentFrame 	= 0;
		startTime 		= System.nanoTime();
		playedOnce 		= false;
	}

	public void SetDelay( long d ) {
		delay = d;
	}

	public void SetFrame( int i ) {
		currentFrame = i;
	}

	public void Update() {

		if (delay == -1) {
			return;
		}

		long elapsed = ( System.nanoTime() - startTime ) / 1000000;
		if ( elapsed > delay ) {
			currentFrame++;
			startTime = System.nanoTime();
		}
		if ( currentFrame == frames.length ) {
			currentFrame = 0;
			playedOnce = true;
		}
	}

	public int GetFrame() {
		return currentFrame;
	}

	public BufferedImage GetImage() {
		return frames[ currentFrame ];
	}

	public boolean HasPlayedOnce() {
		return playedOnce;
	}
}
