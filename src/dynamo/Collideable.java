package dynamo;

public class Collideable {
	BoundCircle boundCircle;
	Collideable() {
		boundCircle = new BoundCircle();
	}
	/**
	*	@fn collide()
	*	@brief Returns the collision distance between the two bound circles.
	*/
	public static boolean collide( BoundCircle b1, VECTOR2 p1, VECTOR2 v1, BoundCircle b2, VECTOR2 p2, VECTOR2 v2, VECTOR2 cPos  ) {
		float dist = p1.squaredDistanceTo(p2);//distanceTo( p2 );
		float rad = (b1.getBoundRadius() + b2.getBoundRadius())*(b1.getBoundRadius() + b2.getBoundRadius());
		
		if( dist < rad )
		{
			// - Collision
			dist = p1.distanceTo(p2);
				
			float d1 = b1.getBoundRadius() + b2.getBoundRadius() - dist;
			
			d1+=0.002f;	//padding
				
			VECTOR2 nv= new VECTOR2();
			
			nv = p1.minus(p2);
			nv.normalize();
			
			cPos = p1.plus( nv );

			p1.plusEquals( nv.times(d1/2F) );
			p2.plusEquals( nv.times(d1/2F).negative() );
			
			return true;
		}
		return false;
	}
}