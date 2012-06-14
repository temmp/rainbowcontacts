package ccdr.rainbow.Tool;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class Wake_lock extends Activity {
	private PowerManager mPowerManager;// 电源控制管理器
	private WakeLock mWakeLock;// 待机锁

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 必须在onCreate后面创建
		mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(
				PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "abc");
	}

	// 恢复时解除锁屏
	public void onResumeAcquire() {
		mWakeLock.acquire();
	}

	// 释放
	public void onPauseRelease() {
		mWakeLock.release();

	}
}
