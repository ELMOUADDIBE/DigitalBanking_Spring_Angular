package ma.enset.digitalbanking_spring_angular.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.enset.digitalbanking_spring_angular.entities.AccountOperation;
import ma.enset.digitalbanking_spring_angular.entities.Customer;
import ma.enset.digitalbanking_spring_angular.entities.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class BankAccountDTO {
    private String type;
}
