package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;

public interface AccountService {

    List<AccountDTO> getAccountsDTO();

    AccountDTO getAccountDTOById(Long id);

    void saveAccount(Account account);
}
