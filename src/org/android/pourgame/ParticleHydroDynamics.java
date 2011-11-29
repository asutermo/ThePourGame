package org.android.pourgame;

import android.util.Log;

public class ParticleHydroDynamics {
	public int width, height, num_particles;
	public Particle[] particles;

	public float   poly6_const,
					poly6_grad_const,
					poly6_lap_const,
					spiky_grad_const,
					visc_lap_const,
					dt, dt2,
					cs, cs2,
					dampf,
					rest_density,
					viscosity,
					mass, imass,
					radius, iradius,
					radius2, iradius2,
					radius3, iradius3,
//					position[][],
//					prev_position[][],
//					velocity[][],
//					normals[][],
//					force[][],
//					pressure[],
//					density[],
//					tension[],
//					idensity[],
//					idensity2[],
					plane_coord[][],
					plane_norm[][],
					distances[][];

	public float acc_x, acc_y, dist, ratio, ilen, step,
					nrm_dx, nrm_dy, tan_dx, tan_dy, dot;
	public float scalar_field[][];
	public float dx, dy, ddx, ddy, fx, fy, r, r2, den, Pij, Vij, Tij;
	public float ray_coord[] = new float[2],
				 ray_dir[] = new float[2],
				 isec[] = new float[2];
	
	public ParticleHydroDynamics(int width, int height) {
		this.width = width;
		this.height = height;
		int dim = 10;
		
		num_particles = dim*dim;
		particles = new Particle[num_particles];
		for(int i=0; i < num_particles; i++)
			particles[i] = new Particle(width, height, width, height);
		
		float orig_x = -0.25f,
		orig_y = -0.25f,
		w = 0.5f, h = 0.5f,
		off = 0.5f/(dim - 1),
		area = w*h;
		
		dt = 0.01f; dt2 = dt*dt;
		cs = 10.0f; cs2 = cs*cs;
		dampf = 0.15f;
		radius = 5*off; iradius = 1.0f/radius;
		radius2 = radius*radius; iradius2 = 1.0f/radius2;
		radius3 = radius*radius2; iradius3 = 1.0f/radius3;
		mass = 1.0f; imass = 1.0f/mass;
		rest_density = 25000.0f;
		viscosity = 1000.0f;
		
		poly6_const = 315.0f/(float)(64.0f*Math.PI*Math.pow(radius, 9));
		poly6_grad_const = -945.0f/(float)(32.0f*Math.PI*Math.pow(radius, 9));
		poly6_lap_const = 945.0f/(float)(32.0f*Math.PI*Math.pow(radius, 9));
		spiky_grad_const = -45.0f/(float)(Math.PI*Math.pow(radius, 6));
		visc_lap_const = 45.0f/(float)(Math.PI*Math.pow(radius, 6));
		
//		position = new float[num_particles][2];
//		prev_position = new float[num_particles][2];
//		velocity = new float[num_particles][2];
//		normals = new float[num_particles][2];
//		force = new float[num_particles][2];
//		pressure = new float[num_particles];
//		tension = new float[num_particles];
//		density = new float[num_particles];
//		idensity = new float[num_particles];
//		idensity2 = new float[num_particles];
		scalar_field = new float[width/2][height/2];
		distances = new float[num_particles][num_particles];
		
		plane_coord = new float[4][2];
		plane_norm = new float[4][2];
		
		plane_coord[0][0] = 0.0f; plane_coord[0][1] = -0.9f;
		plane_norm[0][0] = 0.0f; plane_norm[0][1] = 1.0f;
		plane_coord[1][0] = 0.9f; plane_coord[1][1] = 0.0f;
		plane_norm[1][0] = -1.0f; plane_norm[1][1] = 0.0f;
		plane_coord[2][0] = -0.9f; plane_coord[2][1] = 0.0f;
		plane_norm[2][0] = 1.0f; plane_norm[2][1] = 0.0f;
		plane_coord[3][0] = 0.0f; plane_coord[3][1] = 0.9f;
		plane_norm[3][0] = 0.0f; plane_norm[3][1] = -1.0f;
		
		for(int i = 0, j, k = 0; i < dim; ++i) {
			for(j = 0; j < dim; ++j, ++k) {
				//particles[k].x = orig_x + i*off - 0.2f; 
				//particles[k].y = orig_y + j*off;
				particles[k].prev_x = particles[k].x;
				particles[k].prev_y = particles[k].y;
				particles[k].xv = -25.0f; particles[k].yv = 0.0f;
				particles[k].pressure = 0.0f;
				particles[k].density = 0.0f;
			}
		}
	}
	
