package fr.polytech.microservices.MicroServices;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode=DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MicroServicesApplicationTests {
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@Test
//	public void getProfilesShouldReturnEmptyArray() throws Exception {
//		this.mockMvc.perform(get("/PS/profiles"))
//				.andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(content().json("[]"));
//	}
//
//	@Test
//	public void putProfileShouldSucceed() throws Exception {
//
//			Profile profile = new Profile(1, "Hans", "test@example.com", "Test");
//			ObjectMapper objectMapper = new ObjectMapper();
//			String profile_json = objectMapper.writeValueAsString(profile);
//				this.mockMvc.perform(put("/PS/profiles")
//								.content(profile_json)
//								.contentType(MediaType.APPLICATION_JSON)
//						)
//						.andDo(print())
//						.andExpect(status().isOk());
//	}
//
//	@Test
//	public void putProfileShouldReturnConflictError() throws Exception {
//
//		Profile profile = new Profile(1, "Hans", "test@example.com", "Test");
//		ObjectMapper objectMapper = new ObjectMapper();
//		String profile_json = objectMapper.writeValueAsString(profile);
//		this.mockMvc.perform(put("/PS/profiles")
//						.content(profile_json)
//						.contentType(MediaType.APPLICATION_JSON)
//				)
//				.andDo(print())
//				.andExpect(status().isOk());
//		this.mockMvc.perform(put("/PS/profiles")
//						.content(profile_json)
//						.contentType(MediaType.APPLICATION_JSON)
//				)
//				.andDo(print())
//				.andExpect(status().isConflict());
//	}
//
//	@Test
//	public void putProfileShouldReturnNullValueError() throws Exception {
//
//		Profile profile = new Profile(1, "Hans", null, "Test");
//		ObjectMapper objectMapper = new ObjectMapper();
//		String profile_json = objectMapper.writeValueAsString(profile);
//		this.mockMvc.perform(put("/PS/profiles")
//						.content(profile_json)
//						.contentType(MediaType.APPLICATION_JSON)
//				)
//				.andDo(print())
//				.andExpect(status().isBadRequest());
//	}
//
//	@Test
//	public void putProfileShouldReturnSyntaxError() throws Exception {
//
//		Profile profile = new Profile(1, "Hans", "theo@hotmail.fr", "Test");
//		ObjectMapper objectMapper = new ObjectMapper();
//		String profile_json = objectMapper.writeValueAsString(profile).replaceAll("}", ":;:");
//		this.mockMvc.perform(put("/PS/profiles")
//						.content(profile_json)
//						.contentType(MediaType.APPLICATION_JSON)
//				)
//				.andDo(print())
//				.andExpect(status().isBadRequest());
//	}
}