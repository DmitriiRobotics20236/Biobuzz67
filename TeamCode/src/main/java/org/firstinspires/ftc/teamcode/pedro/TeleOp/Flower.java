package org.firstinspires.ftc.teamcode.pedro.TeleOp;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Flower {
    Servo servo1, servo2;

    public double currentPosition1 = 0.5;
    public double currentPosition2 = 0.5;

    double targetPosition1 = 0.0;
    double targetPosition2 = 0.0;



    public Flower(HardwareMap hardwareMap) {
        servo1 = hardwareMap.get(Servo.class, "SFL");
        servo2 = hardwareMap.get(Servo.class, "SFR");

        servo1.setDirection(Servo.Direction.REVERSE);

        servo1.setPosition(currentPosition1);
        servo2.setPosition(currentPosition2);
    }


    public void drive(boolean sost) {
        if (sost) {
            targetPosition1 = 0.5;
            targetPosition2 = 0.5;
        } else {
            targetPosition1 = 0.0;
            targetPosition2 = 0.0;
        }

        servo1.setPosition(targetPosition1);
        servo2.setPosition(targetPosition2);

        currentPosition1 = targetPosition1;
        currentPosition2 = targetPosition2;
    }
}