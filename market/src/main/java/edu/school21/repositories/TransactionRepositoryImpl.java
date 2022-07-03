package edu.school21.repositories;

import edu.school21.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.List;

@Component
public class TransactionRepositoryImpl implements TransactionRepository {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private final RowMapper<Transaction> stockRowMapper = (ResultSet resultSet, int i) -> {
		return new Transaction(resultSet.getInt("broker_id"),
				resultSet.getInt("market_id"),
				resultSet.getString("instrument"),
				resultSet.getInt("quantity"),
				resultSet.getString("type"),
				resultSet.getString("status"),
				resultSet.getString("order_id"));
	};

	@Override
	public Transaction findById(Long id) {
		return jdbcTemplate.queryForObject("select * from transaction where id = :id",
				new MapSqlParameterSource("id", id), stockRowMapper);
	}

	@Override
	public List<Transaction> findAll() {
		return jdbcTemplate.query("select * from transaction", stockRowMapper);
	}

	@Override
	public void save(Transaction entity) {
		BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(entity);
		beanPropertySqlParameterSource.registerSqlType("status", Types.VARCHAR);
		jdbcTemplate.update("insert into transaction(broker_id, market_id, instrument, quantity, status, type, order_id) " +
						"values (:broker_id, :market_id, :instrument, :quantity, :status, :type, :order_id)",
				beanPropertySqlParameterSource);
	}

	@Override
	public void update(Transaction entity) {
		BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(entity);
		beanPropertySqlParameterSource.registerSqlType("status", Types.VARCHAR);
		jdbcTemplate.update("update transaction set broker_id = :broker_id, market_id = :market_id, instrument = :instrument," +
						"quantity = :quantity, status = :status, type = :type, order_id = :order_id where id = :id",
				beanPropertySqlParameterSource);
	}

	@Override
	public void delete(Long id) {
		jdbcTemplate.update("delete from transaction where id = :id", new MapSqlParameterSource("id", id));
	}
}

