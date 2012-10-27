package fm.servlets.filter;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthMessage;
import net.oauth.example.provider.core.SampleOAuthProvider;
import net.oauth.server.OAuthServlet;

/**
 *
 * @author Atsushi OHNO
 */
public class OAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try{
            OAuthMessage requestMessage = OAuthServlet.getMessage((HttpServletRequest)request, null);
            OAuthAccessor accessor = SampleOAuthProvider.getAccessor(requestMessage);
            SampleOAuthProvider.VALIDATOR.validateMessage(requestMessage, accessor);
            request.setAttribute("OAuthAccessor", accessor);
            chain.doFilter(request, response);
        } catch (Exception e){
            SampleOAuthProvider.handleException(e, (HttpServletRequest)request, (HttpServletResponse)response, false);
        }
    }

    @Override
    public void destroy() {
    }

}
