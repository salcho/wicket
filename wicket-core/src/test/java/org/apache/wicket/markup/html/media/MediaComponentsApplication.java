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
package org.apache.wicket.markup.html.media;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.caching.NoOpResourceCachingStrategy;

public class MediaComponentsApplication extends WebApplication
{
	@Override
	public Class<? extends Page> getHomePage()
	{
		return MediaTagsTestPage.class;
	}

	@Override
	protected void init()
	{
		super.init();

		getResourceSettings().setCachingStrategy(NoOpResourceCachingStrategy.INSTANCE);

		IPackageResourceGuard packageResourceGuard = org.apache.wicket.Application.get()
				.getResourceSettings()
				.getPackageResourceGuard();
		if (packageResourceGuard instanceof SecurePackageResourceGuard)
		{
			SecurePackageResourceGuard securePackageResourceGuard = (SecurePackageResourceGuard)packageResourceGuard;
			securePackageResourceGuard.addPattern("+*.vtt");
			securePackageResourceGuard.addPattern("+*.srt");
			securePackageResourceGuard.addPattern("+*.mp3");
			securePackageResourceGuard.addPattern("+*.m4a");
		}
	}
}