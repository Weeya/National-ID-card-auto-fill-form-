package idcard;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InsertDataToDB {
    
    private String Nation;
    private char Gender;
    private String FirstNameEN;
    private String LastNameEN;
    private String BirthDay;
    private String BirthDate;
    private String BirthMonth;
    private String BirthYear;
    private String Age;
    private String IDPassport;
    private String IDPerson;
    private String HNNum;
    private String TitleNameEng;
    private String FirstNameTH;
    private String LastNameTH;
    private String Address;
    private String Telephone;
    private String Disease;
    private String Description;
    private ConnectDB connectDB;

    public InsertDataToDB() throws SQLException, ClassNotFoundException, FileNotFoundException, UnsupportedEncodingException{
        
        connectDB = new ConnectDB();

    }
    
    public void start(){

        createFormBirthAndAge();
        try {
            connectDB.insertData(Nation, Gender, FirstNameEN, LastNameEN, BirthDay, Age, IDPassport, IDPerson, HNNum, TitleNameEng, FirstNameTH, LastNameTH, Address, Telephone, Disease, Description);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InsertDataToDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(InsertDataToDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(InsertDataToDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(InsertDataToDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public void createFormBirthAndAge() {
        
        BirthDay = BirthYear + "-" + BirthMonth + "-" + BirthDate;
        Age = Integer.toString(Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(BirthYear));
        
    }
    /**
     * @param Nation the Nation to set
     */
    public void setNation(String Nation) {
        this.Nation = Nation;
    }

    /**
     * @param Gender the Gender to set
     */
    public void setGender(char Gender) {
        this.Gender = Gender;
        
    }

    /**
     * @param FirstNameEN the FirstNameEN to set
     */
    public void setFirstNameEN(String FirstNameEN) {
        this.FirstNameEN = FirstNameEN;
    }

    /**
     * @param LastNameEN the LastNameEN to set
     */
    public void setLastNameEN(String LastNameEN) {
        this.LastNameEN = LastNameEN;
    }

    /**
     * @param BirthDay the BirthDay to set
     */
//    public void setBirthDay(String BirthDay) {
//        this.BirthDay = BirthDay;
//    }

    public void setBirthDate(String BirthDate) {
        this.BirthDate = BirthDate;
    }
    
    public void setBirthMonth(String BirthMonth) {
        this.BirthMonth = BirthMonth;
    }
    
    public void setBirthYear(String BirthYear) {
        this.BirthYear = BirthYear;
    }
    /**
     * @param Age the Age to set
     */
//    public void setAge(String Age) {
//        this.Age = Age;
//    }

    /**
     * @param IDPassport the IDPassport to set
     */
    public void setIDPassport(String IDPassport) {
        this.IDPassport = IDPassport;
    }

    /**
     * @param IDPerson the IDPerson to set
     */
    public void setIDPerson(String IDPerson) {
        this.IDPerson = IDPerson;
    }

    /**
     * @param HNNum the HNNum to set
     */
    public void setHNNum(String HNNum) {
        this.HNNum = HNNum;
    }

    /**
     * @param TitleNameEng the TitleNameEng to set
     */
    public void setTitleNameEng(String TitleNameEng) {
        this.TitleNameEng = TitleNameEng;
    }

    /**
     * @param FirstNameTH the FirstNameTH to set
     */
    public void setFirstNameTH(String FirstNameTH) {
        this.FirstNameTH = FirstNameTH;
    }

    /**
     * @param LastNameTH the LastNameTH to set
     */
    public void setLastNameTH(String LastNameTH) {
        this.LastNameTH = LastNameTH;
    }

    /**
     * @param Address the Address to set
     */
    public void setAddress(String Address) {
        this.Address = Address;
    }

    /**
     * @param Telephone the Telephone to set
     */
    public void setTelephone(String Telephone) {
        this.Telephone = Telephone;
    }

    /**
     * @param Disease the Disease to set
     */
    public void setDisease(String Disease) {
        this.Disease = Disease;
    }

    /**
     * @param Description the Description to set
     */
    public void setDescription(String Description) {
        this.Description = Description;
    }
    
    

    
}
