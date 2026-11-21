package org.firstinspires.ftc.teamcode.pedro.TeleOp;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Turel {
    CRServo servo1, servo2;

    public Turel(HardwareMap hardwareMap) {
        servo1 = hardwareMap.get(CRServo.class, "STL");
        servo2 = hardwareMap.get(CRServo.class, "STR");
    }

    public void drive(boolean leftBumper, boolean rightBumper, boolean block) {
        if (leftBumper) {
            servo1.setPower(-1);
            servo2.setPower(-1);
        } else if (rightBumper) {
            servo1.setPower(1);
            servo2.setPower(1);
        } else {
            servo1.setPower(0.0);
            servo2.setPower(0.0);
        }

        if (block) {
            servo1.setPower(0.02);
            servo2.setPower(-0.02);
        }
    }
}