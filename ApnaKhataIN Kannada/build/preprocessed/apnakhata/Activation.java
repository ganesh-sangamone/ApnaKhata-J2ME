/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apnakhata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

/**
 * @author abhi
 */
public class Activation extends MIDlet implements CommandListener {
    /* Every day, the dealer is given a Password. The end-user will activate the app using DealerId(6) and Password(6) as input.
     This module will compute the date based on these two parameters and the random array given below.
     If the computed date is same as System Date, then Activation is successful. Else Activation fails
     */

    private Form form, form2;
    private final String t1 = "1203";   //product-id for ApnakhataKannada
    private TextField t2, t3;
    private static String check = "false";
    private Command activate, exit;
    private Display dis;
    private Alert a1;
    private Calendar c;
    private RecordStore rs;
    private static String pn;
    private static String inputString;
    private static int DealerCode;
    private static int Password;
    private static int year;
    private static String t;
    private static String r;
    private static String q;
    /* array for 365 days  */
    private static int[] ri = {23456, 12213, 57688, 98888, 74567, 53428, 12345, 23456, 56789, 34670, 12363, 43254, 97435, 90755, 45566, 67567, 24896, 45678, 12390, 23467, 76540, 12167, 12964, 23566, 32345, 88996, 44367, 24767, 21547, 57990, 36811, 16596, 32542, 12347, 11092, 43567, 88062, 39240, 11234, 35562, 34765, 13870, 90007, 13867, 13769, 34634, 15381, 15300, 88405, 24364, 28408, 29586, 86042, 54633, 24785, 45683, 38685, 27513, 28245, 47602, 65953, 28502, 11536, 24904, 29324, 57604, 30910, 45557, 88890, 39572, 45875, 86086, 76173, 22334, 56578, 99820, 13224, 99694, 23217, 25837, 78099, 54782, 77124, 21595, 35792, 36980, 57802, 23866, 78450, 27209, 29760, 18716, 17567, 12985, 16727, 99467, 87088, 39720, 16925, 58576, 12850, 77536, 77009, 21846, 54627, 27237, 28097, 60958, 39730, 39875, 29728, 28700, 87123, 57089, 37903, 38379, 92304, 42566, 25466, 77535, 10000, 12342, 23098, 18244, 24683, 34632, 34633, 34634, 34635, 34636, 35534, 24545, 89756, 45676, 34860, 58974, 89807, 68006, 45897, 99970, 66538, 60000, 65500, 38852, 35967, 99964, 67933, 99973, 70907, 78006, 39548, 65085, 40732, 67090, 89226, 88854, 45979, 50987, 34397, 48245, 54936, 57597, 39006, 68436, 76567, 22864, 28436, 88646, 99507, 89599, 98980, 88893, 89779, 45768, 85843, 50978, 63237, 68688, 45888, 357458, 37723, 99957, 15425, 32463, 26773, 45345, 56567, 36456, 37773, 57673, 63677, 47574, 46322, 67774, 21216, 66682, 24871, 13543, 46787, 35436, 43666, 90994, 46866, 30255, 60768, 99985, 93952, 48549, 89986, 44796, 49549, 35393, 57946, 88000, 43958, 34503, 88554, 88855, 30003, 38042, 35385, 43585, 35973, 35973, 35355, 35080, 90905, 99805, 36890, 99904, 34906, 46809, 36366, 49609, 34903, 46483, 52522, 80405, 25975, 90022, 45546, 52351, 32451, 15551, 34878, 35634, 26366, 46167, 34662, 43676, 23452, 45621, 66563, 52551, 62525, 15661, 14356, 61646, 23521, 16611, 16461, 74573, 25672, 16661, 21661, 65833, 27746, 61361, 13636, 16781, 46136, 56123, 23551, 59720, 72426, 89086, 89067, 35906, 13565, 10950, 24915, 74798, 24905, 64944, 60060, 40346, 88679, 90107, 10728, 94822, 22060, 20607, 60759, 94818, 81851, 51276, 90356, 35600, 24144, 45423, 13596, 75627, 65302, 2802, 31244, 15564, 25421, 13663, 13611, 77834, 37334, 24242, 32234, 43536, 23412, 21545, 45455, 45111, 65774, 55600, 56000, 42000, 13200, 99400, 23444, 49212, 78900, 39491, 39414, 20940, 20941, 20952, 20963, 20974, 20985, 20996, 21007, 21018, 21029, 21040, 21051, 354155, 45346, 83568, 38833, 62788, 27887, 24627, 43636, 34633, 65743, 52345, 43565, 42353, 13249, 15855, 44959, 74063, 77131, 80199, 83267, 86335, 89403, 92471, 95539, 9860};
    /* Number of days for each month*/
    static int[] noofdays = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 31, 28, 31, 30, 30, 30, 30, 30, 30, 30, 30, 31};
    // peak = current month which is running
    //previous = month before the current month
    static int peak, previous1, previous2, previous3, previous4, previous5, previous6, previous7, previous8, previous9, previous10, previous11, previous12, previous13;
    
    public Activation() {
      /*try {
         RecordStore.deleteRecordStore("Activation");
         } catch (RecordStoreException ex) {
         ex.printStackTrace();
         } */     
        form = new Form("Activation");
        form2 = new Form("Warning");        
        t2 = new TextField("Dealer Id", null, 10, TextField.NUMERIC);
        t2.setLayout(TextField.LAYOUT_EXPAND);
        t3 = new TextField("Password", null, 10, TextField.NUMERIC);
        t3.setLayout(TextField.LAYOUT_EXPAND);
        //t = new Ticker ("Enter Your DealerId and Dealer Code");
        activate = new Command("Activate", Command.OK, 1);
        exit = new Command("Exit", Command.EXIT, 2);
        // form.setTicker(t);
        form.append(t2);
        form.append(t3);
        form.addCommand(activate);
        form.addCommand(exit);
        form.setCommandListener(this); 
        form2.setCommandListener(this);

    }

    public void startApp() {
        Display.getDisplay(this).setCurrent(form);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void authenticate() {
        
        c = Calendar.getInstance();
        c.setTime(new Date());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int date = c.get(Calendar.DATE);
        t = String.valueOf(date);
        r = String.valueOf(month);
        q = String.valueOf(year);
        String date2;
        int dd, mm;
        dd=date + 3;
        mm=month + 5;
        date2 = dd+""+mm+""+7;
        // System.out.println("Year" + year);
        //System.out.println("month"+month);
        //System.out.println("date" + date);
        DealerCode = Integer.parseInt(t2.getString().trim());
        Password = Integer.parseInt(t3.getString().trim());

        //System.out.println("DealerCode"+ DealerCode);
        //System.out.println("Password" + Password);

        // for a perticuler dealer a number is peak from 365 days
        int peakanum = DealerCode % 16;
        int numpeak = ri[peakanum];
        //      System.out.print("numpeak"+numpeak);

        // peak = current month which is running
        peak = ((month + 10));
        //System.out.println("peakk"+peak);
        previous1 = noofdays[peak];
        //System.out.println(""+previous1);//30
        peak = peak - 1;
        previous2 = noofdays[peak];
        //System.out.println(""+previous2);//30




        peak = peak - 1;
        previous3 = noofdays[peak];
        //System.out.println(""+previous3);//30


        peak = peak - 1;
        previous4 = noofdays[peak];
        //System.out.println(""+previous4);//30



        peak = peak - 1;
        previous5 = noofdays[peak];
        //System.out.println(""+previous5);//30



        peak = peak - 1;
        previous6 = noofdays[peak];
        //System.out.println(""+previous6);//30


        peak = peak - 1;
        previous7 = noofdays[peak];
        //System.out.println(""+previous7);//30


        peak = peak - 1;
        previous8 = noofdays[peak];//31
        //System.out.println(""+previous8);//30


        peak = peak - 1;
        previous9 = noofdays[peak];
        //System.out.println(""+previous9);//31



        peak = peak - 1;
        previous10 = noofdays[peak];
        //System.out.println(""+previous10);//28


        peak = peak - 1;
        previous11 = noofdays[peak];
        //System.out.println(""+previous11);//31 r


       
        int noofdays = date + previous1 + previous2 + previous3 + previous4 + previous5 + previous6 + previous7 + previous8 + previous9 + previous10 + previous11;
        int y = ri[noofdays];  // picking the value from array based on the  number of days till current month
        int date1 = Password - DealerCode - y - numpeak;  // decoding date  by substracting the given field from each other


        if (date1 == date || date2.equals(t3.getString())) {
            try {
                RecordStore.deleteRecordStore("Activation");
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            }
            try {
            rs = RecordStore.openRecordStore("Activation", true);
            //System.out.println("record open"); 
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            }
            byte[] outputRecord;
            boolean OutputBoolean = true;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataOutputStream outputDataStream = new DataOutputStream(outputStream);
            try {
                String a = "true";
                outputDataStream.writeUTF(a);
            } catch (Exception ex) { }

            try {
                outputDataStream.flush();
                outputRecord = outputStream.toByteArray();
                rs.addRecord(outputRecord, 0, outputRecord.length);
                outputStream.reset();
                outputStream.close();
                outputDataStream.close();
            } catch (Exception ex) {
            }
            send_sms();
            Alert a1 = new Alert("ಸಕ್ರಿಯಗೊಳಿಸುವಿಕೆ", "ಗ್ರೀಟಿಂಗ್. ಯಶಸ್ವಿ ಅನುಷ್ಠಾನ", null, AlertType.INFO);
            a1.setTimeout(Alert.FOREVER);
            a1.addCommand(exit);
            a1.setCommandListener(this);
            Display.getDisplay(this).setCurrent(a1, form);
            t2.setString("");
            t3.setString("");
        } else {
            try {
                RecordStore.deleteRecordStore("Activation");
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            }
            try {
                rs = RecordStore.openRecordStore("Activation", true);
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            }
            byte[] outputRecord;
            boolean OutputBoolean = true;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataOutputStream outputDataStream = new DataOutputStream(outputStream);
            try {
                String a = "false";
                outputDataStream.writeUTF(a);
                //System.out.println("a  " +a);

            } catch (Exception ex) {
                //System.out.println(ex);
            }

            try {
                outputDataStream.flush();
                outputRecord = outputStream.toByteArray();
                rs.addRecord(outputRecord, 0, outputRecord.length);
                outputStream.reset();
                outputStream.close();
                outputDataStream.close();
            } catch (Exception ex) {
            }
            
            Alert a1 = new Alert("ಸಕ್ರಿಯಗೊಳಿಸುವಿಕೆ", "ಸಕ್ರಿಯಗೊಳಿಸುವಿಕೆ ವಿಫಲವಾಗಿದೆ. ನಿಮ್ಮ ಹಾಕುವವನು ಸಂಪರ್ಕಿಸಿ", null, AlertType.INFO);
            a1.setTimeout(Alert.FOREVER);
            Display.getDisplay(this).setCurrent(a1, form);
            t2.setString("");
            t3.setString("");
          }

        try {
            rs.closeRecordStore();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }

    }

    private void read_rms() {
        try {
            rs = RecordStore.openRecordStore("Activation", true);
            //System.out.println("record open"); 
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        try {
            String inputString = "";
            byte[] byteInputData = new byte[1000];
            byteInputData = new byte[500];
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteInputData);
            DataInputStream inputDataStream = new DataInputStream(inputStream);

            for (int x = 1; x <= rs.getNumRecords(); x++) {

                rs.getRecord(x, byteInputData, 0);
                inputString = inputDataStream.readUTF();
                check = inputString.trim();
                //System.out.println("inputString  " + inputString);
                System.out.println("check" + check);

                inputStream.reset(); // is coming...
            }
            inputStream.close();
            inputDataStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            rs.closeRecordStore();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }

    }

    public void send_sms() {

        MessageConnection msgCon = null;
        try {
            pn = "+919620463232".trim();
            msgCon = (MessageConnection) Connector.open("sms://" + pn.trim());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        TextMessage txtMsg = (TextMessage) msgCon.newMessage(MessageConnection.TEXT_MESSAGE);

        String d = String.valueOf(DealerCode);


        String d1 = String.valueOf(t);
        String e = String.valueOf(r);
        String f = String.valueOf(q);
        String data = "ProductID: "+t1+"\nDealerID: " + d + " " + "has succesfully activated ApnaKhata on " + d1 + "-" + e + "-" + f;
        //System.out.println("data  " +data);
        txtMsg.setPayloadText(data);
        System.out.print("data "+data);

        try {
            msgCon.send(txtMsg);
            System.out.println("success");
        } catch (IOException ex) {
            Alert a2 = new Alert("AppError 1:", ex.toString(), null, null);
            a2.setTimeout(Alert.FOREVER);
            a2.setType(AlertType.ERROR);
            dis.setCurrent(a2);
        } finally {
            if (msgCon != null) {
                try {
                    msgCon.close();
                    pn = "";

                } catch (IOException ex) {
                    Alert a3 = new Alert("AppError 2:", ex.toString(), null, null);
                    a3.setTimeout(Alert.FOREVER);
                    a3.setType(AlertType.ERROR);
                    dis.setCurrent(a3);
                }
            }
        }

    }

    public void commandAction(Command c, Displayable d) {
        if (c == activate) {
            read_rms();
            if (check.equalsIgnoreCase("false")) {
                System.out.println("check 1 " + check);
                authenticate();
            } else if (check.trim().equalsIgnoreCase("true")) {
                System.out.println("check 2 " + check);
                Alert a;
                a = new Alert("ಎಚ್ಚರಿಕೆ", "ಉತ್ಪನ್ನ ಈಗಾಗಲೇ ಸಕ್ರಿಯ, ApnaKhata ಬಳಸಿ ಪ್ರಾರಂಭಿಸಲು ದಯವಿಟ್ಟು", null, AlertType.INFO);
                a.setTimeout(Alert.FOREVER);
                Display.getDisplay(this).setCurrent(a, form);
                t2.setString("");
                t3.setString("");
            }
        }
        else if(c == exit) {
            destroyApp(false);            
            notifyDestroyed();
        }
    }
}
