package org.apache.wicket.protocol.http;

import javax.servlet.http.HttpServletRequest;

/**
 * Default resource isolation policy used in {@link CsrfPreventionRequestCycleListener}
 * that implements the {@link ResourceIsolationPolicy} interface. This default
 * policy is based on <a href="https://web.dev/fetch-metadata/">https://web.dev/fetch-metadata/</a>.
 *
 * @see <a href="https://web.dev/fetch-metadata/">https://web.dev/fetch-metadata/</a>
 *
 * @author Santiago Diaz - saldiaz@google.com
 * @author Ecenaz Jen Ozmen - ecenazo@google.com
 */
public class DefaultResourceIsolationPolicy implements ResourceIsolationPolicy
{

    @Override
    public boolean isRequestAllowed(HttpServletRequest request)
    {
        String site = request.getHeader(SEC_FETCH_SITE_HEADER);

        // Allow same-site and browser-initiated requests
        if (SAME_ORIGIN.equals(site) || SAME_SITE.equals(site) || NONE.equals(site)) {
            return true;
        }

        // Allow simple top-level navigations except <object> and <embed>
        return isAllowedTopLevelNavigation(request);
    }

    private boolean isAllowedTopLevelNavigation(HttpServletRequest request)
    {
        String mode = request.getHeader(SEC_FETCH_MODE_HEADER);
        String dest = request.getHeader(SEC_FETCH_DEST_HEADER);

        // TODO: Does the method need to be upper cased?
        boolean isSimpleTopLevelNavigation = MODE_NAVIGATE.equals(mode) || "GET".equals(request.getMethod());
        boolean isNotObjectOrEmbedRequest = !DEST_EMBED.equals(dest) && !DEST_OBJECT.equals(dest);

        return isSimpleTopLevelNavigation && isNotObjectOrEmbedRequest;
    }
}
