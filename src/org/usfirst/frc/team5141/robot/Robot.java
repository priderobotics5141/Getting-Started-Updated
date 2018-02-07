package org.usfirst.frc.team5141.robot; // What is package and why is it making the code wonky?

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Spark;
import com.kauailabs.navx.frc.AHRS;
import com.kauailabs.navx.frc.AHRS.SerialDataType;


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
	
	
	AHRS ahrs;
	
	Joystick gamePad0 = new Joystick (0);
	double leftStickY = gamePad0.getRawAxis(1);
    double rightStickY = gamePad0.getRawAxis(5);
	
	VictorSP leftOne = new VictorSP(0);
	VictorSP leftTwo = new VictorSP(1);
	VictorSP leftThree = new VictorSP(2);
	SpeedControllerGroup leftDrive = new SpeedControllerGroup(leftOne, leftTwo, leftThree);
	VictorSP rightOne = new VictorSP(3);
	VictorSP rightTwo = new VictorSP(4);
	VictorSP rightThree = new VictorSP(5);
	SpeedControllerGroup rightDrive = new SpeedControllerGroup(rightOne, rightTwo, rightThree);
	
	VictorSP elevator = new VictorSP(8);
	
	Spark testLight = new Spark(9);
	
	DifferentialDrive driveTrain = new DifferentialDrive(leftDrive, rightDrive);
	
	Compressor c = new Compressor(0);
	Solenoid s0 = new Solenoid(0);
	Solenoid s1 = new Solenoid(1);
	Solenoid s2 = new Solenoid(2);
	
	int autoLoopCounter;
	int toggCount = 0;
	int toggCount1 = 0;
	int toggCount2 = 0;
	int toggCount3 = 0;
	int elevatorState=0;
	int elevatorDestiny = 0;
	double drive;
	double red = .61 ;
	double blue = .87;
	double gold = .67;
	
	
	DigitalInput limitSwitch0 = new DigitalInput(0);
	DigitalInput limitSwitch1 = new DigitalInput(1);
	DigitalInput limitSwitch2 = new DigitalInput(2);
	DigitalInput limitSwitch3 = new DigitalInput(3);
	DigitalInput magSwitch = new DigitalInput(4);
	Timer timer = new Timer();
	
	//Game Dependent Variables
	char startPosition = 'L';
	char switchPosition;
	char scalePosition;
	String destination = "Sw"; 
	// Sw = Switch & Sc = Scale
	
	
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
    	leftDrive.setInverted(true);
    	rightDrive.setInverted(true);
    	CameraServer.getInstance().startAutomaticCapture();
    	ahrs = new AHRS(SerialPort.Port.kMXP, SerialDataType.kProcessedData, (byte)50);
    }
    
   
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
    	timer.reset();
    	timer.start();
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	switchPosition = gameData.charAt(0);
    	scalePosition = gameData.charAt(1);
    	c.setClosedLoopControl(false);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        SmartDashboard.putNumber("QuaternionZ", ahrs.getQuaternionZ());
        ahrs.enableLogging(true);

    	switch(startPosition) {
    	case 'L': if(destination == "Sw") {
			if(switchPosition == 'L') {
				if(!(ahrs.getQuaternionZ() > 0 && ahrs.getQuaternionZ() < .1)) {
					driveTrain.tankDrive(-.4,.4);
				}
				else 
				{
					driveTrain.tankDrive(0, 0);
				}
			/*	if(timer.get() > 1 && timer.get() < 2) {
					driveTrain.tankDrive(.5,-.5);
				}
				if(timer.get() > 2 && timer.get() < 3) {
					driveTrain.tankDrive(.5, .5);
				}
				if(timer.get() > 3 && timer.get() < 4) {
					driveTrain.tankDrive(0,0);
					elevator.set(.5);// CHANGE TO THE LOADER DRIVE
				}
				if(timer.get() > 4 && timer.get() < 5) {
					elevator.set(0);
				}
			*/	//LSwL
			}
			else {
				
				//LSwR
			}
			break;
		}
		if(destination == "Sc")	{
			if(scalePosition == 'L') {
				//LScL
			}
			else {
				//LScR
			}
			break;
		}
    	case 'C': if(destination == "Sw") {
			if(switchPosition == 'L') {
				//CSwL
			}
			else {
				//CSwR
			}
			break;
		}
		if(destination == "Sc")	{
			if(scalePosition == 'L') {
				//CScL
			}
			else {
				//CScR
			}
			break;
		}
    	case 'R':if(destination == "Sw") {
			if(switchPosition == 'L') {
				//RSwL
			}
			else {
				//RSwR
			}
			break;
		}
		if(destination == "Sc")	{
			if(scalePosition == 'L') {
				//RScL
			}
			else {
				//RScR
			}
			break;
		}
    	}
    		
    		
    	
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
    		double leftStick = (-gamePad0.getRawAxis(1) *.5)*(gamePad0.getRawAxis(3)+1);
    		double rightStick = (-gamePad0.getRawAxis(5) *.5)*(gamePad0.getRawAxis(3)+1);
    		driveTrain.tankDrive(leftStick, rightStick);
    	
    		if(gamePad0.getPOV()==0) {
    			elevator.set(.5);
    		}
    		if(gamePad0.getPOV()==180) {
    			elevator.set(-.5);
    		}
    		if(gamePad0.getPOV()==-1) {
    			elevator.set(0);
    			driveTrain.tankDrive(0, 0);
    		}
    		if(gamePad0.getPOV()==90) {
    			driveTrain.tankDrive(.7, -.7);
    		}
    		if(gamePad0.getPOV()==270) {
    			driveTrain.tankDrive(-.7, .7);
    		}
    		
    	  
    	if(gamePad0.getRawButton(5) == true && toggCount2 == 0) {
    		  c.setClosedLoopControl(!c.getClosedLoopControl());
    		  toggCount2++;
    	}
    	if(gamePad0.getRawButton(5) == false && toggCount2 == 1) {
    		  toggCount2 = 0;
    	}
    	  
    	if(gamePad0.getRawButton(6) == true && toggCount == 0) {
    			s0.set(!s0.get());//S0 doesn't work without S2 going at the same time?
    			s1.set(!s1.get());// Gear pneumatics
    			s2.set(!s2.get());//
    			toggCount++;
    		}
    	if(gamePad0.getRawButton(6) == false && toggCount == 1) {
    				toggCount = 0;
    			}
    			
    	//BELOW ARE ELEVATOR CONTROLS	
    	//limit switches
    	
    	/** TOGGLE BASE CODE
    	 * if(button.get() == true && toggCount == 0)
    	 *   something
    	 *   toggCount++
    	 * if(button.get() == false && toggCount == 1)
    	 *   something
    	 *   toggCount++
    	 * etc...
    	 * 
    	 */
    	
    	//elevator code 
    	
    	elevatorState = limitSwitch0.get()? elevatorState:0;
    	elevatorState = limitSwitch1.get()? elevatorState:2;
    	elevatorState = limitSwitch2.get()? elevatorState:4;
    	elevatorState = limitSwitch3.get()? elevatorState:6;
    	
    	
    	if((elevator.get() > 0 && elevatorState == 4)||(elevator.get() < 0 && elevatorState == 6)) {
    		elevatorState = 5;
    	}
    	//if((elevator.get() > 0 && elevatorState == 2)||(elevator.get() < 0 && elevatorState == 4)) {
    	//	elevatorState = 3;
    	//}
    	if((elevator.get() > 0 && elevatorState == 0)||(elevator.get() < 0 && elevatorState == 2)) {
    		elevatorState = 1;
    	}
    	
    	
    	
    	if(gamePad0.getRawButtonPressed(1) == true) {
    		elevatorDestiny = 0;
    	}
    	if(gamePad0.getRawButtonPressed(2) == true) {
    		elevatorDestiny = 2;
    	}
    	if(gamePad0.getRawButtonPressed(3) == true) {
    		elevatorDestiny = 4;
    	}
    	if(gamePad0.getRawButtonPressed(4) == true) {
    		elevatorDestiny = 6; 
    	}
    	/*
    	if(elevatorDestiny > elevatorState) {
    		elevator.set(.5);
    	}
    	if(elevatorDestiny < elevatorState) {
    		elevator.set(-.5);
    	}
    	if(elevatorDestiny == elevatorState) {
    		elevator.set(0);
    	}
    	*/
    	}
    	
    	
    	  public static void main (String[] args) {
              Robot.main(args);
          }

		
}

    		
    /**
     * This function is called periodically during test mode
     */
   