package org.apache.wicket.coep;

import org.apache.wicket.request.http.WebResponse;

import java.util.Arrays;

public class CoepConfiguration
{
	enum CoepMode {
		ENFORCING("Cross-Origin-Embedder-Policy"),
        REPORTING("Cross-Origin-Embedder-Policy-Report-Only");

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

    public void addCoepHeader(WebResponse resp) {
	    //TODO: handle reporting - report-to ?
	    resp.setHeader(mode.header, REQUIRE_CORP);
    }
}
