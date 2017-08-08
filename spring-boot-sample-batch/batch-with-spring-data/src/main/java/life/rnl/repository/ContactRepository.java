package life.rnl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import life.rnl.domain.Contact;

public interface ContactRepository extends JpaRepository<Contact, String> {

}
