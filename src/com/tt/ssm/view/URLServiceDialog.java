package com.tt.ssm.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;

import com.tt.ssm.service.impl.URLService;

@SuppressWarnings("serial")
public class URLServiceDialog extends JDialog implements ActionListener {

	private static final int DIALOG_WIDTH = 640;
	
	private static final int DIALOG_HEIGHT = 480;
	
	private Callback callback;
	
	private JTextField name;
	
	private JTextField group;
	
	private JTextField url;
	
	private JTextField expectedResponseCode;
	
	private JTextField interval;
	
	private JTextField warning;
	
	private JTextField error;
	
	public URLServiceDialog(JFrame parent, Callback callback) {
		super(parent);
		this.callback = callback;
		setTitle("URL Service");
		
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		setResizable(false);
		
		int x = parent.getX() + parent.getWidth() / 2 - DIALOG_WIDTH / 2;
		int y = parent.getY() + parent.getHeight() / 2 - DIALOG_HEIGHT / 2;
		setLocation(x, y);
		
		setModal(true);
	
		setLayout(new BorderLayout());
		
		add(createContentPanel(), BorderLayout.CENTER);
		add(createButtonsPanel(), BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if ("cancel".equals(e.getActionCommand())) {
			dispose();
		}
		if ("test".equals(e.getActionCommand())) {
			final URLService service = getService();
			if (service != null) {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						service.request();
						JOptionPane.showMessageDialog(URLServiceDialog.this, service.getResponse(), "Test", JOptionPane.INFORMATION_MESSAGE);
					}
				});
			}
		}
		if ("ok".equals(e.getActionCommand())) {
			URLService service = getService();
			if (service != null) {
				callback.onURLServiceAdded(service);
				dispose();
			}
		}
	}
	
	public interface Callback {
		
		public void onURLServiceAdded(URLService service);
		
	}
	
	/* ********** private ********** */
	
	private URLService getService() {
		// name
		String name = this.name.getText();
		if (name == null || name.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter the name", "Warning", JOptionPane.WARNING_MESSAGE);
			return (null);
		}
		// group
		String group = this.group.getText();
		if (group == null || group.isEmpty()) {
			// ignore
		}
		// url
		String url = this.url.getText();
		if (url == null || url.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter the URL", "Warning", JOptionPane.WARNING_MESSAGE);
			return (null);
		}
		// expected response code
		int expectedResponseCode;
		try {
			expectedResponseCode = Integer.parseInt(this.expectedResponseCode.getText());
			if (expectedResponseCode < 100 || expectedResponseCode > 599) {
				JOptionPane.showMessageDialog(this, "Please enter a valid response code (100 - 599)", "Warning", JOptionPane.WARNING_MESSAGE);
				return (null);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Please enter a valid response code (100 - 599)", "Warning", JOptionPane.WARNING_MESSAGE);
			return (null);
		}
		// interval
		long interval;
		try {
			interval = Long.parseLong(this.interval.getText());
			if (interval < 1000 || interval > 60 * 60 * 1000) {
				JOptionPane.showMessageDialog(this, "Please enter a valid interval (1000 - 600000)", "Warning", JOptionPane.WARNING_MESSAGE);
				return (null);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Please enter a valid interval (1000 - 600000)", "Warning", JOptionPane.WARNING_MESSAGE);
			return (null);
		}
		// warning
		long warning;
		try {
			warning = Long.parseLong(this.warning.getText());
			if (warning < 0 || warning > 60 * 60 * 1000) {
				JOptionPane.showMessageDialog(this, "Please enter a valid warning threshold (0 - 600000)", "Warning", JOptionPane.WARNING_MESSAGE);
				return (null);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Please enter a valid warning threshold (0 - 600000)", "Warning", JOptionPane.WARNING_MESSAGE);
			return (null);
		}
		// error
		long error;
		try {
			error = Long.parseLong(this.error.getText());
			if (error < 0 || error > 60 * 60 * 1000) {
				JOptionPane.showMessageDialog(this, "Please enter a valid error threshold (0 - 600000)", "Warning", JOptionPane.WARNING_MESSAGE);
				return (null);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Please enter a valid error threshold (0 - 600000)", "Warning", JOptionPane.WARNING_MESSAGE);
			return (null);
		}
		return (new URLService(name, group, url, expectedResponseCode, interval, warning, error));
	}
	
	private JPanel createContentPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5), BorderFactory.createRaisedBevelBorder()));
		
		JLabel nameLabel = new JLabel("* Name");
		panel.add(nameLabel);
		
		name = new JTextField("", 24);
		panel.add(name);
		
		JLabel groupLabel = new JLabel("Group");
		panel.add(groupLabel);
		
		group = new JTextField("", 24);
		panel.add(group);
		
		JLabel urlLabel = new JLabel("* URL");
		panel.add(urlLabel);
		
		url = new JTextField("http://", 24);
		panel.add(url);
		
		JLabel expectedResponseCodeLabel = new JLabel("* Expected response code (HTTP status)");
		panel.add(expectedResponseCodeLabel);
		
		expectedResponseCode = new JTextField("200", 24);
		panel.add(expectedResponseCode);
		
		JLabel intervalLabel = new JLabel("* Interval (ms)");
		panel.add(intervalLabel);
		
		interval = new JTextField("10000", 24);
		panel.add(interval);
		
		JLabel warningLabel = new JLabel("* Warning threshold (ms)");
		panel.add(warningLabel);
		
		warning = new JTextField("1000", 24);
		panel.add(warning);
		
		JLabel errorLabel = new JLabel("* Error threshold (ms)");
		panel.add(errorLabel);
		
		error = new JTextField("5000", 24);
		panel.add(error);
		
		SpringLayout sl = new SpringLayout();
		
		sl.putConstraint(SpringLayout.WEST, nameLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, nameLabel, 10, SpringLayout.NORTH, panel);
		
		sl.putConstraint(SpringLayout.EAST, name, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, name, 10, SpringLayout.NORTH, panel);
		
		sl.putConstraint(SpringLayout.WEST, groupLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, groupLabel, 10, SpringLayout.SOUTH, name);
		
		sl.putConstraint(SpringLayout.EAST, group, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, group, 10, SpringLayout.SOUTH, name);
		
		sl.putConstraint(SpringLayout.WEST, urlLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, urlLabel, 10, SpringLayout.SOUTH, group);
		
		sl.putConstraint(SpringLayout.EAST, url, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, url, 10, SpringLayout.SOUTH, group);
		
		sl.putConstraint(SpringLayout.WEST, expectedResponseCodeLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, expectedResponseCodeLabel, 10, SpringLayout.SOUTH, url);
		
		sl.putConstraint(SpringLayout.EAST, expectedResponseCode, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, expectedResponseCode, 10, SpringLayout.SOUTH, url);
		
		sl.putConstraint(SpringLayout.WEST, intervalLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, intervalLabel, 10, SpringLayout.SOUTH, expectedResponseCode);
		
		sl.putConstraint(SpringLayout.EAST, interval, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, interval, 10, SpringLayout.SOUTH, expectedResponseCode);
		
		sl.putConstraint(SpringLayout.WEST, warningLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, warningLabel, 10, SpringLayout.SOUTH, interval);
		
		sl.putConstraint(SpringLayout.EAST, warning, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, warning, 10, SpringLayout.SOUTH, interval);
		
		sl.putConstraint(SpringLayout.WEST, errorLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, errorLabel, 10, SpringLayout.SOUTH, warning);
		
		sl.putConstraint(SpringLayout.EAST, error, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, error, 10, SpringLayout.SOUTH, warning);
		
		panel.setLayout(sl);
		
		return (panel);
	}
	
	private JPanel createButtonsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton cancel = new JButton("Cancel");
		cancel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
		cancel.getActionMap().put("cancel", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);
		panel.add(cancel);
		JButton test = new JButton("Test");
		test.setActionCommand("test");
		test.addActionListener(this);
		panel.add(test);
		JButton ok = new JButton("Ok");
		ok.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ok");
		ok.getActionMap().put("ok", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				URLService service = getService();
				if (service != null) {
					callback.onURLServiceAdded(service);
					dispose();
				}
			}
		});
		ok.setActionCommand("ok");
		ok.addActionListener(this);
		panel.add(ok);
		return (panel);
	}
	
}
