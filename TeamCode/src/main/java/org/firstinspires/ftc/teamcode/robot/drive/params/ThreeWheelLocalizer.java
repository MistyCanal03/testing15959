package org.firstinspires.ftc.teamcode.robot.drive.params;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.robot.drive.DrivetrainController;
import org.firstinspires.ftc.teamcode.util.Encoder;
import org.firstinspires.ftc.teamcode.util.MockDcMotorEx;

import java.util.Arrays;
import java.util.List;

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    /--------------\
 *    |     ____     |
 *    |     ----     |
 *    | ||        || |
 *    | ||        || |
 *    |              |
 *    |              |
 *    \--------------/
 *
 */
@Config
public class ThreeWheelLocalizer extends ThreeTrackingWheelLocalizer {
    public static double TICKS_PER_REV = 8192;
    public static double WHEEL_RADIUS = 0.6889764; // in (35mm/2 = 17.5mm)
    public static double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    public static double LATERAL_DISTANCE = 16.4691936; // in; distance between the left and right odometry wheels (prev: 16.4559389109)

    public static double FORWARD_OFFSET = -0.24; // in; offset of the lateral wheel (about 0.5cm back)

    public static double LEFT_MULTIPLIER = 1.0038088556; //left encoder multiplier
    public static double RIGHT_MULTIPLIER = 1.0038088556; //right encoder multiplier
    public static double LATERAL_MULTIPLIER = 0.9978140079; // Multiplier in the Y direction (strafe)
    //TODO: Tune X/Y Multiplier for error (https://www.learnroadrunner.com/dead-wheels.html#tuning-three-wheel)

    /**
     * Options for inaccurate heading
     * Retune everyhing
     * Lower speed/acceleration!!!
     * Tune LEFT_MULTIPLIER
     * Use splines that turn instead of direct turns
     * Tune HEADING_PID, more aggresive
     * Use Hub IMU to correct for heading error
     * Use vision targets to correct
     * Try two wheel odometry with IMU
     */
    private Encoder leftEncoder, rightEncoder, frontEncoder;

    public ThreeWheelLocalizer(HardwareMap hardwareMap) {
        super(Arrays.asList(
                new Pose2d(0, LATERAL_DISTANCE / 2, 0), // left
                new Pose2d(0, -LATERAL_DISTANCE / 2, 0), // right
                new Pose2d(FORWARD_OFFSET, 0, Math.toRadians(90)) // front
        ));

        if (DrivetrainController.TESTING) {
            leftEncoder = new Encoder(new MockDcMotorEx("left_rear"));
            rightEncoder = new Encoder(new MockDcMotorEx("right_rear"));
            frontEncoder = new Encoder(new MockDcMotorEx("right_front"));
        } else {
            leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "intake")); //Original: left_rear
            rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "intake2")); //Original: right_rear
            frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "rear_intake")); //Original: right_front
        }

        frontEncoder.setDirection(Encoder.Direction.REVERSE);
        leftEncoder.setDirection(Encoder.Direction.REVERSE);

    }

    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCurrentPosition()) * LEFT_MULTIPLIER,
                encoderTicksToInches(rightEncoder.getCurrentPosition()) * RIGHT_MULTIPLIER,
                encoderTicksToInches(frontEncoder.getCurrentPosition()) * LATERAL_MULTIPLIER
        );
    }

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {

        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCorrectedVelocity()) * LEFT_MULTIPLIER,
                encoderTicksToInches(rightEncoder.getCorrectedVelocity()) * LEFT_MULTIPLIER,
                encoderTicksToInches(frontEncoder.getCorrectedVelocity()) * LATERAL_MULTIPLIER
        );
    }
}
