import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;


public class GUI extends JFrame implements ActionListener, ChangeListener{
	
	private JFrame frame, getStartedFrame, aboutMenuFrame;
	private JButton resetButton, importImageButton, exitGetStartedFrame = null, exitAboutMenuFrame = null;
	private JFileChooser fileChooser;
	private JMenuBar menuBar;
	private JMenu fileMenu, helpMenu;
	private JMenuItem quitMenuItem, startedMenuItem, aboutMenuItem;
	private JSlider pixSlider;
	private File picFile;
	private BufferedImage buffPic = null;
	private JPanel bottomPanel, centerPanel;
	private JLabel centerPanLabel;
	
	final int PIX_MIN = 1;
	final int PIX_MAX = 10;
	final int PIX_INIT = 1;
	int pixelChoice = 1;
	
	public GUI() {
		
		//This creates an instance of JFrame.
		frame = new JFrame("PIXELATOR!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//This establishes the Panel for the bottom buttons.
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		
		//This will establish the middle panel that will contain the picture.
		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBackground(Color.red);
		centerPanLabel = new JLabel("Welcome to the Pixelator! Please import an image to begin.", JLabel.CENTER);
		
		
		//This will give the frame default settings
		frame.setSize(700,500);
		frame.setLocationRelativeTo(null);
		
		//This creates the Reset and an import button.
		resetButton = new JButton("Reset");
		importImageButton = new JButton("Import");
		
		//This section will add the button to the frame, then output the frame.
		bottomPanel.add(resetButton, BorderLayout.EAST);
		resetButton.addActionListener(this);
		bottomPanel.add(importImageButton, BorderLayout.WEST);
		importImageButton.addActionListener(this);
		
		//This will build the JSlider.
		pixSlider = new JSlider(JSlider.HORIZONTAL,PIX_MIN,PIX_MAX,PIX_INIT);
		pixSlider.setMajorTickSpacing(1);
		pixSlider.setMinorTickSpacing(1);
		pixSlider.setPaintTicks(true);
		pixSlider.setPaintLabels(true);
		
		bottomPanel.add(pixSlider, BorderLayout.CENTER);
		
		
		//This will add the bottomPanel to the frame.
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
		//This will add the center panel label containing the picture to the center panel, then finally to the frame
		centerPanel.add(centerPanLabel);
		frame.add(centerPanel);
		
		
		//This will establish the menu bar and each of the menus.
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		helpMenu = new JMenu("Help");
		
		//These will assign letter shortcuts to each of the Menus.
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.setDisplayedMnemonicIndex(0);
		helpMenu.setMnemonic(KeyEvent.VK_H);
		helpMenu.setDisplayedMnemonicIndex(0);
		
		//This will populate the menuBar.
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

		//This will populate the file menu.
		quitMenuItem = new JMenuItem("Quit",KeyEvent.VK_Q);
		quitMenuItem.setDisplayedMnemonicIndex(0);
		fileMenu.add(quitMenuItem);
		quitMenuItem.addActionListener(this);
		
		//This will populate the help menu.
		startedMenuItem = new JMenuItem("Get Started",KeyEvent.VK_E);
		startedMenuItem.setDisplayedMnemonicIndex(1);
		aboutMenuItem = new JMenuItem("About", KeyEvent.VK_A);
		aboutMenuItem.setDisplayedMnemonicIndex(0);
		helpMenu.add(startedMenuItem);
		startedMenuItem.addActionListener(this);
		helpMenu.add(aboutMenuItem);
		aboutMenuItem.addActionListener(this);
		
		//This will add the menuBar to the frame.
		frame.add(menuBar, BorderLayout.NORTH);
		
		//This will make the frame visible so users can actually see it.
		frame.setVisible(true);
		
		
		//This will create the frame for the getStartedFrame menuItem
		getStartedFrame = new JFrame ("Getting Started");
		getStartedFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getStartedFrame.setLayout(new BorderLayout());
		JTextArea getStartedFrameTextArea = new JTextArea("Welcome to Pixelator! Please push Import, " 
				+ "then select the photo you would like me to pixelate! " 
				+ "Use the slider to adjust the amount of pixelation!", 3, 40);
		
		getStartedFrameTextArea.setLineWrap(true);
		getStartedFrameTextArea.setWrapStyleWord(true);
		getStartedFrame.add(getStartedFrameTextArea, BorderLayout.NORTH);
		exitGetStartedFrame = new JButton ("Return to the pixelator");
		exitGetStartedFrame.addActionListener(this);
		getStartedFrame.setLocation(450,400);
		getStartedFrame.add(exitGetStartedFrame, BorderLayout.SOUTH);
		getStartedFrame.pack();
		
		//This will create the frame for the about page of the application
		aboutMenuFrame = new JFrame ("About");
		aboutMenuFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		aboutMenuFrame.setLayout(new BorderLayout());
		JTextArea aboutFrameTextArea = new JTextArea("Pixelator - A basic application for pixelating imported pictures. V1.1", 3, 40);
		
		aboutFrameTextArea.setLineWrap(true);
		aboutFrameTextArea.setWrapStyleWord(true);
		aboutMenuFrame.add(aboutFrameTextArea, BorderLayout.NORTH);
		exitAboutMenuFrame = new JButton ("Return to the pixelator");
		exitAboutMenuFrame.addActionListener(this);
		aboutMenuFrame.setLocation(450,400);
		aboutMenuFrame.add(exitAboutMenuFrame, BorderLayout.SOUTH);
		aboutMenuFrame.pack();
	}

	//This method is where the action is actually performed.
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == resetButton){
			pixSlider.setValue(1);
			centerPanLabel.setIcon(new ImageIcon(pixelate(buffPic, 1)));
			if(buffPic.getWidth()>200)
				frame.setSize(buffPic.getWidth(), buffPic.getHeight()+120);
			centerPanel.add(centerPanLabel);
			frame.getContentPane().add(centerPanel);
		}
		
