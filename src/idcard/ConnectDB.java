package idcard;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;  

public class ConnectDB {
    
    //-----url, username, password
    private String url = "jdbc:mysql://localhost:3306/patient_dentiiscan";
    private String userName = "root";
    private String password = "admin";
    private String query;
    
    private int Patient_ID; //<<-- auto increment
    private String reportDate;

    Connection con;
    PreparedStatement st;
    
    public ConnectDB() throws SQLException, ClassNotFoundException{
        
        query = "insert into patient values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    }
    
    public void getCurrentDate(){
        
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();        

        reportDate = df.format(today);
        
    }
    
    public void insertData(String Nation,char Gender,String FirstNameEN,String LastNameEN,String BirthDay,String Age,String IDPassport,String IDPerson
            ,String HNNum,String TitleNameEng,String FirstNameTH,String LastNameTH,String Address,String Telephone,String Disease,String Description) throws ClassNotFoundException, SQLException, FileNotFoundException, UnsupportedEncodingException{
        
        getCurrentDate();

        //----- load and register driver
        loadAndRegister();
        
        //----- create connection
        createConnection();
        
        //----- prepare statement
        st = con.prepareStatement(query);
        
        //-----set statement
        //----- not null column
        st.setInt(1,Patient_ID);
        st.setString(2,Nation);
        st.setString(6,String.valueOf(Gender));
        st.setString(8,FirstNameEN);
        st.setString(9,LastNameEN);
        st.setString(4, IDPerson);
        st.setString(12,BirthDay);
        st.setString(13,Age);
        st.setString(18,reportDate);
        st.setString(19,"null");
        
        st.setString(7,TitleNameEng);
        st.setString(3, IDPassport);
        st.setString(10,FirstNameTH);
        st.setString(11, LastNameTH);
        st.setString(14, Address);
        st.setString(5, HNNum);
        st.setString(15, Telephone);
        st.setString(16, Disease);
        st.setString(17, Description);
        
        
        //----- execute
        execute();
                
    }
    
    public void loadAndRegister() throws ClassNotFoundException{
        
        //-----load and register driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        
    }
    
    public void createConnection() throws SQLException{
    
        //-----create connection
        con = DriverManager.getConnection(url, userName, password);
        
    }
    
    public void execute() throws SQLException{
        
        //-----execute
        int cnt = st.executeUpdate(); //use with prepare statement
        
    }
    
    public void closeConnection() throws SQLException{
        
        //-----close connection
        st.close();
        con.close();
        
    }
    
}
