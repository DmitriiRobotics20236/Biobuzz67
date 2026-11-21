package org.firstinspires.ftc.teamcode.pedro;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;


@TeleOp(name = "Flywheel Tuner", group = "Tuning")
public class FlywheelTunerTutorial extends OpMode {

    private DcMotorEx flywheelMotor1;
    private DcMotorEx flywheelMotor2;
    private Servo servo1;

    private double currentPosition1 = 0.0;

    // Target speeds are in RPM
    private final double highVelocityRPM = 6000.0;
    private final double lowVelocityRPM = 900.0;
    private double targetVelocityRPM = highVelocityRPM;

    // Tune these values on the robot; do not assume these are optimal.
    private double P = 0.0;
    private double F = 0.0;

    private final double[] stepSizes = {10.0, 1.0, 0.001, 0.0001};
    private int stepIndex = 1;

    @Override
    public void init() {
        flywheelMotor1 = hardwareMap.get(DcMotorEx.class, "motor1");
        flywheelMotor2 = hardwareMap.get(DcMotorEx.class, "motor2");

        flywheelMotor1.setDirection(DcMotorSimple.Direction.FORWARD);
        flywheelMotor2.setDirection(DcMotorSimple.Direction.FORWARD);

        flywheelMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheelMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Keep the motors from running while initializing.
        flywheelMotor1.setPower(0.0);
        flywheelMotor2.setPower(0.0);

        applyPIDF();

        servo1 = hardwareMap.get(Servo.class, "servo1");
        servo1.setPosition(currentPosition1);

        telemetry.addLine("Init complete");
        telemetry.addLine("Y: switch target RPM (6000 / 900)");
        telemetry.addLine("DPAD left/right: F -/+ | DPAD down/up: P -/+");
        telemetry.addLine("B: change tuning step | Right trigger: servo position");
        telemetry.update();
    }

    private void applyPIDF() {
        PIDFCoefficients coefficients = new PIDFCoefficients(P, 0.0, 0.0, F);
        flywheelMotor1.setPIDFCoefficients(
                DcMotor.RunMode.RUN_USING_ENCODER, coefficients);
        flywheelMotor2.setPIDFCoefficients(
                DcMotor.RunMode.RUN_USING_ENCODER, coefficients);
    }

    @Override
    public void loop() {
        if (gamepad1.yWasPressed()) {
            targetVelocityRPM =
                    (targetVelocityRPM == highVelocityRPM)
                            ? lowVelocityRPM
                            : highVelocityRPM;
        }

        if (gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }

        double step = stepSizes[stepIndex];

        if (gamepad1.dpadLeftWasPressed()) {
            F = Math.max(0.0, F - step);
        }
        if (gamepad1.dpadRightWasPressed()) {
            F += step;
        }
        if (gamepad1.dpadDownWasPressed()) {
            P = Math.max(0.0, P - step);
        }
        if (gamepad1.dpadUpWasPressed()) {
            P += step;
        }

        currentPosition1 = gamepad1.right_trigger;
        servo1.setPosition(currentPosition1);

        // Changing PIDF coefficients while tuning
        applyPIDF();

        // DcMotorEx velocity in AngleUnit.DEGREES is degrees per second.
        // 1 RPM = 6 degrees/second.
        double targetDegreesPerSecond = targetVelocityRPM * 6.0;

        flywheelMotor1.setVelocity(targetDegreesPerSecond, AngleUnit.DEGREES);
        flywheelMotor2.setVelocity(targetDegreesPerSecond, AngleUnit.DEGREES);

        double motor1RPM =
                flywheelMotor1.getVelocity(AngleUnit.DEGREES) / 6.0;
        double motor2RPM =
                flywheelMotor2.getVelocity(AngleUnit.DEGREES) / 6.0;

        double error1RPM = targetVelocityRPM - motor1RPM;
        double error2RPM = targetVelocityRPM - motor2RPM;

        telemetry.addData("Target RPM", "%.0f", targetVelocityRPM);
        telemetry.addData("Motor 1 RPM", "%.0f", motor1RPM);
        telemetry.addData("Motor 1 Error RPM", "%.0f", error1RPM);
        telemetry.addData("Motor 2 RPM", "%.0f", motor2RPM);
        telemetry.addData("Motor 2 Error RPM", "%.0f", error2RPM);
        telemetry.addData("P", "%.6f", P);
        telemetry.addData("F", "%.6f", F);
        telemetry.addData("Step", "%.4f", step);
        telemetry.addData("Servo Position", "%.2f", currentPosition1);
        telemetry.update();
    }

    @Override
    public void stop() {
        if (flywheelMotor1 != null) {
            flywheelMotor1.setPower(0.0);
        }
        if (flywheelMotor2 != null) {
            flywheelMotor2.setPower(0.0);
        }
    }
}
