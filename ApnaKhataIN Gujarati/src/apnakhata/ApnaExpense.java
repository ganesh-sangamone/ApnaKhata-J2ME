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
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class ApnaExpense extends MIDlet implements CommandListener {

    private List lst1, settiingslst1, settiingslst2, yearlst, monthlst, daylst;
    private static int counter;
    private Display display;
    private TextField txtDetail, txtAmount, txtReport, txtDate, txtMonth, txtYear;
    private Command save, submit, exit, settingsnext1, settingssave1, back1, settingsback2, settingsexit1, settingsexit2, submit3, back3;
    private Form form1, form2, form3;
    private Hashtable hd;
    private static Vector db = new Vector();
    private static String settings_cur, settings_month, cur, mon;
    private static String ss;
    private RecordStore rs;

    private Alert alert;
    private Calendar cl;

    public ApnaExpense() {
        /*try {
         RecordStore.deleteRecordStore("Reports");
         } catch (RecordStoreException ex) {
         ex.printStackTrace();
         }*/
        exit = new Command("Exit", Command.EXIT, 1);
        submit = new Command("Submit", Command.SCREEN, 1);
        lst1 = new List("ApnaExpense", List.IMPLICIT);
        lst1.append("Enter Expense", null);
        lst1.append("Report", null);
        lst1.append("Settings", null);
        lst1.addCommand(exit);
        lst1.addCommand(submit);
        display = Display.getDisplay(this);
        lst1.setSelectCommand(submit);
        lst1.setCommandListener(this);

    }

    public void startApp() {
        display.setCurrent(lst1);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {

        if (c == submit) {
            
            if (lst1.getSelectedIndex() == 0) {
                read_settings();
                if(cur !=null && mon !=null) {
                    form1_operation();   // expense form gui
                }
                else {
//                    Alert a;
//                    a = new Alert("Warning","Please set Date and Currency from the settings", null, AlertType.WARNING);
//                    a.setTimeout(Alert.FOREVER);
//                    display.setCurrent(a);
                    
                    settings_cur = "INR";
                settings_month="word";
                write_settings();
                }
            } else if (lst1.getSelectedIndex() == 1) {
                read_rms();
                yearlst_operation();  // report gui
            } else {
                settings_operation1();
            }
        } else if (c == exit) {
            try {
                destroyApp(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            notifyDestroyed();
        } else if (c == save) {
            if (txtDetail.getString().equals("") || txtAmount.getString().equals("")) {
                Alert a;
                a = new Alert("Warning", "Enter any expense", null, AlertType.INFO);
                a.setTimeout(2000);
                display.setCurrent(a);
            } else {
                expense_function();  //expense entry form, save operation
            }
        } else if (c == back1) {
            Display.getDisplay(this).setCurrent(lst1); //ApnaExpense screen
            db.setSize(0);
        } /*else if(c == submit2) {
         * read_rms();
         * if(lst2.getSelectedIndex() == 0) {
         * 
         * yearlst_operation(); // Gui for yearly
         * }
         * else if(lst2.getSelectedIndex() == 1) {
         * monthlst_operation();
         * }
         * else if(lst2.getSelectedIndex() == 2) {
         * dailylst_operation();
         * }
         * }
        
         else if(c == back2) {
         Display.getDisplay(this).setCurrent(lst1); //ApnaExpense screen
         db.setSize(0);
         }*/ else if (c == submit3) {
            form_year_operation();  //yearly computation
        } else if (c == back3) {
            Display.getDisplay(this).setCurrent(lst1); //report selection screen
            db.setSize(0);
        } else if (c == settingsnext1) {
            settings_operation2();
        } else if (c == settingsback2) {
            Display.getDisplay(this).setCurrent(settiingslst1);
        } else if (c == settingssave1) {
            settings_cur = settiingslst1.getString(settiingslst1.getSelectedIndex());
            if (settiingslst2.getSelectedIndex() == 0) {
                settings_month = "number";
            } else {
                settings_month = "word";
            }
            write_settings();
        } else if (c == settingsexit1) {
            Display.getDisplay(this).setCurrent(lst1);
        } else if (c == settingsexit2) {
            Display.getDisplay(this).setCurrent(lst1);
        }
    }

    private void form1_operation() {

        form1 = new Form("Enter Expense");
        cl = Calendar.getInstance();
        cl.setTime(new Date());
        int year = cl.get(Calendar.YEAR);
        int month = cl.get(Calendar.MONTH) + 1;
        int date = cl.get(Calendar.DATE);

        txtYear = new TextField("Year  ", String.valueOf(year), 4, TextField.NUMERIC);
        txtMonth = new TextField("Month", String.valueOf(month), 2, TextField.NUMERIC);
        txtDate = new TextField("Date  ", String.valueOf(date), 2, TextField.NUMERIC);
        txtDetail = new TextField("Details", "", 100, TextField.ANY);
        txtDetail.setLayout(TextField.LAYOUT_VEXPAND);
        txtYear.setLayout(TextField.LAYOUT_EXPAND);

        txtMonth.setLayout(TextField.LAYOUT_EXPAND);
        txtDate.setLayout(TextField.LAYOUT_EXPAND);
        txtAmount = new TextField("Amount", "", 7, TextField.DECIMAL);
        txtAmount.setLayout(TextField.LAYOUT_EXPAND);
        save = new Command("Save", Command.SCREEN, 1);
        back1 = new Command("Back", Command.BACK, 1);
        form1.append(txtYear);
        form1.append(txtMonth);
        form1.append(txtDate);
        form1.append(txtDetail);
        form1.append(txtAmount);
        form1.addCommand(back1);
        form1.addCommand(save);
        Display.getDisplay(this).setCurrent(form1);
        form1.setCommandListener(this);

    }

    private void expense_function() {

        String a4 = "(" + txtDetail.getString() + ")";         
        String a1 = "#" + txtYear.getString() + "#";
        String a2 = "$" + txtMonth.getString() + "$";
        String a3 = "%" + txtDate.getString() + "%";
        String a5 = "<" + txtAmount.getString() + ">";
        String all = a1 + " " + a2 + " " + a3 + " " + a4 + " " + a5;
        try {
            rs = RecordStore.openRecordStore("Reports", true);
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        byte[] outputRecord;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream outputDataStream = new DataOutputStream(outputStream);
        try {
            outputDataStream.writeUTF(all);  
        } catch (Exception ex) {
            System.out.println(ex);
        }
        Alert a = new Alert("Message", "Data Saved...", null, AlertType.INFO);
        a.setTimeout(1000);
        display.setCurrent(a);
        txtDetail.setString("");
        txtAmount.setString("");
        try {
            outputDataStream.flush();
            outputRecord = outputStream.toByteArray();
            rs.addRecord(outputRecord, 0, outputRecord.length);
            outputStream.reset();
            outputStream.close();
            outputDataStream.close();
            rs.closeRecordStore();
        } catch (Exception ex) {
        }
    }

    private void read_rms() {
        try {
            rs = RecordStore.openRecordStore("Reports", true);
            System.out.println("record open");
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        try {
            String inputYear = "";
            byte[] byteInputData;
            byteInputData = new byte[500];
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteInputData);
            DataInputStream inputDataStream = new DataInputStream(inputStream);
            counter = rs.getNumRecords();
            if (rs.getNumRecords() > 0) {
                for (int x = 1; x <= rs.getNumRecords(); x++) {
                    if (rs.getRecordSize(x) > byteInputData.length) {
                        byteInputData = new byte[1000];
                    }
                    rs.getRecord(x, byteInputData, 0);
                    inputYear = inputDataStream.readUTF();
                    db.addElement(inputYear);
                    inputStream.reset();
                }
                inputStream.close();
                inputDataStream.close();
            }
            System.out.println("y1 " + db);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        try {
            rs.closeRecordStore();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

    public String[] split(String splitStr) {
        String temp, year, month, t, t1, axe;      
        double amt;
        Hashtable hash_year;
        StringBuffer token = new StringBuffer();
        StringBuffer ex = new StringBuffer();
        hash_year = new Hashtable();
        Vector v = new Vector();
        char[] chars = splitStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ',') {
                temp = token.toString();
                ex.append(temp.substring(temp.indexOf('#') + 1, temp.lastIndexOf('#')));
                year = ex.toString();
                ex.setLength(0);
                ex.append(temp.substring(temp.indexOf('$') + 1, temp.lastIndexOf('$')));
                month = ex.toString();
                ex.setLength(0);

                ex.append(temp.substring(temp.indexOf('<') + 1, temp.lastIndexOf('>')));
                amt = Double.parseDouble(ex.toString());

                token.setLength(0);
                ex.setLength(0);
                //Yearly calculation
                read_settings();
                date_hash();
               
                if (mon.equals("word")) {
                    String dy = month + "-" + year;
                    axe = hd.get(month).toString();
                    System.out.println("Month " + axe);
                    axe += "-" + year;
                    if (!hash_year.containsKey(dy)) {
                        hash_year.put(dy, String.valueOf(amt));                        
                        System.out.println("Transision 1 " + dy);
                        t = dy + "             " + cur + " " + String.valueOf(amt);
                        t1 = axe + "             " + cur + " " + String.valueOf(amt);
                        v.removeElement(t);
                        v.removeElement(t1);
                        t = axe + "             " + cur + " " + String.valueOf(amt);
                        v.addElement(t);
                        //v.addElement(hash_year.toString());
                        //System.out.println("Single entries "+hash.toString()); 
                    } else if (hash_year.containsKey(dy)) {
                        double x = Double.parseDouble(hash_year.get(dy).toString());
                        amt += x;
                        t = dy + "             " + cur + " " + String.valueOf(x);
                        v.removeElement(t);
                        t = axe + "             " + cur + " " + String.valueOf(x);
                        v.removeElement(t);
                        t1 = axe + "             " + cur + " " + String.valueOf(amt);
                        v.removeElement(t1);
                        hash_year.remove(dy);
                        System.out.println("Transision 2 " + dy);
                        hash_year.put(dy, String.valueOf(amt));
                        t = axe + "             " + cur + " " + String.valueOf(amt);
                        v.addElement(t);                    
                    }
                    //Monthly calculation                      
                } else { //number
                    String dy = month + "-" + year;
                    axe = hd.get(month).toString();
                    System.out.println("Month " + axe);
                    axe += "-" + year;
                    if (!hash_year.containsKey(dy)) {
                        hash_year.put(dy, String.valueOf(amt));
                        System.out.println("Transision 1 " + dy);
                        t = dy + "             " + cur + " " + String.valueOf(amt);
                        t1 = axe + "             " + cur + " " + String.valueOf(amt);
                        v.removeElement(t);
                        v.removeElement(t1);
                        t = dy + "             " + cur + " " + String.valueOf(amt);
                        v.addElement(t);
                    } else if (hash_year.containsKey(dy)) {
                        double x = Double.parseDouble(hash_year.get(dy).toString());
                        amt += x;
                        t = dy + "             " + cur + " " + String.valueOf(x);
                        v.removeElement(t);
                        t = axe + "             " + cur + " " + String.valueOf(x);
                        v.removeElement(t);
                        t1 = axe + "             " + cur + " " + String.valueOf(amt);
                        v.removeElement(t1);
                        hash_year.remove(dy);
                        System.out.println("Transision 2 " + dy);
                        hash_year.put(dy, String.valueOf(amt));
                        t = dy + "             " + cur + " " + String.valueOf(amt);
                        v.addElement(t);               
                    }
                }
            } else {
                token.append(chars[i]);
            }
        }
        String[] arr = new String[v.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = v.elementAt(i).toString();
        }
        return arr;
    }

    private void yearlst_operation() {
        String abc = "", header;
        String[] s;
        yearlst = new List("Report ", List.IMPLICIT);
        header = "  M/Y" + "                  " + "Amount";
        for (int i = 0; i < db.size(); i++) {
            abc += db.elementAt(i).toString() + ",";
        }
        System.out.println("abc " + abc);
        s = split(abc);
        yearlst.append(header, null);
        for (int i = 0; i < s.length; i++) {
            yearlst.append(s[i], null);
        }
        yearlst.setFitPolicy(List.TEXT_WRAP_ON);
        back3 = new Command("Back", Command.BACK, 1);
        submit3 = new Command("Submit", Command.SCREEN, 1);
        yearlst.addCommand(back3);
        //yearlst.addCommand(submit3);
        display.setCurrent(yearlst);
        yearlst.setCommandListener(this);
    }

    private void form_year_operation() {
        String selectedString;
        if (yearlst.getSelectedIndex() > 0) {
           selectedString = yearlst.getString(yearlst.getSelectedIndex());
           ss = extract(selectedString," ");
          
        }
    }

    private void settings_operation1() {
        settingsnext1 = new Command("Next", Command.SCREEN, 1);
        settingsexit1 = new Command("Exit", Command.EXIT, 1);
        settiingslst1 = new List("Select Currency", List.EXCLUSIVE);
        settiingslst1.append("INR", null);
        settiingslst1.append("USD", null);
        settiingslst1.append("EUR", null);
        settiingslst1.addCommand(settingsnext1);
        settiingslst1.setSelectCommand(settingsnext1);
        settiingslst1.addCommand(settingsexit1);
        display.setCurrent(settiingslst1);
        settiingslst1.setCommandListener(this);
    }

    private void settings_operation2() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        date_hash();
        String date_digit = String.valueOf(month) + " - " + String.valueOf(year);
        String date_words = hd.get(String.valueOf(month)) + " - " + String.valueOf(year);

        settingsback2 = new Command("Back", Command.BACK, 1);
        settingsexit2 = new Command("Exit", Command.EXIT, 1);
        settingssave1 = new Command("Save", Command.SCREEN, 1);
        settiingslst2 = new List("Select Month Format", List.EXCLUSIVE);
        settiingslst2.append(date_digit, null);
        settiingslst2.append(date_words, null);
        settiingslst2.addCommand(settingsback2);
        settiingslst2.addCommand(settingsexit2);
        settiingslst2.addCommand(settingssave1);
        settiingslst2.setSelectCommand(settingssave1);
        display.setCurrent(settiingslst2);
        settiingslst2.setCommandListener(this);
    }

    private void date_hash() {
        hd = new Hashtable();
        hd.put(String.valueOf(1), String.valueOf("Jan"));
        hd.put(String.valueOf(2), String.valueOf("Feb"));
        hd.put(String.valueOf(3), String.valueOf("Mar"));
        hd.put(String.valueOf(4), String.valueOf("Apr"));
        hd.put(String.valueOf(5), String.valueOf("May"));
        hd.put(String.valueOf(6), String.valueOf("Jun"));
        hd.put(String.valueOf(7), String.valueOf("Jul"));
        hd.put(String.valueOf(8), String.valueOf("Aug"));
        hd.put(String.valueOf(9), String.valueOf("Sep"));
        hd.put(String.valueOf(10), String.valueOf("Oct"));
        hd.put(String.valueOf(11), String.valueOf("Nov"));
        hd.put(String.valueOf(12), String.valueOf("Dec"));
    }

    private void read_settings() {
        try {        
            rs = RecordStore.openRecordStore("settings", true);
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
            System.out.println("record opendsfssaesefsdfsdfsdsdfsdgsdf");
                
                
                
                try {
                    byte[] byteInputData;
                    byteInputData = new byte[100];
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(byteInputData);
                    DataInputStream inputDataStream = new DataInputStream(inputStream);
                    counter = rs.getNumRecords();
                    if (rs.getNumRecords() > 0) {
                        for (int x = 1; x <= rs.getNumRecords(); x++) {
                            if (rs.getRecordSize(x) > byteInputData.length) {
                                byteInputData = new byte[500];
                            }
                            rs.getRecord(x, byteInputData, 0);
                            cur = inputDataStream.readUTF().trim();
                            mon = inputDataStream.readUTF().trim();
                            System.out.println("cur " + cur);
                            System.out.println("mon " + mon);
                            inputStream.reset();
                        }
                        inputStream.close();
                        inputDataStream.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (RecordStoreException ex) {
                    ex.printStackTrace();
                }
                try {
                    rs.closeRecordStore();
                } catch (RecordStoreException ex) {
                    ex.printStackTrace();
                }
               
    }

    private void write_settings() {
        try {
            RecordStore.deleteRecordStore("settings");
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        try {
            rs = RecordStore.openRecordStore("settings", true);
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        byte[] outputRecord;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream outputDataStream = new DataOutputStream(outputStream);
        try {
            
            outputDataStream.writeUTF(settings_cur);
            outputDataStream.writeUTF(settings_month);
            System.out.println("settings" + settings_cur + "," + settings_month);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        display.setCurrent(lst1);
        
        
//        Alert a = new Alert("Message", "Data Saved...", null, AlertType.INFO);
//        a.setTimeout(1000);
//        display.setCurrent(a, lst1);
        try {
            outputDataStream.flush();
            outputRecord = outputStream.toByteArray();
            rs.addRecord(outputRecord, 0, outputRecord.length);
            outputStream.reset();
            outputStream.close();
            outputDataStream.close();
            rs.closeRecordStore();
        } catch (Exception ex) {
        }
    }

    private String extract(String selectedString, String delimiter) {
     StringBuffer token = new StringBuffer();
     Vector tokens = new Vector();

     char[] chars = selectedString.toCharArray();
     for (int i=0; i < chars.length; i++) {
         if (delimiter.indexOf(chars[i]) != -1) {             
             if (token.length() > 0) {
                 if(!" ".equals(token.toString())){
                 System.out.println("Space 1:"+token.toString());
                 tokens.addElement(token.toString().trim());
                 token.setLength(0);
                 }
             }break;
         }           
         else {
             token.append(chars[i]);
         }
     }    
     return tokens.elementAt(0).toString();
    }
}