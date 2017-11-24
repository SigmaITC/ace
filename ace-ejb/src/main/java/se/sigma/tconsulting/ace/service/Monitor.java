package se.sigma.tconsulting.ace.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import se.sigma.tconsulting.ace.model.SUEData;
/**
 * Connects to the system under test (SUE). Another alternative is that the SUE connects to the monitor.
 * It all depends on how the SUE is constructed.
 */
@Stateless
public class Monitor {
	
	@Inject
	private EntityManager em;
	
	@Inject
	private MetricsAnalyzer analyzer;
	
	public void handleData(SUEData data) {
		/* Here we would combine the data from the SUE and convert to represent a better
		 * structure for internal usage. Now we just send the data forward.
		 * Another possibility is to have multiple probes, and awaiting data from all of them
		 * before sending for analysis. 
		 */
		em.persist(data);
		analyzer.analyzeData(data);
	}
}
