/*
 *  Copyright 2018 original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.springframework.cloud.gcp.security.iap.claims;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IssueTimeInPastClaimVerifier implements ClaimVerifier {
	private static final Log LOGGER = LogFactory.getLog(IssueTimeInPastClaimVerifier.class);

	// TODO: make injectable for testing. Or is that a security hole waiting to happen?
	private static Clock clock = Clock.systemUTC();

	@Override
	public boolean verify(Map<String, Object> claims) {
		Date currentTime = Date.from(Instant.now(clock));
		LOGGER.info(String.format("Token issued at %s; current time %s", claims.get("iat"), currentTime));
		if (!Date.from(Instant.ofEpochSecond((Integer) claims.get("iat"))).before(currentTime)) {
			LOGGER.warn("Issue time claim verification failed.");
			return false;
		}

		return true;
	}
}
