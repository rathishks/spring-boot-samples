package life.rnl.transformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import life.rnl.domain.Vehicle;
import life.rnl.dto.VehicleView;
import life.rnl.repository.VehicleRepository;

@Component
public class VehicleTransformer implements Transformer<VehicleView, Vehicle> {

	private VehicleRepository vehicleRepository;

	@Autowired
	public VehicleTransformer(VehicleRepository vehicleRepository) {
		this.vehicleRepository = vehicleRepository;
	}
	
	public VehicleTransformer() {
	}

	@Override
	public Vehicle compose(VehicleView item) {
		Vehicle vehicle = null;

		Long id = item.getId();
		if (id != null) {
			vehicle = vehicleRepository.findOne(id);
		} else {
			vehicle = new Vehicle();
		}

		vehicle.setDateManufactured(item.getDateManufactured());
		vehicle.setDateSold(item.getDateSold());

		return vehicle;
	}

	@Override
	public VehicleView decompose(Vehicle item) {
		VehicleView vehicleView = new VehicleView();

		vehicleView.setId(item.getId());
		vehicleView.setDateManufactured(item.getDateManufactured());
		vehicleView.setDateSold(item.getDateSold());

		return vehicleView;
	}
}