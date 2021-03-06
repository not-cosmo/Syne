import com.leapmotion.leap.*;

import java.io.IOException;
import java.awt.*;
import java.lang.Math;
import java.util.ArrayList;

import com.leapmotion.leap.*;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture.State;



class SampleListener extends Listener {
	
	LetterCheck thumb = new LetterCheck();
	LetterCheck index = new LetterCheck();
	LetterCheck middle = new LetterCheck();
	LetterCheck ring = new LetterCheck();
	LetterCheck pinky = new LetterCheck();
	
    public void onInit(Controller controller) {
        System.out.println("Initialized");
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
        controller.enableGesture(Gesture.Type.TYPE_SWIPE);
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
        controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
        controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
    }

    public void onDisconnect(Controller controller) {
        //Note: not dispatched when running in a debugger.
        System.out.println("Disconnected");
    }

    public void onExit(Controller controller) {
        System.out.println("Exited");
    }

    public void onFrame(Controller controller) {
        // Get the most recent frame and report some basic information
        Frame frame = controller.frame();
        /*System.out.println("Frame id: " + frame.id()
                         + ", timestamp: " + frame.timestamp()
                         + ", hands: " + frame.hands().count()
                         + ", fingers: " + frame.fingers().count()
                         + ", tools: " + frame.tools().count()
                         + ", gestures " + frame.gestures().count());

		*/
        //Get hands
        for(Hand hand : frame.hands()) {
            String handType = hand.isLeft() ? "Left hand" : "Right hand";
            /*System.out.println("  " + handType + ", id: " + hand.id()
                             + ", palm position: " + hand.palmPosition());
            */

            // Get the hand's normal vector and direction
            Vector normal = hand.palmNormal();
            Vector direction = hand.direction();

            // Calculate the hand's pitch, roll, and yaw angles
            /*System.out.println("  pitch: " + Math.toDegrees(direction.pitch()) + " degrees, "
                             + "roll: " + Math.toDegrees(normal.roll()) + " degrees, "
                             + "yaw: " + Math.toDegrees(direction.yaw()) + " degrees");
            */

            // Get arm bone
            Arm arm = hand.arm();
            /*System.out.println("  Arm direction: " + arm.direction()
                             + ", wrist position: " + arm.wristPosition()
                             + ", elbow position: " + arm.elbowPosition());
            */

            // Get fingers
            int counter = 0;
            boolean [] ifAllTrue = new boolean[5];
            String[][] allDataList = new String [5][12];
            int counterFing = 0;
            int counterBone = 0;
            ArrayList<Vector> fingerVector = new ArrayList<Vector>();
            for (Finger finger : hand.fingers()) {
            	
                /*System.out.println("    " + finger.type() + ", id: " + finger.id()
                                 + ", length: " + finger.length()
                                 + "mm, width: " + finger.width() + "mm");
                 */
                

                //Get Bone\s
                for(Bone.Type boneType : Bone.Type.values()) {
                    Bone bone = finger.bone(boneType);
                   /* System.out.println("      " + bone.type()
                                     + " bone, start: " + bone.prevJoint()
                                     + ", end: " + bone.nextJoint()
                                     + ", direction: " + bone.direction());
                    */
                    fingerVector.add(bone.direction());
                    allDataList[counterFing][counterBone] = Float.toString(bone.direction().getX());
                    allDataList[counterFing][counterBone+1] = Float.toString(bone.direction().getY());
                    allDataList[counterFing][counterBone+2] = Float.toString(bone.direction().getZ());
                    counterBone+=3;
                }
                counterFing++;
                counterBone=0;
                if(finger.id()%10==0){
                	if(thumb.checkIfA(finger.type(),fingerVector.get(0),fingerVector.get(1),fingerVector.get(2),fingerVector.get(3))){
                		System.out.println("thumb true");
                		ifAllTrue[0] = true;
                	}else{
                		//System.out.println("thumb false");
                	}
                }else if(finger.id()%10==1){
                	
                	if(index.checkIfA(finger.type(),fingerVector.get(0),fingerVector.get(1),fingerVector.get(2),fingerVector.get(3))){
                		System.out.println("index true");
                		ifAllTrue[1] = true;
                	}else{
                		//System.out.println("index false");
                	}
                }else if(finger.id()%10==2){
                	if(middle.checkIfA(finger.type(),fingerVector.get(0),fingerVector.get(1),fingerVector.get(2),fingerVector.get(3))){
                		System.out.println("middle true");
                		ifAllTrue[2] = true;
                	}else{
                		//System.out.println("middle false");
                	}
                }else if(finger.id()%10==3){
                	if(ring.checkIfA(finger.type(),fingerVector.get(0),fingerVector.get(1),fingerVector.get(2),fingerVector.get(3))){
                		System.out.println("ring true");
                		ifAllTrue[3] = true;
                	}else{
                		//System.out.println("ring false");
                	}
                }else if(finger.id()%10==4){
                	if(pinky.checkIfA(finger.type(),fingerVector.get(0),fingerVector.get(1),fingerVector.get(2),fingerVector.get(3))){
                		System.out.println("pinky true");
                		ifAllTrue[4] = true;
                	}else{
                		//System.out.println("ring false");
                	}
                }
                fingerVector = new ArrayList<Vector>();
                //System.out.println(counter);
                //counter++;
            }
            
           
            for (int i=0; i < ifAllTrue.length; i++) {
            	if (ifAllTrue[i]) {
            		System.out.println(ifAllTrue[i]);
            		counter++;
            	}
            }
            if(counter==5){
           		System.out.println("dsgdsgdfgfdgfdgd");
           	}
            counter = 0;
            ifAllTrue= new boolean[5];
            fingerVector = new ArrayList<Vector>();
        }

        // Get tools
        for(Tool tool : frame.tools()) {
            /*System.out.println("  Tool id: " + tool.id()
                             + ", position: " + tool.tipPosition()
                             + ", direction: " + tool.direction());
            */
        }

        GestureList gestures = frame.gestures();
        for (int i = 0; i < gestures.count(); i++) {
            Gesture gesture = gestures.get(i);

            switch (gesture.type()) {
                case TYPE_CIRCLE:
                	
                    CircleGesture circle = new CircleGesture(gesture);

                    // Calculate clock direction using the angle between circle normal and pointable
                    String clockwiseness;
                    if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI/2) {
                        // Clockwise if angle is less than 90 degrees
                        clockwiseness = "clockwise";
                    } else {
                        clockwiseness = "counterclockwise";
                    }

                    // Calculate angle swept since last frame
                    double sweptAngle = 0;
                    if (circle.state() != State.STATE_START) {
                        CircleGesture previousUpdate = new CircleGesture(controller.frame(1).gesture(circle.id()));
                        sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
                    }

                    /*System.out.println("  Circle id: " + circle.id()
                               + ", " + circle.state()
                               + ", progress: " + circle.progress()
                               + ", radius: " + circle.radius()
                               + ", angle: " + Math.toDegrees(sweptAngle)
                               + ", " + clockwiseness);
                     */
                    break;
                case TYPE_SWIPE:
                    SwipeGesture swipe = new SwipeGesture(gesture);
                    /*System.out.println("  Swipe id: " + swipe.id()
                               + ", " + swipe.state()
                               + ", position: " + swipe.position()
                               + ", direction: " + swipe.direction()
                               + ", speed: " + swipe.speed());
                      */
                    break;
                case TYPE_SCREEN_TAP:
                    ScreenTapGesture screenTap = new ScreenTapGesture(gesture);
                    /*System.out.println("  Screen Tap id: " + screenTap.id()
                               + ", " + screenTap.state()
                               + ", position: " + screenTap.position()
                               + ", direction: " + screenTap.direction());
                     */
                    break;
                case TYPE_KEY_TAP:
                    KeyTapGesture keyTap = new KeyTapGesture(gesture);
                    /*System.out.println("  Key Tap id: " + keyTap.id()
                               + ", " + keyTap.state()
                               + ", position: " + keyTap.position()
                               + ", direction: " + keyTap.direction());
                    */
                    break;
                default:
                    System.out.println("Unknown gesture type.");
                    break;
            }
        }

        if (!frame.hands().isEmpty() || !gestures.isEmpty()) {
            System.out.println();
        }
    }
}

/*public class SpeakApp {
    public static  void main(String[] args) {
        // Create a sample listener and controller
        SampleListener listener = new SampleListener();
        Controller controller = new Controller();

        // Have the sample listener receive events from the controller
        controller.addListener(listener);

        // Keep this process running until Enter is pressed
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Remove the sample listener when done
        controller.removeListener(listener);
    }
}*/
