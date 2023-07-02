package ua.foxminded.lezhenin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "categories", schema = "cms")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cms.categories_id_seq")
	@SequenceGenerator(name = "cms.categories_id_seq", sequenceName = "cms.categories_id_seq", allocationSize = 1)
	private int id;

	@Column(name = "name", nullable = true, length = 255)
	private String name;

}
