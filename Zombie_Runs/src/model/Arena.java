package model;

import java.util.ArrayList;
import java.util.List;
//import java.util.Random;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.World;

import view.IView;

/**
 * 
 * @author pezza
 *
 */
public class Arena extends World {

	//private final static int BIOME_NUM = 100;

	private List<IView> views;

	private Player runner;
	private List<Coins> coins;
	private List<Hurdles> barriers;

	private double spawnTimeout = 2;
	private double spawnTimer = spawnTimeout;
	
	/**
	 * 
	 */
	public Arena() {
		
		views = new ArrayList<IView>();
		coins = new ArrayList<Coins>();
		barriers = new ArrayList<Hurdles>();

		initArena();

		addListener( new CollisionAdapter() {
			@Override
			public boolean collision(Body b1, BodyFixture f1, Body b2, BodyFixture f2) {
				return Arena.this.collision(b1, f1, b2, f2);
			}
		});		

	}

	/**
	 * 
	 */
	private void initArena() {

		setGravity(ZERO_GRAVITY);
		setBounds(bounds);

		runner = new PlayerA("player");
		runner.translate(0, -3);
		addBody(runner);
		
	}
	
	/**
	 * 
	 * @return
	 */
	public Player getPlayer() {
		return runner;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Coins> getCoins() {
		return coins;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Hurdles> getHurdles() {
		return barriers;
	}
	
	/**
	 * 
	 * @param view
	 */
	public void addView(IView view) {
		views.add(view);
	}
	
	/**
	 * 
	 */
	public void notifyViews() {
		for (IView view : views) {
			view.update();
		}	
	}
	
	/**
	 * 
	 */
	public void updatev(double elapsedTime) {

		super.updatev(elapsedTime);

		List<Body> allB = getBodies();
		for(Body b : allB) {
			if(!(b instanceof PlayerA))
			{
				if(b instanceof Hurdles) {
					((Hurdles) b).update(elapsedTime);
				}
				else if(b instanceof Coins) {
					((Coins) b).update(elapsedTime);
				}

			}
		}

		spawnTimer -= elapsedTime;
		if(spawnTimer <= 0) {
			spawnTimer = spawnTimeout;
		}

		runner.update(elapsedTime);
		notifyViews();

	}

	/**
	 * 
	 * @param b
	 */
	private void deleteBody(Body b) {

		if ( b instanceof Hurdles ) {
			barriers.remove(b);
		} else if  ( b instanceof Coins ) {
			coins.remove(b);
		}

	}
	
	/**
	 * 
	 * @param b
	 * @param body
	 */
	private void hit(Hurdles b, Body body) {
		if (body instanceof Player) {
			hit(b, (Player) body);
			((Player) body).receiveDamage(10);
		} 
	}
	
	/**
	 * 
	 * @param c
	 * @param body
	 */
	private void hit(Coins c, Body body) {
		if(body instanceof Player) {
			hit(c,(Player) body);
		}
	}
	
	/**
	 * 
	 * @param b1
	 * @param f1
	 * @param b2
	 * @param f2
	 * @return
	 */
	private boolean collision(Body b1, BodyFixture f1, Body b2, BodyFixture f2) {
		
		if ( b1 instanceof Hurdles && !( b2 instanceof Hurdles ) ) {
			hit((Hurdles)b1, b2);
		} else if ( b2 instanceof Hurdles && !( b1 instanceof Hurdles ) ) {
			hit((Hurdles)b2, b1);
		}
		else if ( b2 instanceof Coins && !( b1 instanceof Coins ) ){
			hit((Coins) b2, b1);			
		}
		else if ( b1 instanceof Coins && !( b2 instanceof Coins)) {
			hit((Coins) b1, b2);	
		}
		return true;
		
	}
	
	/**
	 * 
	 * @param b
	 * @param p
	 */
	private void hit(Hurdles b, Player p) {
		
		b.hit(p);
		if ( ! b.isActive() ) {
			deleteBody(b);
		}
		
	}
	
	/**
	 * 
	 * @param c
	 * @param p
	 */
	private void hit(Coins c, Player p) {
		
		c.hit(p);
		if ( ! c.isActive() ) {
			deleteBody(c);
			double value = c.getValue();
			p.addScore(value);
			System.out.println("SCORE= " + p.getScore());
		}
		
	}

}
