package com.tt.ssm.view;

import java.awt.BorderLayout;
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

@SuppressWarnings("serial")
public class ServerDialog extends JDialog implements ActionListener {

	private static final int DIALOG_WIDTH = 640;
	
	private static final int DIALOG_HEIGHT = 480;
	
	private Callback callback;
	
	private JTextField host;
	
	private JTextField port;
	
	public ServerDialog(JFrame parent, Callback callback) {
		super(parent);
		this.callback = callback;
		setTitle("Server");
		
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
		if ("start".equals(e.getActionCommand())) {
			handleStart();
		}
	}
	
	public interface Callback {
		
		public void onServerStartRequested(String host, int port);
		
	}
	
	/* ********** private ********** */
	
	private void handleStart() {
		// host
		String host = this.host.getText();
		if (host == null || host.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter the host", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		// port
		int port;
		try {
			port = Integer.parseInt(this.port.getText());
			if (port < 1 || port > 65535) {
				JOptionPane.showMessageDialog(this, "Please enter a valid port (1 - 65535)", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Please enter a valid port (1 - 65535)", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		callback.onServerStartRequested(host, port);
		dispose();
	}
	
	private JPanel createContentPanel() {
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5), BorderFactory.createRaisedBevelBorder()));
		
		JLabel hostLabel = new JLabel("* Host");
		panel.add(hostLabel);
		
		host = new JTextField("0.0.0.0", 24);
		panel.add(host);
		
		JLabel portLabel = new JLabel("* Port");
		panel.add(portLabel);
		
		port = new JTextField("10010", 24);
		panel.add(port);

		SpringLayout sl = new SpringLayout();
		
		sl.putConstraint(SpringLayout.WEST, hostLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, hostLabel, 10, SpringLayout.NORTH, panel);
		
		sl.putConstraint(SpringLayout.EAST, host, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, host, 10, SpringLayout.NORTH, panel);
		
		sl.putConstraint(SpringLayout.WEST, portLabel, 10, SpringLayout.WEST, panel);
		sl.putConstraint(SpringLayout.NORTH, portLabel, 10, SpringLayout.SOUTH, host);
		
		sl.putConstraint(SpringLayout.EAST, port, -10, SpringLayout.EAST, panel);
		sl.putConstraint(SpringLayout.NORTH, port, 10, SpringLayout.SOUTH, host);
		
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
		JButton ok = new JButton("Ok");
		ok.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "start");
		ok.getActionMap().put("start", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleStart();
			}
		});
		ok.setActionCommand("start");
		ok.addActionListener(this);
		panel.add(ok);
		return (panel);
	}
	
}
