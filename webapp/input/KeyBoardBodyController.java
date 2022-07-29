package org.shikhar.simphy.input;

import com.jogamp.newt.event.KeyEvent;

import org.dyn4j.geometry.Vector2;
import org.shikhar.simphy.Preferences;
import org.shikhar.simphy.Simphy;
import org.shikhar.simphy.dynamics.SimphyBody;
import org.shikhar.simphy.input.Input.Hold;

/**
 * Controls body on key being pressed
 * 
 * @author DELL
 *
 */
public class KeyBoardBodyController {
	/** body being controlled */
	private SimphyBody body;
	private Keyboard keyboard;
	


	private int rightAccKey = -1;
	private int leftAccKey = -1;
	private int upAccKey = -1;
	private int downAccKey = -1;
	private int acwAngACCKey = -1;
	private int cwAngACCKey = -1;
	private int destroyKey = -1;

	private double xAcc = 2;
	private double yAcc = 12;
	private double angleAcc = 1.5;

	/**
	 * minimal construction with no key set
	 * @param body
	 * @param keyboard KeyBoard object obtained in polling event
	 */
	public KeyBoardBodyController(SimphyBody body, Keyboard keyboard) {
		this.body = body;
		this.keyboard=keyboard;
		
	}
	
	/**
	 * returns body associated with the controller
	 * @return
	 */
	public SimphyBody getBody() {
		return body;
	}

	/**
	 * Sets body associated with the controller
	 * @param body
	 */
	public void setBody(SimphyBody body) {
		this.body = body;
	}
	
	public Keyboard getKeyboard() {
		return keyboard;
	}

	public void setKeyboard(Keyboard keyboard) {
		this.keyboard = keyboard;
	}


	/**
	 * return keys for the body in order of rightAccKey, leftAccKey, upAccKey, downAccKey, acwAngACCKey,cwAngACCKey
	 * where -1 shows undefined key
	 */
	public int[] getKeys(){
		return new int[]{rightAccKey, leftAccKey, upAccKey, downAccKey, acwAngACCKey,cwAngACCKey};
	}
	
	/**
	 * Set default keys for the body
	 */
	public void setDefaultKeys(){
		setKeys(KeyEvent.VK_D,KeyEvent.VK_A,KeyEvent.VK_W,KeyEvent.VK_S,KeyEvent.VK_Z,KeyEvent.VK_X,KeyEvent.VK_DELETE);
	}
	
	/**
	 * Sets keys
	 */
	public void setKeys(int[] keys){
		setKeys(keys[0], keys[1], keys[2], keys[3], keys[4],keys[5],keys[6]);
	}
	/**
	 * Set keyCodes for the controller (Set -1 to ignore key handling for the corresponding key) 
	 * @param rightKey KeyCcode to  accelerate body right (m/s2)
	 * @param leftKey  KeyCcode to  accelerate body left (m/s2)
	 * @param upkey KeyCcode to  accelerate body up (m/s2)
	 * @param downKey  KeyCcode to  accelerate body down (m/s2)
	 * @param acwAcc  KeyCcode to  accelerate body ACW (rad/s2)
	 * @param cwACC  KeyCcode to  accelerate body CW (rad/s2)
	 * @param destroyKey
	 */
	public void setKeys(int rightKey, int leftKey, int upkey, int downKey, int acwAcc, int cwACC, int destroyKey){
		this.rightAccKey =rightKey;
		this.leftAccKey = leftKey;
		this.upAccKey = upkey;
		this.downAccKey = downKey;
		this.acwAngACCKey = acwAcc;
		this.cwAngACCKey = cwACC;
		this.destroyKey = destroyKey;
		/*
		if(rightKey>0)this.keyboard.add(new Input(rightKey, Hold.HOLD));
		if(leftKey>0)this.keyboard.add(new Input(leftKey, Hold.HOLD));
		if(upkey>0)this.keyboard.add(new Input(upkey, Hold.HOLD));
		if(downKey>0)this.keyboard.add(new Input(downKey, Hold.HOLD));
		if(acwAcc>0)this.keyboard.add(new Input(acwAcc, Hold.HOLD));
		if(cwACC>0)this.keyboard.add(new Input(cwACC, Hold.HOLD));
		if(destroyKey>0)this.keyboard.add(new Input(destroyKey, Hold.NO_HOLD));
		*/
	}
	

