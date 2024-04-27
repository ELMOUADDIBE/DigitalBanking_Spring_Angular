package ma.enset.digitalbanking_spring_angular.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.enset.digitalbanking_spring_angular.entities.enums.AccountStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CurrentBankAccountDTO extends BankAccountDTO{
    private String id;
    private double balance;
    private Date creationDate;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double overdraft;
}
