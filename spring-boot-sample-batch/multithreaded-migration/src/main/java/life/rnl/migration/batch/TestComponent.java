package life.rnl.migration.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import life.rnl.migration.destination.repository.PartRepository;

@Component
public class TestComponent {
	@Autowired
	private PartRepository partRepository;

	public PartRepository getPartRepository() {
		return partRepository;
	}
}
