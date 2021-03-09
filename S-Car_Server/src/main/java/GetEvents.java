import com.example.s_car.Driver;
import com.example.s_car.Event;

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


@WebServlet(urlPatterns = {"/GetEvents"})
public class GetEvents extends HttpServlet {

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
        System.out.println("inside");
        InputStream in = request.getInputStream();
        ObjectInputStream inputFromApp = new ObjectInputStream(in);
        OutputStream outstr = response.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outstr);

        try {
            // response.setContentType("application/x-java-serialized-object");
            int ownerId = (Integer) inputFromApp.readObject();
            System.out.println(ownerId);
            if(ownerId == 0) {
                ownerId = 0;  // default to all
            }
            Class.forName( "com.mysql.cj.jdbc.Driver" );
            Connection  con = DriverManager.getConnection("jdbc:mysql://localhost:3306/scar","root","root" );

            PreparedStatement find = con.prepareStatement("select * from events where ownerId LIKE ?");
            find.setInt(1, ownerId);
            ResultSet rs = find.executeQuery();
            while(rs.next()) {
                System.out.println("sent event");
                Event event = new Event(rs.getInt("eventId"), rs.getInt("ownerId"),rs.getString("title")
                        ,rs.getString("eventDate"),rs.getString("eventTime"),rs.getString("address"),
                        rs.getString("town"),rs.getString("county"));
                oos.writeObject(event);

            }

            rs.close();
            find.close();
            con.close();
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }
        finally{
            Event last = new Event();
            oos.writeObject(last);  // customer with a blank id indicates the last one
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
