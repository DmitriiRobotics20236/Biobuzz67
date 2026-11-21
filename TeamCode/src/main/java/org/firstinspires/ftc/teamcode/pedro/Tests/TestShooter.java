package org.firstinspires.ftc.teamcode.pedro.Tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "ShooterOsnova")
public class TestShooter extends LinearOpMode {

    DcMotorEx motor1, motor2;

    @Override
    public void runOpMode() throws InterruptedException {

        motor1 = hardwareMap.get(DcMotorEx.class, "Motor1");
        motor2 = hardwareMap.get(DcMotorEx.class, "Motor2");

        motor1.setDirection(DcMotor.Direction.FORWARD);
        motor2.setDirection(DcMotor.Direction.FORWARD);

        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        while (opModeIsActive()) {

            double x = gamepad1.left_stick_y;

            motor1.setPower(x);
            motor2.setPower(x);

            double ticksPerSecond1 = motor1.getVelocity();
            double ticksPerSecond2 = motor2.getVelocity();

            double rps1 = ticksPerSecond1 / 28.0;
            double rps2 = ticksPerSecond2 / 28.0;

            telemetry.addData("Скорость (RPS)1", rps1);
            telemetry.addData("Скорость (RPS)2", rps2);
            telemetry.update();
        }
    }
}