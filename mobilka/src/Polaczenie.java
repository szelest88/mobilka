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


//hm, co bêdzie robi³ obiekt tej klasy?
/*
Ogólnie idzie o przerzucenie czêœci funkcjonalnoœci z Aplikacja_mobilna.java tutaj, tylko pytanie, jak zgrabnie uda siê to po³¹czyæ.
Ok, teraz dzia³a, ale trochê wyszed³ syf w kodzie.
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
			// Fetch the required page, reading up to a maximum of 128 bytes
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
				for(int i = 0 ; i<reply.length();i++) //zabawa z formatowaniem, przyda³by siê parser...
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
				messageLabel.setText("Wynik:\n"+reply); //commented when added

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
	String slij(String url, String co, Form wyslijForm, String requeststring)
	{

			HttpConnection hc = null; 
			DataInputStream dis = null; 
			DataOutputStream dos = null; 
			StringBuffer messagebuffer = new StringBuffer(); 
			try 
			{ //open up a http connection with the Web server for both send and receive operations 
			    hc = (HttpConnection) Connector.open(url, Connector.READ_WRITE); // Set the request method to POST 				
		    	    hc.setRequestMethod(HttpConnection.POST); 
		  	    //send the string entered by user byte by byte 
			    dos = hc.openDataOutputStream(); 
			    byte[] request_body = requeststring.getBytes(); 
			    for (int i = 0; i < request_body.length; i++) 
		  	    { 
		  	        dos.writeByte(request_body[i]); 
			    } 
		            dos.flush(); 
			    dos.close(); 
			    //retrieve the response back from the server
		            dis = new DataInputStream(hc.openInputStream()); 
			    int ch; 
			    //check the content length first 
			    long len = hc.getLength(); 
			    if(len!=-1) 
			    { 
			         for(int i = 0;i<len;i++) 
		    		     if((ch = dis.read())!= -1) 
		 	                 messagebuffer.append((char)ch); 
			    } 
			    else 
			     { // if the content length is not available 
			          while ((ch = dis.read()) != -1) 
		                      messagebuffer.append((char) ch); 
			    } 
			    dis.close(); 
				
			} 
			
			catch (IOException ioe) 
			{ 
				messagebuffer = new StringBuffer("error"); 
			} 
			finally 
			{ // Free up i/o streams and http connection 
				try 
				{ 
				    if (hc != null) 
					hc.close(); 
				}catch (IOException ignored) {} 
				try 
				{ 
		    		    if (dis != null) 
					dis.close(); 
		      	        }catch (IOException ignored) {} 
				try 
				{ 
		  		    if (dos != null) 
					dos.close(); 
			        } 
				catch (IOException ignored) {} 
			} 
			return messagebuffer.toString(); 
	}	
	
	}
