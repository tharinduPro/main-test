package net.dgie.xmltask;
import java.util.Date;
import java.util.Timer;

public class Run extends javax.swing.JFrame {

	private java.awt.Button button1;
	private java.awt.Button buttonFourceUpdate;
	private java.awt.Label label1;
	private java.awt.TextField textField1;

	public Run() {
		initComponents();
	}

	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		label1 = new java.awt.Label();
		button1 = new java.awt.Button();
		buttonFourceUpdate = new java.awt.Button();
		textField1 = new java.awt.TextField();

		setDefaultCloseOperation(3);

		label1.setText("更新间隔（以分钟为单位）");

		button1.setLabel("启动");
		button1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				button1ActionPerformed(evt);
			}
		});

		textField1.setText("10");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap().addComponent(
						label1, javax.swing.GroupLayout.DEFAULT_SIZE, 142,
						Short.MAX_VALUE).addPreferredGap(
						javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(textField1,
								javax.swing.GroupLayout.PREFERRED_SIZE, 83,
								javax.swing.GroupLayout.PREFERRED_SIZE).addGap(
								32, 32, 32).addComponent(button1,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE).addGap(
								66, 66, 66)));
		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout
										.createSequentialGroup()
										.addGap(97, 97, 97)
										.addGroup(
												layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(
																label1,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																textField1,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																button1,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap(177, Short.MAX_VALUE)));

		pack();
	}

    Timer timer = null;
	private void button1ActionPerformed(java.awt.event.ActionEvent evt) {
        if( button1.getLabel().equals( "启动" ) ) {
            timer = new Timer();
            PropertyReader pr = new PropertyReader();
            long period = Long.valueOf( textField1.getText() ) * 1000 * 60;
            timer.scheduleAtFixedRate( new MainTimerTask( textField1.getText() ), new Date(), period );
            button1.setLabel( "停止" );
            System.out.println( "已启动" );
        }
        else {
            button1.setLabel( "启动" );
            timer.cancel();
            System.out.println( "已停止" );
        }
	}


	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Run().setVisible(true);
			}
		});
	}

}

