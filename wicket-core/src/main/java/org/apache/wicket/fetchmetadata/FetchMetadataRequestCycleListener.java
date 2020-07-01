package org.apache.wicket.fetchmetadata;

import java.util.Arrays;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.wicket.core.request.handler.IPageRequestHandler;
import org.apache.wicket.fetchmetadata.FetchMetadataConfiguration.FetchMetadataMode;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;

public class FetchMetadataRequestCycleListener implements IRequestCycleListener {
  private static final int errorCode = HttpServletResponse.SC_FORBIDDEN;

  private FetchMetadataConfiguration configuration;

  public FetchMetadataRequestCycleListener(FetchMetadataConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void onRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler) {
    if (!shouldCheckRequest(handler)) {
      return;
    }

    HttpServletRequest request = (HttpServletRequest)cycle.getRequest().getContainerRequest();

    // TODO: Check exemptions. Regex vs plain? Are there any mechanisms for defining wildcard URLs?
    String path = request.getContextPath();
    Optional<String> exemptionMatch = Arrays.stream(configuration.exemptions)
        .filter(ex -> ex.equals(path))
        .findFirst();

    // This request matches an exemption
    if (exemptionMatch.isPresent()) {
      return;
    }

    for (ResourceIsolationPolicy policy : configuration.policies) {
      if (!policy.isRequestAllowed(request)) {
        if (configuration.mode == FetchMetadataMode.REPORTING) {
          // TODO: Log failure
        } else {
          // TODO: Error message
          throw new AbortWithHttpErrorCodeException(errorCode, "Forbidden");
        }
      }
    }
  }

  // TODO: What other request handlers should we cover?
  private static boolean shouldCheckRequest(IRequestHandler handler) {
    return handler instanceof IPageRequestHandler;
  }
}
