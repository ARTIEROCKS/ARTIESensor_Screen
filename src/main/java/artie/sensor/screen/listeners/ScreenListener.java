package artie.sensor.screen.listeners;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.github.agomezmoron.multimedia.recorder.listener.VideoRecorderEventListener;
import com.github.agomezmoron.multimedia.recorder.listener.VideoRecorderEventObject;

import artie.sensor.common.dto.BufferedImageSerializable;
import artie.sensor.common.dto.SensorObject;
import artie.sensor.common.enums.SensorObjectTypeEnum;

@Component
public class ScreenListener implements VideoRecorderEventListener {

	private List<SensorObject> screenCaptures;
	private Logger logger = LoggerFactory.getLogger(ScreenListener.class);
	
	
	@Override
	public void frameAdded(VideoRecorderEventObject args) {
		
		//If the screen captures are not null
		if(this.screenCaptures != null){
			BufferedImageSerializable bis = new BufferedImageSerializable(args.getScreenCapture().getSource());
			String strBis = "";
			
			try {
				strBis = bis.imageSerialization();
			} catch (IOException e) {
				this.logger.error(e.getMessage());
			}
			
			SensorObject so = new SensorObject(new Date(), strBis, SensorObjectTypeEnum.IMAGE, "screen");
			this.screenCaptures.add(so);
		}
	}
	
	/**
	 * Parameterized constructor
	 * @param screenCaptures
	 */
	public ScreenListener(List<SensorObject> screenCaptures){
		this.screenCaptures = screenCaptures;
	}

}
