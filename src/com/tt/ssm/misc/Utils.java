package com.tt.ssm.misc;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Utils {

	public static Dimension getCurrentDisplayDimension(JFrame frame) {
		try {
			DisplayMode displayMode = frame.getGraphicsConfiguration().getDevice().getDisplayMode();
			return (new Dimension(displayMode.getWidth(), displayMode.getHeight()));
		} catch (Exception e) {
			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
			return (dimension);		
		}
	}
	
}
