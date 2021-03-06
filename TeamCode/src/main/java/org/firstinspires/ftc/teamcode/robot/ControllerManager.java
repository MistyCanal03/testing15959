package org.firstinspires.ftc.teamcode.robot;

import androidx.annotation.Nullable;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.opmodes.auto.params.FieldConstants;
import org.firstinspires.ftc.teamcode.robot.camera.CameraController;
import org.firstinspires.ftc.teamcode.robot.drive.DrivetrainController;
import org.firstinspires.ftc.teamcode.robot.systems.HubController;
import org.firstinspires.ftc.teamcode.robot.systems.IntakeController;
import org.firstinspires.ftc.teamcode.robot.systems.RearIntakeController;
import org.firstinspires.ftc.teamcode.robot.systems.ShooterController;
import org.firstinspires.ftc.teamcode.robot.systems.WobbleController;

import java.util.HashMap;
import java.util.Map;

public class ControllerManager implements Controller{

    // Maps case-insensitive controllerName to a controller
    private Map<String, Controller> controllers = new HashMap<>();
    protected final Object lock = new Object();
    Telemetry telemetry;
    MultipleTelemetry telemetryd;

    public ControllerManager(Telemetry telemetry, MultipleTelemetry telemetryd) {
        this.telemetry = telemetry;
        this.telemetryd = telemetryd;
    }

    public void make(HardwareMap hardwareMap, Telemetry telemetry, MultipleTelemetry telemetryd){
        add(new CameraController(hardwareMap, telemetryd), FieldConstants.Camera);
        add(new DrivetrainController(hardwareMap, telemetryd), FieldConstants.Drive);
        add(new HubController(hardwareMap, telemetryd), FieldConstants.Hub);
        add(new IntakeController(hardwareMap, telemetryd), FieldConstants.Intake);
        add(new RearIntakeController(hardwareMap, telemetryd), FieldConstants.VertIntake);
        add(new ShooterController(hardwareMap, telemetry, telemetryd), FieldConstants.Shooter);
        add(new WobbleController(hardwareMap, telemetryd), FieldConstants.Wobble);
    }

    public void add(Controller controller, String controllerName) {
        synchronized (lock) {
            controllers.put(controllerName.toLowerCase(), controller);
        }
    }

    @Nullable
    public <T> T get(Class<? extends T> classOrInterface, String controllerName) {
        synchronized (lock) {
            controllerName = controllerName.trim().toLowerCase();
            T result = tryGet(classOrInterface, controllerName);
            if (result == null) {
                telemetryd.addData("ControllerManager", String.format("Unable to find a controller with controllerName \"%s\" and type %s",
                            controllerName,
                            classOrInterface.getSimpleName()));
            }
            return result;
        }
    }

    @Nullable
    public <T> T tryGet(Class<? extends T> classOrInterface, String controllerName) {
        synchronized (lock) {
            controllerName = controllerName.trim().toLowerCase();
            Controller controller = controllers.get(controllerName);
            if (controller != null) {
                if (classOrInterface.isInstance(controller)) {
                    return classOrInterface.cast(controller);
                }
            }

            return null;
        }
    }

    @Nullable
    public Controller get(String controllerName){
        synchronized (lock) {
            return controllers.get(controllerName.toLowerCase());
        }
    }

    @Override
    public void init() {
        synchronized (lock) {
            telemetryd.addData("ControllerManager", "init");
            for (Map.Entry<String, Controller> entry : controllers.entrySet()) {
                Controller c = (Controller) entry.getValue();
                c.init();
            }
        }
    }

    @Override
    public void start() {
        synchronized (lock) {
            telemetryd.addData("ControllerManager", "start");
            for (Map.Entry<String, Controller> entry : controllers.entrySet()) {
                Controller c = (Controller) entry.getValue();
                c.start();
            }
        }
    }

    @Override
    public void stop() {
        synchronized (lock) {
            telemetryd.addData("ControllerManager", "stop");
            for (Map.Entry<String, Controller> entry : controllers.entrySet()) {
                Controller c = (Controller) entry.getValue();
                c.stop();
            }
        }
    }
}
