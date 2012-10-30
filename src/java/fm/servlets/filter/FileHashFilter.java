/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fm.servlets.filter;

import java.io.IOException;
import java.io.PrintWriter;
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
        hashPattern = Pattern.compile("^.*/(file|share)/([0-9a-f]{64})$", Pattern.CASE_INSENSITIVE);
        //hashPattern = Pattern.compile("^.*/(file|share)/([0-9a-f]+)$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = ((HttpServletRequest)request).getRequestURI();

        Matcher m = hashPattern.matcher(path);
        String hash;
        if (m.find()) {
            hash = m.group(2);
        } else {
            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            try {
                HashMap<String, String> e = new HashMap<String, String>(1);
                e.put("error", "Invalid URI");
                pw.println(JSON.encode(e));
            } finally {
                pw.close();
            }

            return;
        }

        /*
         * TODO: Check file exists
         */
        /* for debug */
        if (hash.equals("ed3772303df9c706450c5975c2d871aeb6800971746456d675e84bcafa179bac")) {
            request.setAttribute("Path", "D:\\OpenCV\\doc");
        }

        request.setAttribute("Hash", hash);
        chain.doFilter(request, response);
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }
}
