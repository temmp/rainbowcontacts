package ccdr.rainbow.Tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.ccdr.rainbowcontacts.R;
import com.ccdr.rainbowcontacts.RainbowContactsActivity;


import ccdr.rainbow.Constants.Constants_Global;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.RemoteViews;

public class UpdateService extends Service{
	
	//����
    private String titleId ;
    private String downloadurl;
    private final static int DOWNLOAD_COMPLETE = 0;
    private final static int DOWNLOAD_FAIL = 1;
    //�ļ��洢
    private File updateDir = null;
    private static File updateFile = null;
    private RemoteViews view = null;
    //֪ͨ��
    int notification_id=19172439;
    private static NotificationManager updateNotificationManager = null;
    private static Notification updateNotification = null;
    //֪ͨ����תIntent
    private static Intent updateIntent = null;
    private static PendingIntent updatePendingIntent = null;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //��ȡ��ֵ
    	 Bundle bundle = intent.getExtras();     
//         String data=bundle.getString("Data");
        titleId = bundle.getString("titleId");
        downloadurl = bundle.getString("apkurl");
        //�����ļ�
        if(android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())){	
        	updateDir = new File(Environment.getExternalStorageDirectory(),Constants_Global.DOWNLOAD_DIR);
            updateFile = new File(updateDir.getPath(),titleId+".apk");
        }else{
        	updateDir = new File("/data/data/pip.UIofPIP/");
            updateFile = new File(updateDir.getPath(),titleId+".apk");
            String cmd = "chmod 644 " +updateFile.getPath();   
        	try {  
        		Runtime.getRuntime().exec("chmod 751 " +updateDir.getPath());
        	    Runtime.getRuntime().exec(cmd);   
        	} catch (IOException e) {   
        	    e.printStackTrace();   
        	} 
        }

        UpdateService.updateNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        UpdateService.updateNotification = new Notification();
     
        //�������ع����У����֪ͨ�����ص�������

        updateNotification=new Notification(R.drawable.icon,"��ʼ����",System.currentTimeMillis());
        updateNotification.contentView = new RemoteViews(getPackageName(),R.layout.updata_nitification); 
        //����֪ͨ����ʾ����
//        updateNotification.icon = android.R.drawable.stat_sys_download;
//        updateNotification.tickerText = "��ʼ����";
//        updateNotification.setLatestEventInfo(this,titleId,"0%",updatePendingIntent);
//        updateNotification.contentView.setTextViewText(R.id.update_notification_progresstext, "0%");

