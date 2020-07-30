package org.apache.wicket.coop;

import org.apache.wicket.util.tester.WicketTestCase;
import org.apache.wicket.coop.CoopConfiguration.CoopMode;
import org.junit.jupiter.api.Test;

import static org.apache.wicket.coop.CoopConfiguration.COOP_HEADER;

public class CoopRequestCycleListenerTest extends WicketTestCase {

	@Test
	public void testCoopHeadersSetCorrectly()
	{
		tester.getApplication().enableCoop(new CoopConfiguration.Builder()
			.withMode(CoopMode.SAME_ORIGIN).withExemptions("exempt").build());
		checkHeaders("/", CoopMode.SAME_ORIGIN);
	}

    @Test
    public void testCoopHeadersNotSetExemptedPath()
    {
        tester.getApplication().enableCoop(new CoopConfiguration.Builder()
                .withMode(CoopMode.SAME_ORIGIN).withExemptions("exempt").build());
        tester.executeUrl("exempt");
        String coopHeaderValue = tester.getLastResponse().getHeader(COOP_HEADER);

        if (coopHeaderValue != null)
        {
            throw new AssertionError("COOP header should be null on exempted path");
        }
    }

    private void checkHeaders(String url, CoopMode mode)
    {
        tester.executeUrl(url);
        String coopHeaderValue = tester.getLastResponse().getHeader(COOP_HEADER);

        if (coopHeaderValue == null)
        {
            throw new AssertionError("COOP header should not be null");
        }

        if (!mode.keyword.equals(coopHeaderValue))
        {
            throw new AssertionError("Unexpected COOP header: " + coopHeaderValue);
        }
    }
}
