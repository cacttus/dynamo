package dynamo;

/**
*	@class TimerState
*	@brief The state of a timer.  Should be superclassed for new timers.
*/
public class TimerState extends BaseState {
	/**
	*	DECLARE ALL PUBLIC STATIC FINAL INT'S HERE
	*/
	public static final int RUNNING 	= 0;
	public static final int STOPPED 	= 1;
	public static final int PAUSED 		= 2;

}