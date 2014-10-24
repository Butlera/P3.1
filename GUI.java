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
	
	private JFrame frame;
	private JButton resetButton, importImageButton;
	private JFileChooser fileChooser;
	private JMenuBar menuBar;
	private JMenu fileMenu, helpMenu;
	private JMenuItem quitMenuItem, startedMenuItem, aboutMenuItem;
	private JSlider pixSlider;
	private File picFile;
	private BufferedImage buffPic = null;
	private BufferedImage buffPicCopy = null;
	private JPanel bottomPanel, centerPanel;
	private JLabel centerPanLabel;
	
	private Desktop desktop;
	private File pdfAboutFile = new File("/Users/AustinButler/Documents/workspace/P3/AboutFile.pdf");
	
	final int PIX_MIN = 0;
	final int PIX_MAX = 20;
	final int PIX_INIT = 0;
	int pixelChoice = 0;
	
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
		pixSlider.setMajorTickSpacing(5);
		pixSlider.setMinorTickSpacing(1);
		pixSlider.setPaintTicks(true);
		pixSlider.setPaintLabels(true);
		
		bottomPanel.add(pixSlider, BorderLayout.CENTER);
		
		pixSlider.addChangeListener(this);
		
		
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
		helpMenu.setMnemonic(KeyEvent.VK_H);
		
		//This will populate the menuBar.
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

		//This will populate the file menu.
		quitMenuItem = new JMenuItem("Quit",KeyEvent.VK_Q);
		fileMenu.add(quitMenuItem);
		quitMenuItem.addActionListener(this);
		
		//This will populate the help menu.
		startedMenuItem = new JMenuItem("Get Started",KeyEvent.VK_E);
		aboutMenuItem = new JMenuItem("About", KeyEvent.VK_A);
		helpMenu.add(startedMenuItem);
		startedMenuItem.addActionListener(this);
		helpMenu.add(aboutMenuItem);
		aboutMenuItem.addActionListener(this);
		
		//This will add the menuBar to the frame.
		frame.add(menuBar, BorderLayout.NORTH);
		
		//This will make the frame visible so users can actually see it.
		frame.setVisible(true);
	}

	//This method is where the action is actually performed.
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == resetButton){
			pixelChoice = 0;
			pixSlider.setValue(0);
//			System.out.println("ResetButton");
		}
		
		if (e.getSource() == importImageButton){
			
			fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
			fileChooser.setFileFilter(filter);
			int returnVal = fileChooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				picFile = fileChooser.getSelectedFile();
//				System.out.println("You have chose to pixelate the picture: " + fileChooser.getSelectedFile().getName());
			}
			
			//This will turn the image file into a buffered image for manipulation.
			try {
				buffPic = ImageIO.read(picFile);
				buffPicCopy = buffPic;
				
			} catch (IOException e1) {
				System.out.println("There was an error importing your photo.");
				e1.printStackTrace();
			}
			
			centerPanLabel.setIcon(new ImageIcon(buffPicCopy));
			frame.setSize(buffPic.getWidth(), buffPic.getHeight()+120);
			centerPanel.add(centerPanLabel);
			frame.getContentPane().add(centerPanel);
		}
		
		if (e.getSource() == startedMenuItem){
//			System.out.println("StartMenu");
		}
		
		if (e.getSource() == aboutMenuItem){
			try {
				desktop.open(new File("/Users/AustinButler/Documents/workspace/P3/AboutFile.pdf"));
			} catch (IOException e1) {
				System.out.println("Missing Files. printStackTrace: ");
				e1.printStackTrace();
			} catch(NullPointerException e1){
				System.out.println("I caught you.");
				e1.printStackTrace();
			}
			
//			System.out.println("aboutMenu");
		}
		
		if (e.getSource() == quitMenuItem){
			System.exit(0);
		}
		
		
		
	}
	
	
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if(!source.getValueIsAdjusting())
		{
			int pixels = (int)source.getValue();
			pixelChoice = pixels;
			System.out.println("You have selected "+ pixelChoice);
			pixelate(buffPicCopy, pixelChoice);
			centerPanLabel.setIcon(new ImageIcon(buffPicCopy));
			frame.setSize(buffPic.getWidth(), buffPic.getHeight()+120);
			centerPanel.add(centerPanLabel);
			frame.getContentPane().add(centerPanel);

		}
	}

	public int getPixelSize(){
		return pixelChoice;
	}

	//This method will actually do the pixelation.
	private BufferedImage pixelate (BufferedImage buffPic, int pixelChoice){
		
		BufferedImage pixelImage = buffPic;

		
		//This for loop will get the average color in the desired pixel size.
        for(int row =0; row<buffPic.getHeight();row+=pixelChoice){
            for(int col=0;col< buffPic.getWidth();col+=pixelChoice){
        		int average=0;
            	average += buffPic.getRGB(col, row);
            	int count = 1;
            	count++;
            	
            	if(count == pixelChoice){
            		average /= 2;
	            	for(int row2 = row; (row2 < row +pixelChoice)&& (row2 < pixelImage.getHeight()); row2++){
	            		for(int col2 = col; (col2 < col + pixelChoice)&&(col2 < pixelImage.getWidth()); col2++){
	            			pixelImage.setRGB(row2,col2,average);
	            		}
	            	}
            	}
            	
            }
        }
		
		return pixelImage;
	}
}

