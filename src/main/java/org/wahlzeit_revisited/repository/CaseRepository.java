package org.wahlzeit_revisited.repository;


import jakarta.inject.Inject;
import org.wahlzeit_revisited.model.Case;
import org.wahlzeit_revisited.model.CaseFactory;

/*
 * Repository to query Case entities
 */
public class CaseRepository extends AbstractRepository<Case> {

    /*
     * AbstractRepository contract
     */

    @Inject
    public CaseFactory factory;

    @Override
    protected PersistentFactory<Case> getFactory() {
        return factory;
    }

    @Override
    protected String getTableName() {
        return "cases";
    }
}
