package org.firstinspires.ftc.teamcode.robot.drive.tuning;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robot.drive.DrivetrainController;

/*
 * This is a simple routine to test turning capabilities.
 */
@Config
//@Disabled
@Autonomous(group = "drive")
public class TurnTest extends LinearOpMode {
    public static double ANGLE = 180; // deg

    @Override
    public void runOpMode() throws InterruptedException {
        DrivetrainController drive = new DrivetrainController(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        drive.turnRelative(Math.toRadians(ANGLE));
    }
}
