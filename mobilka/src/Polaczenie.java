import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Form;


//hm, co b�dzie robi� obiekt tej klasy?
/*
Og�lnie idzie o przeczucenie cz�ci funckonalno�ci z Aplikacja_mobilna.java tutaj, tylko pytanie, jak zgrabnie uda si� to po��czy�.
 */
public class Polaczenie 
{

	HttpConnection conn;

	void polacz(String url, Display display, Form addressForm, InputStream is) //w wywo�aniu b�dzie serverURL.getString()
	{
		conn = null;

		try 
		{
			if (!url.startsWith("http://") && !url.startsWith("https://")) 
			{
				url = "http://" + url;
			}
			conn = (HttpConnection)Connector.open(url, Connector.READ_WRITE);
		} catch (Exception ex) {
			System.out.println(ex);
			ex.printStackTrace();
			Alert alert = new Alert("Invalid Address",
					"The supplied address is invalid\n" +
					"Please correct it and try again.", null,
					AlertType.ERROR);
			alert.setTimeout(Alert.FOREVER);
			display.setCurrent(alert, addressForm);
			return;
		}

		try {
			// Fetch the required page, reading up to a maximum of 128 bytes
			if (conn.getResponseCode() == HttpConnection.HTTP_OK) {
				is = conn.openInputStream();
				final int MAX_LENGTH = 1024;
				byte[] buf = new byte[MAX_LENGTH];
				int total = 0;
				while (total < MAX_LENGTH) {
					int count = is.read(buf, total, MAX_LENGTH - total);
					if (count < 0) {
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
				for(int i = 0 ; i<reply.length();i++) //zabawa z formatowaniem, przyda�by si� parser...
				{

					if(enter)
					{
						result+="\n";
						enter=false;
					}

					if (reply.charAt(i)=='<' ){blokowanie=true;continue;}
					if(reply.charAt(i)=='>'){blokowanie=false;continue;}
					if(reply.charAt(i)=='/'){enter=true;continue;}
					if(!blokowanie)
						result+=reply.charAt(i);

				}
				reply=result;
			}

		}

}