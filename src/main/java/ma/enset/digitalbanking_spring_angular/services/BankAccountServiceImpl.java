package ma.enset.digitalbanking_spring_angular.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.digitalbanking_spring_angular.dtos.*;
import ma.enset.digitalbanking_spring_angular.entities.*;
import ma.enset.digitalbanking_spring_angular.entities.enums.AccountStatus;
import ma.enset.digitalbanking_spring_angular.entities.enums.OperationType;
import ma.enset.digitalbanking_spring_angular.exception.BalanceInsiffucientException;
import ma.enset.digitalbanking_spring_angular.exception.BankAccountNotFoundException;
import ma.enset.digitalbanking_spring_angular.exception.CustomerNotFoundException;
import ma.enset.digitalbanking_spring_angular.mappers.BankAccountMapperImpl;
import ma.enset.digitalbanking_spring_angular.repositories.AccountOperationRepository;
import ma.enset.digitalbanking_spring_angular.repositories.BankAccountRepository;
import ma.enset.digitalbanking_spring_angular.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl bankAccountMapper;

    @Override
    public Customer saveCustomer(Customer customer) {
        return null;
    }

    @Override
    public CustomerDTO saveCustomerDTO(CustomerDTO customerDTO) {
        log.info("Saving customer");
        Customer customer = bankAccountMapper.toCustomerDTO(customerDTO);
        Customer customerEntity = customerRepository.save(customer);
        return bankAccountMapper.fromCustomer(customerEntity);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer customer = bankAccountMapper.toCustomerDTO(customerDTO);
        Customer customerEntity = customerRepository.save(customer);
        return bankAccountMapper.fromCustomer(customerEntity);
    }

    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customerRepository.delete(customer);
    }

    @Override
    public void saveCurrentAccount(double balance, double overdraft, Long customerId) throws CustomerNotFoundException {
        Customer client = customerRepository.findById(customerId).orElse(null);
        if(client == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount currentAccount = CurrentAccount.builder()
                .id(UUID.randomUUID().toString())
                .balance(balance)
                .customer(client)
                .status(AccountStatus.CREATED)
                .creationDate(new Date())
                .overdraft(overdraft)
                .build();
        bankAccountRepository.save(currentAccount);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentAccountDTO(double balance, double overdraft, Long customerId) throws CustomerNotFoundException {
        Customer client = customerRepository.findById(customerId).orElse(null);
        if(client == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount currentAccount = CurrentAccount.builder()
                .id(UUID.randomUUID().toString())
                .balance(balance)
                .customer(client)
                .status(AccountStatus.CREATED)
                .creationDate(new Date())
                .overdraft(overdraft)
                .build();
        bankAccountRepository.save(currentAccount);
        return bankAccountMapper.fromCurrentBankAccount(currentAccount);
    }

    @Override
    public void saveSavingAccount(double balance, double rate, Long customerId) throws CustomerNotFoundException {

    }

    @Override
    public SavingBankAccountDTO saveSavingAccountDTO(double balance, double rate, Long customerId) throws CustomerNotFoundException {
        Customer client = customerRepository.findById(customerId).orElse(null);
        if(client == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingAccount = SavingAccount.builder()
                .id(UUID.randomUUID().toString())
                .balance(balance)
                .customer(client)
                .status(AccountStatus.CREATED)
                .creationDate(new Date())
                .rate(rate)
                .build();
        bankAccountRepository.save(savingAccount);
        return bankAccountMapper.fromSavingBankAccount(savingAccount);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream().map(c->bankAccountMapper.fromCustomer(c)).collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public List<BankAccountDTO> listBankAccounts() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if(bankAccount instanceof CurrentAccount) {
                return bankAccountMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
            } else if(bankAccount instanceof SavingAccount) {
                return bankAccountMapper.fromSavingBankAccount((SavingAccount) bankAccount);
            }
            return null;
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Account not found"));
        if(bankAccount instanceof CurrentAccount) {
//            return bankAccountMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
        } else if(bankAccount instanceof SavingAccount) {
            return bankAccountMapper.fromSavingBankAccount((SavingAccount) bankAccount);
        }
        return null;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceInsiffucientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Account not found"));
        if(bankAccount.getBalance() < amount) {
            throw new BalanceInsiffucientException("Insufficient balance");
        }
        accountOperationRepository.save(AccountOperation.builder()
                .amount(amount)
                .bankAccount(bankAccount)
                .description(description)
                .operationDate(new Date())
                .operationType(OperationType.DEBIT)
                .build());
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundException("Account not found"));
        accountOperationRepository.save(AccountOperation.builder()
                .amount(amount)
                .bankAccount(bankAccount)
                .description(description)
                .operationDate(new Date())
                .operationType(OperationType.CREDIT)
                .build());
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String fromAccountId, String toAccountId, double amount) throws BankAccountNotFoundException, BalanceInsiffucientException {
        debit(fromAccountId,amount,"Transfer to "+toAccountId);
        credit(toAccountId,amount,"Transfer from "+fromAccountId);
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return bankAccountMapper.fromCustomer(customer);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(accountOperation -> bankAccountMapper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String id, int page, int size) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElse(null);
        if (bankAccount == null) {
            throw new BankAccountNotFoundException("Account not found");
        }

        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(id, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream()
                .map(bankAccountMapper::fromAccountOperation)
                .collect(Collectors.toList());

        accountHistoryDTO.setAccountOperations(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setAccountType(bankAccount.getClass().getSimpleName());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        accountHistoryDTO.setPageSize(size);
        return accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        return customerRepository.searchCustomer(keyword).stream().map(bankAccountMapper::fromCustomer).collect(Collectors.toList());
    }

}
