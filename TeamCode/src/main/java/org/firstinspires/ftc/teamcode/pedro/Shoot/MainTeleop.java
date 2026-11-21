package org.firstinspires.ftc.teamcode.pedro.Shoot;





import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.pedro.Shoot.Test_SH;
import org.firstinspires.ftc.teamcode.pedro.Shoot.Vat;

@TeleOp(name = "MainTeleOp", group = "TeleOp")
public class MainTeleop extends LinearOpMode {

    private Test_SH shooter;
    private Vat vat;

    @Override
    public void runOpMode() {

        shooter = new Test_SH();
        vat = new Vat(hardwareMap);

        shooter.init(hardwareMap);

        VoltageSensor battery =
                hardwareMap.voltageSensor.iterator().next();

        telemetry.addLine("MainTeleOp initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // SHOOTER — Gamepad 1
            shooter.updateShooter(gamepad1, telemetry);

            // INTAKE — Gamepad 2
            vat.drive(
                    gamepad2.right_trigger > 0.1,
                    gamepad2.left_trigger > 0.1,
                    gamepad2.a
            );

            // Напряжение аккумулятора
            telemetry.addData(
                    "Battery Voltage",
                    "%.2f V",
                    battery.getVoltage()
            );

            // Ток Intake
            telemetry.addData(
                    "Intake Current",
                    "%.2f A",
                    vat.getCurrentAmps()
            );

            // Мощность Intake
            telemetry.addData(
                    "Intake Power",
                    "%.2f",
                    vat.getPower()
            );

            telemetry.update();
        }

        shooter.stop();
        vat.stop();
    }
}