	public float poly6(float r2)
	{ return poly6_const*((float)Math.pow(radius2 - r2, 3)); }
		
	public float poly6_grad(float r2)
	{ return poly6_grad_const*((float)Math.pow(radius2 - r2, 2)); }
	
	public float poly6_lap(float r2)
	{ return poly6_lap_const*((radius2 - r2)*(7*r2 - 3*radius2)); }
	
	public float spiky_grad(float r)
	{ return spiky_grad_const*((float)Math.pow(radius - r, 2)); }
	
	public float visc_lap(float r)
	{ return visc_lap_const*(radius - r); }
	

	public void computeDensity() {
		int i, j;

		for(i = 0; i < num_particles; ++i)
			particles[i].density = 0.0f;

		for(i = 0; i < num_particles; ++i) {
			particles[i].density += mass*poly6(0.0f);

			for(j = i + 1; j < num_particles; ++j)
			{
				dx = (particles[j].x - particles[i].x);
				dy = (particles[j].y - particles[i].y);
				r2 = (dx*dx + dy*dy);
				if(r2 < radius2)
				{
					den = mass*poly6(r2);
					particles[i].density += den;
					particles[j].density += den;
					distances[i][j] = (float)Math.sqrt(r2);
				}
				else
					distances[i][j] = -1.0f;
			}
		}

		for(i = 0; i < num_particles; ++i) {
			particles[i].idensity = 1.0f/particles[i].density;
			particles[i].idensity2 = particles[i].idensity*particles[i].idensity;
			particles[i].pressure = cs2*(particles[i].density - rest_density);
		}
	}

	public void computeForces()
	{
		int i, j;
		for(i = 0; i < num_particles; ++i)
		{
			particles[i].forcex = (float) -(dampf*particles[i].xv);
			particles[i].forcey = (float) -(9.8f + dampf*particles[i].yv);
			particles[i].tension = 0.0f;
			particles[i].normalx = 0.0f;
			particles[i].normaly = 0.0f;
		}

		for(i = 0; i < num_particles; ++i)
		{
			for(j = i + 1; j < num_particles; ++j)
				if(distances[i][j] > 0.0f)
				{
					r = distances[i][j];
					r2 = r*r;
					dx = (particles[i].x - particles[j].x);
					dy = (particles[i].y - particles[j].y);
					ddx = (float) (particles[j].xv - particles[i].xv);
					ddy = (float) (particles[j].yv - particles[i].yv);
					Pij = -mass*(particles[i].pressure*particles[i].idensity2 +
							particles[j].pressure*particles[j].idensity2)*
							spiky_grad(r);
					Vij = (viscosity*mass*visc_lap(r))*(particles[i].idensity*particles[j].idensity);

					fx = (Pij*dx + Vij*ddx);
					fy = (Pij*dy + Vij*ddy);

					particles[i].forcex += fx; particles[i].forcey += fy;
					particles[j].forcex -= fx; particles[j].forcey -= fy;
				}
		}
	}

	public float collide(float plane_coord[], float plane_nrm[],
			float ray_coord[], float ray_dir[],
			float isec[])
	{
		float dt = (plane_nrm[0]*ray_dir[0] + plane_nrm[1]*ray_dir[1]),
		dx = (ray_coord[0] - plane_coord[0]),
		dy = (ray_coord[1] - plane_coord[1]),
		dist = -(plane_nrm[0]*dx + plane_nrm[1]*dy)/dt;

		if(dist < 0.0) return -1.0f;

		isec[0] = ray_coord[0] + dist*ray_dir[0];
		isec[1] = ray_coord[1] + dist*ray_dir[1];

		return dist;
	}
	
