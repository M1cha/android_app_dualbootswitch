package com.xiaomi.dualbootswitch;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	static {
		System.loadLibrary("DualbootSwitch");
	}

	private native String getBootmode();

	private native String setBootmode(String bootmode);

	private void gainMiscAccess() throws Exception {
		CommandCapture command = new CommandCapture(0,
				"busybox chmod 0777 /dev/block/platform/msm_sdcc.1/by-name/misc");

		RootTools.getShell(true).add(command).waitForFinish();

	}

	private TextView labelSystem;
	private Button switchButton;
	private TextView errorText;
	private int targetSystem = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			gainMiscAccess();
			setContentView(R.layout.activity_main);
			labelSystem = (TextView)findViewById(R.id.labelSystem);
			switchButton = (Button)findViewById(R.id.switchButton);
			updateUI();
		} catch (Exception e) {
			setContentView(R.layout.error);
			errorText = (TextView)findViewById(R.id.errorText);
			errorText.setText(e.getMessage());
		}

	}

	private void updateUI() {
		String bootmode = getBootmode();

		if (bootmode.equals("boot-system0")) {
			targetSystem = 1;
			switchButton.setText("Switch to System 2");
			labelSystem.setText(bootmode+" (System1)");
		} else if (bootmode.equals("boot-system1")) {
			targetSystem = 0;
			switchButton.setText("Switch to System 1");
			labelSystem.setText(bootmode+" (System2)");
		} else {
			targetSystem = -1;
			switchButton.setText("Cannot Switch");
			labelSystem.setText(bootmode);
		}
	}

	public void switchBootmode(View v) {
		if (targetSystem == 0) {
			setBootmode("boot-system0");
		} else if (targetSystem == 1) {
			setBootmode("boot-system1");
		}
		updateUI();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
