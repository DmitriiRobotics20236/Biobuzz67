
package org.firstinspires.ftc.teamcode.pedro;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.List;


@Autonomous(name = "AprilTag Tracking")
public class ZaebSAprilTegom extends OpMode {

    private Limelight3A limelight;
    private DcMotor fl, bl, fr, br;

    private final int targetTagId = 21;

    private final double power = 0.35;
    private final double deadband = 1.0;

    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        fl = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        bl = hardwareMap.get(DcMotor.class, "backLeftMotor");
        fr = hardwareMap.get(DcMotor.class, "frontRightMotor");
        br = hardwareMap.get(DcMotor.class, "backRightMotor");

        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.FORWARD);
        br.setDirection(DcMotorSimple.Direction.FORWARD);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(0);
    }

    @Override
    public void start() {
        limelight.start();
    }

    @Override
    public void loop() {
        LLResult result = limelight.getLatestResult();

        if (result == null || !result.isValid()) {
            stopDrive();
            telemetry.addData("Limelight", "No valid result");
            telemetry.update();
            return;
        }

        List<LLResultTypes.FiducialResult> tags =
                result.getFiducialResults();

        LLResultTypes.FiducialResult target = null;

        for (LLResultTypes.FiducialResult tag : tags) {
            if (tag.getFiducialId() == targetTagId) {
                target = tag;
                break;
            }
        }

        if (target == null) {
            stopDrive();
            telemetry.addData("AprilTag", "Target not found");
            telemetry.addData("Required ID", targetTagId);
            telemetry.addData("Visible tags", tags.size());
            telemetry.update();
            return;
        }

        double tx = target.getTargetXDegrees();
        double turnPower = 0.0;

        if (tx > deadband) {
            turnPower = power;
        } else if (tx < -deadband) {
            turnPower = -power;
        }

        double flPower = turnPower;
        double blPower = -turnPower;
        double frPower = turnPower;
        double brPower = -turnPower;

        fl.setPower(flPower);
        bl.setPower(blPower);
        fr.setPower(frPower);
        br.setPower(brPower);

        telemetry.addData("AprilTag ID", target.getFiducialId());
        telemetry.addData("TX", tx);
        telemetry.addData("Strafe Power", turnPower);
        telemetry.addData(
                "Status",
                Math.abs(tx) <= deadband ? "Centered" : "Tracking"
        );

        telemetry.update();
    }

    private void stopDrive() {
        fl.setPower(0);
        bl.setPower(0);
        fr.setPower(0);
        br.setPower(0);
    }

    @Override
    public void stop() {
        stopDrive();
        limelight.stop();
    }
}
