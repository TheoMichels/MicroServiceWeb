package fr.polytech.authentification.authentifDemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.polytech.authentification.authentifDemo.Model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode=DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthentifDemoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void putUserShouldSucceed() throws Exception {

		User user = new User();
		user.setId(1L);
		ObjectMapper objectMapper = new ObjectMapper();
		String profile_json = objectMapper.writeValueAsString(user);
		this.mockMvc.perform(put("/AS/users")
						.content(profile_json)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void putUserShouldReturnConflict() throws Exception {

		User user = new User();
		user.setId(1L);
		ObjectMapper objectMapper = new ObjectMapper();
		String profile_json = objectMapper.writeValueAsString(user);

		this.mockMvc.perform(put("/AS/users")
						.content(profile_json)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk());

		this.mockMvc.perform(put("/AS/users")
						.content(profile_json)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isConflict());
	}

	@Test
	public void userExistsShouldSucceed() throws Exception {

		User user = new User();
		user.setId(1L);
		ObjectMapper objectMapper = new ObjectMapper();
		String profile_json = objectMapper.writeValueAsString(user);

		this.mockMvc.perform(put("/AS/users")
						.content(profile_json)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/AS/users/"+user.getId())
						.content(profile_json)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void userExistsShouldFail() throws Exception {

		User user = new User();
		user.setId(1L);
		ObjectMapper objectMapper = new ObjectMapper();
		String profile_json = objectMapper.writeValueAsString(user);

		this.mockMvc.perform(get("/AS/users/"+user.getId())
						.content(profile_json)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isNotFound());
	}
}
