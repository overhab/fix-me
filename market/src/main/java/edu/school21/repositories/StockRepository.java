package edu.school21.repositories;

import edu.school21.models.Stock;

import java.util.List;

public interface StockRepository extends CrudRepository<Stock> {
	List<String> findAllWithLimit();
	Stock findByName(String name);
}
