import com.example.s_car.Driver;
import com.example.s_car.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(name = "AddDriver", urlPatterns = {"/AddDriver"})
public class AddDriver extends HttpServlet {

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

        String mess = null;
        response.setContentType("application/octet-stream");
        InputStream in = request.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(in);
        OutputStream outstr = response.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(outstr);

        try {
          Driver driver = (Driver) ois.readObject();

            if(driver != null) {
                Class.forName( "com.mysql.cj.jdbc.Driver" );
                Connection  con = DriverManager.getConnection("jdbc:mysql://localhost:3306/scar","root","root" );
                PreparedStatement addToLogin = con.prepareStatement("Insert Into login  Values (null,?,?,false)", Statement.RETURN_GENERATED_KEYS);
                addToLogin.setString(1,driver.getEmailAddress());
                addToLogin.setString(2,driver.getPassword());
                int rs = addToLogin.executeUpdate();
                ResultSet resultSet = addToLogin.getGeneratedKeys();
                int loginID = 0;
                if(resultSet.next() && rs >0){
                    rs =0;
                    loginID = resultSet.getInt(1);
                    PreparedStatement addToDriver = con.prepareStatement("Insert Into drivers  Values (null,?,?,?,?,?,?,?,?)");
                    addToDriver.setInt(1,loginID);
                    addToDriver.setInt(2,driver.getOwnerId());
                    addToDriver.setString(3,driver.getName());
                    addToDriver.setString(4,driver.getPhoneNumber());
                    addToDriver.setString(5,driver.getCarNumber());
                    addToDriver.setString(6,driver.getCarKey());
                    addToDriver.setString(7,driver.getDrivingPermission());
                    addToDriver.setInt(8,driver.getImageId());
                    rs = addToDriver.executeUpdate();
                    addToDriver.close();
                    if(rs >0){
                       mess = "Driver was added";
                    }else{
                        PreparedStatement delete = con.prepareStatement("DELETE FROM login  where loginId like ?");
                        delete.setInt(1,loginID);
                        mess = "Nothing was added";
                    }
                }else{
                    mess = "Nothing was added";
                }
                addToLogin.close();
                con.close();
            }
        }
        catch(Exception ex)        {
            System.out.println(ex.toString());
        }
        finally {
            oos.writeObject(mess);
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
