package ma.enset.digitalbanking_spring_angular.repositories;

import ma.enset.digitalbanking_spring_angular.entities.BankAccount;
import ma.enset.digitalbanking_spring_angular.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String>{
}
