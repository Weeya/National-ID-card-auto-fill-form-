package idcard;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import lib.RDNIDlibD;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IDCard {

    private int readerInst;
    private int IDpos = 13;
    private int cntType = 23; //type of info
    private int diffVal = 3424; //(unicode - Window874)
    
    //--------minimum size of array--------
    private int rListSize = 1000; //minimum size of rList[]
    private int NIDNumSize = 14; //minimum size of NIDNumber[]
    private int NIDTextSize = 1024; //minimum size of NIDText[]
    private int strDataSize = 512; //minimum size of strData[]
    private int ridDataSize = 256; //minimum size of ridData[]
    
    private int statusOpen;
    private int statusReaderList;
    private int statusReaderInfo;
    private int statusConnect;
    private int statusIDNum;
    private int statusText;
    private int statusInfo;
    private int statusUpdateLicense;
    private int statusDeselectReader;
    private int statusDisconnectReader;
    private int statusCloseReader;
    private int statusLicenseInfo;
    private int set;
    
    private byte[] rList;
    private byte[] NIDText;
    private byte[] softwareInfo;
    private byte[] ridData;
    private byte[] NIDNumber;
    private byte[] strData;
    private String[] Info;
    private List<Integer> intText;
    public Map<String, String> InfoDict;
    public RDNIDlibD func;
    
    
    private static String path = "D:\\intern CTI\\IDCard\\";
    
    
    public IDCard() {
        
        func = (RDNIDlibD) Native.loadLibrary("RDNIDlibD", RDNIDlibD.class);
    }

    public static void main(String[] args) throws UnsupportedEncodingException, SQLException, ClassNotFoundException, FileNotFoundException {
        
        NativeLibrary.addSearchPath("RDNIDlibD", path);
        new IDCard();
    }

    //<editor-fold defaultstate="collapsed" desc="status return code">
    /*
        return code | meaning
        ____________|__________________
        0           | success
        -1          | internal error
        -2          | invalid license
        -3          | reader not found
        -4          | connection error
        -5          | get photo error
        -6          | get text error
        -7          | invalid card
        -8          | unknown card version
        -9          | disconnect error
        -10         | init erro
        -11         | reader not support
        -12         | license file error
        -13         | parameter error
        -15         | internet error
        -16         | card not found
        -18         | license uppdate error
    
    */
    //</editor-fold>
    
    public void openReader() {

        //--------open NID Card DLL--------
        statusOpen = (int) func.openNIDLibRD(path + "RDNIDLib.DLD");
        //System.out.println("open status(0 ready to work): " + Integer.toString(statusOpen));

        //--------list name of reader list--------
        getReaderList();
        
        //--------select which reader--------
        SelectReader();

        
        //--------get reader's information-------- 
        getReaderInfo();
        
        //--------connect card if '0' = connect-------- 
        ConnectCard();
        
    }
    
    public void getReaderList(){
        
        //--------list name of reader list--------
        rList = new byte[rListSize];
        statusReaderList = func.getReaderListRD(rList, rListSize); 
//        System.out.print("reader list: "); //if have more than 1***
        
        for (int i = 0; rList[i] != '\0'; i++) {
//            System.out.print(Character.toString((char) rList[i])); //delete '0'
        }
        System.out.println();
        
    }
    
    public void SelectReader(){
        
        //--------select which reader--------
        readerInst = func.selectReaderRD(rList);
        
    }
    
    public void getReaderInfo(){
        
        //--------get reader's information--------
        ridData = new byte[ridDataSize];
        int temp = 0;
        statusReaderInfo = func.getRidDD(readerInst, ridData);
//        System.out.print("reader's information: ");
        for (int i = 0; ridData[i] != '\0'; i++) {
            //System.out.print(ridData[i] + " ");
            temp = Byte.toUnsignedInt(ridData[i]);
//            System.out.print(Integer.toBinaryString(temp) + " ");
        }
        System.out.println();
//        System.out.println("select reader(+ readerInst): " + readerInst);

    }
    
    public void ConnectCard(){
        
        //--------connect card if '0' = connect--------
        statusConnect = func.connectCardRD(readerInst);
//        System.out.println("status connection(-11 not support,0 success): " + statusConnect);
        
    }
    
    public void getLicenseInfo(){
        
        statusLicenseInfo = func.getLicenseInfoRD(strData);
        
    }
    
    public String getIDNum() {
        
        //--------get array NIDNumber--------
        NIDNumber = new byte[NIDNumSize];
        statusIDNum = func.getNIDNumberRD(readerInst, NIDNumber);
        
        //--------init value to string array--------
        String idNum = "";
        
        for (int i = 0; i < IDpos; i++) {
            idNum += (char) NIDNumber[i];
        }
        System.out.println(idNum);
        return idNum;
        
    }

    public void getText() {
        
        NIDText = new byte[NIDTextSize];
        intText = new ArrayList<>();

        statusText = func.getNIDTextRD(readerInst, NIDText, NIDTextSize);
        
        for (int i = 0; NIDText[i] != '\0'; i++) {
            
            if (NIDText[i] < 0) { //Thai (-128) to (-1) --> unsigned --> Window 874
                intText.add(Byte.toUnsignedInt(NIDText[i]) + diffVal);
            } else {              //English (0) to (127) --> ascii == unicode
                intText.add(Byte.toUnsignedInt(NIDText[i]));
            }
        } 
        
        //---------remove all space from int list
        intText.removeIf((Integer i) -> {
            return i == ' ';
        });
        
        //----- change integer to String
        intToStrArr();
        
    }

    public void intToStrArr(){
    
        Info = new String[cntType];
        
        //------------init "" to String[]
        for (int i = 0; i < cntType; i++) {
            Info[i] = "";
        }
        
        set = 0;
        int tempInt;
        for (int i = 0; i < intText.size(); i++) {

            //--------separate info by '#'
            if (intText.get(i) == '#') {
                set++;
            } else { 
                tempInt = intText.get(i);
                Info[set] += (Character.toString((char) tempInt));
            }
        }
        
        //----- gender: male = 1, female = 2
        getGender();
        
        //----- format yyyymmdd split to yyyy, mm, dd
        getDayMonthYear();
        
        //----- all data(String array) -> dict
        getInfoDict();
        
    }
    
    public void getGender(){

        if("1".equals(Info[17])){
            Info[17] = "M";
        } else {
            Info[17] = "F";
        }
        
    }
    
    public void getDayMonthYear(){
        
        char [] tempChar = Info[18].toCharArray();
        
        //------year
        Info[19] = Info[18].substring(0,4);
        //------month
        Info[20] = Info[18].substring(4,6);
        //------date
        Info[21] = Info[18].substring(6,8);
        
    }
    
    public Map<String, String> getInfoDict() {
        
        InfoDict = new HashMap<>();
        
        InfoDict.put("IDNumber", Info[0]);
        InfoDict.put("TitleNameTH", Info[1]);
        InfoDict.put("FirstNameTH", Info[2]);
        InfoDict.put("MiddleNameTH", Info[3]);
        InfoDict.put("LastNameTH", Info[4]);
        InfoDict.put("TitleNameEN", Info[5]);
        InfoDict.put("FirstNameEN", Info[6]);
        InfoDict.put("MiddleNameEN", Info[7]);
        InfoDict.put("LastNameEN", Info[8]);
        
        InfoDict.put("HOME_NO", Info[9]); 
        InfoDict.put("MOO", Info[10]);
        InfoDict.put("TROK", Info[11]);
        InfoDict.put("SOI", Info[12]);
        InfoDict.put("ROAD", Info[13]);
        InfoDict.put("TUMBON", Info[14]);
        InfoDict.put("AMPHOE", Info[15]);
        InfoDict.put("PROVINCE", Info[16]);
        InfoDict.put("Gender", Info[17]);
        InfoDict.put("BirthYear", Info[19]);
        InfoDict.put("BirthMonth", Info[20]);
        InfoDict.put("BirthDate", Info[21]);

        return InfoDict;

    }
            
    public void getSoftwareInfo(){
        
        //--------get library info--------
        softwareInfo = new byte[strDataSize];
        statusInfo = func.getSoftwareInfoRD(softwareInfo);
        System.out.print("Library information: ");
        
        for (int i = 0; softwareInfo[i] != '\0'; i++) {
            System.out.print((char) softwareInfo[i]);
        }
        System.out.println();
        
    }
    
    public void updateLicense(){
        
        //--------update license file--------
        statusUpdateLicense = (int) func.updateLicenseFileRD("D:\\intern CTI\\IDCard\\RDNIDLib.DLD");
        System.out.println("uodate license status: " + statusUpdateLicense);
    }
            
    public void DisconnectReader(){
        
        //--------disconnect card--------
        statusDisconnectReader = func.disconnectCardRD(readerInst);
        System.out.println("disconnect status: " + statusDisconnectReader);
        
    }
  
    public void DeselectReader(){
        
        //--------deselect--------
        statusDeselectReader = func.deselectReaderRD(readerInst);
        System.out.println("deselect status: " + statusDeselectReader);
        
    }
     
    public void CloseReader(){
        
        //--------close NID--------
        statusCloseReader = func.closeNIDLibRD();
        System.out.println("close status: " + statusCloseReader);
        
    }

}
