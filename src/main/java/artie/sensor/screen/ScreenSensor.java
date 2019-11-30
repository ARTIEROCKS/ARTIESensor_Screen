package artie.sensor.screen;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import artie.sensor.common.dto.SensorObject;
import artie.sensor.common.services.ArtieClientSensorImpl;
import artie.sensor.screen.enums.ConfigurationEnum;
import artie.sensor.screen.services.ScreenService;

@Service
public class ScreenSensor extends ArtieClientSensorImpl{

	//Configuration
	@Value("${artie.sensor.screen.fps}")
	private String screenFps;
	@Value("${artie.sensor.screen.write-video-local}")
	private String screenWriteVideoLocal;
	@Value("${artie.sensor.screen.filename}")
	private String screenFileName;
	@Value("${artie.sensor.screen.active}")
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
		this.name = "Screen Sensor";
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
			this.screenService.start(Integer.parseInt(this.configuration.get(ConfigurationEnum.SCREEN_FPS.toString())),
									 this.configuration.get(ConfigurationEnum.SCREEN_FILE_NAME.toString()),
									 Boolean.parseBoolean(this.configuration.get(ConfigurationEnum.SCREEN_WRITE_VIDEO_LOCAL.toString()))
									 );
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
	
	/**
	 * Getting the sensor data from the listeners
	 * @return
	 */
	public List<SensorObject> getSensorData(){
		
		//Cleaning all the information stored
		this.sensorData.clear();
				
		//Getting the information from the keyboard listener
		if(this.screenServiceIsActive){
			this.screenService.getScreenCaptures().forEach(screen->this.sensorData.add(screen));
			this.screenService.clearScreens();
		}
		
		return this.sensorData;
	}
}
