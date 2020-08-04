package org.apache.wicket.coep;

import org.apache.wicket.request.http.WebResponse;

import java.util.Arrays;

/**
 * Specifies the configuration for Cross-Origin Embedder Policy to be used for
 * {@link CoepRequestCycleListener}. Users can specify the paths that should be exempt from COEP and
 * one of 2 modes (<code>REPORTING, ENFORCING</code>) for the policy.
 *
 * You can enable COEP headers by adding it to the request cycle listeners in your
 * {@link org.apache.wicket.protocol.http.WebApplication#init() application's init method}:
 *
 * <pre>
 * &#064;Override
 * protected void init()
 * {
 * 	// ...
 * 	enableCoep(new CoepConfiguration.Builder().withMode(CoepMode.ENFORCING)
 * 		.withExemptions("EXEMPTED PATHS").build());
 * 	// ...
 * }
 * </pre>
 *
 * @author Santiago Diaz - saldiaz@google.com
 * @author Ecenaz Jen Ozmen - ecenazo@google.com
 *
 * @see CoepRequestCycleListener
 */
public class CoepConfiguration
{
	enum CoepMode
	{
		ENFORCING("Cross-Origin-Embedder-Policy"), REPORTING(
			"Cross-Origin-Embedder-Policy-Report-Only");

		final String header;

		CoepMode(String header)
		{
			this.header = header;
		}
	}

	static final String REQUIRE_CORP = "require-corp";

	private final String[] exemptions;
	private final CoepMode mode;

	private CoepConfiguration(String[] exemptions, CoepMode mode)
	{
		this.exemptions = exemptions;
		this.mode = mode;
	}

	public static class Builder
	{
		private String[] exemptions;
		private CoepMode mode;

		Builder withExemptions(String... exemptions)
		{
			this.exemptions = exemptions;
			return this;
		}

		Builder withMode(CoepMode mode)
		{
			this.mode = mode;
			return this;
		}

		CoepConfiguration build()
		{
			return new CoepConfiguration(exemptions, mode);
		}
	}

	public boolean isExempted(String path)
	{
		return Arrays.stream(exemptions).anyMatch(ex -> ex.equals(path));
	}

	public void addCoepHeader(WebResponse resp)
	{
		resp.setHeader(mode.header, REQUIRE_CORP);
	}
}
