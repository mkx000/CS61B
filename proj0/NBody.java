

public class NBody{
	public static void main(String[] args){
		double T, dt;
		T = Double.parseDouble(args[0]);
		dt = Double.parseDouble(args[1]);
		String filename = args[2];

		double radius = readRadius(filename);
		Planet[] planets = readPlanets(filename);
		for (int i = 0; i < planets.length; i++){
			planets[i].imgFileName = "./images/" + planets[i].imgFileName;
		}


		String imagePath = "images/starfield.jpg";	

		StdDraw.setScale(-radius, radius);
		StdDraw.clear();

		for (int i = 0; i < planets.length; i++){
			planets[i].draw();
		}

		StdDraw.enableDoubleBuffering();

		double timeVar = 0;
		while(timeVar < T){
			double[] xForces = new double[planets.length], yForces = new double[planets.length];
			for (int i = 0; i < planets.length; i++){
				xForces[i] = planets[i].calcNetForceExertedByX(planets);
				yForces[i] = planets[i].calcNetForceExertedByY(planets);
			}

			for (int i = 0; i < planets.length; i++)
				planets[i].update(dt, xForces[i], yForces[i]);

			StdDraw.picture(0, 0, imagePath);
			for (int i = 0; i < planets.length; i++)
				planets[i].draw();

			StdDraw.show();
			StdDraw.pause(1000);
		}

		timeVar += dt;
	}			

	public static double readRadius(String file){
		In in = new In(file);
		
		in.readInt();
		double r = in.readDouble();
		return r;
	}

	public static Planet[] readPlanets(String file){
		In in = new In(file);
		in.readInt();
		in.readDouble();

		Planet[] Bodies = new Planet[5];
		for (int i = 0; i < 5; i++){
			double xPos, yPos, xVel, yVel, mass;
			xPos = in.readDouble();
			yPos = in.readDouble();
			xVel = in.readDouble();
			yVel = in.readDouble();
			mass = in.readDouble();

			String img = in.readString();
			Bodies[i] = new Planet(xPos, yPos, xVel, yVel, mass, img);

			//System.out.println(Bodies[i].imgFileName);
			//System.out.println(img);
			//System.out.println(Bodies[i].xxPos);
		}

		return Bodies;

	}	
}


