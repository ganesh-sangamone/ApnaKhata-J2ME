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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * @author Ganesh
 */
public class AddBank extends MIDlet implements CommandListener {
    private Display display;
    private Form form;
    private ChoiceGroup cg;
    private Command save, exit;
    private RecordStore rs;

    private String str, data, check = "false";
    
    private void bankOperation() {
        
        String[] sb;
        try{
        str = file("Banks.txt");            
        }
        catch(Exception e){
           Alert a;
           a = new Alert("App Error",e.toString(), null, null);
           a.setTimeout(Alert.FOREVER);
           display.setCurrent(a);
        }
        sb = Split1(str,"\n");
        for(int i=0;i<sb.length;i++) {
            //System.out.println("sb value "+sb[i]);
                cg.append(sb[i],null);
            }
        cg.setFitPolicy(Choice.TEXT_WRAP_ON);
        cg.setLayout(ChoiceGroup.LAYOUT_EXPAND);      
        form = new Form("ApnaKhata SMS Banking");        
        form.addCommand(exit);
        form.addCommand(save);
        form.append(cg);
        form.setCommandListener(this);
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
    
    public AddBank(){       
        display = Display.getDisplay(this);  
        read_rms();
        save = new Command("Save", Command.SCREEN, 1);  
        exit = new Command("Exit", Command.EXIT, 1);   
        if(check.equalsIgnoreCase("true")) {
            cg = new ChoiceGroup("ನಿಮ್ಮ ಬ್ಯಾಂಕ್ ಆರಿಸಿ", ChoiceGroup.MULTIPLE);           
            bankOperation();
            readRecords();
        }
        else {
            Alert a3;
            a3 = new Alert("ಎಚ್ಚರಿಕೆ", "ಉತ್ಪನ್ನ ಸಕ್ರಿಯಗೊಳಿಸಿ, ದಯವಿಟ್ಟು", null, AlertType.WARNING);
            a3.setTimeout(Alert.FOREVER);
            a3.addCommand(exit);            
            a3.setCommandListener(this);
            display.setCurrent(a3);          
            
        }
    } 
    
    public static String[] Split1(String splitStr, String delimiter) {
     StringBuffer token = new StringBuffer();
     Vector tokens = new Vector();

     char[] chars = splitStr.toCharArray();
     for (int i=0; i < chars.length; i++) {
         if (delimiter.indexOf(chars[i]) != -1) {             
             if (token.length() > 0) {
                 if(!" ".equals(token.toString())){                
                 tokens.addElement(token.toString().trim());
                 token.setLength(0);
                 }
             }
         }          
         else {
             token.append(chars[i]);
         }
     }    
     if (token.length() > 0) {
         tokens.addElement(token.toString().trim());
     }
     
     String[] splitArray = new String[tokens.size()];
     for (int i=0; i < splitArray.length; i++) {
         splitArray[i] = (String)tokens.elementAt(i);
     }
     return splitArray;
 }
    
    public String file(String filename){
        InputStream is = getClass().getResourceAsStream(filename.trim());
        InputStreamReader irs = null;
        StringBuffer sb = new StringBuffer();
        try {
            irs = new InputStreamReader(is,"UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        try{
            int chars;
            while ((chars = irs.read()) != -1){
            sb.append((char) chars);
          
        }
        is.close();
        return sb.toString();
        }catch (Exception e){
        form.append("App Error: "+e.toString());
        }
        return null;
    }
    
    public void save() {
        Alert alr, a;
        try {
            try {
                rs = RecordStore.openRecordStore("addbank", true)  ;
                //System.out.println("record open");  
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            } 
            byte[] array;
            ByteArrayOutputStream aos = new ByteArrayOutputStream();
            DataOutputStream os = new DataOutputStream(aos);
            boolean picks[] = new boolean[cg.size()];
            String message = "";
            if(cg.getSelectedFlags(picks) > 0){            
                for (int x = 0; x < picks.length; x++)
                {
                    if (picks[x])
                    {               
                        message = cg.getString(x).trim(); 
                        try {
                            os.writeUTF(message);
                            os.writeInt(x);
                            os.flush();
                            array = aos.toByteArray();
                            try {                                
                                rs.addRecord(array, 0, array.length);                                   
                                aos.reset();
                            } catch (RecordStoreException ex) {
                                ex.printStackTrace();
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }
                }
                alr = new Alert("ಮಾಹಿತಿ","ಸಂಗ್ರಹಿಸಲಾದ ಡೇಟಾ", null, AlertType.INFO);
                alr.setTimeout(1000);
                display.setCurrent(alr);
                alr.addCommand(exit);
                alr.setCommandListener(this);
            }
            else {
                a = new Alert("ಎಚ್ಚರಿಕೆ", "ಕನಿಷ್ಠ ಒಂದು ಬ್ಯಾಂಕ್ ಪರಿಶೀಲಿಸಿ".trim(), null, AlertType.INFO);
                a.setTimeout(Alert.FOREVER);
                display.setCurrent(a);
          }
          os.close();
          aos.close();          
          
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            rs.closeRecordStore();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
         
    }
    
    private void readRecords(){
        try {
                rs = RecordStore.openRecordStore("addbank", true)  ;
                System.out.println("record open");  
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            }
        try{
            String inputString = null;
            byte[] byteInputData;
            int[] inputInteger = new int[rs.getNumRecords()+1];
            byteInputData = new byte[cg.size()];
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteInputData);
            DataInputStream inputDataStream = new DataInputStream(inputStream);
            
            if (rs.getNumRecords() > 0) {
            for (int x = 1; x <= rs.getNumRecords(); x++)
            {
                if (rs.getRecordSize(x) > byteInputData.length)
                {
                    byteInputData = new byte[300];
                }
                rs.getRecord(x, byteInputData, 0);
                inputString = inputDataStream.readUTF();                
                inputInteger[x] = inputDataStream.readInt();               
                inputStream.reset();
            }
            inputStream.close();
            inputDataStream.close();
            for(int j = 1; j<= inputInteger.length; j++) {
                String stra = String.valueOf(inputInteger[j]);
                System.out.println("Data "+stra);
                cg.setSelectedIndex(inputInteger[j], true);
                }
           
            display.setCurrent(form);
        }
        }
        
        catch (Exception e) { }
        try {
            rs.closeRecordStore();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
  }
    
    public void startApp() {
        display.setCurrent(form);
    }
    
    public void pauseApp() {
    }
    
    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable d) {
        if (c == save)
        {
            try {
                RecordStore.deleteRecordStore("addbank");
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            }
            save();
            
        }
        else if(c == exit) {
            destroyApp(false);
            notifyDestroyed();
        }
    }
}
