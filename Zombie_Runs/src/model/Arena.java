package model;

import java.util.ArrayList;
import java.util.List;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.CollisionAdapter;
import org.dyn4j.dynamics.World;

import view.IView;

public class Arena extends World {

	final static int XMAX = 5; 
	final static int XMIN = -5;
	final static int YMAX = 10;
	final static int YMIN = 7;
	final static int COIN_NUM = 2;
	final static int HURDL_NUM = 3;

	private List<IView> views;

	private Player runner;
	private List<Coins> coins;
	private List<Hurdles> barriers;

	private double spawnTimeout = 2;
	private double spawnTimer = spawnTimeout;

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

	private void initArena() {

		setGravity(ZERO_GRAVITY);
		setBounds(bounds);

		runner = new PlayerA("player");
		runner.translate(0, -3);
		addBody(runner);

		spawnObjs(coins, barriers, COIN_NUM, HURDL_NUM);

	}

	private void spawnObjs(List<Coins> c, List<Hurdles> h, int numC, int numH) {


		for(int i = 0; i < numH; i++) {

			Hurdles hurdle = new Hurdles();
			h.add(i,hurdle);
			double x = (Math.random() * (XMAX - XMIN + 1)) + XMIN;
			double y = (Math.random() * (YMAX - YMIN + 1)) + YMIN;
			addBody(hurdle);
			hurdle.translate(x,y);

		}

		for(int i = 0; i < numC; i++) {

			Coins coin = new Coins(100);
			c.add(i,coin);
			double x = (Math.random() * (XMAX - XMIN + 1)) + XMIN;
			double y = (Math.random() * (YMAX - YMIN + 1)) + YMIN;
			addBody(coin);
			coin.translate(x,y);

		}

	}

	public Player getPlayer() {
		return runner;
	}

	public List<Coins> getCoins() {
		return coins;
	}

	public List<Hurdles> getHurdles() {
		return barriers;
	}

	public void addView(IView view) {
		views.add(view);
	}

	public void notifyViews() {
		for (IView view : views) {
			view.update();
		}	
	}

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
			spawnObjs(coins, barriers, COIN_NUM, HURDL_NUM);
			spawnTimer = spawnTimeout;
		}

		runner.update(elapsedTime);
		notifyViews();

	}

	private void deleteBody(Body b) {

		if ( b instanceof Hurdles ) {
			barriers.remove(b);
		} else if  ( b instanceof Coins ) {
			coins.remove(b);
		}

	}

	private void hit(Hurdles b, Body body) {
		if (body instanceof Player) {
			hit(b, (Player) body);
			((Player) body).receiveDamage(10);
		} 
	}
	
	private void hit(Coins c, Body body) {
		if(body instanceof Player) {
			hit(c,(Player) body);
		}
	}

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

	private void hit(Hurdles b, Player p) {
		
		b.hit(p);
		if ( ! b.isActive() ) {
			deleteBody(b);
		}
		
	}
	
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
