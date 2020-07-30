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
import java.util.Optional;

public class CoopConfiguration
{

	enum CoopMode
	{
		UNSAFE_NONE {
			@Override
			public String toString()
			{
				return "unsafe-none";
			}
		},

		SAME_SITE {
			@Override
			public String toString()
			{
				return "same-site";
			}
		},

		SAME_ORIGIN {
			@Override
			public String toString()
			{
				return "same-origin";
			}
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
		Optional<String> exemptionMatch = Arrays.stream(exemptions).filter(ex -> ex.equals(path))
			.findFirst();
		return exemptionMatch.isPresent();
	}

	public void addCoopHeader(WebResponse resp)
	{
		resp.setHeader(COOP_HEADER, mode.toString());
	}
}
