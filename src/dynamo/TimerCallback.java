package dynamo;

/**
*	@class TimerCallback
*	@brief Passed as an argument to a timer for a callback.
*	@remarks Must be superclassed.
*/
public abstract class TimerCallback {
	abstract public void execute( TimerCallbackParameter tcp );
}