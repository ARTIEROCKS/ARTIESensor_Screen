package artie.sensor.screenwebcam.listeners;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.github.agomezmoron.multimedia.recorder.listener.VideoRecorderEventListener;
import com.github.agomezmoron.multimedia.recorder.listener.VideoRecorderEventObject;

import artie.sensor.common.dto.SensorObject;

@Component
public class ScreenListener implements VideoRecorderEventListener {

	private List<SensorObject> screenCaptures;
	
	@Override
	public void frameAdded(VideoRecorderEventObject args) {
		
		//If the screen captures are not null
		if(this.screenCaptures != null){
			SensorObject so = new SensorObject(new Date(), args.getScreenCapture());
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
