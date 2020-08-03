package org.apache.wicket.coep;

import org.apache.wicket.util.tester.WicketTestCase;
import org.junit.jupiter.api.Test;

import static org.apache.wicket.coep.CoepConfiguration.REQUIRE_CORP;

public class CoepRequestCycleListenerTest extends WicketTestCase
{
    @Test
    public void testEnforcingCoepHeadersSetCorrectly()
    {
        tester.getApplication().enableCoep(new CoepConfiguration.Builder()
                .withMode(CoepConfiguration.CoepMode.ENFORCING).withExemptions("exempt").build());
        checkHeaders("/", CoepConfiguration.CoepMode.ENFORCING);
    }

    @Test
    public void testReportingCoepHeadersSetCorrectly()
    {
        tester.getApplication().enableCoep(new CoepConfiguration.Builder()
                .withMode(CoepConfiguration.CoepMode.REPORTING).withExemptions("exempt").build());
        checkHeaders("/", CoepConfiguration.CoepMode.REPORTING);
    }

    @Test
    public void testCoepHeadersNotSetExemptedPath()
    {
        tester.getApplication().enableCoep(new CoepConfiguration.Builder()
                .withMode(CoepConfiguration.CoepMode.ENFORCING).withExemptions("exempt").build());
        tester.executeUrl("exempt");
        String coopHeaderValue = tester.getLastResponse().getHeader(CoepConfiguration.CoepMode.ENFORCING.header);

        if (coopHeaderValue != null)
        {
            throw new AssertionError("COOP header should be null on exempted path");
        }
    }

    private void checkHeaders(String url, CoepConfiguration.CoepMode mode)
    {
        tester.executeUrl(url);
        String coepHeaderValue = tester.getLastResponse().getHeader(mode.header);

        if (coepHeaderValue == null)
        {
            throw new AssertionError("COEP" + mode + "header should not be null");
        }

        if (!REQUIRE_CORP.equals(coepHeaderValue))
        {
            throw new AssertionError("Unexpected COEP header: " + coepHeaderValue);
        }
    }
}
