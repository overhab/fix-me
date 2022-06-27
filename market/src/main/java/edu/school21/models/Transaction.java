package edu.school21.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "broker_id")
	private long broker_id;

	@Column(name = "market_id")
	private long market_id;

	private Stock stock;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "status")
	private Status status;

}
