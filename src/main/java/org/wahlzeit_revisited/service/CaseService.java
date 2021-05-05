package org.wahlzeit_revisited.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.wahlzeit_revisited.dto.CaseDto;
import org.wahlzeit_revisited.model.*;
import org.wahlzeit_revisited.repository.CaseRepository;
import org.wahlzeit_revisited.repository.PhotoRepository;

import java.sql.SQLException;
import java.util.List;

public class CaseService {

    @Inject
    PhotoRepository photoRepository;
    @Inject
    Transformer transformer;

    @Inject
    CaseFactory factory;
    @Inject
    CaseRepository repository;

    public List<CaseDto> getCases() throws SQLException {
        List<Case> cases = repository.findAll();

        List<CaseDto> responseDto = transformer.transformCases(cases);
        return responseDto;
    }

    public CaseDto createCase(User flagger, long photoId, String reason) throws SQLException {
        FlagReason flagReason = FlagReason.getFromString(reason);
        Photo photo = photoRepository.findById(photoId).orElseThrow(() -> new NotFoundException("Unknown photoId"));

        Case photoCase = factory.createPhotoCase(flagger, photo, flagReason);
        photoCase = repository.insert(photoCase);

        CaseDto responseDto = transformer.transform(photoCase);
        return responseDto;
    }

    public CaseDto closeCase(long caseId) throws SQLException {
        Case closeCase = repository.findById(caseId).orElseThrow(() -> new NotFoundException("Unknown caseId"));
        closeCase.setDecided();
        closeCase = repository.update(closeCase);

        CaseDto responseDto = transformer.transform(closeCase);
        return responseDto;
    }


}
