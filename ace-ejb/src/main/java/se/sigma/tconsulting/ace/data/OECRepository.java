package se.sigma.tconsulting.ace.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import se.sigma.tconsulting.ace.model.OECData;

@ApplicationScoped
public class OECRepository {
	@Inject
	private EntityManager em;

	public List<OECData> getAll() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<OECData> criteria = cb.createQuery(OECData.class);
        Root<OECData> oec = criteria.from(OECData.class);
        criteria.select(oec);
        return em.createQuery(criteria).getResultList();
	}
	
	public void deleteAll() {
		em.createQuery("DELETE from OEC").executeUpdate();
	}
}