	/**
	 * returns array containing acc accX, accY, angularAcc in order
	 * @return
	 */
	public double[] getAcc(){
		return new double[]{xAcc,yAcc,angleAcc};
	}
	
	/**
	 * Sets values of acceleration for the controller (Set 0 to ignore)
	 * @param acc
	 */
	public void setAcc(double[] acc){
		setAcc(acc[0],acc[1],acc[2]);
	}
	
	/**
	 * Sets values of acceleration for the controller (Set 0 to ignore)
	 * @param accX
	 * @param accY
	 * @param accAngle
	 */
	public void setAcc(double accX, double accY, double accAngle){
		xAcc = Math.abs(accX);
		yAcc = Math.abs(accY);
		angleAcc = Math.abs(accAngle);
	}
	
	/**
	 * Update body as per the keys
	 * @param dt time elapsed from last update
	 */

	public boolean update(double dt){
		
		//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("Update key performed on "+body.getName());
		// do not process if body is not the part of world
		if(body==null || body.getWorld()==null)return false;
		
		boolean consumed=false;
		//forward key
		if( keyboard.isPressed(rightAccKey)){
			//Vector2 f=new Vector2(body.getMassData().getMass()*xAcc,0);
			//f=Preferences.getVectorFromCustomAxes(f);
			//body.applyForce(f);//
			body.setLinearVelocity(body.getLinearVelocity().sum(xAcc*dt,0));
			//keyboard.reset(rightAccKey);
			consumed=true;
		}
		
		//backward key
		if(keyboard.isPressed(leftAccKey)){
			//Vector2 f=new Vector2(-body.getMassData().getMass()*xAcc,0);
			//f=Preferences.getVectorFromCustomAxes(f);
			//body.applyForce(f);//
			body.setLinearVelocity(body.getLinearVelocity().sum(-xAcc*dt,0));
			//keyboard.reset(leftAccKey);
			consumed=true;
		}
		//upward key
		if(keyboard.isPressed(upAccKey)){
			//d//body.setLinearVelocity(body.getLinearVelocity().sum(0,yAcc*dt));
			//Vector2 f=new Vector2(0,body.getMassData().getMass()*yAcc);
			//f=Preferences.getVectorFromCustomAxes(f);
			body.setLinearVelocity(body.getLinearVelocity().sum(0,yAcc*dt));
			//body.applyForce(f);
			consumed=true;
			//keyboard.reset(upAccKey);
			//if(org.shikhar.simphy.Simphy.DEBUG )System.out.println("Update key Force ="+body.getMass().getMass()*yAcc);
		}
		
		//downward key
		if(yAcc>0 && keyboard.isPressed(downAccKey)){
			//Vector2 f=new Vector2(0,-body.getMassData().getMass()*yAcc);
			//f=Preferences.getVectorFromCustomAxes(f);
			//body.applyForce(f);
			body.setLinearVelocity(body.getLinearVelocity().sum(0,-yAcc*dt));
			consumed=true;
			//body.applyImpulse(new Vector2(0,-body.getMass().getMass()*yAcc*dt));//body.setLinearVelocity(body.getLinearVelocity().sum(0,-yAcc*dt));
			//keyboard.reset(leftAccKey);
		}
		
		//rotate ACW key
		if(angleAcc>0 && keyboard.isPressed(acwAngACCKey)){
			//body.setAngularVelocity(body.getAngularVelocity()+angleAcc*dt);
			body.applyTorque(body.getMassData().getInertia()*angleAcc);
			consumed=true;
			//keyboard.reset(acwAngACCKey);
		}
		
		//rotate cloackwise Key
		if(angleAcc>0  && keyboard.isPressed(cwAngACCKey)){
			//body.setAngularVelocity(body.getAngularVelocity()-angleAcc*dt);
			body.applyTorque(-body.getMassData().getInertia()*angleAcc);
			consumed=true;
			//keyboard.reset(cwAngACCKey);
		}
		
		//destroy body key
		if(destroyKey>0 && keyboard.isPressed(destroyKey)){
			Simphy.instance.removeBody(body);
			keyboard.reset(destroyKey);
			consumed=true;
		}
		return consumed;
	}

}
