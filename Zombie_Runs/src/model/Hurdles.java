package model;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Vector2;

public class Hurdles extends Body {
	
	final static double DENSITY = 1;
	final static double MAXSCROLLING = 0.5;
	
	BodyFixture fixture;
	
	private double primaryTimeout = 0.04;
	private double primaryTimer = primaryTimeout;
	
	public Hurdles()  {
		
		fixture = new BodyFixture(Geometry.createRectangle(1.5,.5));
		fixture.setDensity(DENSITY);
		fixture.setRestitution(1.0);
		fixture.setFriction(2);
		fixture.setDensity(1);
		this.addFixture(fixture);
		this.setMass(MassType.INFINITE);
		setLinearDamping(.20);
		
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
