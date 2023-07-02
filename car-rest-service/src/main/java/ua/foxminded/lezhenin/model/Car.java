package ua.foxminded.lezhenin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Entity
@Table(name = "cars", schema = "cms")
public class Car {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cms.cars_id_seq")
	@SequenceGenerator(name = "cms.cars_id_seq", sequenceName = "cms.cars_id_seq", allocationSize = 1)
	private int id;

	@Column(name = "object_id")
	private String objectId;

	@Column(name = "make")
	private String make;

	@Column(name = "year")
	private int year;

	@Column(name = "model")
	private String model;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

}
