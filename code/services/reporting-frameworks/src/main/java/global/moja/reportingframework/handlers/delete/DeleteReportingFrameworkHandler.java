/*
 * Copyright (C) 2021 Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingframework.handlers.delete;

import global.moja.reportingframework.repository.ReportingFrameworksRepository;
import global.moja.reportingframework.exceptions.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class DeleteReportingFrameworkHandler {

	@Autowired
	ReportingFrameworksRepository repository;
	
	/**
	 * Deletes a Reporting Framework record
	 *
	 * @param request the request containing the details of the Reporting Framework record to be deleted
	 * @return the response containing the number of Reporting Frameworks records deleted
	 */
	public Mono<ServerResponse> deleteReportingFramework(ServerRequest request) {

		log.trace("Entering deleteReportingFramework()");
		
		return 
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(deleteReportingFrameworkById(Long.parseLong(request.pathVariable("id"))),Integer.class)
				.onErrorMap(e -> new ServerException("Reporting Framework deletion failed", e));

	}
	
	private Mono<Integer> deleteReportingFrameworkById(Long id){
		
		return 
			repository
				.deleteReportingFrameworkById(id);
	}

}
