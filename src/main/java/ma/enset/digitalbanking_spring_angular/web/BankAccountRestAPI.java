package ma.enset.digitalbanking_spring_angular.web;

import ma.enset.digitalbanking_spring_angular.dtos.AccountHistoryDTO;
import ma.enset.digitalbanking_spring_angular.dtos.AccountOperationDTO;
import ma.enset.digitalbanking_spring_angular.dtos.BankAccountDTO;
import ma.enset.digitalbanking_spring_angular.dtos.SavingBankAccountDTO;
import ma.enset.digitalbanking_spring_angular.exception.BalanceInsiffucientException;
import ma.enset.digitalbanking_spring_angular.exception.BankAccountNotFoundException;
import ma.enset.digitalbanking_spring_angular.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BankAccountRestAPI {
    private BankAccountService bankAccountService;

    public BankAccountRestAPI(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{id}")
    public BankAccountDTO getBankAccount(@PathVariable String id) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(id);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listBankAccounts() {
        return bankAccountService.listBankAccounts();

    }
    @GetMapping("/accounts/{id}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String id) {
        return bankAccountService.accountHistory(id);
    }

    @GetMapping("/accounts/{id}/pageOperations")
    public AccountHistoryDTO getAccountHistory(@PathVariable String id, @RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name="size", defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(id, page, size);
    }

    //debit trasfere credit
    @PostMapping("/accounts/credit/{id}")
    public void credit(@PathVariable String id, @RequestParam double amount, @RequestParam String desc) throws BankAccountNotFoundException {
        bankAccountService.credit(id, amount, desc);
    }

    @PostMapping("/accounts/debit/{id}")
    public void debit(@PathVariable String id, @RequestParam double amount, @RequestParam String desc) throws BankAccountNotFoundException, BalanceInsiffucientException {
        bankAccountService.debit(id, amount, desc);
    }

    @PostMapping("/accounts/transfer")
    public void transfer(@RequestParam String from, @RequestParam String to, @RequestParam double amount) throws BankAccountNotFoundException, BalanceInsiffucientException {
        bankAccountService.transfer(from, to, amount);
    }

}
