
package org.firstinspires.ftc.teamcode.pedro.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Shooter_2 {

    public DcMotorEx motor1, motor2;

    private Servo axonServo;

    private double servoPosition = 0.5;
    private static final double STEP = 0.01;

    double currentPosition1 = 0.0;

    public double highVeVelocity = 6000;
    public double lowVelocity = 900;

    double curTargetVelocity = highVeVelocity;
    double F = 0;
    double P = 0;

    double[] stepSizes = {10.0, 1.0, 0.001, 0.0001};

    int stepIndex = 1;

    public Shooter_2(HardwareMap hardwareMap) {
        motor1 = hardwareMap.get(DcMotorEx.class, "motor1");
        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor1.setDirection(DcMotorSimple.Direction.FORWARD);

        motor2 = hardwareMap.get(DcMotorEx.class, "motor2");
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setDirection(DcMotorSimple.Direction.FORWARD);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        motor1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        motor2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        axonServo = hardwareMap.get(Servo.class, "servo1");

        axonServo.setPosition(servoPosition);
    }

    public void updateShooter(boolean y, boolean b, boolean shoot, double st_sh) {

        if (y) {
            if (curTargetVelocity == highVeVelocity) {
                curTargetVelocity = lowVelocity;
            } else {
                curTargetVelocity = highVeVelocity;
            }
        }

        if (b) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }

        if (shoot) {
            F += stepSizes[stepIndex];
            P += stepSizes[stepIndex];
        } else {
            F -= stepSizes[stepIndex];
            P -= stepSizes[stepIndex];
        }

        axonServo.setPosition(st_sh);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);

        motor1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        motor2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        motor1.setVelocity(curTargetVelocity);
        motor2.setVelocity(curTargetVelocity);

        servoPosition = Math.max(0.0, Math.min(1.0, servoPosition));

        axonServo.setPosition(servoPosition);

        double curVelocity = motor1.getVelocity();
        double error = curTargetVelocity - curVelocity;
    }
}
