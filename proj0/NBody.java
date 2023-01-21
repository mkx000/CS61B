import java.util.ArrayList;

public class NBody{
	public static void main(String[] args){
		double T, dt;
		T = Double.parseDouble(args[0]);
		dt = Double.parseDouble(args[1]);
		String filename = args[2];

		double radius = readRadius(filename);
		Planet[] planets = readPlanets(filename);

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

		//if (!in.isEmpty())
		//	System.out.println("in is not empty");

		int numOfplanets = in.readInt();
		//System.out.println(in.readDouble());
		in.readDouble();

		Planet[] planets = new Planet[numOfplanets];
		for (int i = 0; i < numOfplanets ; i++){
			double xpos, ypos, xvel, yvel, mass;
			xpos = in.readDouble();
			ypos = in.readDouble();
			xvel = in.readDouble();
			yvel = in.readDouble();
			mass = in.readDouble();
			
			String img = in.readString();
			//planets[i] = new planet(xpos, ypos, xvel, yvel, mass, "./images/" + img); 
			planets[i] = new Planet(xpos, ypos, xvel, yvel, mass, "./images/" + img); 

			//system.out.println(img);
			//system.out.println(planets[i].imgfilename);

		}
		return planets;
			
		/*arraylist<planet> bodies = new arraylist<planet>();

		if (!in.isEmpty())
			System.out.println("in is not empty");

		for (int i = 0; i < numOfplanets; i++){
			double xPos, yPos, xVel, yVel, mass;
			xPos = in.readDouble();
			yPos = in.readDouble();
			xVel = in.readDouble();
			yVel = in.readDouble();
			mass = in.readDouble();
			
			String img = in.readString();
			//Planet planet = new Planet(xPos, yPos, xVel, yVel, mass, "./images/" + img); 
			Planet planet = new Planet(xPos, yPos, xVel, yVel, mass, img); 
			Bodies.add(planet);
		}
		return (Planet [])Bodies.toArray();*/
	}	
}


