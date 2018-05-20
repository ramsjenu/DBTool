package com.tcs.tools.business.analysis.main;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class FileParseApplet extends JApplet implements Runnable {
	private JButton b1 = new JButton("Parse Files");
	 static JFrame frame = new JFrame();
	 Container cp = getContentPane();
	 
	  private JTextField txt = new JTextField(25);
	  private JLabel lbl = new JLabel("SQL Files Folder:");
	  private JLabel procesing = new JLabel("");
	  UserInterFaceOpening lUserInterFaceOpening = new UserInterFaceOpening();
	  private String filePath ="";
	  
	  //lbl.setBounds(50, 150, 80, 25);
	  /* (non-Javadoc)
	 * @see java.lang.Runnable#run() 24 
	 */
	public void run() {
		  String lRunId = lUserInterFaceOpening.invokeFileUpload(filePath,"","");
		  System.out.println(":::lRunId:::"+lRunId);
		  procesing.setText("");
		  init();
		  procesing.invalidate();
		  procesing.revalidate();
		  procesing.repaint(); 
		  procesing.updateUI();
		  b1.setEnabled(true);
		  //cp.repaint();
		  JOptionPane.showMessageDialog(this,"Parsing Complete....Your Running Seq Id:"+lRunId+" Populating summary data,Pls wait");
		  lUserInterFaceOpening.invokeParsedDataInsert(lRunId,"","");
		 // lUserInterFaceOpening.invokeSpCallFirstLevel(lRunId,"","");
		  JOptionPane.showMessageDialog(this,"Process Complete....Your Running Seq Id:"+lRunId+" Pls close the window.....");
		  //JOptionPane.getFrameForComponent(procesing).repaint();
		  //JOptionPane.getRootFrame().getComponent(2).repaint();
		  //JOptionPane.getRootFrame().getComponent(1).repaint();
		  //JOptionPane.getRootFrame().getComponent(0).repaint();
		  procesing.getParent().repaint();
		 //frame.repaint();
		  //frame.getContentPane().repaint();
	  }
	  public FileParseApplet(String pFilePath){
		  filePath =pFilePath;
	  }
	  class ButtonListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	      //String name = ((JButton) e.getSource()).getText();
	      //txt.setText(name);
	    	//b1.setdisable();
	    	b1.setEnabled(false);
	    	filePath =txt.getText();
	      procesing.setText("Processing Files Please Wait...");
	      //Rectangle labelRect = procesing.getBounds();
			//labelRect.x = 0;
			//labelRect.y = 0;
	    	//procesing.paintImmediately(labelRect);
	      FileParseApplet mt = new FileParseApplet(txt.getText());
		    Thread newThrd = new Thread(mt);
		    newThrd.start();
		    
	    	 //procesing.setText(txt.getText());
	    }
	  }

	  private ButtonListener bl = new ButtonListener();
	 
	  public void init() {
	    b1.addActionListener(bl);
	    //b2.addActionListener(bl);
	    cp = getContentPane();
	    Container panel1 = new Container();//makeIt("Top", Component.TOP_ALIGNMENT);
	    cp.setLayout(new FlowLayout());
	    //cp.setLayout(new GridLayout(0, 2, 10, 10));
	    //cp.setLayout(null);
	    //panel1.add(lbl);
	    //panel1.add(txt);
	    //cp.add(panel1);
	    cp.add(lbl);
	    cp.add(txt);
	    cp.add(b1);
	    cp.add(procesing);
	    //cp.add(b2);
	  }

	  public static void main(String[] args) {
	    run(new FileParseApplet(null), 500, 500);
	  }

	  public static void run(JApplet applet, int width, int height) {
		
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.getContentPane().add(applet);
	    frame.setSize(width, height);
	    applet.init();
	    applet.start();
	    frame.setVisible(true);
	  }
	  
	  

}
