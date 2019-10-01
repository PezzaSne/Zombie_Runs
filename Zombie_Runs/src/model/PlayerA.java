package model;

import org.dyn4j.dynamics.BodyFixture;

public class PlayerA extends Player {
	
	BodyFixture fixture;
	
	public PlayerA(String name) {
		super(name, 100);
	}
	
	public void addScore(double x) {
		this.score += x;
	}
	
	public double getScore() {
		return score;
	}

}
