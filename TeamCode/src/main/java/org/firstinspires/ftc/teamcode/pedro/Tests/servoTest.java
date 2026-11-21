package org.firstinspires.ftc.teamcode.pedro.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "servo_test_4")
public class servoTest extends LinearOpMode {

    Servo servo1, servo2;

    double currentPosition1 = 0.5;
    double currentPosition2 = 0.5;

    @Override
    public void runOpMode() {
        servo1 = hardwareMap.get(Servo.class, "Servo1");
        servo2 = hardwareMap.get(Servo.class, "Servo2");

        servo1.setDirection(Servo.Direction.REVERSE);

        servo1.setPosition(currentPosition1);
        servo2.setPosition(currentPosition2);

        waitForStart();

        while (opModeIsActive()) {

            double y1 = -gamepad1.left_stick_y;
            double y2 = gamepad1.right_stick_y;

            if (Math.abs(y1) > 0.1) {
                currentPosition1 += y1 * 0.5;
            }

            if (Math.abs(y1) > 0.1) {
                currentPosition2 += y1 * 0.5;
            }

            currentPosition1 = Math.max(0, Math.min(1, currentPosition1));
            currentPosition2 = Math.max(0, Math.min(1, currentPosition2));

            servo1.setPosition(currentPosition1);
            servo2.setPosition(currentPosition2);

            telemetry.addData("Servo 1", currentPosition1);
            telemetry.addData("Servo 2", currentPosition2);
            telemetry.update();

            sleep(20);
        }
    }
}