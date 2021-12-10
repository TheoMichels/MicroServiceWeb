package fr.polytech.microservices.MicroServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.polytech.microservices.MicroServices.Model.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
@DirtiesContext(classMode=DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MicroServicesApplicationTests {

	@Autowired
	private MockRestServiceServer server;

	@Autowired
	private MockMvc mockMvc;

	@Value("${service.authentication}")
	private String auth_service_url;

	@Test
	public void getProfilesShouldReturnEmptyArray() throws Exception {
		this.mockMvc.perform(get("/PS/profiles"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json("[]"));
	}

	@Test
	public void putProfileShouldSucceed() throws Exception {

		Profile profile = new Profile(1, "Hans", "test@example.com", "Test");
		ObjectMapper objectMapper = new ObjectMapper();
		String profile_json = objectMapper.writeValueAsString(profile);

		server.expect(once(),
						requestTo(auth_service_url+"/AS/users")).andExpect(method(HttpMethod.PUT))
				.andRespond(withSuccess("1",
						MediaType.APPLICATION_JSON));

		this.mockMvc.perform(put("/PS/profiles")
								.content(profile_json)
								.contentType(MediaType.APPLICATION_JSON)
						)
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void putProfileShouldReturnConflictError() throws Exception {

		Profile profile = new Profile(1, "Hans", "test@example.com", "Test");
		ObjectMapper objectMapper = new ObjectMapper();
		String profile_json = objectMapper.writeValueAsString(profile);

		server.expect(once(),
						requestTo(auth_service_url+"/AS/users")).andExpect(method(HttpMethod.PUT))
				.andRespond(withSuccess("1",
						MediaType.APPLICATION_JSON));

		this.mockMvc.perform(put("/PS/profiles")
						.content(profile_json)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isOk());
		this.mockMvc.perform(put("/PS/profiles")
						.content(profile_json)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isConflict());
	}

	@Test
	public void putProfileShouldReturnNullValueError() throws Exception {

		Profile profile = new Profile(1, "Hans", null, "Test");
		ObjectMapper objectMapper = new ObjectMapper();
		String profile_json = objectMapper.writeValueAsString(profile);

		this.mockMvc.perform(put("/PS/profiles")
						.content(profile_json)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void putProfileShouldReturnSyntaxError() throws Exception {

		Profile profile = new Profile(1, "Hans", "theo@hotmail.fr", "Test");
		ObjectMapper objectMapper = new ObjectMapper();
		String profile_json = objectMapper.writeValueAsString(profile).replaceAll("}", ":;:");
		this.mockMvc.perform(put("/PS/profiles")
						.content(profile_json)
						.contentType(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(status().isBadRequest());
	}
}