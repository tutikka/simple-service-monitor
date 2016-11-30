package com.tt.ssm;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.tt.ssm.misc.Logger;
import com.tt.ssm.view.SSMFrame;

public class SSM {
	
	private static Logger logger = Logger.getLogger(SSM.class);
	
	public static void main(String[] args) {
		logger.i("main");
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// laf
				try {
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						logger.i("found installed laf: " + info.getClassName());
					}
					//
					// Examples from Ubuntu Linux with Java 7
					//
					// javax.swing.plaf.metal.MetalLookAndFeel
					// javax.swing.plaf.nimbus.NimbusLookAndFeel
					// com.sun.java.swing.plaf.motif.MotifLookAndFeel
					// com.sun.java.swing.plaf.gtk.GTKLookAndFeel
					//
					String laf = System.getProperty("ssm.laf.class");
					if (laf != null) {
						logger.i("setting laf: " + laf);
						UIManager.setLookAndFeel(laf);
					} else {
						logger.i("using default laf");
					}
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