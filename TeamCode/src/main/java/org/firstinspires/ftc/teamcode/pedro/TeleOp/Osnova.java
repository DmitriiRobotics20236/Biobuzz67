package org.firstinspires.ftc.teamcode.pedro.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Osnova")
public class Osnova extends OpMode {

    Shooter shooter;
    Intake intake;
    Koleska koleska;
    Turel turel;
    Flower flower;

    boolean intakeOn = false;
    boolean previousX = false;
    boolean sostFlower = false;

    @Override
    public void init() {

    //    telemetry = new MultipleTelemetry(
      //          telemetry,
        //        FtcDashboard.getInstance().getTelemetry()
       // );

        shooter = new Shooter(hardwareMap);
        intake = new Intake(hardwareMap);
        koleska = new Koleska(hardwareMap);
        turel = new Turel(hardwareMap);
        flower = new Flower(hardwareMap);
    }

    @Override
    public void loop() {

        shooter.updateShooter(gamepad1, telemetry);

        boolean currentX = gamepad1.x;

        if (currentX && !previousX) {
            intakeOn = !intakeOn;
        }

        previousX = currentX;

        intake.drive(
                gamepad1.right_bumper,
                gamepad1.left_bumper,
                gamepad1.dpad_left
        );

        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = -gamepad1.right_stick_x;

        koleska.drive(y, x, rx);

        turel.drive(
                gamepad1.left_bumper,
                gamepad1.right_bumper,
                gamepad1.aWasPressed()
        );

        if (gamepad1.xWasPressed()) {
            sostFlower = !sostFlower;
            flower.drive(sostFlower);
        }

        telemetry.addData("position1", flower.currentPosition1);
        telemetry.addData("position2", flower.currentPosition2);
        telemetry.addData("Intake On", intakeOn);

        telemetry.update();
    }
}