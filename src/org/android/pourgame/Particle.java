package org.android.pourgame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;


/**
 * @author impaler
 *
 */
public class Particle {
	
	public static final int STATE_ALIVE = 0;	// particle is alive
	public static final int STATE_DEAD = 1;		// particle is dead
	
	public static final int DEFAULT_LIFETIME 	= 200;	// play with this
	public static final int MAX_DIMENSION		= 5;	// the maximum width or height
	public static final int MAX_SPEED			= 10;	// maximum speed (per update)
	
	public int state;			// particle is alive or dead
	public float width;		// width of the particle
	public float height;		// height of the particle
	public float x, y;			// horizontal and vertical position
	public float prev_x, prev_y; // previous horiz and vert position
	public double xv, yv;		// vertical and horizontal velocity
	public float radius;
	public int age;			// current age of the particle
	public int lifetime;		// particle dies when it reaches this value
	public int color;			// the color of the particle
	public Paint paint;		// internal use to avoid instantiation
	public int maxx, maxy;
	public float normalx, normaly;
	public float forcex, forcey;
	public float tension, density, idensity, idensity2, pressure;
	
	
	// helper methods -------------------------
	public boolean isAlive() {
		return this.state == STATE_ALIVE;
	}
	public boolean isDead() {
		return this.state == STATE_DEAD;
	}

	public Particle(int x, int y, int maxx, int maxy) {
		this.x = x;
		this.y = y;
		this.maxx = maxx;
		this.maxy = maxy;
		this.state = Particle.STATE_ALIVE;
		this.radius = 10;

		this.lifetime = DEFAULT_LIFETIME;
		this.age = 0;
		this.xv = (rndDbl(0, MAX_SPEED * 2) - MAX_SPEED);
		this.yv = (9.81f);
		// smoothing out the diagonal speed
		if (xv * xv + yv * yv > MAX_SPEED * MAX_SPEED) {
			xv *= 0.7;
			yv *= 0.7;
		}
		this.color = Color.argb(255, 70, 70, 100);
		this.paint = new Paint(this.color);
	}
	
	/**
	 * Resets the particle
	 * @param x
	 * @param y
	 */
	public void reset(float x, float y) {
		this.state = Particle.STATE_ALIVE;
		this.x = x;
		this.y = y;
		this.age = 0;
	}

	// Return an integer that ranges from min inclusive to max inclusive.
	static int rndInt(int min, int max) {
		return (int) (min + Math.random() * (max - min + 1));
	}

	static double rndDbl(double min, double max) {
		return min + (max - min) * Math.random();
	}
	
	public void update() {
		if (this.state != STATE_DEAD) {
			this.x += this.xv;
			this.y += this.yv;
			
//			// extract alpha
//			int a = this.color >>> 24;
//			a -= 2;								// fade by 5
//			if (a <= 0) {						// if reached transparency kill the particle
//				this.state = STATE_DEAD;
//			} else {
//				this.color = (this.color & 0x00ffffff) + (a << 24);		// set the new alpha
//				this.paint.setAlpha(a);
//				this.age++;						// increase the age of the particle
//				this.width *= 1.05;
//				this.height *= 1.05;
//			}
//			if (this.age >= this.lifetime) {	// reached the end if its life
//				this.state = STATE_DEAD;
//			}
			
			// Handle collision with sides.
			if( (x < radius) || (x > maxx-radius) ) {
				Log.d("Collision", "Changing XV");
				xv = -xv;
			}
			if( (y > maxy-radius) )
				yv = -yv;
		}
	}
	
	public void update(RectF container) {
		// update with collision
		if (this.isAlive()) {
			if (this.x <= container.left || this.x >= container.right - this.width) {
				this.xv *= -1;
			}
			// Bottom is 480 and top is 0 !!!
			if (this.y <= container.top || this.y >= container.bottom - this.height) {
				this.yv *= -1;
			}
		}
		update();
	}

	public void draw(Canvas canvas) {
//		paint.setARGB(255, 128, 255, 50);
		paint.setColor(this.color);
		//canvas.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, paint);
		canvas.drawCircle(x, y, radius, paint);
	}

}
