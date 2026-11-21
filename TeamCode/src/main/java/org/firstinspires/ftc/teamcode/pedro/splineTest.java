package org.firstinspires.ftc.teamcode.pedro;

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
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Disabled
@Autonomous
public class splineTest extends OpMode {
    private Follower follower; // you added this before
    private final PoseFactory p = PoseFactory.degrees(); // the factory we added before, we are still at the top of the class
    // our poses
    private final Pose startPose = p.of(56, 8, 90);
    private final Pose scorePose1 = p.of(14, 47, 180);
    private final Pose scorePose2 = p.of(7, 47, 180);
    private final Pose scorePose3 = p.of(56, 8, 90);
    private final Pose parkPose = p.of(11, 95, 90);


    private final Pose controlPose1 = p.of(40, 43, 45);
    private final Pose controlPose2 = p.of(27, 30, 45);


    //poses from before
    private Path startToScore() {
        return line(startPose, scorePose1).linear(startPose, scorePose1);
    }

    private Path Score2() {
        return line(scorePose1, scorePose2).linear(scorePose1, scorePose2);
    }

    private Path Score3() {
        return curve(scorePose2, controlPose1, scorePose3).linear(scorePose2, scorePose3);
    }

    private Path park() {
        return curve(scorePose3, controlPose2, parkPose).linear(scorePose3, parkPose);
    }

    private Command autoRoutine() {
        return sequential(
                follow(follower, startToScore()),
                follow(follower, Score2()),
                follow(follower, Score3()),
                follow(follower, park())
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
        // add your other methods needed in the loop here

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        telemetry.update();
    }
}