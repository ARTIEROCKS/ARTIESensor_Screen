package artie.sensor.screenwebcam;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import artie.sensor.common.services.ArtieClientSensorImpl;
import artie.sensor.screenwebcam.enums.ConfigurationEnum;
import artie.sensor.screenwebcam.services.ScreenService;

@Service
public class ScreenWebcamSensor extends ArtieClientSensorImpl{

	//Configuration
	@Value("${artie.sensor.screenwebcam.screen.fps}")
	private String screenFps;
	@Value("${artie.sensor.screenwebcam.screen.write-video-local}")
	private String screenWriteVideoLocal;
	@Value("${artie.sensor.screenwebcam.screen.filename}")
	private String screenFileName;
	@Value("${artie.sensor.screenwebcam.screen.active}")
	private String screenActive;
	
	//Services
	@Autowired
	private ScreenService screenService;
	
	//Attributes
	private boolean screenServiceIsActive = false;
	
	/**
	 * About the sensor information
	 */
	public void sensorInformation(){
		this.name = "Screen and Webcam Sensor";
		this.version = "0.0.1";
		this.author = "Luis-Eduardo Imbernón";
	}
	
	@PostConstruct
	public void init(){
		this.sensorInformation();
		
		//Initialize the configuration
		this.configuration.putIfAbsent(ConfigurationEnum.SCREEN_FPS.toString(),this.screenFps);
		this.configuration.putIfAbsent(ConfigurationEnum.SCREEN_WRITE_VIDEO_LOCAL.toString(),this.screenWriteVideoLocal);
		this.configuration.putIfAbsent(ConfigurationEnum.SCREEN_FILE_NAME.toString(),this.screenFileName);
		this.configuration.putIfAbsent(ConfigurationEnum.SCREEN_ACTIVE.toString(),this.screenActive);
		
		this.screenServiceIsActive = false;
	}
	
	@Override
	public void start() {
		
		//If the screen capture is enabled
		if(Boolean.parseBoolean(this.configuration.get(ConfigurationEnum.SCREEN_ACTIVE.toString()))){
			this.screenService.start();
			this.screenServiceIsActive = true;
		}
	}

	@Override
	public void stop() {
		//If the screen capture is active
		if(this.screenServiceIsActive){
			this.screenService.stop();
		}
	}
}
