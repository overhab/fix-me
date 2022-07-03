package edu.school21.service;

import edu.school21.models.FixMessage;
import edu.school21.models.Transaction;
import edu.school21.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public Transaction prepareTransaction(FixMessage fixMessage, String status) {
		return new Transaction(Integer.parseInt(fixMessage.getTargetId()), Integer.parseInt(fixMessage.getSenderId()),
				fixMessage.getInstrument(), fixMessage.getQuantity(), fixMessage.getType(), status.toUpperCase(Locale.ROOT),
				fixMessage.getOrderId());
	}

	@Override
	public void createTransaction(Transaction transaction) {
		transactionRepository.save(transaction);
	}

	@Override
	public void updateTransaction(Transaction transaction) {
		transactionRepository.update(transaction);
	}

	@Override
	public void deleteTransaction(Long id) {
		transactionRepository.delete(id);
	}
}
