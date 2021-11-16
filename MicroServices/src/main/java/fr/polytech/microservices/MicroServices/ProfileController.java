package fr.polytech.microservices.MicroServices;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class ProfileController {
	
	private final AtomicLong counter = new AtomicLong();
	private final Map<Long, Profile> profiles = new HashMap<>();
	private final Set<String> emails = new HashSet<>();
	private Logger logger = LoggerFactory.getLogger(ProfileController.class);

	@Value("$service.authentication")
	private String auth_service_url;
	
	@GetMapping("/PS/profiles")
	@CrossOrigin
	public Collection<Profile> profiles(){
		return profiles.values();
	}

	@GetMapping("/PS/profiles/{id}/name")
	@CrossOrigin
	public String profile_get_name(@PathVariable(value = "id") Long id) {
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

		AuthServiceUser auth_service_user = new AuthServiceUser(new_id);
		RestTemplate restTemplate = new RestTemplate();
		Long check_id = restTemplate.postForObject(
				auth_service_url+"/AS/users",
				auth_service_user,
				Long.class);

		if(!check_id.equals(new_id)) throw new RuntimeException();

		profiles.put(new_id, profile);
		emails.add(email);
		logger.info(String.format("Profile created: [%d] %s.",
				new_id, profile.getEmail()));
		return profile;
	}
	
	@DeleteMapping("/PS/profiles/{id}")
	@CrossOrigin
	public void profiles_delete(@PathVariable(value = "id") Long id, @RequestHeader(value = "X-token") String token) {
		logger.trace(String.format("DELETE /PS/profiles/%d", id));

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Token", token);
		HttpEntity<String> entity = new HttpEntity<String>("", headers);

		ResponseEntity<Long> response = restTemplate.exchange(auth_service_url+"/AS/token", HttpMethod.GET, entity, Long.class);
		Long token_user = response.getBody();
		if(!Objects.equals(token_user, id)) throw new RuntimeException();

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
