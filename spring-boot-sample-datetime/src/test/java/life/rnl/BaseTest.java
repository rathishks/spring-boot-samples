package life.rnl;

import java.util.Calendar;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import life.rnl.domain.Vehicle;
import life.rnl.repository.VehicleRepository;

public class BaseTest {
	@Autowired
	VehicleRepository vehicleRepository;
	
	Vehicle vehicle;
	
	ObjectMapper objectMapper = new ObjectMapper();

	@Before
	public void setup() {
		vehicle = new Vehicle();
		vehicle.setDateManufactured(Calendar.getInstance());
		vehicle.setDateSold(Calendar.getInstance());
		vehicleRepository.save(vehicle);
	}
}
