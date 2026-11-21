package org.firstinspires.ftc.teamcode.pedro.Shoot;







import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class Vat {

    private DcMotorEx motor;
    private Servo servo;

    public Vat(HardwareMap hardwareMap) {

        motor = hardwareMap.get(DcMotorEx.class, "MI");

        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);

        servo = hardwareMap.get(Servo.class, "BS");
    }

    public void drive(
            boolean rightTrigger,
            boolean leftTrigger,
            boolean fix
    ) {

        if (rightTrigger && !leftTrigger) {
            motor.setPower(-1);

        } else if (!rightTrigger && leftTrigger) {
            motor.setPower(1);

        } else {
            motor.setPower(0);
        }

        servo.setPosition(fix ? 0.5 : 0);
    }

    public double getCurrentAmps() {
        return motor.getCurrent(CurrentUnit.AMPS);
    }

    public double getPower() {
        return motor.getPower();
    }

    public void stop() {
        motor.setPower(0);
    }
}

