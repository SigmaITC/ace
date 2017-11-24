package se.sigma.tconsulting.ace.service;

import static se.sigma.tconsulting.ace.service.ExperimentControllerState.STATIC;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import se.sigma.tconsulting.ace.data.OECRepository;
import se.sigma.tconsulting.ace.model.OECData;

@ApplicationScoped
public class ExperimentController {
	private static final int OPTIMIZATION_POINTS = 500;
	private static final int LEARNING_POINTS = 100;
	
	@Inject
	private VersionManager versionManager;
	
	@Inject
	private OECRepository oecRepository;
	
	private ExperimentControllerState currentState = STATIC;

	/*
	 *  Used to indicate that the system should check for state switching and so forth.
	 */
	public void newInput() {
		currentState = getCurrentState();
		
		switch (currentState) {
		case LEARNING:
			learningActions();
			break;
		case OPTIMIZING:
			optimizationActions();
			break;
		case STATIC:
		default:
			staticActions();
		}
	}
	
	public void runExperiment() {
		// TODO Contact conflict manager for approval 
		purgeData();
		newInput(); // This will not detect any data and thus initiate learning
	}

	/*
	 * Decide on what state the system is in based on how much data has been collected.
	 * There are of course better ways for this, but suffices for now 
	 */
	private ExperimentControllerState getCurrentState() {
		List<OECData> oecList = oecRepository.getAll();
		
		if (oecList.size() < LEARNING_POINTS) {
			return ExperimentControllerState.LEARNING;
		} else if (oecList.size() < OPTIMIZATION_POINTS) {
			return ExperimentControllerState.OPTIMIZING;
		}
		
		return ExperimentControllerState.STATIC;
	}
	
	private void learningActions() {
		versionManager.generateLearningConfiguration();
	}
	
	private void optimizationActions() {
		versionManager.generateOptimizationConfiguration();
	}
	
	private void staticActions() {
		/*
		 * We could generate a report here and let purge simply remove data
		 */
	}
	
	private void purgeData() {
		/*
		 * We call delete all here, but in reality we would never do so. The data could
		 * be used for offline processing for instance.
		 */
		//oecRepository.deleteAll();
	}
}
