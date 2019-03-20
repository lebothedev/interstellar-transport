package za.co.discovery.transport.interstellar.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Model representation of a planet in the galaxy
 */
@Entity
@Table(name = "PLANET")
@NamedQueries({
		@NamedQuery(name = Planet.FIND_PLANET_BY_NODE, query = "SELECT p FROM Planet p WHERE p.node = :node"),
		@NamedQuery(name = Planet.FIND_ALL_PLANETS, query = "SELECT p FROM Planet p")
})
public class Planet implements Serializable {

	private static final long serialVersionUID = -343305905800194724L;

	public static final String FIND_PLANET_BY_NODE = "Planet.findByNode";
	public static final String FIND_ALL_PLANETS = "Planet.findAll";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false, unique = true)
	@Getter @Setter
	private Long id;

	@Getter @Setter
	@Column(name = "NODE", nullable = false, unique = true)
	private String node;

	@Getter @Setter
	@Column(name = "NAME", nullable = false)
	private String name;

	public Planet() {}

	public Planet(String node, String name) {
		this.node = node;
		this.name = name;
	}

	@Override
	public String toString() {
		return "Planet{" +
				"id=" + id +
				", node='" + node + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
