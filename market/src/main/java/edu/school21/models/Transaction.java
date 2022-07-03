package edu.school21.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
public class Transaction {

	public Transaction(int broker_id, int market_id, String instrument, int quantity, String type, String status, String order_id) {
		this.broker_id = broker_id;
		this.market_id = market_id;
		this.instrument = instrument;
		this.quantity = quantity;
		this.type = type;
		this.status = Status.valueOf(status);
		this.order_id = order_id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "broker_id")
	private int broker_id;

	@Column(name = "market_id")
	private int market_id;

	@Column(name = "instrument")
	private String instrument;

	@Column(name = "quantity")
	private int quantity;

	@Column(name = "type")
	private String type;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "status")
	private Status status;

	@Column(name = "order_id")
	private String order_id;
}
