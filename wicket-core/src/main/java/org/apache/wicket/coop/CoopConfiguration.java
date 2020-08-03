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

import org.apache.wicket.request.http.WebResponse;

import java.util.Arrays;

/**
 * Specifies the configuration for Cross-Origin Opener Policy to be used for {@link CoopRequestCycleListener}.
 * Users can specify the paths that should be exempt from COOP and one of 3 modes
 * (<code>UNSAFE_NONE, SAME_SITE, SAME_ORIGIN</code>) for the policy.
 *
 * You can enable this CSRF prevention filter by adding it to the request cycle listeners in your
 * {@link org.apache.wicket.protocol.http.WebApplication#init() application's init method}:
 *
 * <pre>
 * &#064;Override
 * protected void init()
 * {
 * 	// ...
 * 	enableCoop(new CoopConfiguration.Builder()
 * 			.withMode(CoopMode.SAME_ORIGIN).withExemptions("EXEMPTED PATHS").build());
 * 	// ...
 * }
 * </pre>
 *
 * @author Santiago Diaz - saldiaz@google.com
 * @author Ecenaz Jen Ozmen - ecenazo@google.com
 *
 * @see CoopRequestCycleListener
 */
public class CoopConfiguration
{
	enum CoopMode {
		UNSAFE_NONE("unsafe-none"),
		SAME_SITE("same-site"),
		SAME_ORIGIN("same-origin");

		final String keyword;

		CoopMode(String keyword) {
			this.keyword = keyword;
		}
	}

	static String COOP_HEADER = "Cross-Origin-Opener-Policy";

	private final String[] exemptions;
	private final CoopMode mode;


	private CoopConfiguration(String[] exemptions, CoopMode mode)
	{
		this.exemptions = exemptions;
		this.mode = mode;
	}

	public static class Builder
	{
		private String[] exemptions;
		private CoopMode mode;

		Builder withExemptions(String... exemptions)
		{
			this.exemptions = exemptions;
			return this;
		}

		Builder withMode(CoopMode mode)
		{
			this.mode = mode;
			return this;
		}

		CoopConfiguration build()
		{
			return new CoopConfiguration(exemptions, mode);
		}
	}

	public boolean isExempted(String path)
	{
		return Arrays.stream(exemptions).anyMatch(ex -> ex.equals(path));
	}

	public void addCoopHeader(WebResponse resp)
	{
		resp.setHeader(COOP_HEADER, mode.keyword);
	}
}