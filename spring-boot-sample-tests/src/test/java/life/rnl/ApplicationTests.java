package life.rnl;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import life.rnl.dto.VehicleView;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ApplicationTests extends BaseTest {

	@Autowired
	ApplicationContext appContext;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testRetrieve() throws Exception {
		this.mockMvc.perform(get("/retrieve/1")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("dateManufactured")));
		
		MvcResult result = this.mockMvc.perform(get("/retrieve/1")).andReturn();
		String json = result.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		VehicleView vehicleView = objectMapper.readValue(json, VehicleView.class);
		Calendar now = Calendar.getInstance();
		Assert.assertTrue(vehicleView.getDateManufactured().before(now));
		System.out.println(vehicleView.getDateManufactured());
		System.out.println(now);
	}
}