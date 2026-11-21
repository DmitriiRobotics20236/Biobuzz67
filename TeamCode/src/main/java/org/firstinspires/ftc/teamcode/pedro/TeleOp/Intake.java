package org.firstinspires.ftc.teamcode.pedro.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
    DcMotorEx motor;
    Servo servo;
    public Intake (HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotorEx.class, "MI");
        motor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);

        servo = hardwareMap.get(Servo.class, "BS");
    }

    public void drive(boolean right_trigger, boolean left_trigger, boolean fix) {
        if (right_trigger && !left_trigger) {
            motor.setPower(-1);
        } else if (!right_trigger && left_trigger) {
            motor.setPower(1);
        } else {
            motor.setPower(0);
        }

        if (fix) {
            servo.setPosition(0.5);
        } else {
            servo.setPosition(0);
        }
    }
}