//        updateNotification.contentView.setProgressBar(R.id.update_notification_progressbar, 100, 0, false);
        //����֪ͨ
        updateNotification.contentView.setProgressBar(R.id.update_notification_progressbar, 100,0, false);
        updateIntent = new Intent(this, RainbowContactsActivity.class);
        updateIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        updatePendingIntent = PendingIntent.getActivity(this,0,updateIntent,0);
        updateNotification.contentIntent = updatePendingIntent; 


        

        //����һ���µ��߳����أ����ʹ��Serviceͬ�����أ��ᵼ��ANR���⣬Service����Ҳ������
        new Thread(new updateRunnable()).start();//��������ص��ص㣬�����صĹ���
        Constants_Global.SHOW_Dialog = 1;
        return super.onStartCommand(intent, flags, startId);
    }


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	public  Handler updateHandler = new  Handler(){
        @Override
        public void handleMessage(Message msg) {
        	switch(msg.what){
            case DOWNLOAD_COMPLETE:
            	System.out.println("complete");
                //�����װPendingIntent
            	updateFile = new File(updateDir.getPath(),titleId+".apk");
                System.out.println("��װ--"+updateFile);
                String cmd = "chmod 644 " +updateFile.getPath();   
             	try {   
             		Runtime.getRuntime().exec("chmod 751 " +updateDir.getPath());
             	    Runtime.getRuntime().exec(cmd);   
             	} catch (IOException e) {   
             	    e.printStackTrace();   
             	} 
             	Uri uri = Uri.fromFile(updateFile);
                Intent installIntent = new Intent(Intent.ACTION_VIEW);
                installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
                updatePendingIntent = PendingIntent.getActivity(UpdateService.this, 0, installIntent, 0);
                
                updateNotification.defaults = Notification.DEFAULT_SOUND;//�������� 
                updateNotification.setLatestEventInfo(UpdateService.this, titleId, "�������,�����װ��", updatePendingIntent);
                updateNotificationManager.notify(0, updateNotification);
                Constants_Global.SHOW_Dialog = 0;
                //ֹͣ����
                stopService(updateIntent);
            case DOWNLOAD_FAIL:
                //����ʧ��
            	System.out.println("fail");
                updateNotification.setLatestEventInfo(UpdateService.this, titleId, "����ʧ��,�����������Ӻ����������", updatePendingIntent);
                updateNotificationManager.notify(0, updateNotification);
                Constants_Global.SHOW_Dialog = 0;
            default:
            	Constants_Global.SHOW_Dialog = 0;
                stopService(updateIntent);
        }

        }
    };
    public long downloadUpdateFile(String downloadUrl, File saveFile) throws Exception {
        //���������ش���ܶ࣬�ҾͲ��������˵��
        int downloadCount = 0;
        int currentSize = 0;
        long totalSize = 0;
        int updateTotalSize = 0;
        
        HttpURLConnection httpConnection = null;
        InputStream is = null;
        FileOutputStream fos = null;
        
        try {
            URL url = new URL(downloadUrl);
            httpConnection = (HttpURLConnection)url.openConnection();
            httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
            if(currentSize > 0) {
                httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
            }
            httpConnection.setConnectTimeout(10000);
            httpConnection.setReadTimeout(20000);
            updateTotalSize = httpConnection.getContentLength();
            if (httpConnection.getResponseCode() == 404) {
            	System.out.println("fail--11");
                throw new Exception("fail!");
            }
            is = httpConnection.getInputStream();                   
            fos = new FileOutputStream(saveFile, false);
            byte buffer[] = new byte[4096];
            int readsize = 0;
            while((readsize = is.read(buffer)) > 0){
                fos.write(buffer, 0, readsize);
                totalSize += readsize;
                //Ϊ�˷�ֹƵ����֪ͨ����Ӧ�óԽ����ٷֱ�����10��֪ͨһ��
                if((downloadCount == 0)||(int) (totalSize*100/updateTotalSize)-10>downloadCount){ 
                    downloadCount += 10;
                   /* updateNotification.setLatestEventInfo(UpdateService.this, "��������", (int)totalSize*100/updateTotalSize+"%", updatePendingIntent);
//                    updateNotification.contentView.setProgressBar(R.id.update_notification_progressbar, 100, (int)(totalSize*100/updateTotalSize), false);
                    updateNotificationManager.notify(0, updateNotification);*/
                    updateNotification.contentView.setProgressBar(R.id.update_notification_progressbar, 100, (int)(totalSize*100/updateTotalSize), false);
                    updateNotification.contentView.setTextViewText(R.id.update_notification_progresstext, (int)(totalSize*100/updateTotalSize)+"%");
                    updateNotificationManager.notify(0, updateNotification);
                }                        
            }
        } finally {
            if(httpConnection != null) {
            	System.out.println("zhixinglalaaaaaaaaaaaaaaaaaaa");
                httpConnection.disconnect();
            }
            if(is != null) {
                is.close();
            }
            if(fos != null) {
                fos.close();
            }
        }
        return totalSize;
    }
    class updateRunnable implements Runnable {
//   	 private final static int DOWNLOAD_COMPLETE = 0;
//   	    private final static int DOWNLOAD_FAIL = 1;
//   	    private String updateDir = Environment
//   		.getExternalStorageDirectory().getAbsolutePath()+"/"+Global.downloadDir+; 
       Message message = updateHandler.obtainMessage();
       @Override
	public void run() {
           message.what = DOWNLOAD_COMPLETE;
           try{
               //����Ȩ��;
               if(!updateDir.exists()){
                   updateDir.mkdirs();
               }
               if(!updateFile.exists()){
                   updateFile.createNewFile();
               }
               //����Ȩ��;
//               long downloadSize = downloadUpdateFile("http://u.androidgame-store.com/android1/new/game1/17/108417/dmyl_1.apk",updateFile);  
               long downloadSize = downloadUpdateFile(downloadurl+titleId+".apk",updateFile);
//               long downloadSize = downloadUpdateFile("http://10.1.11.29:8888/Demo_Test/Download/3.0.apk",updateFile);
               System.out.println("downloadSize--"+downloadSize);
               if(downloadSize>0){
                   //���سɹ�
                   updateHandler.sendMessage(message);
               }
           }catch(Exception ex){
               ex.printStackTrace();
               message.what = DOWNLOAD_FAIL;
               //����ʧ��
               updateHandler.sendMessage(message);
           }
       }
   }

   }

//}
