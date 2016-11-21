package com.tt.ssm.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.tt.ssm.service.impl.JDBCService;

@SuppressWarnings("serial")
public class JDBCServiceDialog extends JDialog implements ActionListener {

	private static final int DIALOG_WIDTH = 640;
	
	private static final int DIALOG_HEIGHT = 480;
	
	private Callback callback;
	
	private JTextField name;
	
	private JTextField group;
	
	private JTextField driver;
	
	private JTextField url;
	
	private JTextField username;
	
	private JPasswordField password;
	
	private JTextField query;
	
	private JTextField interval;
	
	private JTextField warning;
	
	private JTextField error;
	
	public JDBCServiceDialog(JFrame parent, Callback callback) {
		super(parent);
		this.callback = callback;
		setTitle("JDBC Service");
		
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
			JDBCService service = getService();
			if (service != null) {
				service.request();
				JOptionPane.showMessageDialog(this, service.getResponse(), "Test", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		if ("ok".equals(e.getActionCommand())) {
			JDBCService service = getService();
			if (service != null) {
				callback.onJDBCServiceAdded(service);
				dispose();
			}
		}
	}
	
	private JDBCService getService() {
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
		// driver
		String driver = this.driver.getText();
		if (driver == null || driver.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter the JDBC driver", "Warning", JOptionPane.WARNING_MESSAGE);
			return (null);
		}
		// url
		String url = this.url.getText();
		if (url == null || url.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter the JDBC url", "Warning", JOptionPane.WARNING_MESSAGE);
			return (null);
		}
		// username
		String username = this.username.getText();
		if (username == null || username.isEmpty()) {
			// ignore
		}
		// password
		char[] password = this.password.getPassword();
		if (password == null || password.length == 0) {
			// ignore
		}
		// query
		String query = this.query.getText();
		if (query == null || query.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter the SQL query", "Warning", JOptionPane.WARNING_MESSAGE);
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
		return (new JDBCService(name, group, driver, url, username, password, query, interval, warning, error));
	}
	
	public interface Callback {
		
		public void onJDBCServiceAdded(JDBCService service);
		
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
		
		JLabel driverLabel = new JLabel("* JDBC driver");
		panel.add(driverLabel);
		
		driver = new JTextField("", 24);
		panel.add(driver);
		
		JLabel urlLabel = new JLabel("* JDBC URL");
		panel.add(urlLabel);
		
		url = new JTextField("jdbc:", 24);
		panel.add(url);
		
		JLabel usernameLabel = new JLabel("Username");
		panel.add(usernameLabel);
		
		username = new JTextField("", 24);
		panel.add(username);
		
		JLabel passwordLabel = new JLabel("Password");
		panel.add(passwordLabel);
		
		password = new JPasswordField("", 24);
		panel.add(password);
		
		JLabel queryLabel = new JLabel("* SQL query");
		panel.add(queryLabel);
		
		query = new JTextField("SELECT 1", 24);
		panel.add(query);
		
		JLabel intervalLabel = new JLabel("* Interval (ms)");
		panel.add(intervalLabel);
		
		interval = new JTextField("10000", 24);
		panel.add(interval);
		
		JLabel warningLabel = new JLabel("* Warning threshold (ms)");
		panel.add(warningLabel);
		
		warning = new JTextField("1000", 24);
		warning.setBackground(Color.YELLOW);
		panel.add(warning);
		
		JLabel errorLabel = new JLabel("* Error threshold (ms)");
		panel.add(errorLabel);
		
		error = new JTextField("5000", 24);
		error.setBackground(Color.RED);
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
		
		sl.putConstraint(SpringLayout.WEST, driverLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, driverLabel, 10, SpringLayout.SOUTH, group);
		
		sl.putConstraint(SpringLayout.EAST, driver, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, driver, 10, SpringLayout.SOUTH, group);
		
		sl.putConstraint(SpringLayout.WEST, urlLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, urlLabel, 10, SpringLayout.SOUTH, driver);
		
		sl.putConstraint(SpringLayout.EAST, url, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, url, 10, SpringLayout.SOUTH, driver);
		
		sl.putConstraint(SpringLayout.WEST, usernameLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, usernameLabel, 10, SpringLayout.SOUTH, url);
		
		sl.putConstraint(SpringLayout.EAST, username, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, username, 10, SpringLayout.SOUTH, url);
		
		sl.putConstraint(SpringLayout.WEST, passwordLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, passwordLabel, 10, SpringLayout.SOUTH, username);
		
		sl.putConstraint(SpringLayout.EAST, password, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, password, 10, SpringLayout.SOUTH, username);
		
		sl.putConstraint(SpringLayout.WEST, queryLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, queryLabel, 10, SpringLayout.SOUTH, password);
		
		sl.putConstraint(SpringLayout.EAST, query, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, query, 10, SpringLayout.SOUTH, password);
		
		sl.putConstraint(SpringLayout.WEST, intervalLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, intervalLabel, 10, SpringLayout.SOUTH, query);
		
		sl.putConstraint(SpringLayout.EAST, interval, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, interval, 10, SpringLayout.SOUTH, query);
		
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
		cancel.setActionCommand("cancel");
		cancel.addActionListener(this);
		panel.add(cancel);
		JButton test = new JButton("Test");
		test.setActionCommand("test");
		test.addActionListener(this);
		panel.add(test);
		JButton ok = new JButton("Ok");
		ok.setActionCommand("ok");
		ok.addActionListener(this);
		panel.add(ok);
		return (panel);
	}
	
}
