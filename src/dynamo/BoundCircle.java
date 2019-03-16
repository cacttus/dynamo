package dynamo;

public class BoundCircle {
	private float boundRadius;
	
	BoundCircle() {
		boundRadius = 16F;
	}
	
	/**
	*	@fn getBoundRadius()	
	*	@brief Returns the radius of this boundcircle.
	*/
	public float getBoundRadius() {
		return boundRadius;
	}
	
	/**
	*	@fn setBoundRadius()	
	*	@brief Sets the radius of this boundcircle.
	*/
	public void setBoundRadius(float f) {
		boundRadius = f;
	}
	
}