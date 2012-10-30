package fm.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletResponse;
import net.arnx.jsonic.JSON;

/**
 * Handle errors
 * @author Atsushi OHNO
 */
public enum ErrorHandler {
    INVALID_URI         ("Invalid URI"),
    INVALID_PARAMETER   ("Invalid parameter"),
    PATH_NOT_FOUND      ("Path not found");

    final String errorStr;
    private ErrorHandler(String text) {
        errorStr = text;
    }

    public void send(ServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            HashMap<String, String> e = new HashMap<String, String>(1);
            e.put("error", errorStr);
            out.println(JSON.encode(e));
        } finally {
            out.close();
        }
    }
}
