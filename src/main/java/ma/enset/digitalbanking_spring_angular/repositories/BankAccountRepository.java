package ma.enset.digitalbanking_spring_angular.repositories;

import ma.enset.digitalbanking_spring_angular.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
