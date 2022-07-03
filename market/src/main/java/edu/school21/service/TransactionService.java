package edu.school21.service;

import edu.school21.models.FixMessage;
import edu.school21.models.Transaction;

public interface TransactionService {
	Transaction prepareTransaction(FixMessage fixMessage, String status);
	void createTransaction(Transaction transaction);
	void updateTransaction(Transaction transaction);
	void deleteTransaction(Long id);
}
