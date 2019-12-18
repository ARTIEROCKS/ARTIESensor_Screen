package artie.sensor.screen.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import artie.sensor.common.dto.SensorObject;
import artie.sensor.common.interfaces.ArtieClientSensor;
import artie.sensor.screen.services.ScreenService;

@Controller
public class ScreenController implements ArtieClientSensor {
	
	@Autowired
	private ScreenService screenService;

	@GetMapping("/artie/sensor/screen/getAuthor")
	@ResponseBody
	public String getAuthor() {
		return this.screenService.getAuthor();
	}
	
	@GetMapping("/artie/sensor/screen/getName")
	@ResponseBody
	public String getName() {
		return this.screenService.getName();
	}

	@GetMapping("/artie/sensor/screen/getConfiguration")
	@ResponseBody
	public Map<String, String> getConfiguration() {
		return this.screenService.getConfiguration();
	}

	@GetMapping("/artie/sensor/screen/getSensorData")
	@ResponseBody
	public List<SensorObject> getSensorData() {
		return this.screenService.getSensorData();
	}

	@GetMapping("/artie/sensor/screen/getVersion")
	@ResponseBody
	public String getVersion() {
		return this.screenService.getVersion();
	}

	@PostMapping(path = "/artie/sensor/screen/configuration", consumes = "application/json")
	public void setConfiguration(@RequestBody Map<String, String> configuration) {
		this.setConfiguration(configuration);
	}

	@GetMapping("/artie/sensor/screen/start")
	@ResponseBody
	public void start() {
		this.screenService.start();		
	}

	@GetMapping("/artie/sensor/screen/stop")
	@ResponseBody
	public void stop() {
		this.screenService.stop();		
	}

}
