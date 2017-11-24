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

import org.apache.commons.math4.ml.clustering.Clusterable;

@Entity
@XmlRootElement
@Table(name = "OEC")
public class OECData implements Comparable<OECData>, Clusterable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinTable(name="Configuration",
		joinColumns = @JoinColumn(name = "id"))
	private SUEConfiguration configuration;
	
	@NotNull
	@Column(name="value")
	private Double oecValue;
	
	@NotNull
	private Boolean bad = false; 

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

	public Double getOecValue() {
		return oecValue;
	}

	public void setOecValue(Double oecValue) {
		this.oecValue = oecValue;
	}

	public Boolean getBad() {
		return bad;
	}

	public void setBad(Boolean bad) {
		this.bad = bad;
	}

	@Override
	public int compareTo(OECData o) {
		return configuration.getValue().compareTo(o.configuration.getValue());
	}

	@Override
	public double[] getPoint() {
		return new double[] {configuration.getValue(), oecValue};
	}
}
