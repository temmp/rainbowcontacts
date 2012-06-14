package ccdr.rainbow.Tool;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class GetServerData {
	String[] F = {"false"}; 
	HttpURLConnection httpConn;
	ByteArrayOutputStream os ;
	InputStream is;
	DataInputStream dis;
	
	public  String[] getServerMessage(String url){
		try {   
            URL u = new URL(url);   
             httpConn = (HttpURLConnection) u.openConnection();   
               
                // httpConn.setRequestProperty("Content-Type", "application/octet-stream");   
                 httpConn.setRequestProperty("Content-Type", "text/html");   
                 httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接   
                 httpConn.setRequestProperty("Charset", "UTF-8");               
        	   int code = httpConn.getResponseCode();          //!!!!        
            if (code == HttpURLConnection.HTTP_OK) {   
                os = new ByteArrayOutputStream();   
                 is =  httpConn.getInputStream();//数据库中的值   
                  
                 dis = new DataInputStream(is);   
                   
                byte[] buffer = new byte[1024];   
                int readLen = -1;   
                while ((readLen = is.read(buffer, 0, 1024)) != -1) {   
                    os.write(buffer);   
                } // end while   
                   
                byte[] dbByte = os.toByteArray();   
                String str = new String(dbByte,"UTF-8");   
                JSONObject demoJson = new JSONObject(str);
                String message = demoJson.getString("instruction");
                String version = demoJson.getString("versioncode");
                String name = demoJson.getString("versionname");
                String downloadurl = demoJson.getString("apkurl");
                String [] message_all={message,version,name,downloadurl};
                
                os.close();
                is.close();
                dis.close();
                httpConn.disconnect();
                return message_all;
            }   
               
        } catch (Exception e) {   
            // TODO Auto-generated catch block   
//            e.printStackTrace();   
        	
				return F;  
			
           
        }
		return F;   
	}
			

	}

