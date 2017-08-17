package life.rnl.migration.destination.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import life.rnl.migration.destination.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

}