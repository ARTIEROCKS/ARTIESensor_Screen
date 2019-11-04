package artie.sensor.screenwebcam.services;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.agomezmoron.multimedia.recorder.VideoRecorder;
import com.github.agomezmoron.multimedia.recorder.configuration.VideoRecorderConfiguration;

import artie.sensor.common.dto.SensorObject;
import artie.sensor.screenwebcam.ScreenWebcamSensor;
import artie.sensor.screenwebcam.enums.ConfigurationEnum;
import artie.sensor.screenwebcam.listeners.ScreenListener;


@Service
public class ScreenService {
	
	@Autowired
	private ScreenWebcamSensor screenWebcamSensor;
	private List<SensorObject> screenCaptures = new ArrayList<SensorObject>();
	
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
	public void start(){
		
		//Setting the video configuration
		int fps = Integer.parseInt(this.screenWebcamSensor.getConfiguration().get(ConfigurationEnum.SCREEN_FPS.toString()));
		String fileName = this.screenWebcamSensor.getConfiguration().get(ConfigurationEnum.SCREEN_FILE_NAME.toString());
		boolean writeVideoLocal = Boolean.parseBoolean(this.screenWebcamSensor.getConfiguration().get(ConfigurationEnum.SCREEN_WRITE_VIDEO_LOCAL.toString()));
		
		VideoRecorderConfiguration.setCaptureInterval(fps);
		
		//If we want to write in the disk a local video
		if(writeVideoLocal){
			VideoRecorderConfiguration.setVideoDirectory(new File(fileName + ".mov"));
		}
		
		//Adding the listener to write the screen captures in memory
		VideoRecorder.addVideoRecorderEventListener(new ScreenListener(this.screenCaptures));
		VideoRecorder.start(fileName + ".mov",writeVideoLocal);
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
	}
	
	/**
     * Function to clear all the screen captures and free the memory
     */
    public void clearScreens(){
    	this.screenCaptures.clear();
    }
}
