package org.firstinspires.ftc.teamcode.pedro.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Koleska {
    DcMotor fl, bl, fr, br;
    public Koleska (HardwareMap hardwareMap) {

        fl = hardwareMap.get(DcMotor.class, "front_left");
        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        bl = hardwareMap.get(DcMotor.class, "back_left");
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        br = hardwareMap.get(DcMotor.class, "back_right");
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        fr = hardwareMap.get(DcMotor.class, "front_right");
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void drive(double forward, double strare, double turn) {
        double flPower = forward + strare - turn;
        double blPower = forward - strare - turn;
        double brPower = forward + strare + turn;
        double frPower = forward - strare + turn;

        double maxPower = Math.max(Math.max(Math.abs(flPower), Math.abs(blPower)),
                Math.max(Math.abs(brPower), Math.abs(frPower)));
        if (maxPower > 1) {
            flPower /= maxPower;
            blPower /= maxPower;
            brPower /= maxPower;
            frPower /= maxPower;
        }

        fl.setPower(flPower);
        bl.setPower(blPower);
        br.setPower(brPower);
        fr.setPower(frPower);
    }
}
