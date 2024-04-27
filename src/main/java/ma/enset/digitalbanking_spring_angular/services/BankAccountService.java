package ma.enset.digitalbanking_spring_angular.services;

import ma.enset.digitalbanking_spring_angular.dtos.*;
import ma.enset.digitalbanking_spring_angular.entities.BankAccount;
import ma.enset.digitalbanking_spring_angular.entities.Customer;
import ma.enset.digitalbanking_spring_angular.exception.BalanceInsiffucientException;
import ma.enset.digitalbanking_spring_angular.exception.BankAccountNotFoundException;
import ma.enset.digitalbanking_spring_angular.exception.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customer);

    CustomerDTO saveCustomerDTO(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId) throws CustomerNotFoundException;

    void saveCurrentAccount(double balance, double overdraft, Long customerId) throws CustomerNotFoundException;

    CurrentBankAccountDTO saveCurrentAccountDTO(double balance, double overdraft, Long customerId) throws CustomerNotFoundException;

    void saveSavingAccount(double balance, double rate, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingAccountDTO(double balance, double rate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    List<BankAccountDTO> listBankAccounts();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;
    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceInsiffucientException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
    void transfer(String fromAccountId, String toAccountId, double amount) throws BankAccountNotFoundException, BalanceInsiffucientException;

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);


    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String id, int page, int size) throws BankAccountNotFoundException;
}
