/*
 * Copyright (c) 2006-2009 by Dirk Riehle, http://dirkriehle.com
 * Copyright (c) 2021 by Aron Metzig
 *
 * This file is part of the Wahlzeit photo rating application.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package org.wahlzeit.service;

import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.wahlzeit.api.dto.CaseDto;
import org.wahlzeit.database.repository.CaseRepository;
import org.wahlzeit.database.repository.PhotoRepository;
import org.wahlzeit.model.*;
import org.wahlzeit.utils.SysLog;

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
