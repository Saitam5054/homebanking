package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return args -> {

			Client client1 = new Client("Melba","Morel","melba@mindhub.com");
			Client client2 = new Client("Philip", "Fry", "Fry@yahoo.com");

			clientRepository.save(client1);
			clientRepository.save(client2);

			Account account1 = new Account("VIN001", LocalDate.now(), 5000D);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1) ,7500D);
			Account account3 = new Account(("VIN003"), LocalDate.now(), 5000D);

			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 5000D, "Credit 1", LocalDate.now());
			Transaction transaction2 = new Transaction(TransactionType.DEBIT , 10000D, "Debit 1", LocalDate.now());

			account1.addTransaction(transaction1);
			account3.addTransaction(transaction2);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
		};
	}
}
