import java.lang.Math;

public class Planet{

	public double xxPos, yyPos, xxVel, yyVel, mass;
	public String imgFileName;
	
	private static final double G = 6.67E-11;
	public Planet(double xP, double yP, double xV, double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

	/*public Planet(Planet b){
		xxPos = b.xxPos;
		yyPos = b.yyPos;
		xxVel = b.xxVel;
		yyVel = b.yyVel;
		mass = b.mass;
		imgFileName = b.imgFileName;
	}*/

	public void draw(){
		StdDraw.picture(xxPos, yyPos, imgFileName);
	}

	public double calcDistance(Planet b){
		double dx = xxPos - b.xxPos;
		double dy = yyPos - b.yyPos;
		double tmp = dx * dx + dy * dy;
		double r = Math.sqrt(tmp);
		return r;
	}

	public double calcForceExertedBy(Planet b){
		double dis = calcDistance(b);
		double force = mass * b.mass * G / (dis * dis);
		return force;	
	}

	public double calcForceExertedByX(Planet b){
		double force = calcForceExertedBy(b);
		double dis = calcDistance(b);
		double forceX = (b.xxPos - xxPos) / dis * force; 
		return forceX;
	}

	public double calcForceExertedByY(Planet b){
		double force = calcForceExertedBy(b);
		double dis = calcDistance(b);
		double forceY = (b.yyPos - yyPos) / dis * force; 
		return forceY;
	}

	public double calcNetForceExertedByX(Planet []planets){
		double netForceX = 0;
		for (int i = 0; i < planets.length; i++){
			if (!this.equals(planets[i]))
				netForceX += calcForceExertedByX(planets[i]);
		}

		return netForceX;
	}

	public double calcNetForceExertedByY(Planet []planets){
		double netForceY = 0;
		for (int i = 0; i < planets.length; i++){
			if (!this.equals(planets[i]))
				netForceY += calcForceExertedByY(planets[i]);
		}

		return netForceY;
	}

	public void update(double dt, double fX, double fY){
		double acceX = fX / mass;
		double VelIncX = acceX * dt;
		xxVel += VelIncX;
		xxPos += xxVel * dt;

		
		double acceY = fY / mass;
		double VelIncY = acceY * dt;
		yyVel += VelIncY;
		yyPos += yyVel * dt;
	}

}
