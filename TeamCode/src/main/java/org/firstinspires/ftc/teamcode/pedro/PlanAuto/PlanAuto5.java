package org.firstinspires.ftc.teamcode.pedro.PlanAuto;

import static com.pedropathing.api.Paths.curve;
import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name = "PlanAuto5")
public class PlanAuto5 extends OpMode {

    private Follower follower;

    private final PoseFactory p = PoseFactory.degrees();

    private final Pose startPose = p.of(56, 8, 90);
    private final Pose scorePose1 = p.of(9, 47, 180);
    private final Pose scorePose2 = p.of(58, 116, -90);
    private final Pose scorePose3 = p.of(47, 113, 90);
    private final Pose scorePose4 = p.of(60, 117, 90);
    private final Pose parkPose = p.of(10, 117, 90);

    private final Pose controlPose1 = p.of(14, 96, 90);
    private Path Score1() {
        return line(startPose, scorePose1)
                .linear(startPose, scorePose1)
                .with(
                        Constants.foresightConfig.maxPathSpeed.at(0.55),
                        Constants.foresightConfig.brakeAggression.at(0.90),
                        Constants.foresightConfig.brakeAtEnd.at(true)
                );
    }

    private Path Score2() {
        return curve(scorePose1, controlPose1, scorePose2)
                .linear(scorePose1, scorePose2)
                .with(
                        Constants.foresightConfig.maxPathSpeed.at(0.55),
                        Constants.foresightConfig.brakeAggression.at(0.90),
                        Constants.foresightConfig.brakeAtEnd.at(true)
                );
    }

    private Path Score3() {
        return line(scorePose2, scorePose3)
                .linear(scorePose2, scorePose3)
                .with(
                        Constants.foresightConfig.maxPathSpeed.at(0.55),
                        Constants.foresightConfig.brakeAggression.at(0.90),
                        Constants.foresightConfig.brakeAtEnd.at(true)
                );
    }

    private Path Score4() {
        return line(scorePose3, scorePose4)
                .linear(scorePose3, scorePose4)
                .with(
                        Constants.foresightConfig.maxPathSpeed.at(0.55),
                        Constants.foresightConfig.brakeAggression.at(0.90),
                        Constants.foresightConfig.brakeAtEnd.at(true)
                );
    }

    private Path Score5() {
        return line(scorePose4, parkPose)
                .linear(scorePose4, parkPose)
                .with(
                        Constants.foresightConfig.maxPathSpeed.at(0.55),
                        Constants.foresightConfig.brakeAggression.at(0.90),
                        Constants.foresightConfig.brakeAtEnd.at(true)
                );
    }





    private Command autoRoutine() {
        return sequential(
                follow(follower, Score1()),
                follow(follower, Score2()),
                follow(follower, Score3()),
                follow(follower, Score4()),
                follow(follower, Score5())
        );
    }

    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();
    }

    @Override
    public void start() {
        schedule(autoRoutine());
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData(
                "Heading",
                Math.toDegrees(follower.pose().heading())
        );
        telemetry.addData("Follower Mode", follower.mode());

        telemetry.update();
    }
}

