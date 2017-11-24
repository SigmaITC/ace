package se.sigma.tconsulting.ace.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name = "SUE_Data")
public class SUEData {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="sue_id")
	private Long id;

	@ManyToOne
	@JoinTable(name="Configuration",
		joinColumns = @JoinColumn(name = "configuration_id"))
	private SUEConfiguration configuration;
	
	// Result should in reality be a metric. The SUE will most likely have more than one metric of interest..
	@NotNull
	private Double result;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SUEConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(SUEConfiguration configuration) {
		this.configuration = configuration;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}
}
