package se.sigma.tconsulting.ace.service;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import se.sigma.tconsulting.ace.model.SUEConfiguration;

@Stateless
public class VersionManager {
	
	@Inject
	private EntityManager em;
	
	@Inject
	private VersionGenerator generator;
	
	@Inject
	private Effector effector;

	public void generateLearningConfiguration() {
		SUEConfiguration configuration = generator.getLearningConfiguration();
		saveConfiguration(configuration);
		effector.push(configuration);
	}

	public void generateOptimizationConfiguration() {
		SUEConfiguration configuration = generator.getOptimizationConfiguration();
		saveConfiguration(configuration);
		effector.push(configuration);
	}
	
	private void saveConfiguration(SUEConfiguration config) {
		em.persist(config);
	}
}
