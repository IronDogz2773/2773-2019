/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.            													  */
/* IronDogz Team 2773														  */
/* 2019 Deep Space Code        												  */
/* v.0.1.1                                        					          */
/*----------------------------------------------------------------------------*/


package frc.robot;

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
	
	public Joystick joy;
	public Joystick joy2;
	
	public double joyY;
	public double joyZ;
	public double accel;
	public double veloY;
	public double veloZ;
	public double staticTurn;

	public double trackLeft;
	public double trackRight;
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

	//Shuffleboard
	
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
		staticTurn = 2;

		trackLeft = 0;
		trackRight = 0;

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

	/*public void SFSetup()
	{
		Shuffleboard.enableActuatorWidgets();
		Shuffleboard.startRecording();
	}*/

	public void outputValues()
	{
		SmartDashboard.putNumber("Test", Math.PI);
		SmartDashboard.putNumber("Time", 999);
		SmartDashboard.putNumber("Left", 999);
		SmartDashboard.putNumber("Right", 999);

	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() 
	{
		System.out.println( "JoyY: " + joyY);
		System.out.println( "JoyZ: " + joyZ);
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
