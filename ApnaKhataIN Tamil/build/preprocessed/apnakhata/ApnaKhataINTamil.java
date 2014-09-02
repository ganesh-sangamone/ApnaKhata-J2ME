/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apnakhata;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

/**
 * @author Ganesh
 */
public class ApnaKhataINTamil extends MIDlet implements CommandListener, ItemStateListener{
 private Form form;
    private Display display;
    private Command exit, submit, back;
    private ChoiceGroup cg; 
    private RecordStore rs;
    
    private GUI child;
    private  Alert alert, aa;    
    private static String str = "", check = "false";
    private static String pn;
    private static String  cn;
    private static String wb;
    private static String inputString = "";
    private int[] inputInteger;
    
    private static Vector command = new Vector();
    private static Vector function = new Vector();    
    private static Vector parameter = new Vector();  
    private static Vector phno = new Vector();  
    private static Hashtable bs;
    
    private int readRecords(){  
        try {
                String input;
                bs = new Hashtable();
                rs = RecordStore.openRecordStore("addbank", false);
                System.out.println("record open 1");  
                if(RecordStore.listRecordStores() != null) {
                    System.out.println("aaaa");  
                            try{            
                           byte[] byteInputData;
                           inputInteger = new int[rs.getNumRecords()+1];
                           byteInputData = new byte[200];
                           ByteArrayInputStream inputStream = new ByteArrayInputStream(byteInputData);
                           DataInputStream inputDataStream = new DataInputStream(inputStream);
                           System.out.println("123");
                           if (rs.getNumRecords() > 0) {
                           for (int x = 1; x <= rs.getNumRecords(); x++)
                           {
                               if (rs.getRecordSize(x) > byteInputData.length)
                               {
                                   byteInputData = new byte[500];
                               }
                               rs.getRecord(x, byteInputData, 0);
                               input = inputDataStream.readUTF();  
                              // System.out.print("bank name           : "+inputString); 
                               inputString += input+"\n";
                               str = inputString.toString();
                               //System.out.print("Str value"+str);
                               inputInteger[x] = inputDataStream.readInt()+1;               
                               bs.put(String.valueOf(input), String.valueOf(inputInteger[x]));
                               inputStream.reset();
                               
                           }
                           System.out.println("BS....."+bs);
                           inputStream.close();
                           inputDataStream.close();          
                          }
                           return 1;
                       }

                       catch (Exception e) { }
                }
                else {
                    System.out.println("Add some banks");
                    return 0;
                }
                
            } catch (RecordStoreException ex) {
               
            } 
        try {
            rs.closeRecordStore();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        return 0;        
  }
    
    private void read_rms() {
        try {
            rs = RecordStore.openRecordStore("Activation", true);
            //System.out.println("record open"); 
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        try {
            String abc = "";
            byte[] byteInputData = new byte[1000];
            byteInputData = new byte[500];
            ByteArrayInputStream inputStream = new ByteArrayInputStream(byteInputData);
            DataInputStream inputDataStream = new DataInputStream(inputStream);

            for (int x = 1; x <= rs.getNumRecords(); x++) {

                rs.getRecord(x, byteInputData, 0);
                abc = inputDataStream.readUTF();
                check = abc.trim();
                //System.out.println("inputString  " + inputString);
                System.out.println("check" + check);

                inputStream.reset(); 
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
    
    private void bankOperation() {
        
        String[] sb;
        try{
        //str = inputString.toString();    
       // System.out.print("Str value"+str);
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
        cg.setSelectedIndex(0, true);
        form = new Form("ApnaKhata Sms Banking");        
        form.addCommand(exit);
        form.addCommand(submit);
        form.append(cg);
        form.setCommandListener(this);
        form.setItemStateListener(this);
    }    
    
    public ApnaKhataINTamil(){    
        int i;
        display = Display.getDisplay(this); 
        submit = new Command("Submit", Command.SCREEN, 1);  
        exit = new Command("Exit", Command.EXIT, 1);   
        read_rms();
        if(check.equalsIgnoreCase("true")) {  //wait
            i = readRecords();            
            cg = new ChoiceGroup("உங்கள் வங்கி தேர்வு", ChoiceGroup.EXCLUSIVE);           
            if(i == 1) {
                bankOperation();
            }
            else {
                 aa = new Alert("எச்சரிக்கை","AddBank சென்று உங்கள் வங்கிகள் தேர்வு", null, AlertType.ALARM);
                aa.setTimeout(Alert.FOREVER);
                aa.addCommand(exit);
                aa.setCommandListener(this);
                display.setCurrent(aa);
            }
        }
        else {
            Alert a3;
            a3 = new Alert("எச்சரிக்கை", "ApnaKhata செயல்படுத்த, தயவு செய்து", null, AlertType.WARNING);
            a3.setTimeout(Alert.FOREVER);
            a3.addCommand(exit);
            a3.setCommandListener(this);
            display.setCurrent(a3);          
            
        }
    }   
        
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
       
    }

    protected void pauseApp() {
        
    }

    protected void startApp() throws MIDletStateChangeException {
        display.setCurrent(form);
    }
    
    public void show()
    {
         Display.getDisplay(this).setCurrent(form);
    }

    public void commandAction(Command c, Displayable d) {
       if (c == submit)
        {
            boolean picks[] = new boolean[cg.size()];
            String message = null;
            cg.getSelectedFlags(picks);
            for (int x = 0; x < picks.length; x++)
            {
                if (picks[x])
                {                    
                    message = bs.get(cg.getString(x))+"\n";                    
                    //child = new GUI(this,message.trim(),cg.getString(x));
                }
            }child = new GUI(this,message.trim(),cg.getString(cg.getSelectedIndex()));           
        }        
        else if (c == exit)
        {
            try {
                destroyApp(false);
            } catch (MIDletStateChangeException ex) {
                ex.printStackTrace();
            }
            notifyDestroyed();
        }        
    }
    
    public void itemStateChanged(Item item) {
        if(item == cg) {
            boolean picks[] = new boolean[cg.size()];
            String message = null;
            cg.getSelectedFlags(picks);
            for (int x = 0; x < picks.length; x++)
            {
                if (picks[x])
                {                    
                    message = bs.get(cg.getString(x))+"\n";                    
                    
                }
            }child = new GUI(this,message.trim(),cg.getString(cg.getSelectedIndex()));  
        }
    }
    
    public static String[] split(String splitStr)
    {
         function.setSize(0);
         command.setSize(0);
         parameter.setSize(0);
         phno.setSize(0);
         String temp;  
         StringBuffer token = new StringBuffer();
         StringBuffer ex = new StringBuffer();   
    
         char[] chars = splitStr.toCharArray();
         for (int i=0; i < chars.length; i++) {
            if (chars[i] == '\n') {        
             temp = token.toString();            
             ex.append(temp.substring(temp.indexOf('$')+1, temp.lastIndexOf('$')));                     
             function.addElement(ex.toString());
             token.setLength(0);
             ex.setLength(0);
             ex.append(temp.substring(temp.lastIndexOf('$')+1,temp.indexOf('|')-1));
             command.addElement(ex.toString());
             token.setLength(0);
             ex.setLength(0);
             ex.append(temp.substring(temp.indexOf("|")+1,temp.lastIndexOf('|')));                         
             parameter.addElement(ex.toString());          
             token.setLength(0);
             ex.setLength(0);
             ex.append(temp.substring(temp.indexOf('<')+1,temp.lastIndexOf('>')));
             phno.addElement(ex.toString());
             token.setLength(0);
             ex.setLength(0);
         } 
         else {
             token.append(chars[i]);
         }
     }
     
     if (token.length() > 0) {
        temp = token.toString();
         ex.setLength(0);
         ex.append(temp.substring(temp.indexOf('*')+1,temp.lastIndexOf('*')));
         function.addElement("கால் வாடிக்கையாளர்".trim());
         cn = ex.toString();
         ex.setLength(0);
         token.setLength(0);
         ex.append(temp.substring(temp.indexOf('@')+1,temp.lastIndexOf('@')));
         function.addElement("இணையத்தளம்");    
         wb = ex.toString();
         token.setLength(0);
         ex.setLength(0);
        //System.out.println("Phone no :"+pn);
     }     
    
     String[] splitArray = new String[function.size()];
     for (int i=0; i < splitArray.length; i++) {
         splitArray[i] = function.elementAt(i).toString().trim();
     }
     return splitArray;
    }
    
    public static String[] Split1(String splitStr, String delimiter) {
     StringBuffer token = new StringBuffer();
     Vector tokens = new Vector();

     char[] chars = splitStr.toCharArray();
     for (int i=0; i < chars.length; i++) {
         if (delimiter.indexOf(chars[i]) != -1) {             
             if (token.length() > 0) {
                 if(!" ".equals(token.toString())){
                 //System.out.println("Space :"+token.toString());
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
            //System.out.println("dsfvsd" +sb);
        }
        is.close();
        return sb.toString();
        }catch (Exception e){
        form.append("App Error: "+e.toString());
        }
        return null;
    }
   
    
     public class GUI implements CommandListener, ItemStateListener {      
        private ApnaKhataINTamil parent;
        private Form form2, form3;
        private String data = "";
        private String[] sb, sb1;
        private Alert alert1, alert2, exceptionAlert, exceptionAlert1, smsSent;
        private ChoiceGroup radioButton;

        private Command back1, submit1, back2, back3, submit2, send1;
        private TextField[] txtFld;
        private int count, count1;
        private String str1 = "";
        
        public final void commandOperation(String message){
              
        try{
            if(str1!=null){
                str1 = file(message);            
            }
            else {
                System.out.println("File Not found");
            }
        }
        catch(Exception e){
          
           alert = new Alert("App Error",e.toString(), null, null);
           alert.setTimeout(Alert.FOREVER);
           display.setCurrent(alert);
        }
        
        sb = split(str1);        
        for(int i=0;i<sb.length;i++) {
                radioButton.append(sb[i],null);                
            }
        form2.addCommand(back1);
        form2.addCommand(submit1); 
        form2.append(radioButton);         
        form2.setCommandListener(this);
        form2.setItemStateListener(this);
        function.setSize(0);
    }
         
        public GUI(ApnaKhataINTamil parent, String message, String bname) {   
        
        back1 = new Command ("Back", Command.BACK, 1);
        submit1 = new Command("Submit", Command.SCREEN, 1);
        send1 = new Command("Send", Command.OK, 1);
        radioButton = new ChoiceGroup("எந்த விருப்பங்களை தேர்வு",Choice.EXCLUSIVE);
        radioButton.setFitPolicy(Choice.TEXT_WRAP_ON);
        radioButton.setLayout(ChoiceGroup.LAYOUT_EXPAND);
        
        form2 = new Form(bname.toString());
        this.parent = parent; 
        commandOperation(message);        
        Display.getDisplay(parent).setCurrent(form2);
        }
        
        public void sms_page(String msg, int len){
        
        if(len!=0) {
        back2 = new Command ("Back", Command.BACK, 1);
        submit2 = new Command("Submit", Command.SCREEN, 1);
        txtFld = new TextField[len];
        form3 = new Form(msg);
        for(int i=0;i<len;i++) {
            txtFld[i] = new TextField(sb1[i].trim(), "", 100, TextField.ANY);           
        }
        for(int j=0;j<len;j++) {
                form3.append(txtFld[j]);
            }
        count1 = len;
        form3.addCommand(back2);
        form3.addCommand(submit2);
        form3.setCommandListener(this);
        Display.getDisplay(parent).setCurrent(form3);
        }
        else {
            data = "";
            data=command.elementAt(radioButton.getSelectedIndex()).toString();
            back3 = new Command ("Back", Command.BACK, 1);
            System.out.println("Bypass data "+data);
              alert2 = new Alert("SMS கட்டணங்கள் விதிக்கப்படும் மொபைல் ஆபரேட்டர் படி பயன்படுத்தப்படும்", data, null, null);
              alert2.setTimeout(Alert.FOREVER);
              alert2.setType(AlertType.INFO);
              alert2.addCommand(back3);
              alert2.addCommand(send1);
              display.setCurrent(alert2);
              alert2.setCommandListener(this);
                           
        }        
    }
        
        public int send_sms(){     
            
        MessageConnection msgCon = null;        
        try {                
            msgCon = (MessageConnection) Connector.open("sms://" +pn.trim());
            System.out.println("phno str:"+pn);  
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
        TextMessage txtMsg = (TextMessage) msgCon.newMessage(MessageConnection.TEXT_MESSAGE);           
        txtMsg.setPayloadText(data);

        try {               
            msgCon.send(txtMsg);  
            
            System.out.println("success");
            return 1;
        } 
        catch (IOException ex) {
            if(msgCon != null){
                try {                       
                    msgCon.close();
                    pn = "";
                    data = "";
                } 
                catch (IOException exx) {
                }
            }
            return 0;
        }
        finally {
            if(msgCon != null){
                try {                       
                    msgCon.close();
                    pn = "";
                    data = "";
                } 
                catch (IOException ex) {
                    exceptionAlert1 = new Alert("AppError 2:",ex.toString(), null, null);
                    exceptionAlert1.setTimeout(Alert.FOREVER);
                    exceptionAlert1.setType(AlertType.ERROR);
                    display.setCurrent(exceptionAlert1);
                }
            }
        }
    }
        
        public void itemStateChanged(Item item) {
           if(item == radioButton) {
               if(radioButton.getSelectedIndex() == radioButton.size()-2) {
                System.out.println("Phno");
                try {
                    platformRequest("tel:" + cn.trim());
                } 
                catch (ConnectionNotFoundException ex) {
                    ex.printStackTrace();
                } 
            }
            else if(radioButton.getSelectedIndex() == radioButton.size()-1) {
                System.out.println("Website");
                try {
                    platformRequest(wb.trim());
                } 
                catch (ConnectionNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
            else {
            String message;            
            message = radioButton.getString(radioButton.getSelectedIndex()).trim();
            count = radioButton.getSelectedIndex();   
            sb = split(str1);
                    sb1 = Split1(parameter.elementAt(count).toString().trim(),"|,[]\n");
                    pn = phno.elementAt(count).toString();
                    System.out.println("parameters before spliting "+parameter.toString());             
                    sms_page(message, sb1.length);                   
                    
                    System.out.println("count length "+count);       
               }
           }
        }
        
        public void commandAction(Command c, Displayable d) {
        
        if(c == back1)
        {
            parent.show();
            parameter.setSize(0);
            command.setSize(0);
            data = "";
            sb1 = null;
            phno.setSize(0);
            //count = 0;
        }
        else if(c == submit1){       
           
            if(radioButton.getSelectedIndex() == radioButton.size()-2) {
                System.out.println("Phno");
                try {
                    platformRequest("tel:" + cn.trim());
                } 
                catch (ConnectionNotFoundException ex) {
                    ex.printStackTrace();
                } 
            }
            else if(radioButton.getSelectedIndex() == radioButton.size()-1) {
                System.out.println("Website");
                try {
                    platformRequest(wb.trim());
                } 
                catch (ConnectionNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
            else {
            String message;            
            message = radioButton.getString(radioButton.getSelectedIndex()).trim();
            count = radioButton.getSelectedIndex();   
            sb = split(str1);
                    sb1 = Split1(parameter.elementAt(count).toString().trim(),"|,[]\n");
                    pn = phno.elementAt(count).toString();
                    System.out.println("parameters before spliting "+parameter.toString());             
                    sms_page(message, sb1.length);                   
                    
                    System.out.println("count length "+count);       
        }
        }
        else if(c == back2){
            Display.getDisplay(parent).setCurrent(form2);         
            parameter.setSize(0);
            command.setSize(0);
            data = "";
            sb1 = null;
            count = 0;
            count1 = 0;
        }
        else if(c == back3){
            Display.getDisplay(parent).setCurrent(form2); 
            parameter.setSize(0);
            command.setSize(0);
            data = "";
            sb1 = null;
            count = 0;
            count1 = 0;
        }
      /*  else if(c == call){       
            try {
                platformRequest("tel:" + cn.trim());
            } catch (ConnectionNotFoundException ex) {
                ex.printStackTrace();
            }        
        }
        else if(c == web){               
            try {
                platformRequest(wb.trim());
            } catch (ConnectionNotFoundException ex) {
                ex.printStackTrace();
            }
        
        }*/
        else if(c == submit2){  
            data="";
            data=command.elementAt(radioButton.getSelectedIndex()).toString();
            System.out.println("data initial content "+data); 
            for(int i=0; i<count1;i++) {
                data = data+" "+txtFld[i].getString().trim();
                alert1 = new Alert("SMS கட்டணங்கள் விதிக்கப்படும் மொபைல் ஆபரேட்டர் படி பயன்படுத்தப்படும்", data, null, null);                
            }
              alert1.setTimeout(Alert.FOREVER);
              alert1.setType(AlertType.INFO);
              alert1.addCommand(back2);
              alert1.addCommand(send1);
              alert1.setCommandListener(this);
              display.setCurrent(alert1);                   
        }
        else if(c == send1){             
            int k = send_sms();
            if(k == 1) {
                smsSent = new Alert("தகவல் ","செய்தி வெற்றிகரமாக அனுப்பப்பட்டது...", null, AlertType.INFO);
                smsSent.setTimeout(Alert.FOREVER);            
                display.setCurrent(smsSent, form2);
                parameter.setSize(0);
                command.setSize(0);
                data = "";
                sb1 = null;
                count = 0;
                count1 = 0;
                //phno.setSize(0); 
            }
            else {
                Alert aa = new Alert("எச்சரிக்கை", "செய்தி அனுப்ப முடியவில்லை", null, AlertType.WARNING);
                aa.setTimeout(Alert.FOREVER);
                aa.addCommand(exit);
                aa.setCommandListener(this);
                display.setCurrent(aa, form2);
            }            
        }        
       }        
     }    
}