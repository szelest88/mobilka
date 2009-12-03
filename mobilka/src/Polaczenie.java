/*
 * RANY!
 * Chcia³o Ci siê otwieraæ ten plik?
 * 				Wyrazy najserdeczniejszego zdziwienia,
 * 				Artur M.
 */

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextBox;



/*
Obiekt tej klasy ma metody do pobierania i wysy³ania danych, nie jest to tak jak na UMLu, ale chwilowo tak by³o wygodniej
 */

public class Polaczenie 
{

	void polacz(String url, Display display, Form addressForm, Form displayForm, StringItem messageLabel){
		InputStream is = null;
		HttpConnection conn = null;

		try 
		{
			if (!url.startsWith("http://") && !url.startsWith("https://")) 
			{
				url = "http://" + url;
			}
			conn = (HttpConnection)Connector.open(url, Connector.READ_WRITE);
		} catch (Exception ex) 
		{
			System.out.println(ex);
			ex.printStackTrace();
			Alert alert = new Alert("Invalid Address",
					"Podany adres jest nieprawid³owy\n" +
					"Popraw go i spróbuj ponownie.", null,
					AlertType.ERROR);
			alert.setTimeout(Alert.FOREVER);
			display.setCurrent(alert, addressForm);
			return;
		}

		try 
		{
			// Fetch the required page, reading up to a maximum of 1024 bytes
			if (conn.getResponseCode() == HttpConnection.HTTP_OK) 
			{
				is = conn.openInputStream();
				final int MAX_LENGTH = 1024;
				byte[] buf = new byte[MAX_LENGTH];
				int total = 0;
				while (total < MAX_LENGTH) 
				{
					int count = is.read(buf, total, MAX_LENGTH - total);
					if (count < 0) 
					{
						break;
					}
					total += count;
				}
				is.close();
				String reply = new String(buf, 0, total); //dane z serwera
				//added

				String result=new String();
				boolean blokowanie=false;
				boolean enter=false;
				for(int i = 0 ; i<reply.length();i++) 	//zabawa z formatowaniem, przyda³by siê parser / jakaœ
														// funkcja deserializuj¹ca (J2ME [*] )
														//pytanie: czy jest sens deserializacji tego szajsu?
														//i tak idzie tylko o to, ¿eby wyœwietliæ dane na ekranik.
				{

					if(enter)
					{
						if(reply.charAt(i)!='\n')result+="\n"; //dupa, poprawiæ to - dodaæ jakies info, co jest w ogóle wyœwietlane i usun¹æ nadmiar "\n"-ów.
						enter=false;
					}

					if (reply.charAt(i)=='<' ){blokowanie=true;continue;}
					if(reply.charAt(i)=='>'){blokowanie=false;enter=true;continue;}
					if(reply.charAt(i)=='/'){continue;}
					if(!blokowanie)
						result+=reply.charAt(i);

				}
				reply=result;
				messageLabel.setText("Wynik:\n");
				messageLabel.setText(messageLabel.getText()+reply); //commented when added

			} else 
			{
				messageLabel.setText("Failed: error " + conn.getResponseCode() +
						"\n" + conn.getResponseMessage());
			}

			// Get the response code and print all the headers
			System.out.println("Response code = " + conn.getResponseCode());
			System.out.println("Response message = " + conn.getResponseMessage());
			for (int i = 0; ; i++) {
				String key = conn.getHeaderFieldKey(i);
				String value = conn.getHeaderField(i);
				if (key == null) 
				{
					// Reached last header
					break;
				}
				System.out.println(key + " = " + value);
			}
			conn.close();
			display.setCurrent(displayForm);
		} catch (IOException ex) 
		{
			System.out.println(ex);
			ex.printStackTrace();
			Alert alert = new Alert("I/O Error",
					"An error occurred while communicating with the server.",
					null, AlertType.ERROR);
			alert.setTimeout(Alert.FOREVER);
			display.setCurrent(alert, addressForm);
			return;
		} finally 
		{
			// Close open streams
			try {
				if (is != null) 
				{
					is.close();
					is = null;
				}
			} catch (IOException ex1) 
			{
			}
			try {
				if (conn != null) 
				{
					conn.close();
					conn = null;
				}
			} catch (IOException ex1) 
			{
			}
		}
	}
	
	//jakas tam g³upota
	void slij(String url, String co)throws IOException 
	//gdzieœ tu jest b³¹d, bo ³¹czyæ siê ³¹czy, ale nie wysy³a danych. Jeszcze nie wiem, gdzie...
	{

	        HttpConnection c = null;
	        InputStream is = null;
	        OutputStream dos = null;//output
	        StringBuffer b = new StringBuffer();
	        try {
	          c = (HttpConnection)Connector.open(url,Connector.READ_WRITE);//?
	          c.setRequestMethod(HttpConnection.POST);
	          c.setRequestProperty("Content-Type", "text/plain");
	          c.setRequestProperty("IF-Modified-Since", "20 Jan 2001 16:19:14 GMT"); //to chyba trzeba po prostu poustawiaæ
 	          c.setRequestProperty("User-Agent","Profile/MIDP-1.0 Configuration/CLDC-1.0");
	          c.setRequestProperty("Content-Language", "en-CA");
	          dos = c.openOutputStream();

	          //start
	          String str = "nadawca=71&odbiorcy=1,2,3&typ=publiczny&timestamp=0&kanal=ppp&tresc=dupa\n"; //w sumie zasadniczy post
	          //bez tego dzia³a, tzn. z pustym stringiem
	          byte postmsg[] = str.getBytes();
	          //for(int i=0;i<postmsg.length;i++) {
	            dos.write(str.getBytes()); //write
	          //}
	          
	          dos.flush();
	          //end

	          is = c.openDataInputStream(); //resp
	          int ch;
	          while ((ch = is.read()) != -1) {
	            b.append((char) ch);
	            if((char)ch!='\n')
	            System.out.println((char)ch);
	          }
	        }catch(IOException e){
	        	System.out.println("Coœ nie tak, a konkretnie to:");
	        	System.out.println(e.toString());
	        } finally {
	           if(is!= null) {
	              is.close();
	           }
	           if(dos != null) {
	              dos.close();
	           }
	           if(c != null) {
	              c.close();
	           }
	        }
	        
	}	
	
	}
