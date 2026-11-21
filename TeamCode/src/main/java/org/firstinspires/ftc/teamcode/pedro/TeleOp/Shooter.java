package org.firstinspires.ftc.teamcode.pedro.TeleOp;


import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;


public class Shooter {

    public DcMotorEx motor1;
    public DcMotorEx motor2;

    private Servo axonServo;

    private double servoPosition = 0.5;

    private static final double SERVO_STEP = 0.015;

    public static double P = 0;
    public static double F = 0;

    public static double highVeVelocity = 6000;
    public static double lowVelocity = 900;

    public static double step1 = 10.0;
    public static double step2 = 1.0;
    public static double step3 = 0.001;
    public static double step4 = 0.0001;

    private int stepIndex = 1;
    private double curTargetVelocity;

    private double lastP = P;
    private double lastF = F;

    public Shooter(HardwareMap hardwareMap) {

        motor1 = hardwareMap.get(DcMotorEx.class, "motor1");
        motor2 = hardwareMap.get(DcMotorEx.class, "motor2");

        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motor1.setDirection(DcMotorSimple.Direction.FORWARD);
        motor2.setDirection(DcMotorSimple.Direction.FORWARD);

        axonServo = hardwareMap.get(Servo.class, "servo1");
        axonServo.setPosition(servoPosition);

        curTargetVelocity = highVeVelocity;

        updatePIDF();
    }

    public void updateShooter(Gamepad gamepad1, Telemetry telemetry) {

        if (gamepad1.yWasPressed()) {
            if (curTargetVelocity == highVeVelocity) {
                curTargetVelocity = lowVelocity;
            } else {
                curTargetVelocity = highVeVelocity;
            }
        }

        if (gamepad1.bWasPressed()) {
            stepIndex++;

            if (stepIndex > 3) {
                stepIndex = 0;
            }
        }

        if (gamepad1.dpadLeftWasPressed()) {
            F -= getCurrentStep();
        }

        if (gamepad1.dpadRightWasPressed()) {
            F += getCurrentStep();
        }

        if (gamepad1.dpadUpWasPressed()) {
            P += getCurrentStep();
        }

        if (gamepad1.dpadDownWasPressed()) {
            P -= getCurrentStep();
        }

        if (P != lastP || F != lastF) {
            updatePIDF();

            lastP = P;
            lastF = F;
        }

        motor1.setVelocity(curTargetVelocity);
        motor2.setVelocity(curTargetVelocity);

        if (gamepad1.right_trigger > 0.1) {
            servoPosition += SERVO_STEP;
        } else if (gamepad1.left_trigger > 0.1) {
            servoPosition -= SERVO_STEP;
        }

        servoPosition = Math.max(
                0.0,
                Math.min(1.0, servoPosition)
        );

        axonServo.setPosition(servoPosition);

        double currentVelocity = motor1.getVelocity();
        double error = curTargetVelocity - currentVelocity;

        telemetry.addData("Target Velocity", curTargetVelocity);
        telemetry.addData("Current Velocity", currentVelocity);
        telemetry.addData("Error", error);

        telemetry.addData("P", P);
        telemetry.addData("F", F);
        telemetry.addData("Step", getCurrentStep());

        telemetry.addData("Servo Position", "%.2f", servoPosition);

        telemetry.addData("Right Trigger", gamepad1.right_trigger);
        telemetry.addData("Left Trigger", gamepad1.left_trigger);
    }

    private void updatePIDF() {

        PIDFCoefficients pidfCoefficients =
                new PIDFCoefficients(P, 0, 0, F);

        motor1.setPIDFCoefficients(
                DcMotor.RunMode.RUN_USING_ENCODER,
                pidfCoefficients
        );

        motor2.setPIDFCoefficients(
                DcMotor.RunMode.RUN_USING_ENCODER,
                pidfCoefficients
        );
    }

    private double getCurrentStep() {

        if (stepIndex == 0) {
            return step1;
        }

        if (stepIndex == 1) {
            return step2;
        }

        if (stepIndex == 2) {
            return step3;
        }

        return step4;
    }
}