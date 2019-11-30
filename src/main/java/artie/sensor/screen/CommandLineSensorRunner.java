package artie.sensor.screen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineSensorRunner implements CommandLineRunner {

	@Autowired
	ScreenSensor screenSensor;
	
	@Override
	public void run(String... args) throws Exception {
		System.setProperty("java.awt.headless", "false");
		screenSensor.start();
	}

}
