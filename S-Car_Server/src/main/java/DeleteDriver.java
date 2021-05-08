import com.example.s_car.Driver;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


@WebServlet(urlPatterns = {"/DeleteDrivers"})
public class DeleteDriver extends HttpServlet {

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
        response.setContentType("application/octet-stream");

        boolean result = false;
        InputStream in = request.getInputStream();
        ObjectInputStream inputFromApp = new ObjectInputStream(in);
        OutputStream outstr = response.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outstr);

        try {
            // response.setContentType("application/x-java-serialized-object");
            Driver driver = (Driver) inputFromApp.readObject();

            Class.forName( "com.mysql.cj.jdbc.Driver" );
            Connection  con = DriverManager.getConnection("jdbc:mysql://localhost:3306/scar?serverTimezone=UTC","root","root" );

            PreparedStatement deleteDriver = con.prepareStatement("delete from drivers  where driverId =?;");
            deleteDriver.setInt(1,driver.getId());
            System.out.println("out");
            if(deleteDriver.executeUpdate()>0){
                System.out.println("inside");
                deleteDriver.close();
                PreparedStatement deleteDriverLogin = con.prepareStatement("delete from login  where loginId =?;");
                deleteDriverLogin.setInt(1,driver.getLoginID());
                if(deleteDriverLogin.executeUpdate()>0) {
                    System.out.println("done");
                    result = true;
                    deleteDriverLogin.close();
                }
            }
            System.out.println("nothing");


            con.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }
        finally{
            oos.writeBoolean(result);
            oos.flush();
            oos.close();

        }

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
