package ccdr.rainbow.Tool;

import android.util.Log;
import android.view.animation.Interpolator;


public class CustomInterpolator implements Interpolator {

	/**
	 * @param input
	 *            A value between 0 and 1.0 indicating our current point in the
	 *            animation where 0 represents the start and 1.0 represents the
	 *            end
	 * @return Returns The interpolation value. This value can be more than 1.0
	 *         for Interpolators which overshoot their targets, or less than 0
	 *         for Interpolators that undershoot their targets.
	 */
	@Override
	public float getInterpolation(float input) {
		Log.d("ANDROID_LAB", "input=" + input);
		
		final float inner = (input * 1.55f) - 1.1f;
		
		return 1.2f - inner * inner;
	}
}