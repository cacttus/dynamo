package dynamo;


import java.lang.System;

/**
*	@class FrameCheck
*	@brief An FPS checker and utility class for games in Java.
*/
public class FrameCheck {
	
	long last_t, 								// - Timestamp at the beginning of the frame.
	cur_t, 										// - Timestamp at the end of the frame.
	frame_t, 									// - Time to execute one frame.
	nFrames;									// - Number of frames since game start.

	BaseTimer 	pollTimer;

	double avg_Rate;							// - Average framerate.
	float frameRate;							// - The immediate frame rate.
	float polledFrameTime;						// - Updates every 500ms
	
	int frameCount;
	int sampleRate;								// - Number of frames to take a sample from.
	int curSample;								// - The sample number for this frame.
	
	public FrameCheck()
	{
		last_t = System.currentTimeMillis();
		curSample=frameCount=0;
		frameRate=0F;
		sampleRate=1;
		nFrames=0;
		pollTimer = new BaseTimer();
		pollTimer.setActionTime( 500 );
		pollTimer.start();
	}
	
	/**
	*	@fn setPollRate()
	*	@brief Set the rate in which to poll frames.
	*/
	public void setSampleRate( int n_frames ) {
		if(n_frames<=0)
			n_frames=1;
		sampleRate = n_frames;
	}
	
	/**
	*	@fn framePollBegin()
	*	@brief Start polling the time it takes for the frame.
	*	@remarks Place this at the beginning of the frame loop.
	*/
	public void framePollBegin() {
		if( curSample == sampleRate ) {
			last_t = System.currentTimeMillis();
		}
	}
	
	/**
	*	@fn framePollEnd()
	*	@brief End the polling of a frame.
	*	@remarks Place this at the end of the frame, after code execution.
	*/
	public void framePollEnd() {
		nFrames++;
		if( curSample == sampleRate ) {
			cur_t = System.currentTimeMillis();
			frame_t = cur_t - last_t;
			// - Now we have t time for n frames.
			frameRate = (float)(1000F/(float)frame_t) * (float)sampleRate;
			if( pollTimer.update() >=1 )
				polledFrameTime = frameRate;

			avg_Rate = (double)( avg_Rate*(1-(1/(double)nFrames)) ) + ( (double)frameRate * (1/(double)nFrames) );	// - Weighted average.
			curSample=0;
		}
		else {
			curSample++;
		}
	}

	/**
	*	@fn getWaitMs()	
	*	@brief Get the amount of Ms that would be required to wait
	*/
	public long getWaitMs() {
		return 0;
	}
	
	/**
	*	@fn getFrameRateImm()
	*	@brief Returns the immediate frame rate at this point in time.
	*/
	public float getRateImm() {
		return frameRate;
	}
	
	/**
	*	@fn getRate()
	*	@brief Returns the Frame rate, polled every .5
	*/
	public float getRate() {
		return polledFrameTime;
	}
	
	/**
	*	@fn getFPS()
	*	@return The amount of frames per second.
	*/
	public double getFPS() {
		return 0.0;
	}
	/**
	*	@fn getAvgRate()
	*	@brief Returns the average frame rate since the start of the game, or the last call to resetAvgRate
	*/
	public double getAvgRate() {
		return avg_Rate;
	}
	
	/**
	*	@fn resetAvgRate()
	*	@brief Resets the average frame rate.	
	*/
	public void resetAvgRate() {
		avg_Rate=0F;
	}
	
	/**
	*	@fn getNumFrames()	
	*	@brief Returns the number of frames passed since system startup.
	*/
	public void getNumFrames() {
		
	}
	
	
}
