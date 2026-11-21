package org.firstinspires.ftc.teamcode.pedro.Shoot;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@Configurable
public class Test_SH {

    private DcMotorEx motor1;
    private DcMotorEx motor2;
    private Servo axonServo;

    private static final double TICKS_PER_REV = 28.0;

    public static double highVeVelocity = 4200;
    public static double lowVelocity = 900;

    public static double P = 24;
    public static double F = 2;

    public static double step1 = 10.0;
    public static double step2 = 1.0;
    public static double step3 = 0.001;
    public static double step4 = 0.0001;

    public static double SERVO_STEP = 0.015;

    private double currentStep = step1;
    private int stepIndex = 0;

    private double targetVelocity = 0;
    private double servoPosition = 0.5;

    private boolean highSpeedMode = false;
    private boolean previousY = false;
    private boolean previousB = false;

    private double appliedP = Double.NaN;
    private double appliedF = Double.NaN;

    public void init(HardwareMap hardwareMap) {

        motor1 = hardwareMap.get(DcMotorEx.class, "motor1");
        motor2 = hardwareMap.get(DcMotorEx.class, "motor2");

        axonServo = hardwareMap.get(Servo.class, "servo1");

        // Направления моторов
        motor1.setDirection(DcMotor.Direction.REVERSE);
        motor2.setDirection(DcMotor.Direction.FORWARD);

        motor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        motor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        axonServo.setPosition(servoPosition);
    }

    public void updateShooter(Gamepad gamepad, Telemetry telemetry) {

        // Переключение скорости кнопкой Y
        if (gamepad.y && !previousY) {
            highSpeedMode = !highSpeedMode;

            targetVelocity = highSpeedMode
                    ? highVeVelocity
                    : lowVelocity;
        }

        previousY = gamepad.y;

        // Переключение шага настройки кнопкой B
        if (gamepad.b && !previousB) {

            stepIndex = (stepIndex + 1) % 4;

            switch (stepIndex) {
                case 0: currentStep = step1; break;
                case 1: currentStep = step2; break;
                case 2: currentStep = step3; break;
                case 3: currentStep = step4; break;
            }
        }

        previousB = gamepad.b;

        // Настройка P и F
        if (gamepad.dpad_up) P += currentStep;
        if (gamepad.dpad_down) P -= currentStep;

        if (gamepad.dpad_right) F += currentStep;
        if (gamepad.dpad_left) F -= currentStep;

        // Применяем PIDF к обоим моторам
        if (P != appliedP || F != appliedF) {

            PIDFCoefficients pidf =
                    new PIDFCoefficients(P, 0, 0, F);

            motor1.setPIDFCoefficients(
                    DcMotor.RunMode.RUN_USING_ENCODER, pidf);

            motor2.setPIDFCoefficients(
                    DcMotor.RunMode.RUN_USING_ENCODER, pidf);

            appliedP = P;
            appliedF = F;
        }

        // Перевод RPM в ticks/sec
        double targetTicks = targetVelocity * TICKS_PER_REV / 60.0;

        // Одинаковая скорость для двух моторов
        motor1.setVelocity(targetTicks);
        motor2.setVelocity(targetTicks);

        // Управление сервоприводом
        if (gamepad.right_trigger > 0.1) {
            servoPosition += SERVO_STEP;
        }

        if (gamepad.left_trigger > 0.1) {
            servoPosition -= SERVO_STEP;
        }

        servoPosition = Math.max(0, Math.min(1, servoPosition));
        axonServo.setPosition(servoPosition);

        // Общая телеметрия
        telemetry.addData("Target RPM", targetVelocity);

        telemetry.addData("Motor 1 RPM", "%.0f", getRPM(motor1));
        telemetry.addData("Motor 2 RPM", "%.0f", getRPM(motor2));

        telemetry.addData("Motor 1 Encoder", motor1.getCurrentPosition());
        telemetry.addData("Motor 2 Encoder", motor2.getCurrentPosition());

        telemetry.addData("Motor 1 Velocity", motor1.getVelocity());
        telemetry.addData("Motor 2 Velocity", motor2.getVelocity());

        telemetry.addData("Motor 1 Current A", "%.2f",
                motor1.getCurrent(CurrentUnit.AMPS));

        telemetry.addData("Motor 2 Current A", "%.2f",
                motor2.getCurrent(CurrentUnit.AMPS));

        telemetry.addData("Motor 1 Power", "%.2f", motor1.getPower());
        telemetry.addData("Motor 2 Power", "%.2f", motor2.getPower());

        telemetry.addData("P", P);
        telemetry.addData("F", F);
        telemetry.addData("Step", currentStep);
        telemetry.addData("Servo", servoPosition);
    }

    private double getRPM(DcMotorEx motor) {
        return motor.getVelocity() * 60.0 / TICKS_PER_REV;
    }

    public void stop() {
        if (motor1 != null) motor1.setVelocity(0);
        if (motor2 != null) motor2.setVelocity(0);
    }
}
