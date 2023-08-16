package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository) {
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

			Loan loan1 = new Loan("Hipotecario", 500000D, Arrays.asList(12, 24, 36, 48, 60));
			Loan loan2 = new Loan("Personal", 100000D, Arrays.asList(6,12,24));
			Loan loan3 = new Loan("Automotriz", 300000D, Arrays.asList(6,12,24,36));

			/*client1.addLoan(loan1);
			client1.addLoan(loan2);
			client2.addLoan(loan3);*/

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan(400000D, 60, client1, loan1);
			ClientLoan clientLoan2 = new ClientLoan(50000D, 12, client1, loan2);

			ClientLoan clientLoan3 = new ClientLoan(100000D, 24, client2, loan2);
			ClientLoan clientLoan4 = new ClientLoan(200000D, 36, client2, loan3);

			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);

			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);

			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);
		};
	}
}
