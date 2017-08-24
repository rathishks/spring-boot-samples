package life.rnl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import life.rnl.domain.Vehicle;
import life.rnl.dto.VehicleView;
import life.rnl.repository.VehicleRepository;
import life.rnl.transformer.VehicleTransformer;

@RestController
public class VehicleController {
	@Autowired
	private VehicleRepository vehicleRepository;

	@PostMapping(path = "/save")
	public VehicleView save(@RequestBody VehicleView vehicleView, VehicleTransformer vehicleTransformer) {
		Vehicle vehicle = vehicleTransformer.compose(vehicleView);
		vehicle = vehicleRepository.save(vehicle);

		VehicleView view = vehicleTransformer.decompose(vehicle);
		return view;
	}

	@GetMapping(path = "/retrieve/{id}")
	public VehicleView retrieve(@PathVariable("id") String id, VehicleTransformer vehicleTransformer) {
		Vehicle vehicle = vehicleRepository.findOne(Long.valueOf(id));
		VehicleView vehicleView = vehicleTransformer.decompose(vehicle);
		return vehicleView;
	}
}