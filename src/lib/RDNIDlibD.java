package lib;

import com.sun.jna.Library;

public interface RDNIDlibD extends Library {

    int openNIDLibRD(String licFilename);

    int closeNIDLibRD();

    int getReaderListRD(byte rList[], int rListSize);

    int selectReaderRD(byte reader[]);

    int deselectReaderRD(int readerInst);

    int connectCardRD(int readerInst);

    int disconnectCardRD(int readerInst);

    int getNIDNumberRD(int readerInst, byte NIDNumber[]);

    int getNIDTextRD(int readerInst, byte NIDText[], int NIDTextSize);

    int getNIDPhotoRD(int readerInst, byte NIDPhoto[], int NIDPhotoSize);

    int getSoftwareInfoRD(byte strData[]);

    int getLicenseInfoRD(byte strData[]);

    int updateLicenseFileRD(String LicFilename);

    int getRidDD(int readerInst, byte ridData[]);

}
