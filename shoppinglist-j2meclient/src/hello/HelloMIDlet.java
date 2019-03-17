/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hello;

import java.io.*;
import java.util.Vector;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;

/**
 * @author Matthias Clausen
 */
public class HelloMIDlet extends MIDlet implements CommandListener {

    private boolean midletPaused = false;

    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Command exitCommand;
    private Command itemCommand;
    private Command backCommand;
    private Command itemCommand1;
    private Command itemCommand2;
    private Command helpCommand;
    private Command exitCommand1;
    private Command newItemCommand;
    private Command submitItemCommand;
    private Command backCommand1;
    private Command cancelNewItemCommand;
    private Command exitCommand2;
    private Command newItemCommand1;
    private Command itemCommand3;
    private Command deleteItemCommand;
    private Command submitServerUrlCommand;
    private Command cancelServerUrlCommand;
    private Command openServerUrlConfig;
    private Command openServerUrlConfig1;
    private Form waitScreen;
    private TextField waitTextField;
    private TextBox newitem;
    private List shoppinglist;
    private TextBox serverUrl;
    //</editor-fold>//GEN-END:|fields|0|
    private Command deleteSelectedItemCommand;
    private Networkmanager nm;
    public Vector itemlist= null;
    public String url = "";
    /**
     * The HelloMIDlet constructor.
     */
    public HelloMIDlet() {
    }

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        // write pre-initialize user code here
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
    //</editor-fold>//GEN-END:|0-initialize|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
        getWaitScreen().deleteAll();
        getWaitScreen().append("Retrieving the list from the server…");
        switchDisplayable(null, getWaitScreen());//GEN-LINE:|3-startMIDlet|1|3-postAction
        // write post-action user code here
        nm = new Networkmanager();
        nm.app = this;
        Thread t = new Thread(nm);
        t.start();
    }//GEN-BEGIN:|3-startMIDlet|2|
    //</editor-fold>//GEN-END:|3-startMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
    }//GEN-BEGIN:|4-resumeMIDlet|2|
    //</editor-fold>//GEN-END:|4-resumeMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
        Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }//GEN-END:|5-switchDisplayable|1|5-postSwitch
        // write post-switch user code here
    }//GEN-BEGIN:|5-switchDisplayable|2|
    //</editor-fold>//GEN-END:|5-switchDisplayable|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Displayables ">//GEN-BEGIN:|7-commandAction|0|7-preCommandAction
    /**
     * Called by a system to indicated that a command has been invoked on a particular displayable.
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:|7-commandAction|0|7-preCommandAction
        // write pre-action user code here
        if (displayable == newitem) {//GEN-BEGIN:|7-commandAction|1|62-preAction
            if (command == cancelNewItemCommand) {//GEN-END:|7-commandAction|1|62-preAction
                // write pre-action user code here
                switchDisplayable(null, getShoppinglist());//GEN-LINE:|7-commandAction|2|62-postAction
                // write post-action user code here
            } else if (command == submitItemCommand) {//GEN-LINE:|7-commandAction|3|60-preAction
                // write pre-action user code here
                getWaitScreen().deleteAll();
                getWaitScreen().append("Submitting item to the server…");
                switchDisplayable(null, getWaitScreen());
//GEN-LINE:|7-commandAction|4|60-postAction
                // write post-action user code here
                try {
                    nm.submitItem(getNewitem().getString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getNewitem().setString(""); /* without this, next time the old text would still be there */
            }//GEN-BEGIN:|7-commandAction|5|104-preAction
        } else if (displayable == serverUrl) {
            if (command == cancelServerUrlCommand) {//GEN-END:|7-commandAction|5|104-preAction
                // write pre-action user code here
                switchDisplayable(null, getShoppinglist());//GEN-LINE:|7-commandAction|6|104-postAction
                // write post-action user code here
            } else if (command == submitServerUrlCommand) {//GEN-LINE:|7-commandAction|7|102-preAction
                if (!serverUrl.getString().toLowerCase().startsWith("http")) {
                    switchDisplayable(
                        new Alert("Error", "URL must start with http(s)://...", null, AlertType.ERROR),
                        getServerUrl());
                }

                try {
                    RecordStore db = RecordStore.openRecordStore("shoppingListDb", true);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(baos);
                    dos.writeUTF(serverUrl.getString());
                    dos.close();
                    baos.close();
                    byte[] bytes = baos.toByteArray();
                    db.setRecord(1, bytes, 0, bytes.length);
                    url = serverUrl.getString();
                    db.closeRecordStore();
                    nm.getShoppinglistFromServer(url);

                } catch (Exception e) {
                    switchDisplayable(
                    new Alert("Error", "Error saving server URL: " + e.getMessage(), null, AlertType.ERROR),
                    getServerUrl());
                }
                // write pre-action user code here
//GEN-LINE:|7-commandAction|8|102-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|9|85-preAction
        } else if (displayable == shoppinglist) {
            if (command == List.SELECT_COMMAND) {//GEN-END:|7-commandAction|9|85-preAction
                // write pre-action user code here
                shoppinglistAction();//GEN-LINE:|7-commandAction|10|85-postAction
                // write post-action user code here
            } else if (command == exitCommand2) {//GEN-LINE:|7-commandAction|11|88-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|12|88-postAction
                // write post-action user code here
            } else if (command == newItemCommand1) {//GEN-LINE:|7-commandAction|13|91-preAction
                // write pre-action user code here
                switchDisplayable(null, getNewitem());//GEN-LINE:|7-commandAction|14|91-postAction
                // write post-action user code here
            } else if (command == openServerUrlConfig1) {//GEN-LINE:|7-commandAction|15|110-preAction
                // write pre-action user code here
                switchDisplayable(null, getServerUrl());//GEN-LINE:|7-commandAction|16|110-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|17|107-preAction
        } else if (displayable == waitScreen) {
            if (command == openServerUrlConfig) {//GEN-END:|7-commandAction|17|107-preAction
                // write pre-action user code here
                switchDisplayable(null, getServerUrl());//GEN-LINE:|7-commandAction|18|107-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|19|7-postCommandAction
        }//GEN-END:|7-commandAction|19|7-postCommandAction
        // write post-action user code here
        if (displayable == shoppinglist && command == deleteSelectedItemCommand) {
                int id = -1;
                ShoppingItem item = (ShoppingItem)(itemlist.elementAt(shoppinglist.getSelectedIndex()));
                if (item != null) id = item.getId();
                try {
                    if (id > 0) nm.deleteItem(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }//GEN-BEGIN:|7-commandAction|20|
    //</editor-fold>//GEN-END:|7-commandAction|20|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand ">//GEN-BEGIN:|18-getter|0|18-preInit
    /**
     * Returns an initiliazed instance of exitCommand component.
     * @return the initialized component instance
     */
    public Command getExitCommand() {
        if (exitCommand == null) {//GEN-END:|18-getter|0|18-preInit
            // write pre-init user code here
            exitCommand = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|18-getter|1|18-postInit
            // write post-init user code here
        }//GEN-BEGIN:|18-getter|2|
        return exitCommand;
    }
    //</editor-fold>//GEN-END:|18-getter|2|





    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: itemCommand ">//GEN-BEGIN:|23-getter|0|23-preInit
    /**
     * Returns an initiliazed instance of itemCommand component.
     * @return the initialized component instance
     */
    public Command getItemCommand() {
        if (itemCommand == null) {//GEN-END:|23-getter|0|23-preInit
            // write pre-init user code here
            itemCommand = new Command("Item", Command.ITEM, 0);//GEN-LINE:|23-getter|1|23-postInit
            // write post-init user code here
        }//GEN-BEGIN:|23-getter|2|
        return itemCommand;
    }
    //</editor-fold>//GEN-END:|23-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand ">//GEN-BEGIN:|25-getter|0|25-preInit
    /**
     * Returns an initiliazed instance of backCommand component.
     * @return the initialized component instance
     */
    public Command getBackCommand() {
        if (backCommand == null) {//GEN-END:|25-getter|0|25-preInit
            // write pre-init user code here
            backCommand = new Command("Back", Command.BACK, 0);//GEN-LINE:|25-getter|1|25-postInit
            // write post-init user code here
        }//GEN-BEGIN:|25-getter|2|
        return backCommand;
    }
    //</editor-fold>//GEN-END:|25-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: itemCommand1 ">//GEN-BEGIN:|28-getter|0|28-preInit
    /**
     * Returns an initiliazed instance of itemCommand1 component.
     * @return the initialized component instance
     */
    public Command getItemCommand1() {
        if (itemCommand1 == null) {//GEN-END:|28-getter|0|28-preInit
            // write pre-init user code here
            itemCommand1 = new Command("Item", Command.ITEM, 0);//GEN-LINE:|28-getter|1|28-postInit
            // write post-init user code here
        }//GEN-BEGIN:|28-getter|2|
        return itemCommand1;
    }
    //</editor-fold>//GEN-END:|28-getter|2|







    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: itemCommand2 ">//GEN-BEGIN:|40-getter|0|40-preInit
    /**
     * Returns an initiliazed instance of itemCommand2 component.
     * @return the initialized component instance
     */
    public Command getItemCommand2() {
        if (itemCommand2 == null) {//GEN-END:|40-getter|0|40-preInit
            // write pre-init user code here
            itemCommand2 = new Command("Item", Command.ITEM, 0);//GEN-LINE:|40-getter|1|40-postInit
            // write post-init user code here
        }//GEN-BEGIN:|40-getter|2|
        return itemCommand2;
    }
    //</editor-fold>//GEN-END:|40-getter|2|







    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: helpCommand ">//GEN-BEGIN:|45-getter|0|45-preInit
    /**
     * Returns an initiliazed instance of helpCommand component.
     * @return the initialized component instance
     */
    public Command getHelpCommand() {
        if (helpCommand == null) {//GEN-END:|45-getter|0|45-preInit
            // write pre-init user code here
            helpCommand = new Command("Help", Command.HELP, 0);//GEN-LINE:|45-getter|1|45-postInit
            // write post-init user code here
        }//GEN-BEGIN:|45-getter|2|
        return helpCommand;
    }
    //</editor-fold>//GEN-END:|45-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: newItemCommand ">//GEN-BEGIN:|49-getter|0|49-preInit
    /**
     * Returns an initiliazed instance of newItemCommand component.
     * @return the initialized component instance
     */
    public Command getNewItemCommand() {
        if (newItemCommand == null) {//GEN-END:|49-getter|0|49-preInit
            // write pre-init user code here
            newItemCommand = new Command("Add", "Add Item", Command.SCREEN, 0);//GEN-LINE:|49-getter|1|49-postInit
            // write post-init user code here
        }//GEN-BEGIN:|49-getter|2|
        return newItemCommand;
    }
    //</editor-fold>//GEN-END:|49-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand1 ">//GEN-BEGIN:|51-getter|0|51-preInit
    /**
     * Returns an initiliazed instance of exitCommand1 component.
     * @return the initialized component instance
     */
    public Command getExitCommand1() {
        if (exitCommand1 == null) {//GEN-END:|51-getter|0|51-preInit
            // write pre-init user code here
            exitCommand1 = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|51-getter|1|51-postInit
            // write post-init user code here
        }//GEN-BEGIN:|51-getter|2|
        return exitCommand1;
    }
    //</editor-fold>//GEN-END:|51-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand1 ">//GEN-BEGIN:|57-getter|0|57-preInit
    /**
     * Returns an initiliazed instance of backCommand1 component.
     * @return the initialized component instance
     */
    public Command getBackCommand1() {
        if (backCommand1 == null) {//GEN-END:|57-getter|0|57-preInit
            // write pre-init user code here
            backCommand1 = new Command("Back", Command.BACK, 0);//GEN-LINE:|57-getter|1|57-postInit
            // write post-init user code here
        }//GEN-BEGIN:|57-getter|2|
        return backCommand1;
    }
    //</editor-fold>//GEN-END:|57-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: submitItemCommand ">//GEN-BEGIN:|59-getter|0|59-preInit
/**
 * Returns an initiliazed instance of submitItemCommand component.
 * @return the initialized component instance
 */
public Command getSubmitItemCommand() {
    if (submitItemCommand == null) {//GEN-END:|59-getter|0|59-preInit
            // write pre-init user code here
        submitItemCommand = new Command("Ok", Command.OK, 0);//GEN-LINE:|59-getter|1|59-postInit
            // write post-init user code here
    }//GEN-BEGIN:|59-getter|2|
    return submitItemCommand;
}
//</editor-fold>//GEN-END:|59-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: cancelNewItemCommand ">//GEN-BEGIN:|61-getter|0|61-preInit
/**
 * Returns an initiliazed instance of cancelNewItemCommand component.
 * @return the initialized component instance
 */
public Command getCancelNewItemCommand() {
    if (cancelNewItemCommand == null) {//GEN-END:|61-getter|0|61-preInit
            // write pre-init user code here
        cancelNewItemCommand = new Command("Cancel", Command.CANCEL, 0);//GEN-LINE:|61-getter|1|61-postInit
            // write post-init user code here
    }//GEN-BEGIN:|61-getter|2|
    return cancelNewItemCommand;
}
//</editor-fold>//GEN-END:|61-getter|2|

//<editor-fold defaultstate="collapsed" desc=" Generated Getter: newitem ">//GEN-BEGIN:|56-getter|0|56-preInit
/**
 * Returns an initiliazed instance of newitem component.
 * @return the initialized component instance
 */
public TextBox getNewitem() {
    if (newitem == null) {//GEN-END:|56-getter|0|56-preInit
            // write pre-init user code here
        newitem = new TextBox("Item name", null, 100, TextField.ANY);//GEN-BEGIN:|56-getter|1|56-postInit
        newitem.addCommand(getSubmitItemCommand());
        newitem.addCommand(getCancelNewItemCommand());
        newitem.setCommandListener(this);//GEN-END:|56-getter|1|56-postInit
            // write post-init user code here
            getNewitem().setInitialInputMode("LATIN_1_SUPPLEMENT");
    }//GEN-BEGIN:|56-getter|2|
    return newitem;
}
//</editor-fold>//GEN-END:|56-getter|2|











    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: waitScreen ">//GEN-BEGIN:|80-getter|0|80-preInit
    /**
     * Returns an initiliazed instance of waitScreen component.
     * @return the initialized component instance
     */
    public Form getWaitScreen() {
        if (waitScreen == null) {//GEN-END:|80-getter|0|80-preInit
            // write pre-init user code here
            waitScreen = new Form("Please wait", new Item[] { getWaitTextField() });//GEN-BEGIN:|80-getter|1|80-postInit
            waitScreen.addCommand(getOpenServerUrlConfig());
            waitScreen.setCommandListener(this);//GEN-END:|80-getter|1|80-postInit
            // write post-init user code here
        }//GEN-BEGIN:|80-getter|2|
        return waitScreen;
    }
    //</editor-fold>//GEN-END:|80-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: waitTextField ">//GEN-BEGIN:|82-getter|0|82-preInit
    /**
     * Returns an initiliazed instance of waitTextField component.
     * @return the initialized component instance
     */
    public TextField getWaitTextField() {
        if (waitTextField == null) {//GEN-END:|82-getter|0|82-preInit
            // write pre-init user code here
            waitTextField = new TextField("", "Please wait\u2026", 32, TextField.ANY);//GEN-BEGIN:|82-getter|1|82-postInit
            waitTextField.setLayout(ImageItem.LAYOUT_CENTER | Item.LAYOUT_TOP | Item.LAYOUT_BOTTOM | Item.LAYOUT_VCENTER | Item.LAYOUT_EXPAND | Item.LAYOUT_VEXPAND);//GEN-END:|82-getter|1|82-postInit
            // write post-init user code here
        }//GEN-BEGIN:|82-getter|2|
        return waitTextField;
    }
    //</editor-fold>//GEN-END:|82-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: shoppinglist ">//GEN-BEGIN:|83-getter|0|83-preInit
    /**
     * Returns an initiliazed instance of shoppinglist component.
     * @return the initialized component instance
     */
    public List getShoppinglist() {
        if (shoppinglist == null) {//GEN-END:|83-getter|0|83-preInit
            // write pre-init user code here
            shoppinglist = new List("Shoppinglist", Choice.IMPLICIT);//GEN-BEGIN:|83-getter|1|83-postInit
            shoppinglist.addCommand(getExitCommand2());
            shoppinglist.addCommand(getNewItemCommand1());
            shoppinglist.addCommand(getOpenServerUrlConfig1());
            shoppinglist.setCommandListener(this);//GEN-END:|83-getter|1|83-postInit
            // write post-init user code here
            deleteSelectedItemCommand = new Command("Delete item", Command.ITEM, 5);
            shoppinglist.setSelectCommand(deleteSelectedItemCommand);
        }//GEN-BEGIN:|83-getter|2|
        return shoppinglist;
    }
    //</editor-fold>//GEN-END:|83-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: shoppinglistAction ">//GEN-BEGIN:|83-action|0|83-preAction
    /**
     * Performs an action assigned to the selected list element in the shoppinglist component.
     */
    public void shoppinglistAction() {//GEN-END:|83-action|0|83-preAction
        // enter pre-action user code here
        String __selectedString = getShoppinglist().getString(getShoppinglist().getSelectedIndex());//GEN-LINE:|83-action|1|83-postAction
        // enter post-action user code here
    }//GEN-BEGIN:|83-action|2|
    //</editor-fold>//GEN-END:|83-action|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand2 ">//GEN-BEGIN:|87-getter|0|87-preInit
    /**
     * Returns an initiliazed instance of exitCommand2 component.
     * @return the initialized component instance
     */
    public Command getExitCommand2() {
        if (exitCommand2 == null) {//GEN-END:|87-getter|0|87-preInit
            // write pre-init user code here
            exitCommand2 = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|87-getter|1|87-postInit
            // write post-init user code here
        }//GEN-BEGIN:|87-getter|2|
        return exitCommand2;
    }
    //</editor-fold>//GEN-END:|87-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: newItemCommand1 ">//GEN-BEGIN:|90-getter|0|90-preInit
    /**
     * Returns an initiliazed instance of newItemCommand1 component.
     * @return the initialized component instance
     */
    public Command getNewItemCommand1() {
        if (newItemCommand1 == null) {//GEN-END:|90-getter|0|90-preInit
            // write pre-init user code here
            newItemCommand1 = new Command("Add Item", Command.SCREEN, 1);//GEN-LINE:|90-getter|1|90-postInit
            // write post-init user code here
        }//GEN-BEGIN:|90-getter|2|
        return newItemCommand1;
    }
    //</editor-fold>//GEN-END:|90-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: itemCommand3 ">//GEN-BEGIN:|95-getter|0|95-preInit
    /**
     * Returns an initiliazed instance of itemCommand3 component.
     * @return the initialized component instance
     */
    public Command getItemCommand3() {
        if (itemCommand3 == null) {//GEN-END:|95-getter|0|95-preInit
            // write pre-init user code here
            itemCommand3 = new Command("Item", Command.ITEM, 0);//GEN-LINE:|95-getter|1|95-postInit
            // write post-init user code here
        }//GEN-BEGIN:|95-getter|2|
        return itemCommand3;
    }
    //</editor-fold>//GEN-END:|95-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: deleteItemCommand ">//GEN-BEGIN:|97-getter|0|97-preInit
    /**
     * Returns an initiliazed instance of deleteItemCommand component.
     * @return the initialized component instance
     */
    public Command getDeleteItemCommand() {
        if (deleteItemCommand == null) {//GEN-END:|97-getter|0|97-preInit
            // write pre-init user code here
            deleteItemCommand = new Command("Delete Item", Command.ITEM, 2);//GEN-LINE:|97-getter|1|97-postInit
            // write post-init user code here
        }//GEN-BEGIN:|97-getter|2|
        return deleteItemCommand;
    }
    //</editor-fold>//GEN-END:|97-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: serverUrl ">//GEN-BEGIN:|100-getter|0|100-preInit
    /**
     * Returns an initiliazed instance of serverUrl component.
     * @return the initialized component instance
     */
    public TextBox getServerUrl() {
        if (serverUrl == null) {//GEN-END:|100-getter|0|100-preInit
            // write pre-init user code here
            serverUrl = new TextBox("Server URL", null, 100, TextField.ANY);//GEN-BEGIN:|100-getter|1|100-postInit
            serverUrl.addCommand(getSubmitServerUrlCommand());
            serverUrl.addCommand(getCancelServerUrlCommand());
            serverUrl.setCommandListener(this);//GEN-END:|100-getter|1|100-postInit
            try {
                RecordStore db = RecordStore.openRecordStore("shoppingListDb", true);
                if (db.getNumRecords() != 1) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    DataOutputStream dos = new DataOutputStream(baos);
                    dos.writeUTF("");
                    dos.close();
                    baos.close();
                    byte[] bytes = baos.toByteArray();
                    db.addRecord(bytes, 0, bytes.length);
                }
                byte[] bytes = db.getRecord(1);
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                DataInputStream dis = new DataInputStream(bais);
                url = dis.readUTF();
                dis.close();
                bais.close();
                db.closeRecordStore();
            } catch (Exception e) {
                switchDisplayable(
                new Alert("Error", "Error opening configuration: " + e.getMessage(), null, AlertType.ERROR),
                getServerUrl());
            }
            serverUrl.setString(url);

        }//GEN-BEGIN:|100-getter|2|
        return serverUrl;
    }
    //</editor-fold>//GEN-END:|100-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: submitServerUrlCommand ">//GEN-BEGIN:|101-getter|0|101-preInit
    /**
     * Returns an initiliazed instance of submitServerUrlCommand component.
     * @return the initialized component instance
     */
    public Command getSubmitServerUrlCommand() {
        if (submitServerUrlCommand == null) {//GEN-END:|101-getter|0|101-preInit
            // write pre-init user code here
            submitServerUrlCommand = new Command("Ok", Command.OK, 0);//GEN-LINE:|101-getter|1|101-postInit
            // write post-init user code here
        }//GEN-BEGIN:|101-getter|2|
        return submitServerUrlCommand;
    }
    //</editor-fold>//GEN-END:|101-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cancelServerUrlCommand ">//GEN-BEGIN:|103-getter|0|103-preInit
    /**
     * Returns an initiliazed instance of cancelServerUrlCommand component.
     * @return the initialized component instance
     */
    public Command getCancelServerUrlCommand() {
        if (cancelServerUrlCommand == null) {//GEN-END:|103-getter|0|103-preInit
            // write pre-init user code here
            cancelServerUrlCommand = new Command("Cancel", Command.CANCEL, 0);//GEN-LINE:|103-getter|1|103-postInit
            // write post-init user code here
        }//GEN-BEGIN:|103-getter|2|
        return cancelServerUrlCommand;
    }
    //</editor-fold>//GEN-END:|103-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: openServerUrlConfig ">//GEN-BEGIN:|106-getter|0|106-preInit
    /**
     * Returns an initiliazed instance of openServerUrlConfig component.
     * @return the initialized component instance
     */
    public Command getOpenServerUrlConfig() {
        if (openServerUrlConfig == null) {//GEN-END:|106-getter|0|106-preInit
            // write pre-init user code here
            openServerUrlConfig = new Command("Item", Command.ITEM, 0);//GEN-LINE:|106-getter|1|106-postInit
            // write post-init user code here
        }//GEN-BEGIN:|106-getter|2|
        return openServerUrlConfig;
    }
    //</editor-fold>//GEN-END:|106-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: openServerUrlConfig1 ">//GEN-BEGIN:|109-getter|0|109-preInit
    /**
     * Returns an initiliazed instance of openServerUrlConfig1 component.
     * @return the initialized component instance
     */
    public Command getOpenServerUrlConfig1() {
        if (openServerUrlConfig1 == null) {//GEN-END:|109-getter|0|109-preInit
            // write pre-init user code here
            openServerUrlConfig1 = new Command("Edit server URL", Command.ITEM, 0);//GEN-LINE:|109-getter|1|109-postInit
            // write post-init user code here
        }//GEN-BEGIN:|109-getter|2|
        return openServerUrlConfig1;
    }
    //</editor-fold>//GEN-END:|109-getter|2|

    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay () {
        return Display.getDisplay(this);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable (null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet ();
        } else {
            initialize ();
            startMIDlet ();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
    }
}
