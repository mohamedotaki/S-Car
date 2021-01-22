

import com.example.s_car.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.*;


@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class Register extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultSet resultSet = null;
        int result = 0;
        String err = "";
         response.setContentType("application/octet-stream");
         InputStream in = request.getInputStream();
         ObjectInputStream ois = new ObjectInputStream(in);
         OutputStream outstr = response.getOutputStream();
         ObjectOutputStream oos = new ObjectOutputStream(outstr);

         try {
             User user = (User) ois.readObject();
            if(user != null) {
                Class.forName( "com.mysql.cj.jdbc.Driver" );
                Connection  con = DriverManager.getConnection("jdbc:mysql://localhost:3306/scar","root","root" );
                PreparedStatement checkOwner = con.prepareStatement("SELECT count(*) FROM drivers , login where carNumber like ? or email like ?");
                checkOwner.setNString(1, user.getCarNumber());
                checkOwner.setNString(2, user.getEmailAddress());
                resultSet = checkOwner.executeQuery();

                resultSet.next();
                if(resultSet.getInt(1) <= 0) {
                    checkOwner.close();
                    PreparedStatement addOwnerLogin = con.prepareStatement("INSERT INTO login VALUE (null ,?,?)");
                    addOwnerLogin.setNString(1, user.getEmailAddress());
                    addOwnerLogin.setNString(2, user.getPassword());
                    result = addOwnerLogin.executeUpdate();
                    addOwnerLogin.close();

                    if (result > 0) {
                        result = 0;
                        PreparedStatement getOwnerId = con.prepareStatement("SELECT loginId FROM login WHERE email like ?");
                        getOwnerId.setNString(1, user.getEmailAddress());
                        resultSet = getOwnerId.executeQuery();
                        resultSet.next();
                        int loginID = resultSet.getInt("loginId");
                        System.out.println(loginID);
                        getOwnerId.close();
                        if(loginID !=0){
                            PreparedStatement addOwner = con.prepareStatement("INSERT INTO drivers VALUE (null,?,?,?,?,?,?,?)");
                            addOwner.setInt(1, loginID);
                            addOwner.setNString(2, user.getName());
                            addOwner.setNString(3, user.getPhoneNumber());
                            addOwner.setNString(4, user.getCarNumber());
                            addOwner.setNString(5, user.getCarKey());
                            addOwner.setBoolean(6, user.isOwner());
                            addOwner.setDate(7, (Date) user.getDrivingPermission());
                            result = addOwner.executeUpdate();

                            addOwner.close();
                            err = "registered";
                        }else{
                            PreparedStatement delete = con.prepareStatement("DELETE FROM login where email like ?");
                            delete.setNString(1, user.getEmailAddress());
                            result = delete.executeUpdate();
                            delete.close();
                            err = "Please try again later";
                        }

                    }else{
                        //owner wasent inserted
                        err = "Please try again later";
                    }
                }else{
                    //owner has been added before email or car no
                    err = "Email or Car Number is been used by different account";
                    checkOwner.close();
                }

                con.close();

            }
        } 
        catch(Exception ex)        {
          System.out.println(ex.toString());
        }
        finally {
             oos.writeObject(err);
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
