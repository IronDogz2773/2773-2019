/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2773.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Spark;
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
	
	public Joystick joy;
	public Joystick joy2;
	
	public double joyY;
	public double joyZ;
	public double accel;
	public double veloY;
	public double veloZ;
	public double maxSpeed;
	
	public Victor FL; //Finnifan_Leftson
	public Victor BL; //Benjamen Leftson
	public SpeedControllerGroup left;
	public Victor FR; //Felix RodrIgeese
	public Victor BR; //Bobbert Raplhq.q
	public SpeedControllerGroup right;
	public DifferentialDrive drive;
	
	//Grabber 
	public Spark GR; //Happy Time
	public Spark GL; //Turny Turn

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);

		joy = new Joystick(1);
		joy2 = new Joystick(2);
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

		GR = new Spark(4);
		GL = new Spark(5);
		
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
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		/*switch (m_autoSelected) {
			case kCustomAuto:
				// Put custom auto code here
				break;
			case kDefaultAuto:
			default:
				// Put default auto code here
				break; 
		} */
	} 

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() 
	{
		drive(-joy.getY(), joy.getZ());
		grab();
	}
	
	// Gets input from contoller and moves robot 
	public void drive(double joyY, double joyZ)
	{
		// Updates variables from Joystick
		maxSpeed = joyY();
  
    //TODO: ACCELERATION CODE GOES HERE snap
		while(joyY > 0.2 && veloY <= 1) {
      			veloY += maxSpeed * .001 + veloY;
     			System.out.println(veloY);
    		}
    		if(veloY > 1) {
      			veloY = 1;
      			System.out.println(veloY);
    		}
		
		if(Math.abs(joyY) > 0.2) // Controls Y axis movement (forwards/backwards)
			drive.tankDrive(veloY, veloY);
		else if(Math.abs(joyZ) > 0.1) // Controls Z axis movement (turning)
			drive.tankDrive(joyZ * 0.8, -joyZ * 0.8);
		else // If no input, no movement
			drive.tankDrive(0, 0); 
		//System.out.println(joyY);
		//System.out.println(joyZ);
		/*if(joyY > 0.2)
			drive.tankDrive(joyY, joyY);*/
		system.out.println(veloY);
	}

	public void grab()
	{
		if(joy.getTrigger())
		{
			GR.set(0.5);
			GL.set(-0.5);
		}
		else if(joy.getRawButton(2))
		{
			GR.set(-0.5);
			GL.set(0.5);
		}
		else
		{
			GR.set(0);
			GL.set(0);
		}
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
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