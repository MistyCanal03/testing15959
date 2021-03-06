package org.firstinspires.ftc.teamcode.opmodes.tele.params;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.opmodes.tele.Tele;

@Config //for FTCDash
public class GamepadMappings {

    Gamepad gamepad1;
    Gamepad gamepad2;

    public GamepadMappings(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    public boolean DriveMode() {
        return gamepad1.right_bumper && !gamepad1.left_bumper;
    }

    public boolean Shoot() {
        return gamepad1.a && !gamepad1.start;
    }

    public boolean PowerShot() {
        return gamepad1.b && !gamepad1.start;
    }

    public boolean AutoIntake() {
        return gamepad1.x;
    }

    public boolean IncrementLeft() {
        return gamepad1.dpad_up;
    }

    public boolean IncrementRight() {
        return gamepad1.dpad_down;
    }

    public boolean Intake() {
        if (Tele.DriverMode == DriverMode.OneDriver) return gamepad1.back;
        return gamepad2.back;
    }
    public boolean StraightenTurret() { return gamepad1.y; }
    public boolean IncreasePower() { return gamepad1.dpad_right; }
    public boolean DecreasePower() { return gamepad1.dpad_left; }

    public boolean Tapper() { return gamepad2.y; }
    public boolean StopAllIntakes() {
        return gamepad2.x;
    }
    public boolean Wobble() {
        if (Tele.DriverMode == DriverMode.OneDriver) return gamepad1.dpad_up || gamepad1.dpad_down;
        return gamepad2.a && !gamepad2.start;
    }
    public boolean WobbleDeliver() {
        if (Tele.DriverMode == DriverMode.OneDriver) return gamepad1.dpad_left || gamepad1.dpad_right;
        return gamepad2.b && !gamepad2.start;
    }

    public boolean Localize() {
        return gamepad1.left_bumper && gamepad1.right_bumper;
    }

    public enum DriverMode {
        OneDriver, TwoDrivers
    }
}