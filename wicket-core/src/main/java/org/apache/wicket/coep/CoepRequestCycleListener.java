package org.apache.wicket.coep;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class CoepRequestCycleListener implements IRequestCycleListener
{
	private static final Logger log = LoggerFactory.getLogger(CoepRequestCycleListener.class);

	private final CoepConfiguration coepConfig;

	public CoepRequestCycleListener(CoepConfiguration coepConfig)
	{
		this.coepConfig = coepConfig;
	}

    @Override
    public void onRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler)
    {
        HttpServletRequest request = (HttpServletRequest)cycle.getRequest().getContainerRequest();
        String path = request.getContextPath();

        if (coepConfig.isExempted(path))
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
                coepConfig.addCoepHeader(webResponse);
            }
        }
    }
}