		if (e.getSource() == importImageButton){
			
			pixSlider.setValue(1);
			pixelChoice = 1;
			pixSlider.addChangeListener(this);
			
			fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
			fileChooser.setFileFilter(filter);
			int returnVal = fileChooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				picFile = fileChooser.getSelectedFile();
			}
			
			//This will turn the image file into a buffered image for manipulation.
			if(picFile != null){
				try {
					buffPic = ImageIO.read(picFile);
					centerPanLabel.setText(null);
				
				} catch (IOException e1) {
					System.out.println("There was an error importing your photo.");
					e1.printStackTrace();
				}
				
				centerPanLabel.setIcon(new ImageIcon(buffPic));
				
				if(buffPic.getWidth() > 200)
				frame.setSize(buffPic.getWidth(), buffPic.getHeight()+120);
				
				centerPanel.add(centerPanLabel);
				frame.getContentPane().add(centerPanel);
			}
			
		}
		
		if (e.getSource() == startedMenuItem){
			getStartedFrame.setVisible(true);	
		}
		
		if (e.getSource() == exitGetStartedFrame){
			getStartedFrame.setVisible(false);
		}
		
		if (e.getSource() == aboutMenuItem){
			aboutMenuFrame.setVisible(true);
		}
		
		if(e.getSource() == exitAboutMenuFrame){
			aboutMenuFrame.setVisible(false);
		}
		if (e.getSource() == quitMenuItem){
			System.exit(0);
		}
		
	}
	
	//This method checks for a change in state for the JSlider.
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if(source.getValueIsAdjusting())
		{
			int pixels = (int)source.getValue();
			pixelChoice = pixels;
			centerPanLabel.setIcon(new ImageIcon(pixelate(buffPic, pixelChoice)));
			if(buffPic.getWidth()>200)
				frame.setSize(buffPic.getWidth(), buffPic.getHeight()+120);
			centerPanel.add(centerPanLabel);
			frame.getContentPane().add(centerPanel);

		}
	}

	//This method will actually do the pixelation.
	private BufferedImage pixelate (BufferedImage buffPic, int pixelChoice){
		
		BufferedImage newImg = null;
		try {
			newImg = ImageIO.read(picFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Raster orig = newImg.getData();
		
		WritableRaster pixelImage = orig.createCompatibleWritableRaster();
		
		for(int y = 0; y < orig.getHeight(); y += pixelChoice){
			for(int x = 0; x < orig.getWidth(); x += pixelChoice){
				
				double[] pixel = new double[3];
				pixel = orig.getPixel(x, y, pixel);
				
				for(int i = y; (i < y + pixelChoice) && (i < pixelImage.getHeight()); i++){
					for(int j = x; (j < x + pixelChoice) && (j < pixelImage.getWidth()); j++){
					pixelImage.setPixel(j, i, pixel);
					}
				}
			}
		}
		
		newImg.setData(pixelImage);
		
		return newImg;
	}
}

