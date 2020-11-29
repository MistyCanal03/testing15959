package org.firstinspires.ftc.teamcode.robot.systems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.Controller;
import org.firstinspires.ftc.teamcode.util.MockDcMotor;
import org.firstinspires.ftc.teamcode.util.MockDcMotorEx;

public class IntakeController implements Controller {
    Telemetry telemetry;
    MockDcMotorEx intake;

    public static String ClassName;

    public IntakeController(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        intake = new MockDcMotorEx("intake", telemetry);
        ClassName = getClass().getSimpleName();
    }

    @Override
    public void init() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        intake.setPower(0);
    }

    public void run(double motorPower){
        telemetry.addData(ClassName, "Intaking");
        intake.setPower(motorPower);
    }
}
