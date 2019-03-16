package dynamo;


import java.lang.System;

	
/**
*
*	@class BaseTimer
*	@brief Simple timer class that runs off of the system time.
*	@remarks Note that this class runs off of real time and not frame time, so
*	you must call the update() method in the update loop of the game.
*
*/
public class BaseTimer {

	TimerState state;
	
	TimerCallback tc = null;
	TimerCallbackParameter tcp = null;
	
	long action_t;
	long t;
	long fireCount;		// - Count of the number of thimes the timer has fired since starting.
	long last_t;
	long start_t;		// - The starting time of the timer.
		
	public BaseTimer() {
		state = new TimerState();
		state.set(TimerState.STOPPED);
		action_t=0;
	}
	
	/*************** GETTER / SETTER METHODS *******************?

	/**
	*	@fn setActionTime()
	*	@brief Sets the fire time for the timer.
	*/
	public void setActionTime( long at ) {
		action_t = at;
	}
	
	/**
	*	@fn setCallback()
	*	@brief Sets the TimerCallback class with an Execute() method to be called back when the timer fires.
	*/
	public void setCallback( TimerCallback cb, TimerCallbackParameter dtcp ) {
		tc=cb;
		tcp = dtcp;
	}
	
	/**
	*	@fn getActionTime()
	*	@return The fire rate of this timer (in ms).
	*/
	public long getActionTime() { 
		return action_t; 
	}
	
	/**
	*	@fn getTime()
	*	@return The current time held in the timer.
	*/
	public long getTime() { 
		return t;
	}
	
	/**
	*	@fn getState()
	*	@return The state of the timer.
	*/
	public TimerState getState() {
		return state;
	}	
	/*************** UPDATE METHODS *******************/
		
	/**
	*	@fn update()
	*	@brief Update the timer's time.
	*	@return The number of times the timer fired during the update.
	*/
	public int update() {
		int fcount=0;
		if( state.get()==TimerState.RUNNING ) {
			long dt = System.currentTimeMillis() - last_t;
			t+=dt;
			while( t>= action_t ) {
				t-=action_t;
				if( tc!=null ) 
					tc.execute(tcp);
				++fcount; 				// - increment the number of times we called the callback.
			}
			fireCount+=fcount;
			last_t=System.currentTimeMillis();
		}
		return fcount;	
	}
	
	/*************** CONTROL METHODS *******************?
	/**
	*	@fn start()
	*	@brief Start running the timer.
	*/
	public void start() {
		state.set(TimerState.RUNNING);
		reset();
	}
	
	/**
	*	@fn pause()
	*	@brief Pauses the timer.  No fires will be reported while the timer pauses, and it will not update.
	*	However, if resume() is called, then the timer will fire respective to the amount of time that has passed since it last fired.
	*/
	public void pause() {
		state.set(TimerState.PAUSED);
	}
	
	/**
	*	@fn resume()
	*	@brief Resume a previously paused timer.
	*	@return False if the timer was not paused, true otherwise.
	*/
	public boolean resume() {
		if( state.get()==TimerState.PAUSED ) {
			state.set(TimerState.RUNNING);
			return true;
		}
		return false;
	};
	
	/**
	*	@fn stop()
	*	@brief Stops the timer.
	*	@return False if the timer was not running, true otherwise.
	*/
	public boolean stop() {
		if( state.get()==TimerState.RUNNING ) {
			reset();
			state.set(TimerState.STOPPED);
			return true;
		}	
		return false;
	}

	/**
	*	@fn reset()
	*	@brief Reset the timer (private);
	*/
	private void reset() {
		last_t = start_t = System.currentTimeMillis();
		t=0;
	}
	
}