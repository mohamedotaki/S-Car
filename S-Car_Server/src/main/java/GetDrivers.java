
import com.example.s_car.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = {"/GetDrivers"})
public class GetDrivers extends HttpServlet {

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

        InputStream in = request.getInputStream();
        ObjectInputStream inputFromApp = new ObjectInputStream(in);
        OutputStream outstr = response.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outstr);

        try {
            // response.setContentType("application/x-java-serialized-object");
            String carKey = (String)inputFromApp.readObject();
            System.out.println(carKey);
            if(carKey == null) {
                carKey = "";  // default to all
            }
            Class.forName( "com.mysql.cj.jdbc.Driver" );
            Connection  con = DriverManager.getConnection("jdbc:mysql://localhost:3306/scar","root","root" );

            PreparedStatement find = con.prepareStatement("select * from drivers , login where keyNo LIKE ? AND isOwner LIKE ? AND login.loginId LIKE drivers.loginId   ;");
            find.setString(1, carKey);
            find.setInt(2, 0);
            ResultSet rs = find.executeQuery();
            while(rs.next()) {
                System.out.println(rs.getString(3));
                User user = new User(rs.getInt("driverId"), rs.getInt("loginId"),rs.getString("fullName"),rs.getString("email"),
                        rs.getString("phoneNumber"),rs.getString("carNumber"),rs.getString("password"),rs.getString("keyNo"),
                        rs.getBoolean("isOwner"),rs.getString("drivingPermission"));
                oos.writeObject(user);
                System.out.println(user);

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
            User last = new User();
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
