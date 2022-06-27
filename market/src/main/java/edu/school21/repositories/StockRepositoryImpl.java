package edu.school21.repositories;

import edu.school21.models.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;

@Component("stockRepository")
public class StockRepositoryImpl implements StockRepository{

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public StockRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private final RowMapper<Stock> stockRowMapper = (ResultSet resultSet, int i) -> {
		return new Stock(resultSet.getLong("id"),
				resultSet.getString("name"),
				resultSet.getInt("quantity"),
				resultSet.getFloat("price"));
	};

	@Override
	public Stock findById(Long id) {
		return jdbcTemplate.queryForObject("select * from stocks where id = :id",
				new MapSqlParameterSource("id", id), stockRowMapper);
	}

	@Override
	public List<Stock> findAll() {
		return jdbcTemplate.query("select * from stocks", stockRowMapper);
	}

	@Override
	public void save(Stock entity) {
		jdbcTemplate.update("insert into stocks(name, quantity, price) values (:name, :quantity, :price)",
				new BeanPropertySqlParameterSource(entity));
	}

	@Override
	public void update(Stock entity) {
		jdbcTemplate.update("update stocks set name = :name, quantity = :quantity, price = :price",
				new BeanPropertySqlParameterSource(entity));
	}

	@Override
	public void delete(Long id) {
		jdbcTemplate.update("delete from stocks where id = :id", new MapSqlParameterSource("id", id));
	}

	@Override
	public List<Stock> findAllWithLimit() {
		return jdbcTemplate.query("select * from stocks order by random() limit 4", stockRowMapper);
	}
}
