package io.github.ifris.loans.service.mapper;

import io.github.ifris.loans.domain.LoanAccount;
import io.github.ifris.loans.service.dto.LoanAccountDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity LoanAccount and its DTO LoanAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LoanAccountMapper extends EntityMapper<LoanAccountDTO, LoanAccount> {


    default LoanAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        LoanAccount loanAccount = new LoanAccount();
        loanAccount.setId(id);
        return loanAccount;
    }
}
