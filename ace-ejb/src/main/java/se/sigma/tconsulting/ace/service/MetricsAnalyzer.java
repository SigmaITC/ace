package se.sigma.tconsulting.ace.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import se.sigma.tconsulting.ace.model.OECData;
import se.sigma.tconsulting.ace.model.SUEData;

/*
 * Analyzes the data received from the monitor. The data is
 * then combined into one overall evaluation criteria (OEC).
 * The OEC is used to decide on how good the configuration
 * generated was.
 * 
 * An OEC can be really hard and tricky to setup. In this simulation
 * we use the generated result from the monitor probe. The only thing
 * done to the result is scaling to smaller numbers.
 */
@Stateless
public class MetricsAnalyzer {
	@Inject
	private EntityManager em;
	
	@Inject
	private ExperimentWatchdog watchdog;
	
	// Creates the OEC value from the SUE data
	public void analyzeData(SUEData data) {
		/* We don't really do much here for now, only
		 * scale the simulation data
		 */
		OECData oec = new OECData();
		oec.setConfiguration(data.getConfiguration());
		oec.setOecValue(scale(data.getResult()));
		em.persist(oec);
		watchdog.validate(oec);
	}
	
	private Double scale(Double value) {
		return value / 100;
	}
}
