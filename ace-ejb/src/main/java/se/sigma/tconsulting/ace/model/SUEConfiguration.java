package se.sigma.tconsulting.ace.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name = "Configuration")
public class SUEConfiguration {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="configuration_id")
	private Long id;
	
	// name it value since this is a prototype and I don't know the name of the wanted configuration
	@NotNull
	private Double value;
	
	@OneToMany
	@JoinTable(name="SUE_Data",
		joinColumns = @JoinColumn(name = "sue_id"))
	private List<SUEData> linkedSUEData;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
}
