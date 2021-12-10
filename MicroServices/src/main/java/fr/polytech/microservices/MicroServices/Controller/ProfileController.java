package fr.polytech.microservices.MicroServices.Controller;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;

import fr.polytech.microservices.MicroServices.Controller.Exceptions.EmailInUseException;
import fr.polytech.microservices.MicroServices.Controller.Exceptions.InvalidTokenException;
import fr.polytech.microservices.MicroServices.Controller.Exceptions.ProfileNotFoundException;
import fr.polytech.microservices.MicroServices.Model.AuthServiceUser;
import fr.polytech.microservices.MicroServices.Model.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
public class ProfileController {
	
	private final AtomicLong counter = new AtomicLong();
	private final Map<Long, Profile> profiles = new HashMap<>();
	private final Set<String> emails = new HashSet<>();
	private Logger logger = LoggerFactory.getLogger(ProfileController.class);

	private final RestTemplate restTemplate;

	public ProfileController(RestTemplateBuilder restTemplateBuilder) {
		restTemplate = restTemplateBuilder.build();
	}

//	@Autowired
//	private KafkaTemplate<String, String> template;
//
//	@Bean
//	public NewTopic topic() {
//		return TopicBuilder.name("profile-changes")
//				.partitions(1)
//				.build();
//	}
//
//	@KafkaListener(id = "profile-listener", topics = "profile-changes")
//	public void listen(String in) {
//		logger.info(in);
//	}
//
//	private void send_message_for_profile(Long profile_id, String message) {
//		template.send(
//				"profile-changes",
//				profile_id.toString(), message);
//	}

	@Value("${service.authentication}")
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
		logger.info(auth_service_url+"/AS/users");
		restTemplate.put(
				auth_service_url+"/AS/users",
				auth_service_user);

		profiles.put(new_id, profile);
		emails.add(email);

		return profile;
	}
	
	@DeleteMapping("/PS/profiles/{id}")
	@CrossOrigin
	public void profiles_delete(@PathVariable(value = "id") Long id, @RequestHeader(value = "X-token") String token) {
		logger.trace(String.format("DELETE /PS/profiles/%d", id));

		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Token", token);
		HttpEntity<String> entity = new HttpEntity<String>("", headers);

		ResponseEntity<Long> response = restTemplate.exchange(
				auth_service_url+"/AS/token",
				HttpMethod.GET, entity,
				Long.class
		);

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

	@PostMapping("/PS/login")
	@CrossOrigin
	public String login(@RequestParam(value = "email") String email,@RequestBody String password) {

		if (!emails.contains(email)) throw new RuntimeException();

		String token = null;

		for(Profile p : profiles.values()) {
			if (p.getEmail().equals(email)) {
				token = restTemplate.postForObject(
						auth_service_url+"/AS/users/"+p.getId()+"/token",
						password,
						String.class);
			}
		}
		if (token == null) throw new RuntimeException("Cannot find a user with this email");
		else return token;
	}

	//Throws an exception if the token is not valid or for a different user
	public void checkTokenAgainstUser(String token, Long user_id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Token", token);
		HttpEntity<String> entity = new HttpEntity<String>("", headers);

		try {
			ResponseEntity<Long> response = restTemplate.exchange(
					auth_service_url + "/AS/token",
					HttpMethod.GET, entity,
					Long.class
			);
			Long token_user = response.getBody();
			if (Objects.equals(token_user, user_id)) throw new InvalidTokenException(user_id, token);
		} catch (HttpClientErrorException.Unauthorized ex) {
			throw new InvalidTokenException(user_id, token);
		}
	}
}
