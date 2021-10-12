package fr.polytech.microservices.MicroServices;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileController {
	
	private final AtomicLong counter = new AtomicLong();
	private final Map<Long, Profile> profiles = new HashMap<>();
	private final Set<String> emails = new HashSet<>();
	private Logger logger = LoggerFactory.getLogger(ProfileController.class);
	
	@GetMapping("/PS/profiles")
	@CrossOrigin
	public Collection<Profile> profiles(){
		return profiles.values();
	}

	@GetMapping("/PS/profiles/{id}/name")
	@CrossOrigin
	public String profile_get_name(@PathVariable(value = "id") Long id){
		logger.trace("GET /PS/profiles");
		if(!profiles.containsKey(id)) {
			throw new ProfileNotFoundException(id);
		}
		return profiles.get(id).getName();
	}

	@GetMapping("/PS/profiles/{id}/email")
	@CrossOrigin
	public String profile_get_email(@PathVariable(value = "id") Long id){
		logger.trace("GET /PS/email");
		if(!profiles.containsKey(id)) {
			throw new ProfileNotFoundException(id);
		}
		return profiles.get(id).getEmail();
	}

	@PutMapping("/PS/profiles")
	@CrossOrigin
	public Profile profiles_put(@RequestBody @Valid Profile profile) {
		logger.trace("PUT /PS/profiles");
		if (emails.contains(profile.getEmail()))
			throw new EmailInUseException(profile.getEmail());
		String email = profile.getEmail();
		long new_id = counter.incrementAndGet();
		profile.setId(new_id);
		profiles.put(new_id, profile);
		emails.add(email);
		logger.info(String.format("Profile created: [%d] %s.",
				new_id, profile.getEmail()));
		return profile;
	}
	
	@DeleteMapping("/PS/profiles/{id}")
	@CrossOrigin
	public void profiles_delete(@PathVariable(value = "id") Long id) {
		logger.trace(String.format("DELETE /PS/profiles/%d", id));
		Profile profile = profiles.get(id);
		emails.remove(profile.getEmail());
		logger.trace(String.format("Deleted profile [%d] %s.", profile.getId(), profile.getName()));
		profiles.remove(id);
	}

	@PutMapping("/PS/profiles/{id}/name")
	@CrossOrigin
	public void update_name(@PathVariable(value="id")Long id, @RequestBody @Valid String name) {
		Profile profile = profiles.get(id);
		profile.setName(name);
	}

	@PutMapping("/PS/profiles/{id}/email")
	@CrossOrigin
	public void update_email(@PathVariable(value="id")Long id, @RequestBody @Valid String email) {
		Profile profile = profiles.get(id);
		profile.setEmail(email);
	}
}
