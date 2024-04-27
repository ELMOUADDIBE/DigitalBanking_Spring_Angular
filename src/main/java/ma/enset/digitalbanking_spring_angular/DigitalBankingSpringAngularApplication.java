package ma.enset.digitalbanking_spring_angular;

import ma.enset.digitalbanking_spring_angular.dtos.BankAccountDTO;
import ma.enset.digitalbanking_spring_angular.dtos.CurrentBankAccountDTO;
import ma.enset.digitalbanking_spring_angular.dtos.CustomerDTO;
import ma.enset.digitalbanking_spring_angular.dtos.SavingBankAccountDTO;
import ma.enset.digitalbanking_spring_angular.entities.*;
import ma.enset.digitalbanking_spring_angular.entities.enums.AccountStatus;
import ma.enset.digitalbanking_spring_angular.entities.enums.OperationType;
import ma.enset.digitalbanking_spring_angular.exception.BalanceInsiffucientException;
import ma.enset.digitalbanking_spring_angular.exception.BankAccountNotFoundException;
import ma.enset.digitalbanking_spring_angular.exception.CustomerNotFoundException;
import ma.enset.digitalbanking_spring_angular.repositories.AccountOperationRepository;
import ma.enset.digitalbanking_spring_angular.repositories.BankAccountRepository;
import ma.enset.digitalbanking_spring_angular.repositories.CustomerRepository;
import ma.enset.digitalbanking_spring_angular.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingSpringAngularApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingSpringAngularApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("Zaid", "Hassan", "Yassine").forEach(name ->
                    bankAccountService.saveCustomerDTO(CustomerDTO.builder()
                            .name(name)
                            .email(name+"@gmail.com")
                            .build()
                    )
            );
            // Create bank accounts
            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentAccountDTO(Math.random()*9000, 500, customer.getId());
                    bankAccountService.saveSavingAccountDTO(Math.random()*9000, 5.5, customer.getId());

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });

            List<BankAccountDTO> bankAccounts = bankAccountService.listBankAccounts();
            for (BankAccountDTO bankAccount : bankAccounts) {
                for (int i = 0; i < 5; i++) {
                    String accountId;
                    if(bankAccount instanceof CurrentBankAccountDTO) {
                        accountId = ((CurrentBankAccountDTO) bankAccount).getId();
                    } else {
                        accountId = ((SavingBankAccountDTO) bankAccount).getId();
                    }
                    bankAccountService.credit(accountId, Math.random()*6000, "Initial credit");
                    bankAccountService.debit(accountId, Math.random()*600, "Initial debit");
                }
            }
        };
    }

//    @Bean
    CommandLineRunner start(CustomerRepository customerRepository, AccountOperationRepository accountOperationRepository, BankAccountRepository bankAccountRepository) {
        return args -> {
            // Create customers
            Stream.of("Zaid", "Hassan", "Yassine").forEach(name ->
                    customerRepository.save(Customer.builder()
                    .name(name)
                    .email(name+"@gmail.com")
                    .build()
            ));

            //Create bank accounts
            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount = CurrentAccount.builder()
                        .id(UUID.randomUUID().toString())
                        .customer(customer)
                        .balance(Math.random()*9000)
                        .creationDate(new Date())
                        .status(AccountStatus.CREATED)
                        .overdraft(500)
                        .build();
                bankAccountRepository.save(currentAccount);

                bankAccountRepository.save(SavingAccount.builder()
                        .id(UUID.randomUUID().toString())
                        .customer(customer)
                        .balance(Math.random()*9000)
                        .creationDate(new Date())
                        .status(AccountStatus.CREATED)
                        .rate(5.5)
                        .build()
                );
            });

            // Create account operations
            bankAccountRepository.findAll().forEach(bankAccount -> {
                for (int i = 0; i < 5; i++) {
                    accountOperationRepository.save(AccountOperation.builder()
                            .amount(Math.random()*6000)
                            .operationDate(new Date())
                            .operationType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT)
                            .bankAccount(bankAccount)
                            .build()
                    );
                }
            });

        };
    }
}