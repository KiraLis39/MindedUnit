package gui;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import core.Events.ColEvent;
import door.ExitClass;
import fox.adds.Out;
import fox.builders.ResourceManager;
import library.TechClassUNames;
import unit.Unit;
import unit.Units.Gender;


@SuppressWarnings("serial")
public class UnitFrame extends JFrame implements WindowListener {
	
	private static Canvas canvas;
	private Thread unitsDrawThread, timerProcessThread, techThread;
	private Long timeMillisWas = System.currentTimeMillis(), timeMillisNow, secondPassedFromStart = 0L;
	private Boolean active = true;
	private static Map<Integer, Unit> UDB0 = new HashMap<Integer, Unit>();
	
	private static UnitFrame uFrame;
	private Graphics2D g2D;
	private BufferStrategy bs;
	private RenderingHints renderingHints;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat();
	private JLabel timaStampLabel;
	private JPanel rightInfoPane, upInfoPane, rightPane;
	private JScrollPane rightScroll;
	private JTextField speedMonitor;
	private static Date today = new Date();
	private static Random universalRand = new Random();
	
	private int SPEEDS[] = {15, 30, 100, 150, 200, 250, 300, 500};
	private int speedMarker = 4;
	
	
	public UnitFrame() {
		renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
//    renderingHints.add(new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY));
//    renderingHints.add(new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR));
//    renderingHints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
//    renderingHints.add(new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
		
        uFrame = this;

		setTitle("Minded Units Frame");
		setPreferredSize(new Dimension(1150, 500));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
				
		upInfoPane = new JPanel(new BorderLayout()) {
			{
				setBackground(Color.DARK_GRAY);
				
				timaStampLabel = new JLabel() {
					{setHorizontalAlignment(0);	setForeground(Color.WHITE);}
				};
				timaStampLabel.setText(
						"<HTML>" + getCurrentTimeStamp() + 
						"<BR>" + 
						"<H5 style=\"text-align: center; padding: 0px; margin: 0px;\"> sec last: " + secondPassedFromStart + "</H4></HTML>");
				
				add(timaStampLabel);
			}
		};
		
		rightPane = new JPanel(new BorderLayout()) {
			{
				setBackground(Color.DARK_GRAY);
				setAlignmentX(0);
				
				rightInfoPane = new JPanel(new GridLayout(0, 1, 2, 2)) {
					{setBackground(Color.DARK_GRAY);}
				};
				
				rightScroll = new JScrollPane(rightInfoPane) {
					{
						setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
						setDoubleBuffered(true);
					}
				};
				
				JPanel rightDownSpeedButtons = new JPanel(new GridLayout(1, 3, 3, 3)) {
					{
						setBorder(new EmptyBorder(3, 3, 3, 3));
						
						speedMonitor = new JTextField() {
							{
								setEditable(false);
								setText("delay: " + SPEEDS[speedMarker]);
								setHorizontalAlignment(CENTER);
							}
						};
						
						add(speedMonitor);
						
						add(new JButton("<<") {
							{
								addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										if (speedMarker + 1 < SPEEDS.length) {speedMarker++;}
										speedMonitor.setText("delay: " + SPEEDS[speedMarker]);
									}
								});
							}
						});
						
						add(new JButton(">>") {
							{
								addActionListener(new ActionListener() {									
									@Override
									public void actionPerformed(ActionEvent e) {
										if (speedMarker - 1 >= 0) {speedMarker--;}
										speedMonitor.setText("delay: " + SPEEDS[speedMarker]);
									}
								});
							}
						});
					}
				};
				
