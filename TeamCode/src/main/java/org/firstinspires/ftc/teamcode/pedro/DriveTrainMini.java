package org.firstinspires.ftc.teamcode.pedro;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@Disabled
@TeleOp(name = "m_b_u", group = "Tests")
public class DriveTrainMini extends LinearOpMode {

    private DcMotor leftL_motor;
    private DcMotor rightL_motor;
    private DcMotor leftR_motor;
    private DcMotor rightR_motor;

    private GoBildaPinpointDriver pinpoint;

    @Override
    public void runOpMode() throws InterruptedException {

        leftL_motor = hardwareMap.get(DcMotor.class, "frontLeftMotor");

        rightL_motor = hardwareMap.get(DcMotor.class, "frontRightMotor");

        leftR_motor = hardwareMap.get(DcMotor.class, "backLeftMotor");

        rightR_motor = hardwareMap.get(
                DcMotor.class,
                "backRightMotor"
        );

        pinpoint = hardwareMap.get(
                GoBildaPinpointDriver.class,
                "pinpoint"
        );

        leftL_motor.setDirection(DcMotor.Direction.REVERSE);
        rightL_motor.setDirection(DcMotor.Direction.FORWARD);

        leftR_motor.setDirection(DcMotor.Direction.REVERSE);
        rightR_motor.setDirection(DcMotor.Direction.FORWARD);

        leftL_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightL_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftR_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightR_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftL_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightL_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftR_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightR_motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        ElapsedTime time = new ElapsedTime();

        waitForStart();

        while (opModeIsActive()) {

            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;

            double leftL = y + x + rx;
            double leftR = y - x + rx;

            double rightL = y - x - rx;
            double rightR = y + x - rx;

        }
    }
}