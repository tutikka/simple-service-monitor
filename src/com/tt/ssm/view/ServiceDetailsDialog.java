package com.tt.ssm.view;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.tt.ssm.service.Service;
import com.tt.ssm.service.ServiceManager.Callback;

@SuppressWarnings("serial")
public class ServiceDetailsDialog extends JDialog implements ActionListener, Callback {

	private static final int DIALOG_WIDTH = 640;
	
	private static final int DIALOG_HEIGHT = 480;
	
	private TimeSeries series;
	
	private JFreeChart chart;

	private Service service;
	
	public ServiceDetailsDialog(JFrame parent, Service service) {
		super(parent);
		this.service = service;
		setTitle("Service Details");
		
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
		if ("close".equals(e.getActionCommand())) {
			dispose();
		}
	}
	
	@Override
	public void onServiceResponded(final Service service) {
		if (service.equals(this.service)) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					series.add(new Second(), service.getResponse().getTime());
				}
			});	
		}
	}

	private JPanel createContentPanel() {
		series = new TimeSeries("Time");
		TimeSeriesCollection collection = new TimeSeriesCollection(series);
		chart = ChartFactory.createTimeSeriesChart(
	            service.getName(), 
	            "Updated",
	            "Time (ms)",
	            collection, 
	            false, 
	            false, 
	            false);
		chart.setAntiAlias(true);
		chart.setBackgroundPaint(new Color(0, 0, 0, 0));
	    XYPlot plot = chart.getXYPlot();
	    ValueAxis domain = plot.getDomainAxis();
	    domain.setAutoRange(true);
	    domain.setFixedAutoRange(60 * 1000); // 60 seconds
	    ValueAxis range = plot.getRangeAxis();
	    range.setRange(0, service.getError());
	    ChartPanel panel = new ChartPanel(chart);
	    panel.setOpaque(false);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5), BorderFactory.createRaisedBevelBorder()));
	    return (panel);
	}
	
	private JPanel createButtonsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton close = new JButton("Close");
		close.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
		close.getActionMap().put("close", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		close.setActionCommand("close");
		close.addActionListener(this);
		panel.add(close);
		return (panel);
	}
	
}
