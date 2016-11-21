package com.tt.ssm;

import javax.swing.SwingUtilities;

import com.tt.ssm.view.SSMFrame;

public class SSM {
	
	public static void main(String[] args) {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
				}
				new SSMFrame();
			}
		};
		SwingUtilities.invokeLater(runnable);
	}
	
}