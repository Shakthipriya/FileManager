/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fm.servlets.filter;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.regex.*;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import net.arnx.jsonic.JSON;

/**
 *
 * @author freiheit
 */
public class FileHashFilter implements Filter {
    private Pattern hashPattern;

    /**
     * Init method for this filter
     */
    @Override
    public void init(FilterConfig filterConfig) {
        //hashPattern = Pattern.compile("^.*/file/([0-9a-f]{64})$", Pattern.CASE_INSENSITIVE);
        hashPattern = Pattern.compile("^.*/file/([0-9a-f]+)$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = ((HttpServletRequest)request).getRequestURI();

        Matcher m = hashPattern.matcher(path);
        if (m.find()) {
            request.setAttribute("Hash", m.group(1));
            chain.doFilter(request, response);
        } else {
            PrintWriter pw = response.getWriter();
            HashMap<String, String> e = new HashMap<String, String>(1);
            e.put("error", "Invalid URI");
            pw.println(JSON.encode(e));
            pw.close();

        }
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }
}
