package model;

import java.util.ArrayList;
import java.util.List;
//import java.util.Random;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Vector2;

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
	private List<Ground> grounds;

	private double spawnTimeout = 2;
	
	/**
	 * 
	 */
	public Arena() {
		
		views = new ArrayList<IView>();
		coins = new ArrayList<Coins>();
		grounds = new ArrayList<Ground>();

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

		setGravity(new Vector2(0, -2));
		setBounds(bounds);

		runner = new PlayerA("player");
		runner.translate(0, -3.8);
		addBody(runner);
		
		for(int i = 0; i < 32; i++) {
			Ground g = new Ground();
			grounds.add(g);
			g.translate((i*.5)-8,-4.5);
			addBody(g);					
		}
		
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
	public List<Ground> getGround() {
		return grounds;
	}
	
	public void addGroundRight() {
		
		Ground last = grounds.get(grounds.size() - 1);
		Vector2 c = last.getWorldCenter();
		Ground first = grounds.get(0);
		Vector2 cF = first.getWorldCenter();
		//System.out.println(cF.x);
		System.out.println(grounds.size()-1);

		if(c.x > 8) {
			
			Ground g = new Ground();
			grounds.add(0,g);
			g.translate(cF.x-0.4,-4.5);
			addBody(g);	
		}
		
	}
	
	public void addGroundLeft() {
		
		Ground last = grounds.get(grounds.size() - 1);
		Vector2 c = last.getWorldCenter();
		Ground first = grounds.get(0);
		Vector2 cF = first.getWorldCenter();
		System.out.println(grounds.size()-1);
		
		if(cF.x < -8) {
			
			Ground g = new Ground();
			grounds.add(grounds.size(),g);
			g.translate(c.x+.5,-4.5);
			addBody(g);	
		}
		
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
				if(b instanceof Ground) {
					((Ground) b).update(elapsedTime);
				}
				else if(b instanceof Coins) {
					((Coins) b).update(elapsedTime);
				}

			}
		}

		runner.update(elapsedTime);
		notifyViews();

	}

	/**
	 * 
	 * @param b
	 */
	private void deleteBody(Body b) {

		if ( b instanceof Ground ) {
		} else if  ( b instanceof Coins ) {
			coins.remove(b);
		}

	}
	
	/**
	 * 
	 * @param b
	 * @param body
	 */
	private void hit(Ground b, Body body) {
		if (body instanceof Player) {
			hit(b, (Player) body);
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
		
		if ( b1 instanceof Ground && !( b2 instanceof Ground ) ) {
			hit((Ground)b1, b2);
		} else if ( b2 instanceof Ground && !( b1 instanceof Ground ) ) {
			hit((Ground)b2, b1);
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
	private void hit(Ground b, Player p) {
		
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
