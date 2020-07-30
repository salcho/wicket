/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.coop;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class CoopRequestCycleListener implements IRequestCycleListener
{
	private static final Logger log = LoggerFactory.getLogger(CoopRequestCycleListener.class);

	private final CoopConfiguration coopConfig;

	public CoopRequestCycleListener(CoopConfiguration cooopConfig)
	{
		this.coopConfig = cooopConfig;
	}

	// TODO: should we implement onEndRequest instead? ie. will we need to use the handler?
	@Override
	public void onRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler)
	{
		HttpServletRequest request = (HttpServletRequest)cycle.getRequest().getContainerRequest();
		String path = request.getContextPath();

		if (coopConfig.isExempted(path))
		{
			if (log.isDebugEnabled())
			{
				log.debug("Request path is exempted from COOP, no COOP header added");
			}
			return;
		}

		if (cycle.getResponse() instanceof WebResponse)
		{
			WebResponse webResponse = (WebResponse)cycle.getResponse();
			if (webResponse.isHeaderSupported())
			{
				coopConfig.addCoopHeader(webResponse);
			}
		}
	}
}