	public void integrate()
	{
		int i, j;

		for(i = 0; i < num_particles; ++i)
		{
			particles[i].prev_x = particles[i].x;
			particles[i].prev_y = particles[i].y;
			acc_x = particles[i].forcex*imass;
			acc_y = particles[i].forcey*imass;
			particles[i].x += (particles[i].xv*dt + acc_x*dt2);
			particles[i].y += (particles[i].yv*dt + acc_y*dt2);
			particles[i].xv = (particles[i].x - particles[i].prev_x)/dt;
			particles[i].yv = (particles[i].y - particles[i].prev_y)/dt;

			ray_coord[0] = particles[i].prev_x;
			ray_coord[1] = particles[i].prev_y;
			ray_dir[0] = (particles[i].x - particles[i].prev_x);
			ray_dir[1] = (particles[i].y - particles[i].prev_y);

			ilen = 1.0f/(float)Math.sqrt(ray_dir[0]*ray_dir[0] + ray_dir[1]*ray_dir[1]);

			ray_dir[0] *= ilen;
			ray_dir[1] *= ilen;

			float prev_delt = dt,
			delt = 0.0f;

			for(;;) {
				for(j = 0; j < plane_coord.length; ++j)	{
					dist = collide(plane_coord[j], plane_norm[j], ray_coord, ray_dir, isec);

					if(dist >= 0.0f) {
						ratio = (1.0f - (dist*ilen));
						step = dt*ratio;

						if(step > delt)	{
							delt = step;

							particles[i].x = isec[0] - 1.0e-4f*ray_dir[0];
							particles[i].y = isec[1] - 1.0e-4f*ray_dir[1];

							dot = (float) (particles[i].xv*plane_norm[j][0] + 
									particles[i].yv*plane_norm[j][1]);

							nrm_dx = plane_norm[j][0]*dot;
							nrm_dy = plane_norm[j][1]*dot;
							tan_dx = (float) (particles[i].xv - nrm_dx);
							tan_dy = (float) (particles[i].yv - nrm_dy);

							particles[i].xv = (tan_dx - 0.6f*nrm_dx);
							particles[i].yv = (tan_dy - 0.6f*nrm_dy);
						}
					}
				}

				if(delt > 0.0f && delt < prev_delt)	{
					particles[i].prev_x = particles[i].x;
					particles[i].prev_y = particles[i].y;
					particles[i].x += particles[i].xv*delt;
					particles[i].y += particles[i].yv*delt;
					prev_delt = delt;
				}
				else break;
			}

			if(particles[i].x > 0.9f) particles[i].x = 0.9f;
			if(particles[i].x < -0.9f)	particles[i].x = -0.9f;
			if(particles[i].y < -0.9f) particles[i].y = -0.9f;
			if(particles[i].y > 0.9f) particles[i].y = 0.9f;
		}
	}

	public void computeScalarField()
	{
		for(int i = 0; i < scalar_field.length; ++i)
			for(int j = 0; j < scalar_field[i].length; ++j)
				scalar_field[i][j] = 0.0f;

		for(int i = 0; i < num_particles; ++i)
		{
			float x = (((float)(width/2))*((particles[i].x + 1.0f)/2)),
			y = (((float)(height/2))*(1.0f - (particles[i].y + 1.0f)/2)),
			xoff, yoff;

			for(xoff = -5; xoff <= 5; ++xoff) {
				for(yoff = -5; yoff <= 5; ++yoff) {
					if((xoff*xoff + yoff*yoff) < 16.0f)
					{
						int ix = (int)Math.round(x + xoff),
						iy = (int)Math.round(y + yoff);
						float val = (16.0f - (xoff*xoff + yoff*yoff))/16.0f;

						if(val > scalar_field[ix][iy])
							scalar_field[ix][iy] = val;
					}
				}
			}
		}
	}
}
