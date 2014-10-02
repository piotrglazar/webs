package com.piotrglazar.webs;

import com.piotrglazar.webs.dto.LoanDto;
import com.piotrglazar.webs.mvc.forms.LoanCreationForm;

import java.util.List;

public interface LoanProvider {
    List<LoanDto> getAllActiveLoans(String username);

    List<LoanDto> getAllArchiveLoans(String username);

    void createLoan(LoanCreationForm loanCreationForm, String username);

    boolean postponeLoanIfUserIsItsOwner(String username, long loanId);
}
