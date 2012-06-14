package ccdr.rainbow.Tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.os.Environment;
import android.text.format.Time;

public class AppExcepiton implements UncaughtExceptionHandler {

	// 获取application 对象；
	private Context mContext;

	private Thread.UncaughtExceptionHandler defaultExceptionHandler;
	// 单例声明CustomException;
	private static AppExcepiton appException;

	public static AppExcepiton getInstance() {
		if (appException == null) {
			appException = new AppExcepiton();
		}
		return appException;
	}

	/**
	 * 获得当前时间
	 * 
	 * @return
	 */
	public String getCurrentTime() {
		Time t = new Time();
		t.setToNow();
		int year = t.year;
		int month = t.month + 1;
		int day = t.monthDay;
		int hour = t.hour;
		int minute = t.minute;
		int second = t.second;
		String time = year + "-" + month + "-" + day + " " + hour + ":"
				+ minute + ":" + second;
		return time;

	}

	public void init(Context context) {
		mContext = context;
		defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 删除超过一年的日志
	 * 
	 * @param path
	 */
	public void deleteOldFile(final String path) {
		File file = new File(path);
		file.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				// TODO Auto-generated method stub
				File file = new File(path + "/" + filename);
				Long ago = file.lastModified();
				Long now = System.currentTimeMillis();
				// 如果最后一次修改时间超过一年：3153600秒
				if ((now - ago) > 31536000) {
					file.delete();
				}
				return false;
			}
		});

	}

	@Override
	public void uncaughtException(Thread thread, Throwable exception) {
		// TODO Auto-generated method stub

		StackTraceElement[] stack = exception.getCause().getStackTrace();
		String path = null;
		if (defaultExceptionHandler != null) {
			String state = Environment.getExternalStorageState();
			// 判断SdCard是否存在并且是可用的
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				path = Environment.getExternalStorageDirectory().getPath();

			}
			// 创建一个logcat目录
			path = path + File.separator + "log";
			File file = new File(path);
			if (!file.exists()) {
				file.mkdir();
			}
			// 删除过期文件
			deleteOldFile(path);
			String time = getCurrentTime();
			String fileName = time.substring(0, 9);

			File myFile = new File(path + "/" + fileName + ".log");
			try {
				String str = "\n" + time + "-->";
				FileOutputStream fos = new FileOutputStream(myFile, true);
				fos.write(str.getBytes());
				for (int i = 0; i < stack.length; i++) {
					fos.write(stack[i].toString().getBytes());
				}

				fos.flush();
				fos.close();

			} catch (Exception e) {

			}
		}
	}
}