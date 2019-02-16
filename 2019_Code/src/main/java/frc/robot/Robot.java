/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

//package org.usfirst.frc.team2773.robot;

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	public static final String kDefaultAuto = "Default";
	public static final String kCustomAuto = "My Auto";
	public String m_autoSelected;
	public SendableChooser<String> m_chooser = new SendableChooser<>();
	public SendableChooser<String> path = new SendableChooser<>();
	
	public Joystick joy;
	public Joystick joy2;
	
	public double joyY;
	public double joyZ;
	public double accel;
	public double veloY;
	public double veloZ;
	public double maxSpeed;

	public double staticTurn;
	public double trackLeft;
	public double trackRight;
	
	public Victor FL; //Finnifan_Leftson
	public Victor BL; //Benjamen Leftson
	public SpeedControllerGroup left;
	public Victor FR; //Felix RodrIgeese
	public Victor BR; //Bobbert Raplhq.q
	public SpeedControllerGroup right;
	public DifferentialDrive drive;
	
	//Grabber 
	public Spark grabber; //Happy Time
	//public Spark GL; //Turny Turn 

	public Spark pole;
	public Spark grabLift;

	DoubleSolenoid solenoid1; 
	Solenoid solenoid2;
	Solenoid solenoid3;
	//Compressor comp;
	
	public String startChar;
	public Timer timer;
	
	public CameraServer camera;

	public boolean[] joyVals;
	public boolean[] joy2Vals;
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		path.setDefaultOption("FR1", "FR1");
		path.addOption("FR2", "FR2");
		path.addOption("BR1", "BR2");
		path.addOption("BR2", "BR2");
		path.addOption("FL1", "FL1");
		path.addOption("FL2", "FL2");
		path.addOption("FM1", "FM1");
		path.addOption("FM2", "FM2");
		SmartDashboard.putData("Path Choices", path);
		outputValues();

		joy = new Joystick(1); //Declaring and assigning default variables
		joy2 = new Joystick(3);
		joyY = 0;
		joyZ = 0;
		
		accel = 0.2;
		veloY = 0;
		veloZ = 0;
		maxSpeed = 0.4;

		FL = new Victor(0);
		FR = new Victor(3);
		BL = new Victor(1);
		BR = new Victor(2);
		
		left = new SpeedControllerGroup(FL, BL);
		right = new SpeedControllerGroup(FR, BR);
		
		drive = new DifferentialDrive(left, right);

		
		staticTurn = 2;
		trackLeft = 0;
		trackRight = 0;
		
		grabber = new Spark(4);
		grabber.setInverted(true);
		//GL = new Spark(5);

		pole = new Spark(7);
		pole.setInverted(true);
		grabLift = new Spark(6);
		grabLift.setInverted(true);

		//solenoid1 = new DoubleSolenoid(0, 1);
		solenoid2 = new Solenoid(0);
		//solenoid3 = new Solenoid(3);
		//comp = new Compressor();
		//comp.start();
		
		startChar = "A";
		timer = new Timer();
		
		camera = CameraServer.getInstance();
		camera.startAutomaticCapture(0).setResolution(1280, 720);
		//camera.setFPS(15);

		joyVals = new boolean[12];
		joy2Vals = new boolean[10];
		
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		startChar = path.getSelected();
		//autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override

	public void autonomousPeriodic() { //all autonomous names
			if(startChar == "FR1")
			{
				close(0, 1);
			}
			else if(startChar == "BR1")
			{
				close(48, 1);
			}
			else if(startChar == "FL2")
			{
				close(0, -1);
			}
			else if(startChar == "BL2")
			{
				close(48, -1);
			}
			else if(startChar == "FR2")
			{
				far(0, 1);
			}
			else if(startChar == "BR2")
			{
				far(48, 1);
			}
			else if(startChar == "FL1")
			{
				far(0, -1);
			}
			else if(startChar == "BL1")
			{
				far(48, -1);
			}
			else if(startChar == "FM2")
			{
				middle(1);
			}
			else if(startChar == "FM1")
			{
				middle(-1);
			}
		} 
	


	public void driveForward(int inches) 
	{
		for(int i = 0; i < inches; i++)
		{
			if(timer.get() < 250) //time it takes to drive one inch
				drive.tankDrive(1, 1);
			 //250 is a placeholder value for how long it takes to drive one inch
		}
		drive.tankDrive(0, 0);
	}
	public void turn45(int direction) //turning 45 degrees
	{
		drive.tankDrive(-1 * direction, 1 * direction);
		//wait(250);
		drive.tankDrive(0, 0);
	}
	public void turn90(int direction) { //turning 90 degrees
		
		drive.tankDrive(-1 * direction, 1 * direction);
		//wait(250);
		drive.tankDrive(0, 0);
	}
	public void middle(int turn) 
	{
		driveForward(81);
		turn45(1);
		driveForward(105);
		turn45(-1);
		driveForward(90);
		turn90(-1);
		driveForward(27);
	}
	public void far(int back, int turn)
	{
		driveForward(81 + back);
		turn90(1);
		driveForward(45);
		turn45(-1);
		turn45(-1);
		turn45(-1);
		driveForward(105);
		turn45(-1);
		driveForward(90);
		turn90(-1);
		driveForward(27);
		//smash into cargo ship
	}
	public void close(int back, int turn)
	{
		driveForward(120 + back);
		turn45(-1);
		driveForward(48);
		turn45(1);
		driveForward(90);
		turn90(1);
		driveForward(27);
		//smash into cargo ship
		
	}
	
	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic()
	{
		drive(-joy.getY(), joy.getZ());
		grab();
		lift();
		//outputValues();
		System.out.println(joy2.getRawAxis(1));
	}
	
	public void drive(double joyY, double joyZ)  // takes input from joystick to control robot drivetrain (treads).
	{
		joyZ = joyZ * 0.5;
		System.out.println( "JoyY: " + joyY);
		System.out.println( "JoyZ: " + joyZ);
		if(Math.abs(joyY) > 0.2)                 // If Joy Y input is greater than the deadzone (0.2), add forward velocity.
		{
			trackLeft = joyY;
			trackRight = joyY;
			if(Math.abs(joyZ) > 0.1 )				 // If Joy Z input is greater than the deadzone (0.2),
			{										 // make tracks turn at  different speeds
				//if(Math.abs(trackLeft) + Math.abs(joyZ) < 1 )  // This if statement and the following one both make sure that the
				//{											   // input reaching the drive method never goes above 1
					trackLeft = trackLeft + joyZ;
				//}
				//if(Math.abs(trackRight) + Math.abs(joyZ) < 1 )
				//{
					trackRight = trackRight - joyZ;
				//}
			}
		}
		else if(Math.abs(joyZ) > 0.1 && joyY < 0.2)				 // If Joy Z input is greater than the deadzone (0.2),
		{										 // make tracks turn at  different speeds
			if(Math.abs(trackLeft) + Math.abs(joyZ) < 1 )  // This if statement and the following one both make sure that the
			{											   // input reaching the drive method never goes above 1
				trackLeft = 0;
				trackLeft = trackLeft + joyZ * staticTurn;
			}
			if(Math.abs(trackRight) + Math.abs(joyZ) < 1 )
			{
				trackRight = 0;
				trackRight = trackRight - joyZ * staticTurn;
			}
		}
		if(Math.abs(joyY) < 0.2 && Math.abs(joyZ) < 0.1)
		{
			trackLeft = 0;
			trackRight = 0;
		}
		System.out.println( "trackLeft: " + trackLeft);
		System.out.println( "trackRight: " + trackRight);
		drive.tankDrive(trackLeft, trackRight);   // Sends the final trackLeft/Right variables to the drive method
	}
	
	public void grab() //method for controling robot grabber
	{
		if(joy.getRawButton(1)) 
		{
			grabber.set(1);
		}
		else if(joy.getRawButton(2))
		{
			grabber.set(-1);
		} 
		else
		{
			grabber.set(0);
			//GL.set(0);
		}
	}

	public void lift()
	{
		//Brings pole up
		if(joy2.getRawAxis(5) < -0.2)
		{
			pole.set(1);
			grabLift.set(-0.6);
		}
		//Brings both down
		else if(joy2.getRawAxis(5) > 0.2)
		{
			pole.set(-1);
			grabLift.set(0.5);
		}
		//Brings grabber up
		else if(joy2.getRawAxis(1) < -0.2)
		{
			//pole.set(1);
			grabLift.set(1);
		}

		//Grabber down
		else if(joy2.getRawAxis(1) > 0.2)
		{
			//pole.set(-1);
			grabLift.set(-1);
		}
		else{
			pole.set(0);
			grabLift.set(0);
		}
	}
	
	public void allInputs()
	{
		for(int i = 0; i < 12; i++)
		{
			joyVals[i] = joy.getRawButton(i + 1);
		}
		for(int i = 0; i < 10; i++)
		{
			joy2Vals[i] = joy2.getRawButton(i + 1);
		}
	}
	
	public void outputValues()
	{
		SmartDashboard.putNumber("Match", Timer.getMatchTime());
		SmartDashboard.putNumber("JoyX", joy.getX());
		SmartDashboard.putNumber("JoyY", joy.getY());
		SmartDashboard.putNumber("JoyZ", joy.getZ());
		SmartDashboard.putBooleanArray("Joystick Values", joyVals);
		SmartDashboard.putBooleanArray("Joystick 2 Values", joy2Vals);
	}


	@Override
	public void testInit() {
		//resetEncoders();
		//comp.start();
	}
	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		System.out.println(joy.getRawButton(1));
		if (joy.getRawButton(1)) {
			//System.out.println("Button Pressed");
			//solenoid1.set(DoubleSolenoid.Value.kReverse);
			//solenoid1.set(DoubleSolenoid.Value.kReverse);
			//solenoid1.set(DoubleSolenoid.Value.kOff);
			//solenoid2.set(true);
			//solenoid3.set(true);
			} else {
				//System.out.println("Button Not Pressed");
				//solenoid1.set(DoubleSolenoid.Value.kForward);
				//solenoid1.set(DoubleSolenoid.Value.kOff);
				//solenoid2.set(false);
				//solenoid3.set(false);	
			}
			//comp.setClosedLoopControl(true);
			//comp.start();
			//System.out.print(comp.enabled() + " : ");
			//System.out.println(comp.getClosedLoopControl());
			//System.out.println(comp.getCompressorCurrent());
			/*System.out.println(comp.getCompressorCurrentTooHighFault());
			System.out.println(comp.getCompressorCurrentTooHighStickyFault());
			System.out.println(comp.getCompressorNotConnectedFault());
			System.out.println(comp.getCompressorNotConnectedStickyFault());
			System.out.println(comp.getCompressorShortedFault());
			System.out.println(comp.getCompressorShortedStickyFault());*/
			System.out.println("YOoooooooooo");
			System.out.println(joy2.getRawAxis(0));
			System.out.println(joy2.getRawAxis(1));
			System.out.println(joy2.getRawAxis(2));
			System.out.println(joy2.getRawAxis(3));
	} 

	@Override
	public void disabledInit() {

		/*System.out.println("Stick X: " + joy.getZ());
		System.out.println("Stick Y: " + joy.getY());
	
		System.out.println("autonomous target: " + startChar);
		
		System.out.println("no idea: " +
				getClass().getClassLoader().getResource("").getPath());*/
	}
} 



                                                  /*:-                          
                                                 /hdms                          
                                                `ommmh.                          
                                               `+mmmd/os+-`                      
                                             `:ohmmmmdmmmmmhs/                    
                                    -+oooo/+sdmmmmbmanmmmmmmmd``                  
             ```...--..```        `+dmyyyhmmmmmmmmmneedsmmmmmmhyy+                
      ``-/+syhhdmmmmmmmdhyso+++++sydms    hmmmmdmmmmtommmmmmmmmmmd`               
     /ydmmmmmmmmmmmmmmmmmmmmmmy:./oo/`    .+so/./hmmmstopmmquichem+               
      -odmmmmmmmjaredmmmmmmmmh`                  .dmmmmmmeatermmmdh               
     .-`.+hmmmmmmismmmmmmmmmmms                  hmmmmmmmmmdhddy+-`               
    `odmmdhshmmmmmthemmmmmmmmmd/     `+shys/      odmmmmdy+-` ``                   
   `ymmmmmkhaimmmmmrealmmmmm:.      `hhailmmy      `-omm-                          
   smsocksmismmmmmmmmvpmmmmm        :mmthemmm`       -mm:                          
  `mmmmmmmmmbadmcoltermmmmmmyo/`     ommorbd/     `/oymd.                          
  -mmmsquidmmmmmwasmmmmyommmmmms      `:+/:`     `dmds+.                           
  `dmmmtastesmmheremd/`hmmmmmd:                  smm/                             
   ommmmmmgoodmmmmmdo` `oydmmmh-   ``        ``   /mmo                             
   :malecmmmmmmmmdo.     ``-+syhsohddh-    +hddy+hmd+`                             
   :mmisn'tmmmmd+.            ``.-:+hmy-.-:md+oyyy+`                               
   ommmrealmmh+`                    /mmddddm/  ``                                  
   `dmmmmmmmy/`                      -josephd                                       
   +mmmmmds-`                         ydunlap                                       
  .dmmmh+.                            .herwin`                                      
 `ymmd/`                               .hmmmmo.                                     
ommmmh/`                               .smmmmdy/.                                  
`:::::::.             BEES              `:::::::*/
