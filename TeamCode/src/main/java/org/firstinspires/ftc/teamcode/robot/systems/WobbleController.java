package org.firstinspires.ftc.teamcode.robot.systems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.Controller;

import static org.firstinspires.ftc.teamcode.util.Sleep.sleep;

@Config
public class WobbleController implements Controller {
    private Telemetry telemetry;
    private HardwareMap hardwareMap;
    private Servo wobbleGrip;
    private Servo wobbleArm;
    private Servo wobbleSide;
    public static String ControllerName;
    public boolean isDown = false;

    //Side Wobble Dropper
    public static double SideHoldPos = 0.8;
    public static double SideReleasePos = 0.2;
    public static Servo.Direction SideDirection = Servo.Direction.FORWARD;

    //Main Wobble Grip
    public static double GripGrabPos = 0.9;
    public static double GripReleasePos = 0.55;
    public static double GripInitPos = 0.85;

    //Main Wobble Arm (rotate)
    public static double ArmDropPos = 0.05;
    public static double ArmPickupPos = 0.7;
    public static double ArmPickupPosAuto = 0.2; //lower, doesn't need to clear wall
    public static double ArmInitPos = 0.9; //to be within 18in
    public static double ArmAutoPos = 0.8; //to be within 18in

    public static int grabDelay = 300;
    public static int releaseDelay = 150;
    public static int pickupDelay = 150;
    public static int dropDelayAuto = 150;
    public static int dropDelayTele = 400;

    public WobbleController(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;

        ControllerName = getClass().getSimpleName();
    }

    @Override
    public void init() {
        wobbleGrip = hardwareMap.get(Servo.class, "wobble_grip");
        wobbleArm = hardwareMap.get(Servo.class, "wobble_arm");
        wobbleSide = hardwareMap.get(Servo.class, "side_wobble");
        wobbleSide.setDirection(SideDirection);
        wobbleGrip.setPosition(GripInitPos);
        wobbleArm.setPosition(ArmInitPos);
        wobbleSide.setPosition(SideHoldPos);
    }

    @Override
    public void start() {
        wobbleArm.setPosition(ArmPickupPos);
    }

    @Override
    public void stop() {
        wobbleGrip.setPosition(GripInitPos);
        wobbleArm.setPosition(ArmInitPos);
        wobbleSide.setPosition(SideHoldPos);
    }

    public void dropAuto() {
        telemetry.addData(ControllerName, "Dropping Wobble");
        drop();
        sleep(dropDelayAuto);
        release();
        sleep(releaseDelay);
    }

    public void pickupAuto() {
        telemetry.addData(ControllerName, "Picking Up Wobble");
        grab();
        sleep(grabDelay);
        wobbleArm.setPosition(ArmPickupPosAuto);
        sleep(pickupDelay);
    }

    public void pickupTele() {
        telemetry.addData(ControllerName, "Picking Up Wobble");
        grab();
        sleep(grabDelay);
        pickup();
        sleep(pickupDelay);
    }

    public void dropTele() {
        telemetry.addData(ControllerName, "Dropping Wobble");
        drop();
        sleep(dropDelayTele);
        release();
        sleep(releaseDelay);
    }

    public void readyToPickup() {
        release();
        drop();
    }

    public void grab(){
        wobbleGrip.setPosition(GripGrabPos);
    }
    public void release(){
        wobbleGrip.setPosition(GripReleasePos);
    }
    public void pickup(){
        isDown = false;
        wobbleArm.setPosition(ArmPickupPos);
    }
    public void drop(){
        isDown = true;
        wobbleArm.setPosition(ArmDropPos);
    }
    public void sideRelease() {
        wobbleSide.setPosition(SideReleasePos);
        sleep(50);
    }
}
