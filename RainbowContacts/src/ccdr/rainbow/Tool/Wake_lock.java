package ccdr.rainbow.Tool;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class Wake_lock extends Activity {
	private PowerManager mPowerManager;// ��Դ���ƹ�����
	private WakeLock mWakeLock;// ������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ������onCreate���洴��
		mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(
				PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "abc");
	}

	// �ָ�ʱ�������
	public void onResumeAcquire() {
		mWakeLock.acquire();
	}

	// �ͷ�
	public void onPauseRelease() {
		mWakeLock.release();

	}
}
