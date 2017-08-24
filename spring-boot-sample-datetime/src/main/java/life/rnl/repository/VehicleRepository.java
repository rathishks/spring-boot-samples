package life.rnl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import life.rnl.domain.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

}
