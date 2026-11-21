package org.firstinspires.ftc.teamcode.pedro;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@Disabled
@Autonomous
public class LymeLight extends OpMode {

    private Limelight3A limelight;

    private DcMotor fl, bl, fr, br;

    double turnPower;
    double forwardPower;

    double targetTx = 0;
    double targetTa = 15;

    double kP = 0.03;
    double kForward = 0.02;

    @Override
    public void init() {

        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        fl = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        bl = hardwareMap.get(DcMotor.class, "backLeftMotor");
        fr = hardwareMap.get(DcMotor.class, "frontRightMotor");
        br = hardwareMap.get(DcMotor.class, "backRightMotor");

        limelight.setPollRateHz(100);
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    @Override
    public void loop() {

        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {

            double tx = result.getTx();
            double ta = result.getTa();

            turnPower = (tx - targetTx) * kP;
            forwardPower = (targetTa - ta) * kForward;

            forwardPower = Range.clip(forwardPower, 0, 0.35);
            turnPower = Range.clip(turnPower, -0.3, 0.3);

            double flPower = forwardPower - turnPower;
            double blPower = forwardPower - turnPower;
            double frPower = forwardPower + turnPower;
            double brPower = forwardPower + turnPower;

            fl.setPower(Range.clip(flPower, -1, 1));
            bl.setPower(Range.clip(blPower, -1, 1));
            fr.setPower(Range.clip(frPower, -1, 1));
            br.setPower(Range.clip(brPower, -1, 1));

            telemetry.addData("Target X", tx);
            telemetry.addData("Target Area", ta);
            telemetry.addData("Turn Power", turnPower);
            telemetry.addData("Forward Power", forwardPower);

        } else {

            stopDrive();

            telemetry.addData("Limelight", "No Targets");
        }

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