package org.usfirst.frc.team5141.robot; // What is package and why is it making the code wonky?

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */

public class Robot extends IterativeRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	
	DriverStation ds = DriverStation.getInstance();
	
	Joystick gamePad0 = new Joystick (0);
	
	VictorSP leftOne = new VictorSP(0);
	VictorSP leftTwo = new VictorSP(1);
	VictorSP leftThree = new VictorSP(2);
	SpeedControllerGroup leftDrive = new SpeedControllerGroup(leftOne, leftTwo, leftThree);
	
	VictorSP rightOne = new VictorSP(3);
	VictorSP rightTwo = new VictorSP(4);
	VictorSP rightThree = new VictorSP(5);
	SpeedControllerGroup rightDrive = new SpeedControllerGroup(rightOne, rightTwo, rightThree);
	
	VictorSP elevator = new VictorSP(8);
	SpeedControllerGroup elevatorDrive = new SpeedControllerGroup(elevator);
	
	DifferentialDrive driveTrain = new DifferentialDrive(leftDrive, rightDrive);
	
	Compressor c = new Compressor(0);
	Solenoid s0 = new Solenoid(0);
	Solenoid s1 = new Solenoid(1);
	Solenoid s2 = new Solenoid(2);
	
	int autoLoopCounter;
	int toggCount = 0;
	int toggCount1 = 0;
	int toggCount2 = 0;
	double drive;
	
	DigitalInput limitSwitch = new DigitalInput(0);
	DigitalInput magSwitch = new DigitalInput(1);
	
//	Counter counter = new Counter(limitSwitch);
	
	/**public void tankDriveAll(double l,double r) {
		driveOne.tankDrive(l,r);
		driveTwo.tankDrive(l,r);
		driveThree.tankDrive(l,r);
	}
	*/
	
	/*public void setRumble(RumbleType rumble) {
		this.rumble = rumble;
		
	}
	
	/**private RumbleType RumbleType(int i) {
	// TODO Auto-generated method stub
	return null;
}*/

	GenericHID.RumbleType rumble(double Output) {
		return null;
	}
		
	
	
	public boolean solenoidAll(boolean aBoolean) {
		s0.set(aBoolean);
		s1.set(aBoolean);
		s2.set(aBoolean);
		return false;
	}
	
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
    	
    }
    
    public boolean isSwitchSet() {
    	return limitSwitch.get();
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
    	//String gameData;
    	//gameData = DriverStation.getInstance().getGameSpecificMessage();
    	//if(gameData.charAt(0) == 'L')
    	//{
    		//Put left auto code
    	//} else {
    		//Put right auto code
    	//}
    	
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    
    }
    
    
     /**This function is called once each time the robot enters tele-operated mode
     */
    
    public void teleopInit(){
    	c.setClosedLoopControl(false);
    	s0.set(true);
    }

    /**
     * This function is called periodically during operator control
     */
    
    // Two controllers can't input to the same things at the same time, gamepad 0 has priority
    	public void teleopPeriodic() {
    		double leftStick = (gamePad0.getRawAxis(1) *.5)*(gamePad0.getRawAxis(3)+1);
    		double rightStick = (gamePad0.getRawAxis(5) *.5)*(gamePad0.getRawAxis(3)+1);
    	
    		driveTrain.tankDrive(leftStick, rightStick);{
    			
    		}
    	
    	  
    	if(gamePad0.getRawButton(1) == true && toggCount2 == 0) {
    		  c.setClosedLoopControl(!c.getClosedLoopControl());
    		  toggCount2++;
    	}
    	else {
    		  if(gamePad0.getRawButton(1) == false && toggCount2 == 1) {
    			  toggCount2 = 0;
    		  }
    			  else {
    		  }
    	}
    	  
    	if(gamePad0.getRawButton(2) == true && toggCount == 0) {
    			s0.set(!s0.get());//S0 doesn't work without S2 going at the same time?
    			s1.set(!s1.get());// Gear pneumatics
    			s2.set(!s2.get());//
    			toggCount++;
    		}
    		else {
    			if(gamePad0.getRawButton(2) == false && toggCount == 1) {
    				toggCount = 0;
    			}
    			else {	
    			}
    		}
			
    	if(gamePad0.getRawButton(3) == true && toggCount1 == 0) {
    		elevatorDrive.set(.5);
			toggCount1++;
		}
		else {
			if(gamePad0.getRawButton(3) == false && toggCount1 == 1) {
				toggCount1 = 2;
			}
			else {
				if(magSwitch.get() == true && toggCount1 == 2) {
					elevatorDrive.set(0);
					toggCount1 = 3;
				}
				else {
					if(gamePad0.getRawButton(3) == true && toggCount1 == 3) {
						toggCount1 = 0;
					}
					else {
						
					}
				}
			}
		}

		}
	}
    		
    /**
     * This function is called periodically during test mode
     */
   