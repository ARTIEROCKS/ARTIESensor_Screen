package artie.sensor.screen.services;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.agomezmoron.multimedia.recorder.VideoRecorder;
import com.github.agomezmoron.multimedia.recorder.configuration.VideoRecorderConfiguration;

import artie.sensor.common.dto.SensorObject;
import artie.sensor.screen.listeners.ScreenListener;


@Service
public class ScreenService {
	
	private List<SensorObject> screenCaptures = new ArrayList<SensorObject>();
	private boolean serviceStarted = false;
	
	//Properties
	public List<SensorObject> getScreenCaptures() {
		return screenCaptures;
	}
	public void setScreenCaptures(List<SensorObject> screenCaptures) {
		this.screenCaptures = screenCaptures;
	}
	
	/**
	 * Function to start the screen recording
	 */
	public void start(int fps, String fileName, boolean writeVideoLocal){
		
		if(!serviceStarted){
			VideoRecorderConfiguration.setCaptureInterval(fps);
			
			//If we want to write in the disk a local video
			if(writeVideoLocal){
				VideoRecorderConfiguration.setVideoDirectory(new File(fileName + ".mov"));
			}
			
			//Adding the listener to write the screen captures in memory
			VideoRecorder.addVideoRecorderEventListener(new ScreenListener(this.screenCaptures));
			VideoRecorder.start(fileName + ".mov",writeVideoLocal);
			this.serviceStarted = true;
		}
	}
	

	/**
	 * Function to stop the screen recording
	 */
	public void stop(){
		
		//Stops the video recording
		try {
			VideoRecorder.stop();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		finally{
			this.serviceStarted = false;
		}
	}
	
	/**
     * Function to clear all the screen captures and free the memory
     */
    public void clearScreens(){
    	this.screenCaptures.clear();
    }
}
