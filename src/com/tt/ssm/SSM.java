package com.tt.ssm;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.tt.ssm.misc.Logger;
import com.tt.ssm.view.SSMFrame;

public class SSM {
	
	private static Logger logger = Logger.getLogger(SSM.class);
	
	public static void main(String[] args) {
		logger.i("main");
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					logger.w("error setting laf: " + e.getMessage());
				}
				new SSMFrame();
			}
		};
		SwingUtilities.invokeLater(runnable);
		logger.i("main completed");
	}
	
}