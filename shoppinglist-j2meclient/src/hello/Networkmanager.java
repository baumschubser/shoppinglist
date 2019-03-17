/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hello;

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import javax.microedition.rms.*;

/**
 *
 * @author Matthias Clausen
 */
public class Networkmanager implements Runnable {
    private JSONArray list = null;
    HelloMIDlet app = null;

    public void submitItem(String text) throws IOException {
        app.getWaitScreen().deleteAll();
        app.getWaitScreen().append("Submitting item to server…");
        app.switchDisplayable(null, app.getWaitScreen());
        HttpConnection httpConn = null;
        try {
            // Open an HTTP Connection object
            httpConn = openConnection(app.url + "/create", HttpConnection.POST);
            httpConn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = null;
            OutputStreamWriter osw = null;
            try {
                os = httpConn.openOutputStream();
                osw = new OutputStreamWriter(os, "utf-8");
                Hashtable map = new Hashtable();
                map.put("text", text);
                osw.write(new JSONObject(map).toString());
            } finally {
                if (os != null) os.close();
            }

            String response = getResponse(httpConn);
            if (httpConn.getResponseCode() != 200) {
                app.switchDisplayable(
                        new Alert("Error", "Error submitting item: " + response, null, AlertType.ERROR),
                        app.getShoppinglist());
            } else {
                int id = Integer.valueOf(response).intValue();
                app.getShoppinglist().append(text, null);
                app.itemlist.addElement(new ShoppingItem(id, text, 0, ""));
            }
        } finally {
            if (httpConn != null) httpConn.close();
        }
        app.switchDisplayable(null, app.getShoppinglist());
    }

    public void deleteItem(int id) throws IOException {
        HttpConnection httpConn = null;
        try {
            // Open an HTTP Connection object
            httpConn = openConnection(app.url + "/delete", HttpConnection.POST);
            httpConn.setRequestProperty("Content-Type", "application/json");

            OutputStream os = null;
            try {
                os = httpConn.openOutputStream();
                Hashtable map = new Hashtable();
                map.put("id", new Integer(id));
                os.write(new JSONObject(map).toString().getBytes());
            } finally {
                if (os != null) os.close();
            }

            String response = getResponse(httpConn);
            if (httpConn.getResponseCode() != 200) {
                app.switchDisplayable(
                        new Alert("Error", "Error deleting item: " + response, null, AlertType.ERROR),
                        app.getShoppinglist());
            } else {
                int index = -1;
                for (int i = 0; i < app.itemlist.size(); i++) {
                    ShoppingItem item = (ShoppingItem)(app.itemlist.elementAt(i));
                    if (item.getId() == id) index = i;
                }
                if (index != -1) {
                    app.itemlist.removeElementAt(index);
                    app.getShoppinglist().delete(index);
                }
            }
        } finally {
            if (httpConn != null) httpConn.close();
        }
    }

    private String getResponse(HttpConnection conn) throws IOException {
        StringBuffer sb = new StringBuffer();
        InputStream is = null;

        try {
            is = conn.openDataInputStream();
            int chr;
            while ((chr = is.read()) != -1)
                sb.append((char) chr);
        } finally {
            if(is!= null) is.close();
        }
        return sb.toString();
    }

    public void getShoppinglistFromServer(String url) throws IOException {

    // open configuration record store or redirect to settings view if not existent
    try {
        RecordStore db = RecordStore.openRecordStore("shoppingListDb", false);
        if (db.getNumRecords() != 1) {
            app.switchDisplayable(null, app.getServerUrl());
        }
        byte[] bytes = db.getRecord(1);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bais);
        url = dis.readUTF();
        dis.close();
        bais.close();
        db.closeRecordStore();
    } catch (Exception e) {
        app.switchDisplayable(new Alert("Error", "Error opening configuration: " + e.getMessage(), null, AlertType.ERROR), app.getServerUrl());
    }

    if (app.url.compareTo("") == 0) {
        app.switchDisplayable(new Alert("Error", "Only got empty server URL", null, AlertType.ERROR), app.getServerUrl());
    }

    HttpConnection httpConn = null;

    InputStream is = null;
    OutputStream os = null;

    try {
      // Open an HTTP Connection object
      httpConn = openConnection(url + "/shoppinglist", HttpConnection.GET);

      /** Initiate connection and check for the response code. If the
        response code is HTTP_OK then get the content from the target
      **/
      int respCode = httpConn.getResponseCode();
      if (respCode == httpConn.HTTP_OK) {
        StringBuffer sb = new StringBuffer();
        os = httpConn.openOutputStream();
        is = httpConn.openDataInputStream();
        int chr;
        while ((chr = is.read()) != -1)
          sb.append((char) chr);

        String response = sb.toString();
                try {
                    list = new JSONArray(response);
                    app.getShoppinglist().deleteAll();
                    if (app.itemlist == null) app.itemlist = new Vector();
                    for (int i = 0; i < list.length(); i++) {
                        app.getShoppinglist().append(list.getJSONObject(i).getString("text"), null);
                        app.itemlist.addElement(
                            new ShoppingItem(
                                list.getJSONObject(i).getInt("id"),
                                list.getJSONObject(i).getString("text"),
                                list.getJSONObject(i).getInt("done"),
                                list.getJSONObject(i).getString("author"))
                            );
                    }
                    app.switchDisplayable(null, app.getShoppinglist());
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
      }
      else {
        System.out.println("Error in opening HTTP Connection. Error#" + respCode);
      }

      } finally {
        if(is!= null)
           is.close();
          if(os != null)
            os.close();
      if(httpConn != null)
            httpConn.close();
    }

    }

    private HttpConnection openConnection(String url, String requestMethod) throws IOException {
        // Open an HTTP Connection object
      HttpConnection httpConn = (HttpConnection)Connector.open(url);

      // Setup HTTP Request
      httpConn.setRequestMethod(requestMethod);
      httpConn.setRequestProperty("User-Agent",
        "Profile/MIDP-1.0 Confirguration/CLDC-1.0");
      
      return httpConn;
    }

    public void run() {
        try {
            getShoppinglistFromServer(app.url);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