				add(rightScroll, BorderLayout.CENTER);
				add(rightDownSpeedButtons, BorderLayout.SOUTH);
			}
		};
		
		canvas = new Canvas() {{setBackground(Color.BLACK);}};
		
		
		add(upInfoPane, BorderLayout.NORTH);
		add(rightPane, BorderLayout.EAST);
		add(canvas);
		
		addWindowListener(this);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		
		letsStartItNow();
	}
	
	
	private void rebuildUnitsInfo() {
		int counter = UDB0.values().size();
		ArrayList<Long> familysList = new ArrayList<Long> (counter);
		
		rightInfoPane.removeAll();
		
//		//HEADER:
//		String[] tableHeaderStrings = {"#", "NAME:",  "ID:", "HP:", "ENERGY:", "LOVE:", "FAMILY:"};
//		for (int headerCol = 0; headerCol < tableHeaderStrings.length; headerCol++) {
//			rightInfoPane.add(new JLabel(tableHeaderStrings[headerCol]) {{setForeground(Color.LIGHT_GRAY);}});
//		}
		
		//BODY:
		for (int i = 0; i < counter; i++) {
			int tmp = i;
			
			rightInfoPane.add(new JPanel(new BorderLayout()) {
				{
					JPanel tableRow = new JPanel(new FlowLayout(FlowLayout.LEFT)) {
						{
							setBackground(Color.DARK_GRAY);
							
							add(new JLabel("" + (tmp + 1)) {{if ((UDB0.get(tmp)).getGender().equals(Gender.FEMA)) {setForeground(Color.MAGENTA);} else {setForeground(Color.CYAN);}}});
							add(new JLabel(UDB0.get(tmp).getName()) {{if ((UDB0.get(tmp)).getGender().equals(Gender.FEMA)) {setForeground(Color.MAGENTA);} else {setForeground(Color.CYAN);}}});
							add(new JLabel("" + UDB0.get(tmp).getID()) {{if ((UDB0.get(tmp)).getGender().equals(Gender.FEMA)) {setForeground(Color.MAGENTA);} else {setForeground(Color.CYAN);}}});
							add(new JLabel("" + UDB0.get(tmp).getHP()) {{if ((UDB0.get(tmp)).getGender().equals(Gender.FEMA)) {setForeground(Color.MAGENTA);} else {setForeground(Color.CYAN);}}});
							add(new JLabel("" + UDB0.get(tmp).getEnergy()) {{if ((UDB0.get(tmp)).getGender().equals(Gender.FEMA)) {setForeground(Color.MAGENTA);} else {setForeground(Color.CYAN);}}});
							add(new JLabel("" + UDB0.get(tmp).getLoveLevel()) {{if ((UDB0.get(tmp)).getGender().equals(Gender.FEMA)) {setForeground(Color.MAGENTA);} else {setForeground(Color.CYAN);}}});
							add(new JLabel("" + UDB0.get(tmp).hasFamily()) {{if ((UDB0.get(tmp)).getGender().equals(Gender.FEMA)) {setForeground(Color.MAGENTA);} else {setForeground(Color.CYAN);}}});
							
							if (!familysList.contains(UDB0.get(tmp).getFamilyStamp())) {familysList.add(UDB0.get(tmp).getFamilyStamp());}
						}
					};
					
					JPanel buttonsRight = new JPanel(new FlowLayout()) {
						{
							setBackground(Color.DARK_GRAY);
							setBorder(new EmptyBorder(0, 2, 0, 2));
							
							add(new JButton() {{setPreferredSize(new Dimension(32, 32)); setIcon(new ImageIcon(ResourceManager.getBufferedImage("buttonIcon00"))); setToolTipText("00"); setFocusPainted(false); setBackground(null); setBorderPainted(false);}});
							add(new JButton() {{setPreferredSize(new Dimension(32, 32)); setIcon(new ImageIcon(ResourceManager.getBufferedImage("buttonIcon01"))); setToolTipText("01"); setFocusPainted(false); setBackground(null); setBorderPainted(false);}});
							add(new JButton() {{setPreferredSize(new Dimension(32, 32)); setIcon(new ImageIcon(ResourceManager.getBufferedImage("buttonIcon02"))); setToolTipText("02"); setFocusPainted(false); setBackground(null); setBorderPainted(false);}});
							add(new JButton() {{setPreferredSize(new Dimension(32, 32)); setIcon(new ImageIcon(ResourceManager.getBufferedImage("buttonIcon03"))); setToolTipText("03"); setFocusPainted(false); setBackground(null); setBorderPainted(false);}});
						}
					};
					
					add(tableRow, BorderLayout.CENTER);
					add(buttonsRight, BorderLayout.EAST);
				}
			});
		}


//		//FOOTER
//		String[] tableFooterStrings = {"ALL: " + UDB0.size(), "Familys: " + familysList.size()};
//		for (int footerCol = 0; footerCol < tableFooterStrings.length; footerCol++) {
//			rightInfoPane.add(new JLabel(tableFooterStrings[footerCol]) {{setForeground(Color.LIGHT_GRAY);}});
//		}
		
		// stump free space:
		int beautyTableSpacing = 20 - counter;
		for (int i = 0; i < beautyTableSpacing; i++) {
			rightInfoPane.add(new JLabel());
		}

		rightPane.setPreferredSize(new Dimension(uFrame.getWidth() / 3, 0));
	}

	private void runThreads() {
		// base drawing thread:
		unitsDrawThread = new Thread(new Runnable() {
			@Override
			public synchronized void run() {
				Out.Console("unitsDrawThread start...");

				while (active) {
					if (UDB0 == null) {return;}
					
					if (!UDB0.isEmpty()) {
						//repaint back canvas:
						do {
				    		do {
				    			g2D = (Graphics2D) bs.getDrawGraphics();
				    			g2D.setRenderingHints(renderingHints);
				    			
				    			// drawing background:
				    			try {g2D = backgroundDraws(g2D);} catch (Exception e) {e.printStackTrace();}
								
								// drawing living units:
								try {g2D = liveUnitsDraws(g2D);} catch (Exception e) {e.printStackTrace();}
								
								// drawing family lines:
								try {g2D = familyLinesDraw(g2D);} catch (Exception e) {e.printStackTrace();}
								
								g2D.drawRect(0, 0, 3, 3);
								g2D.dispose();
				    		} while (bs.contentsRestored());
						} while (bs.contentsLost());
						
						bs.show();
						
						try {Thread.sleep(SPEEDS[speedMarker]);} catch (Exception e) {}
					}
					
					Thread.yield();
				}
			}

			private Graphics2D backgroundDraws(Graphics2D g2D) {
				g2D.setColor(new Color(0.3f, 0.3f, 0.3f, 0.5f));
				g2D.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
				g2D.fillRect(3, 5, canvas.getWidth() - 6, canvas.getHeight() - 10);
				return g2D;
			}

			private Graphics2D familyLinesDraw(Graphics2D g2D) {universalTaskManager(2, g2D); return g2D;}

			private Graphics2D liveUnitsDraws(Graphics2D g2D) {universalTaskManager(1, g2D); return g2D;}
		});
		unitsDrawThread.start();
		
		// technical variables thread:
		techThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					// remove dead bodies:
					try {universalTaskManager(0, null);} catch (Exception e) {e.printStackTrace();}
					
					if (universalRand.nextInt(3) == 0) {UDB0.get(0).say();}
					
					try {Thread.sleep(5000);} catch (InterruptedException e) {e.printStackTrace();}
				}
			}
		});
		techThread.start();
		
		// other little tasks:
		timerProcessThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Out.Console("processThread start...");
				
				while (active) {
					timeMillisNow = System.currentTimeMillis() - timeMillisWas;
					
					if (timeMillisNow > 1000) {
						rebuildUnitsInfo();
						//every one second events:
						secondPassedFromStart++;
						timeMillisWas = System.currentTimeMillis();
						
						timaStampLabel.setText(
								"<HTML>" + 
								getCurrentTimeStamp() + 
								"<BR><H5 style=\"text-align: center; padding: 0px; margin: 0px;\"> sec last: " + 
								secondPassedFromStart + 
								"</H4></HTML>");
						try {Thread.sleep(39);} catch (Exception e) {}
					} else {Thread.yield();}
				}
			}
		});
		timerProcessThread.start();
		
		Out.Print("Universe field in active! All runThreads in the process..");
		Out.Print("*** *** *** *** *** *** *** *** *** *** *** *** ***\n");
	}
	
	private synchronized Graphics2D universalTaskManager(int operationCode, Graphics2D g2D) {
		if (operationCode == 0) {
			for (Iterator<Unit> it = UDB0.values().iterator(); it.hasNext(); ) {
				if (it.next().isDestroyed()) {it.remove();}
			}
		} else if (operationCode == 1) {
			Unit uNow;
			
			for (int i = UDB0.values().size() - 1; i > 0; i--) {
				uNow = UDB0.get(i);
				if (uNow == null) {continue;}
				
				if (!uNow.isDestroyed()) {
					for (int k = UDB0.values().size() - 1; k > 0; k--) {
						if (uNow != UDB0.get(k) && UDB0.get(k) != null && !UDB0.get(k).isDestroyed()) {
							if (uNow.getBodyRect2D().intersects(UDB0.get(k).getBodyRect2D())) {
								uNow.onCollision(new ColEvent(uNow), UDB0.get(k));
							}
						}
					}
	
					g2D = uNow.draw(g2D);
				}
			}
		} else if (operationCode == 2) {
			int x, y;
			Unit uNow, unit;
			g2D.setColor(Color.YELLOW);
			
			for (Iterator<Unit> it = UDB0.values().iterator(); it.hasNext(); ) {
				uNow = (Unit) it.next();
				if (uNow == null) {continue;}
				
				for (Iterator<Unit> it2 = UDB0.values().iterator(); it2.hasNext(); ) {
					unit = it2.next();
					if (unit == null) {continue;}
					
					if (unit.isParent(uNow)) {
						x = (int) uNow.getCenterPoint2D().getX();
						y = (int) uNow.getCenterPoint2D().getY();
						
						g2D.drawLine((int) unit.getCenterPoint2D().getX(), (int) unit.getCenterPoint2D().getY(), x, y);
						g2D.drawRect((x - 5), (y - 5), 3, 3);
					}
				}
			}
		}
		return g2D;
	}

	private void letsStartItNow() {
		// Let`s produce the ChousenOne Unit to create the New Universe!
		for (int i = 0; i < 3; i++) {
			UDB0.put(UDB0.size(), new Unit(UDB0.size(), "angel", 	TechClassUNames.newMaleName(), 		Gender.MALE, 		(short) 	0, 		100, 	
					new Point2D.Double(
							(30 + universalRand.nextDouble() * 200) + universalRand.nextDouble() * 200, 
							(30 + universalRand.nextDouble() * 200) + universalRand.nextDouble() * 200), null, null));
			Out.Print("Add to DB a new Unit with id = " + UDB0.get(UDB0.size() - 1) + " and name = " + UDB0.get(UDB0.size() - 1).getName());
			
			UDB0.put(UDB0.size(), new Unit(UDB0.size(), "angel", 	TechClassUNames.newFemaName(), 		Gender.FEMA, 		(short) 	0, 		100, 	
					new Point2D.Double(
							(30 + universalRand.nextDouble() * 200) + universalRand.nextDouble() * 200, 
							(30 + universalRand.nextDouble() * 200) + universalRand.nextDouble() * 200), null, null));
			Out.Print("Add to DB a new Unit with id = " + UDB0.get(UDB0.size() - 1) + " and name = " + UDB0.get(UDB0.size() - 1).getName());
		}
		
		System.out.println();
		System.out.println("Units count in BD = " + UDB0.size() + ". Content: ");
		for (Unit u : UDB0.values()) {System.out.println(u.getName());}
		System.out.println();
		
		// starts the Threads:
		runThreads();
		
		Out.Print("UnitFrame: letsStartItNow: Game is start!");
	}
	
	public static void destroyUnit(Unit unitToDestroy) {
		Out.Console("UnitFrame: destroyUnit: removing unit ID:" + unitToDestroy.getID() + " >> " + unitToDestroy.getName());
		UDB0.remove(unitToDestroy.getID());
	}

	public static Dimension getParentDimension() {return uFrame.getSize();}
	
	@Override
	public void windowClosing(WindowEvent e) {ExitClass.Exit(0);}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

	public static void createNewOne(Unit unit0, Unit unit1) {
		if (unit0.isChildExist() && unit1.isChildExist()) {
//			Out.Console(unit0.getName() + " and " + unit1.getName() + " meet, but has children already.");
			return;
		}
		if (unit0.isParent(unit1) || unit1.isParent(unit0)) {
//			Out.Console(unit0.getName() + " and " + unit1.getName() + " is family.");
			return;
		}
		
		Out.Console("\nCreating new child here...");
		
		int sexRandInt = universalRand.nextInt(1);
		int hpRandInt = universalRand.nextInt(50) + 50;
		Double posRandDouble = universalRand.nextDouble() * 10 + 100;
		
		// give a mother stamp of father:
		if (unit0.getGender().equals(Gender.MALE)) {unit1.changeFamilyStamp(unit0.getFamilyStamp());
		} else {unit0.changeFamilyStamp(unit1.getFamilyStamp());}
		
		// create a child:
		switch(sexRandInt) {
			case 0: 
				UDB0.put(
						UDB0.size(), 
						new Unit(UDB0.size(), "noTitle", 
								TechClassUNames.newMaleName(), 	Gender.MALE, 			(short) 	0, 		hpRandInt, 
								new Point2D.Double(posRandDouble + 60, posRandDouble - 60), unit0, unit1)
						);
				Out.Print("\nUnitFrame: Add to DB a new Unit with id = " + (UDB0.values().size() - 1) + " and name = " + UDB0.get(UDB0.size() - 1).getName());
				break;
			case 1:
			default: 
				UDB0.put(
						UDB0.size(), new Unit(UDB0.size(), "noTitle", 
								TechClassUNames.newFemaName(), 	Gender.FEMA, 		(short) 0, 		hpRandInt, 
								new Point2D.Double(posRandDouble - 60, posRandDouble + 60), unit0, unit1)
						);
			Out.Print("\nUnitFrame: Add to DB a new Unit with id = " + (UDB0.values().size() - 1) + " and name = " + UDB0.get(UDB0.size() - 1).getName());
		}
	}

	public static String getCurrentTimeStamp() {return dateFormat.format(today.getTime());}
}