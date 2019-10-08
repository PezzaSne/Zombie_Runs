package model;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

public class Ground extends Body {
	
	final static double DENSITY = 100;
	
	BodyFixture fixture;
	
	private double primaryTimeout = .04;
	private double primaryTimer = primaryTimeout;
	
	public Ground()  {
		
		fixture = new BodyFixture(Geometry.createSquare(1));
		fixture.setDensity(DENSITY);
		fixture.setRestitution(.001);
		fixture.setFriction(1);
		this.addFixture(fixture);
		this.setMass(MassType.INFINITE);
		setLinearDamping(.20);
		
	}
	
	public void hit(Body b) {
		setActive(true);		
	}
	
	public void update(double elapsedTime) {
		
		primaryTimer -= elapsedTime;
	}

}
