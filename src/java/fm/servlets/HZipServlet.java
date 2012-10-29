package fm.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.arnx.jsonic.JSON;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Directory/File compress handler
 * @author Atsushi OHNO
 */
public class HZipServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getAttribute("Path") != null) {
            File f = new File((String)request.getAttribute("Path"));
            String zipName;
            if (!f.isDirectory() && f.getName().lastIndexOf(".") != -1) {
                zipName = f.getName().substring(0, f.getName().lastIndexOf("."));
            } else {
                zipName = f.getName();
            }
            response.setHeader("Content-Disposition","attachment; filename=" + zipName + ".zip");
            response.setContentType("application/x-compress");

            ZipArchiveOutputStream zip = new ZipArchiveOutputStream(response.getOutputStream());
            zip.setEncoding("Windows-31J");

            try {
                addAll(zip, f.getAbsolutePath(), f);
                zip.finish();
                zip.flush();
            } finally {
                zip.close();
            }
        } else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            try {
                HashMap<String, String> e = new HashMap<String, String>(1);
                e.put("error", "Path not found");
                out.println(JSON.encode(e));
                /*
                 * TODO output your page here. You may use following sample code.
                 */
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet HZipServlet</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Servlet HZipServlet at " + request.getContextPath() + "</h1>");
                out.println("<h2>Hash: " + request.getAttribute("Hash") + "</h2>");
                out.println("<p>"+request.getAttribute("javax.servlet.forward.request_uri") +"</p>");
                out.println("</body>");
                out.println("</html>");
            } finally {
                out.close();
            }
        }
    }

    private void addAll(ArchiveOutputStream archive, String basePath, File targetFile) throws IOException {
        if (targetFile.isDirectory()) {
            File[] children = targetFile.listFiles();

            if (children.length == 0) {
                addDir(archive, basePath, targetFile);
            } else {
                for (File file : children) {
                    addAll(archive, basePath, file);
                }
            }

        } else {
            addFile(archive, basePath, targetFile);
        }

    }

    private void addFile(ArchiveOutputStream archive, String basePath, File file) throws IOException {
        String path = file.getAbsolutePath();
        String name = path.substring(basePath.length()+1);

        archive.putArchiveEntry(new ZipArchiveEntry(name));
        IOUtils.copy(new FileInputStream(file), archive);
        archive.closeArchiveEntry();
    }

    private void addDir(ArchiveOutputStream archive, String basePath, File file) throws IOException {
        String path = file.getAbsolutePath();
        String name = path.substring(basePath.length()+1);

        archive.putArchiveEntry(new ZipArchiveEntry(name + "/"));
        archive.closeArchiveEntry();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
