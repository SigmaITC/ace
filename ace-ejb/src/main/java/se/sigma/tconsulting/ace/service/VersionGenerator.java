package se.sigma.tconsulting.ace.service;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.math4.ml.clustering.CentroidCluster;
import org.apache.commons.math4.ml.clustering.KMeansPlusPlusClusterer;

import se.sigma.tconsulting.ace.data.OECRepository;
import se.sigma.tconsulting.ace.model.OECData;
import se.sigma.tconsulting.ace.model.SUEConfiguration;
import se.sigma.tconsulting.ace.util.Range;

@ApplicationScoped
public class VersionGenerator {
	private static final double RANGE_START = 0;
	private static final double RANGE_STOP = 10;
	private static final double WANTED_BASELINE = 2d;
	private static final int NUMBER_OF_CLUSTERS = 3;
	private static final int MAX_ITERATIONS = 1000;
	
	@Inject
	private OECRepository oecRepository;
	
	private List<Range> ranges;

	public SUEConfiguration getLearningConfiguration() {
		SUEConfiguration configuration = new SUEConfiguration();
		Double value = getRandom(RANGE_START, RANGE_STOP);
		configuration.setValue(value);
		return configuration;
	}

	private Double getRandom(double start, double stop) {
		Long seed = System.currentTimeMillis();
		Random random = new Random(seed);
		DoubleStream stream = random.doubles(start, stop);
		return stream.findFirst().getAsDouble();
	}

	public SUEConfiguration getOptimizationConfiguration() {
		identifyRanges();
		SUEConfiguration configuration = new SUEConfiguration();
		
//		  rangeToUse = np.random.randint(0, len(self._ranges))
//        dataRange = np.array(self._rangeData[rangeToUse])
//        km = KMeans(n_clusters=Constants.CONFIGURATION_K_MEANS, init='k-means++', n_jobs=1)
//        km.fit_predict(dataRange)
//        orderedClusters = km.cluster_centers_[km.cluster_centers_[:,0].argsort()]
//        self._latest_clusters[rangeToUse] = orderedClusters
//        bestDistance = orderedClusters[1, 0]
//        lowerDistance = (bestDistance + orderedClusters[0, 0]) / 2
//        higherDistance = (bestDistance + orderedClusters[2, 0]) / 2
//        return np.random.uniform(lowerDistance, higherDistance)
		
		// Select a random range to operate on
		Random random = new Random();
		int rangeToUse = random.nextInt(ranges.size());
		
		List<OECData> dataInRange = getDataForRange(ranges.get(rangeToUse));
		
		KMeansPlusPlusClusterer<OECData> kmeans = new KMeansPlusPlusClusterer<>(NUMBER_OF_CLUSTERS, MAX_ITERATIONS);
		List<CentroidCluster<OECData>> centroids = kmeans.cluster(dataInRange);
		
		List<OECData> oecCentroids = centroids.stream()
			.map(c -> (OECData) c.getCenter())
			.collect(Collectors.toList());
		
		// These are always three
		Collections.sort(oecCentroids);
		Double midValue = oecCentroids.get(1).getConfiguration().getValue();
		Double minValue = (midValue + oecCentroids.get(0).getConfiguration().getValue()) /2;
		Double maxValue = (midValue + oecCentroids.get(2).getConfiguration().getValue()) /2;
		Double value = getRandom(minValue, maxValue);
		
		configuration.setValue(value);
		return configuration;
	}

	/*
	 * Identifies valid ranges, but only if there are none since before.
	 * A range is based on configuration value and OEC results.
	 * OEC values are the Y-axis and configuration values are X-axis.
	 * We are interested in identified configuration values which surpass
	 * the wanted baseline.
	 */
	private void identifyRanges() {
		if (ranges != null) {
			return;
		}
		
		List<OECData> oecList = oecRepository.getAll();
		Collections.sort(oecList);
		
		for (int i = 0; i < oecList.size(); i++) {
			OECData data = oecList.get(i);
			if (data.getOecValue() < WANTED_BASELINE) {
				continue;
			}
			
			Double rangeStart = data.getConfiguration().getValue();
			Double rangeStop = 0d;
			for (; i < oecList.size() && data.getOecValue() >= WANTED_BASELINE; i++) {
				rangeStop = data.getConfiguration().getValue();
				data = oecList.get(i);
			}
			ranges.add(new Range(rangeStart, rangeStop));
		}
	}
	
	private List<OECData> getDataForRange(Range range) {
		List<OECData> oecList = oecRepository.getAll();
		return oecList.stream()
			.filter(oec -> oec.getConfiguration().getValue() >= range.getStart())
			.filter(oec -> oec.getConfiguration().getValue() <= range.getStop())
			.collect(Collectors.toList());
	}
}
