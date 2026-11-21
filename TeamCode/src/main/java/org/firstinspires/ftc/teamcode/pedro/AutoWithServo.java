package org.firstinspires.ftc.teamcode.pedro;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;

import com.pedropathing.follower.Follower;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import com.pedropathing.ivy.Scheduler;
import com.pedropathing.ivy.Command;

import static com.pedropathing.api.Paths.*;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;

@Disabled
@Autonomous
public class AutoWithServo extends OpMode {

    private Follower follower;
    private Servo axon;

    private final PoseFactory p = PoseFactory.degrees();

    // Poses
    private final Pose startPose = p.of(56, 8, 90);
    private final Pose scorePose = p.of(16, 20, 180);
    private final Pose parkPose = p.of(12, 92, 180);

    // Положения Axon
    private static final double AXON_START = 0.0;
    private static final double AXON_SCORE = 0.8;

    // Пути
    private Path startToScore() {
        return line(startPose, scorePose)
                .linear(startPose, scorePose);
    }

    private Path park() {
        return line(scorePose, parkPose)
                .linear(scorePose, parkPose);
    }

    // Автоном
    private Command autoRoutine() {
        return sequential(

                // 1. Едем со старта к score
                follow(follower, startToScore()),

                // 2. Поворачиваем Axon
                instant(() -> axon.setPosition(AXON_SCORE)),

                // 3. Ждём 0.5 секунды
                waitMs(3000),

                // 4. Возвращаем Axon
                instant(() -> axon.setPosition(AXON_START)),

                // 5. Едем парковаться
                follow(follower, park())
        );
    }

    @Override
    public void init() {

        Scheduler.reset();

        // Создаём Follower
        follower = Constants.create(hardwareMap);

        // Получаем сервопривод из конфигурации FTC
        axon = hardwareMap.get(Servo.class, "axon");

        // Начальное положение Axon
        axon.setPosition(AXON_START);

        // Начальная позиция робота
        follower.setPose(startPose);
        follower.update();

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Axon", axon.getPosition());
        telemetry.update();
    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    @Override
    public void loop() {

        // Обновляем Pedro
        follower.update();

        // Обновляем Ivy
        Scheduler.execute();

        // Telemetry
        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData(
                "Heading",
                Math.toDegrees(follower.pose().heading())
        );

        telemetry.addData("Follower Mode", follower.mode());
        telemetry.addData("Axon Position", axon.getPosition());

        telemetry.update();
    }
}

