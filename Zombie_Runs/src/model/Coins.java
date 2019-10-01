package model;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

public class Coins extends Body {
	
	final static double DENSITY = 0.2;
	final static double MAXSCROLLING = 0.5;

	BodyFixture fixture;
	private double value;
	
	private double primaryTimeout = 0.04;
	private double primaryTimer = primaryTimeout;
	
	public Coins(double value) {
		
		fixture = new BodyFixture(Geometry.createCircle(0.5));
		fixture.setDensity(DENSITY);
		fixture.setRestitution(1.0);
		fixture.setFriction(2);
		fixture.setDensity(1);
		fixture.setRestitution(1);
		this.addFixture(fixture);
		this.setMass(MassType.FIXED_ANGULAR_VELOCITY);
		setLinearDamping(.20);
		
		this.value = value;
		
	}
	
	public double getValue() {
		return value;
	}
	
	public void hit(Body b) {
		setActive(false);		
	}
	
	public void update(double elapsedTime) {
		
		primaryTimer -= elapsedTime;
		if(primaryTimer <= 0) {
			shift(new Vector2(0, -0.02));
			primaryTimer = primaryTimeout;
		}	
		
	}


}
