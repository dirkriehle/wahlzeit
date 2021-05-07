package org.wahlzeit_revisited.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.wahlzeit_revisited.dto.CaseDto;
import org.wahlzeit_revisited.model.*;
import org.wahlzeit_revisited.repository.CaseRepository;
import org.wahlzeit_revisited.repository.PhotoRepository;
import org.wahlzeit_revisited.utils.SysLog;

import java.sql.SQLException;
import java.util.List;

/*
 * Business logic of case
 */
public class CaseService {

    @Inject
    PhotoRepository photoRepository;
    @Inject
    Transformer transformer;

    @Inject
    CaseFactory factory;
    @Inject
    CaseRepository repository;

    /**
     * Returns all existing cases
     *
     * @return all Cases
     * @throws SQLException internal errr
     */
    public List<CaseDto> getAllCases() throws SQLException {
        List<Case> cases = repository.findAll();
        SysLog.logSysInfo(String.format("Fetched %s cases", cases.size()));
        List<CaseDto> responseDto = transformer.transformCases(cases);
        return responseDto;
    }

    /**
     * Creates a new Case
     *
     * @param flagger owner of the case
     * @param photoId offensive photo
     * @param reason  why
     * @return newly created CaseDto
     * @throws SQLException internal error
     */
    public CaseDto createCase(User flagger, long photoId, String reason) throws SQLException {
        FlagReason flagReason = FlagReason.getFromString(reason);
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new NotFoundException("Unknown photoId"));

        Case photoCase = factory.createPhotoCase(flagger, photo, flagReason);
        photoCase = repository.insert(photoCase);

        SysLog.logSysInfo(String.format("Created case: %s ", photoCase.getId()));
        CaseDto responseDto = transformer.transform(photoCase);
        return responseDto;
    }

    /**
     * Close a existing case
     *
     * @param caseId case to close
     * @return closed case
     * @throws SQLException internal error
     */
    public CaseDto closeCase(long caseId) throws SQLException {
        Case closeCase = repository.findById(caseId).orElseThrow(() -> new NotFoundException("Unknown caseId"));
        closeCase.setDecided();
        closeCase = repository.update(closeCase);

        SysLog.logSysInfo(String.format("Closed case: %s ", closeCase.getId()));
        CaseDto responseDto = transformer.transform(closeCase);
        return responseDto;
    }

}
