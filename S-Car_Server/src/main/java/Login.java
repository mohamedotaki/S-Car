import com.example.s_car.Driver;
import com.example.s_car.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.InetAddress;
import java.sql.*;


@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        System.out.println("1");
         int result;
         response.setContentType("application/octet-stream");
         InputStream in = request.getInputStream();
         ObjectInputStream ois = new ObjectInputStream(in);
         OutputStream outstr = response.getOutputStream();
         ObjectOutputStream oos = new ObjectOutputStream(outstr);

         System.out.println("2");

         try {
            String email = ois.readObject().toString();
             String pass = ois.readObject().toString();
             System.out.println(email +"\n"+pass);
            if(!email.isEmpty() && !pass.isEmpty()) {
                Class.forName( "com.mysql.cj.jdbc.Driver" );
                Connection  con = DriverManager.getConnection("jdbc:mysql://localhost:3306/scar?serverTimezone=UTC","root","root" );
                PreparedStatement checkLogin = con.prepareStatement("SELECT loginId, isOwner FROM login where email like ? and password like ?");
                checkLogin.setString(1, email);
                checkLogin.setString(2, pass);
               ResultSet resultSet = checkLogin.executeQuery();
               resultSet.next();
               result= resultSet.getInt(1);
               boolean isOwner = resultSet.getBoolean(2);
               System.out.println(result);
                if(result>0){
                    if(isOwner){
                        resultSet =null;
                        PreparedStatement getUser = con.prepareStatement("SELECT ownerId , fullName, phoneNumber, carNumber, keyNo, imageId FROM owners where loginId like ?");
                        getUser.setInt(1, result);
                        resultSet = getUser.executeQuery();
                        resultSet.next();
                        User user = new User(resultSet.getInt(1),result,resultSet.getString(2),email,resultSet.getString(3),
                                resultSet.getString(4),pass,resultSet.getString(5),resultSet.getInt(6));
                        oos.writeObject(user);
                        System.out.println("sent");
                    }else {
                        resultSet =null;
                        PreparedStatement getUser = con.prepareStatement("SELECT * FROM drivers where loginId like ?");
                        getUser.setInt(1, result);
                        resultSet = getUser.executeQuery();
                        resultSet.next();
                        Driver driver = new Driver(resultSet.getInt("driverId"),result,resultSet.getInt("ownerId")
                                ,resultSet.getString("fullName"),email,resultSet.getString("phoneNumber"),
                                resultSet.getString("carNumber"),pass,resultSet.getString("keyNo"),resultSet.getInt("imageId")
                                ,resultSet.getString("drivingPermission"));
                        User user = driver;
                        oos.writeObject(user);
                        System.out.println("sent driver");
                    }



                }else oos.writeBoolean(false);
                checkLogin.close();
                con.close();

            }
        } 
        catch(Exception ex)        {
          System.out.println(ex.toString());
        }
        finally {
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
