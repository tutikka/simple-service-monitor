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

import com.tt.ssm.service.impl.TCPService;

@SuppressWarnings("serial")
public class TCPServiceDialog extends JDialog implements ActionListener {

	private static final int DIALOG_WIDTH = 640;
	
	private static final int DIALOG_HEIGHT = 480;
	
	private Callback callback;
	
	private JTextField name;
	
	private JTextField group;
	
	private JTextField host;
	
	private JTextField port;
	
	private JTextField interval;
	
	private JTextField warning;
	
	private JTextField error;
	
	public TCPServiceDialog(JFrame parent, Callback callback) {
		super(parent);
		this.callback = callback;
		setTitle("TCP Service");
		
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
			final TCPService service = getService();
			if (service != null) {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						service.request();
						JOptionPane.showMessageDialog(TCPServiceDialog.this, service.getResponse(), "Test", JOptionPane.INFORMATION_MESSAGE);
					}
				});
			}
		}
		if ("ok".equals(e.getActionCommand())) {
			TCPService service = getService();
			if (service != null) {
				callback.onTCPServiceAdded(service);
				dispose();
			}
		}
	}
	
	private TCPService getService() {
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
		// host
		String host = this.host.getText();
		if (host == null || host.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter the host", "Warning", JOptionPane.WARNING_MESSAGE);
			return (null);
		}
		// port
		int port;
		try {
			port = Integer.parseInt(this.port.getText());
			if (port < 1 || port > 65535) {
				JOptionPane.showMessageDialog(this, "Please enter a valid port (1 - 65535)", "Warning", JOptionPane.WARNING_MESSAGE);
				return (null);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Please enter a valid port (1 - 65535)", "Warning", JOptionPane.WARNING_MESSAGE);
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
		return (new TCPService(name, group, host, port, interval, warning, error));
	}
	
	public interface Callback {
		
		public void onTCPServiceAdded(TCPService service);
		
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
		
		JLabel hostLabel = new JLabel("* Host (or IP address)");
		panel.add(hostLabel);
		
		host = new JTextField("", 24);
		panel.add(host);
		
		JLabel portLabel = new JLabel("* Port");
		panel.add(portLabel);
		
		port = new JTextField("", 24);
		panel.add(port);
		
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
		
		sl.putConstraint(SpringLayout.WEST, hostLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, hostLabel, 10, SpringLayout.SOUTH, group);
		
		sl.putConstraint(SpringLayout.EAST, host, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, host, 10, SpringLayout.SOUTH, group);
		
		sl.putConstraint(SpringLayout.WEST, portLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, portLabel, 10, SpringLayout.SOUTH, host);
		
		sl.putConstraint(SpringLayout.EAST, port, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, port, 10, SpringLayout.SOUTH, host);
		
		sl.putConstraint(SpringLayout.WEST, intervalLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, intervalLabel, 10, SpringLayout.SOUTH, port);
		
		sl.putConstraint(SpringLayout.EAST, interval, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, interval, 10, SpringLayout.SOUTH, port);
		
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
				TCPService service = getService();
				if (service != null) {
					callback.onTCPServiceAdded(service);
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
