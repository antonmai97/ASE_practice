package edu.tum.ase.darkmode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DarkmodeApplication {

	private boolean darkmodestate = false;
	private long lastToggleTime = 0;
	private long toggle_Cooldown = 3000; // 3 second cooldown to prevent flickering 

	public static void main(String[] args) {
		SpringApplication.run(DarkmodeApplication.class, args);
	}
	
	@RequestMapping(path = "/dark-mode/toggle", method = RequestMethod.GET)
	public void toggleDarkMode() {
		long currentTime = System.currentTimeMillis(); //get currentTime

		//Check if cooldown is over yet
		if (currentTime - lastToggleTime < toggle_Cooldown){
			System.out.println("Toggle Cooldown is still active!"); 
			return;
		}

		lastToggleTime = currentTime;
		darkmodestate = !darkmodestate;
		System.out.println("Dark Mode got toggled. Darkmode: " + darkmodestate) ;		
	}

	@RequestMapping(path = "/dark-mode", method = RequestMethod.GET)
		public boolean getDarkModeState(){
			return darkmodestate;
		}
}
