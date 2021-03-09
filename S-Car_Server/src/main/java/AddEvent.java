import com.example.s_car.Event;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;


@WebServlet(name = "AddEvent", urlPatterns = {"/AddEvent"})
public class AddEvent extends HttpServlet {

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int result = 0;
        response.setContentType("application/octet-stream");
        InputStream in = request.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(in);
        OutputStream outstr = response.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outstr);

        try {
            Event event = (Event) ois.readObject();
            Class.forName( "com.mysql.cj.jdbc.Driver" );
            Connection  con = DriverManager.getConnection("jdbc:mysql://localhost:3306/scar","root","root" );
            if(event != null && event.getId() == 0) {
                PreparedStatement addToEvents = con.prepareStatement("Insert Into events  Values (null,?,?,?,?,?,?,?)");
                addToEvents.setInt(1,event.getOwnerId());
                addToEvents.setString(2,event.getTitle());
                addToEvents.setString(3,event.getAddress1());
                addToEvents.setString(4,event.getTown());
                addToEvents.setString(5,event.getCounty());
                addToEvents.setString(6, event.getDate());
                addToEvents.setString(7, event.getTime());
                result = addToEvents.executeUpdate();
                addToEvents.close();
                con.close();
            }else if(event != null && event.getId() != 0){
                PreparedStatement updateEvent = con.prepareStatement("UPDATE events  set title =?, address=?,\" +\n" +
                        "                        \"town=?,county=?,eventDate=?,eventTime=? where eventId =?");
                updateEvent.setString(1,event.getTitle());
                updateEvent.setString(2,event.getAddress1());
                updateEvent.setString(3,event.getTown());
                updateEvent.setString(4,event.getCounty());
                updateEvent.setString(5, event.getDate());
                updateEvent.setString(6, event.getTime());
                updateEvent.setInt(7, event.getId());
                result = updateEvent.executeUpdate();
                updateEvent.close();
                con.close();
            }
        }
        catch(Exception ex)        {
            System.out.println(ex.toString());
        }
        finally {
            if(result == 1){
                oos.writeBoolean(true);
            }else {
                oos.writeBoolean(false);
            }
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
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
