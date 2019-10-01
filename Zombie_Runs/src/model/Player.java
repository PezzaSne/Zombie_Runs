package model;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

public abstract class Player extends Body {
	
	final static double DENSITY = 0.2;
	final static double DEFAULT_SPEED = 20;
	final static double MAXSCROLLING = 0.5;
	
	BodyFixture fixture;
	private String name;
	private double maxHealthPoints;
	private double currentHealthPoints;
	protected double score;
	
	private double primaryTimeout = 0.5;
	private double primaryTimer = primaryTimeout;
	
	
	public Player(String name, double maxHealthPoints) {
		
		fixture = new BodyFixture(Geometry.createCircle(0.5));
		fixture.setDensity(DENSITY);
		fixture.setRestitution(.5);
		fixture.setFriction(2);
		
		addFixture(fixture);
		setMass(MassType.FIXED_ANGULAR_VELOCITY);
		setLinearDamping(1);
		
		this.name = name;
		this.maxHealthPoints = maxHealthPoints;
		this.currentHealthPoints = maxHealthPoints;
		score = 0;
		
	}
	
	public double getMaxHP() {
		return maxHealthPoints;
	}
	
	public double getCurrentHP() {
		return currentHealthPoints;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract double getScore();
	public abstract void addScore(double x);	

	
	public void receiveDamage(double damageReceived) {
		this.currentHealthPoints -= damageReceived;
	}
	
	public void update(double elapsedTime) {
		primaryTimer -= elapsedTime;
		if(primaryTimer <= 0) {
			//translate(0, MAXSCROLLING);
			primaryTimer = primaryTimeout;
		}
	}
	
}
