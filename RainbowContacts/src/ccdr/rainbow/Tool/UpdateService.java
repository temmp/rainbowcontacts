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
	
	//标题
    private String titleId ;
    private String downloadurl;
    private final static int DOWNLOAD_COMPLETE = 0;
    private final static int DOWNLOAD_FAIL = 1;
    //文件存储
    private File updateDir = null;
    private static File updateFile = null;
    private RemoteViews view = null;
    //通知栏
    int notification_id=19172439;
    private static NotificationManager updateNotificationManager = null;
    private static Notification updateNotification = null;
    //通知栏跳转Intent
    private static Intent updateIntent = null;
    private static PendingIntent updatePendingIntent = null;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //获取传值
    	 Bundle bundle = intent.getExtras();     
//         String data=bundle.getString("Data");
        titleId = bundle.getString("titleId");
        downloadurl = bundle.getString("apkurl");
        //创建文件
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
     
        //设置下载过程中，点击通知栏，回到主界面

        updateNotification=new Notification(R.drawable.icon,"开始下载",System.currentTimeMillis());
        updateNotification.contentView = new RemoteViews(getPackageName(),R.layout.updata_nitification); 
        //设置通知栏显示内容
//        updateNotification.icon = android.R.drawable.stat_sys_download;
//        updateNotification.tickerText = "开始下载";
//        updateNotification.setLatestEventInfo(this,titleId,"0%",updatePendingIntent);
//        updateNotification.contentView.setTextViewText(R.id.update_notification_progresstext, "0%");

//        updateNotification.contentView.setProgressBar(R.id.update_notification_progressbar, 100, 0, false);
        //发出通知
        updateNotification.contentView.setProgressBar(R.id.update_notification_progressbar, 100,0, false);
        updateIntent = new Intent(this, RainbowContactsActivity.class);
        updateIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        updatePendingIntent = PendingIntent.getActivity(this,0,updateIntent,0);
        updateNotification.contentIntent = updatePendingIntent; 


        

        //开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
        new Thread(new updateRunnable()).start();//这个是下载的重点，是下载的过程
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
                //点击安装PendingIntent
            	updateFile = new File(updateDir.getPath(),titleId+".apk");
                System.out.println("安装--"+updateFile);
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
                
                updateNotification.defaults = Notification.DEFAULT_SOUND;//铃声提醒 
                updateNotification.setLatestEventInfo(UpdateService.this, titleId, "下载完成,点击安装。", updatePendingIntent);
                updateNotificationManager.notify(0, updateNotification);
                Constants_Global.SHOW_Dialog = 0;
                //停止服务
                stopService(updateIntent);
            case DOWNLOAD_FAIL:
                //下载失败
            	System.out.println("fail");
                updateNotification.setLatestEventInfo(UpdateService.this, titleId, "下载失败,请检查网络连接后重启软件。", updatePendingIntent);
                updateNotificationManager.notify(0, updateNotification);
                Constants_Global.SHOW_Dialog = 0;
            default:
            	Constants_Global.SHOW_Dialog = 0;
                stopService(updateIntent);
        }

        }
    };
    public long downloadUpdateFile(String downloadUrl, File saveFile) throws Exception {
        //这样的下载代码很多，我就不做过多的说明
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
                //为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
                if((downloadCount == 0)||(int) (totalSize*100/updateTotalSize)-10>downloadCount){ 
                    downloadCount += 10;
                   /* updateNotification.setLatestEventInfo(UpdateService.this, "正在下载", (int)totalSize*100/updateTotalSize+"%", updatePendingIntent);
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
               //增加权限;
               if(!updateDir.exists()){
                   updateDir.mkdirs();
               }
               if(!updateFile.exists()){
                   updateFile.createNewFile();
               }
               //增加权限;
//               long downloadSize = downloadUpdateFile("http://u.androidgame-store.com/android1/new/game1/17/108417/dmyl_1.apk",updateFile);  
               long downloadSize = downloadUpdateFile(downloadurl+titleId+".apk",updateFile);
//               long downloadSize = downloadUpdateFile("http://10.1.11.29:8888/Demo_Test/Download/3.0.apk",updateFile);
               System.out.println("downloadSize--"+downloadSize);
               if(downloadSize>0){
                   //下载成功
                   updateHandler.sendMessage(message);
               }
           }catch(Exception ex){
               ex.printStackTrace();
               message.what = DOWNLOAD_FAIL;
               //下载失败
               updateHandler.sendMessage(message);
           }
       }
   }

   }

//}
