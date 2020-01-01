package artie.sensor.screen.services;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.agomezmoron.multimedia.recorder.VideoRecorder;
import com.github.agomezmoron.multimedia.recorder.configuration.VideoRecorderConfiguration;

import artie.sensor.common.dto.SensorObject;
import artie.sensor.common.services.ArtieClientSensorImpl;
import artie.sensor.screen.enums.ConfigurationEnum;
import artie.sensor.screen.listeners.ScreenListener;

@Service
public class ScreenService extends ArtieClientSensorImpl{

	//Attributes
	private List<SensorObject> screenCaptures = new ArrayList<SensorObject>();
	private boolean serviceStarted = false;
	
	//Configuration
	@Value("${artie.sensor.screen.fps}")
	private String screenFps;
	@Value("${artie.sensor.screen.write-video-local}")
	private String screenWriteVideoLocal;
	@Value("${artie.sensor.screen.filename}")
	private String screenFileName;
	@Value("${artie.sensor.screen.active}")
	private String screenActive;
	
	@Value("${artie.sensor.keyboardmouse.name}")
	private String paramName;
	
	@Value("${artie.sensor.keyboardmouse.version}")
	private String paramVersion;
	
	@Value("${artie.sensor.keyboardmouse.author}")
	private String paramAuthor;
	
	/**
	 * About the sensor information
	 */
	public void sensorInformation(){
		this.name = this.paramName;
		this.version = this.paramVersion;
		this.author = this.paramVersion;
	}
	
	@PostConstruct
	public void init(){
		this.sensorInformation();
		
		//Initialize the configuration
		this.configuration.putIfAbsent(ConfigurationEnum.SCREEN_FPS.toString(),this.screenFps);
		this.configuration.putIfAbsent(ConfigurationEnum.SCREEN_WRITE_VIDEO_LOCAL.toString(),this.screenWriteVideoLocal);
		this.configuration.putIfAbsent(ConfigurationEnum.SCREEN_FILE_NAME.toString(),this.screenFileName);
		this.configuration.putIfAbsent(ConfigurationEnum.SCREEN_ACTIVE.toString(),this.screenActive);
		
	}
	
	@Override
	public void start() {
		
		System.setProperty("java.awt.headless", "false");
		boolean writeVideoLocal = Boolean.parseBoolean(this.configuration.get(ConfigurationEnum.SCREEN_WRITE_VIDEO_LOCAL.toString()));
		int fps = Integer.parseInt(this.configuration.get(ConfigurationEnum.SCREEN_FPS.toString()));
		String fileName = this.configuration.get(ConfigurationEnum.SCREEN_FILE_NAME.toString());
		
		//If the service has not started
		if(!serviceStarted){
			
			//Setting the capture interval
			VideoRecorderConfiguration.setCaptureInterval(fps);
			
			//If we want to write in the disk a local video
			if(writeVideoLocal){
				VideoRecorderConfiguration.setVideoDirectory(new File(fileName + ".mov"));
				
				//Adding the listener to write the screen captures in memory
				VideoRecorder.addVideoRecorderEventListener(new ScreenListener(this.screenCaptures));
				VideoRecorder.start(fileName + ".mov",writeVideoLocal);
				this.serviceStarted = true;
			}
			
		}
	}

	
	@Override
	public void stop() {
		
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
	 * Getting the sensor data from the listeners
	 * @return
	 */
	public List<SensorObject> getSensorData(){
		
		//Cleaning all the information stored
		this.sensorData.clear();
				
		//Getting the information from the screen
		int elements = this.screenCaptures.size();
		for(int i=0; i<elements; i++) {
			this.sensorData.add(this.screenCaptures.get(i));
		}
		this.screenCaptures.clear();
		
		return this.sensorData;
	}

}
