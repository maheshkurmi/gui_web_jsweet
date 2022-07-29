package org.shikhar.simphy.input;

import org.dyn4j.geometry.Vector2;

public class GestureEvent {
		public enum GESTURETYPE{
			NONE,FLINGE,PINCH_AND_PAN,STOP
		}
		public GESTURETYPE gestureType=GESTURETYPE.NONE;
		public double x,y,dx,dy;
		public int fingerCount;
		public double zoom=1;
		public double angle=0;
		private boolean consumed=false;
		private VelocityTracker tracker1=new VelocityTracker();
		private VelocityTracker tracker2=new VelocityTracker();
		private Finger f1,f2;
		private Vector2 v1=new Vector2();
		private Vector2 v2=new Vector2();
		private double initialDistance;
		private double initialAngle;
		private boolean active=false;
		private int maxFlingDelay =300;
		private long gestureStartTime=0;
		/*
		//half width in pixels of the square around an initial touch event,
		public float halfTapSquareSize=20;
		//time in millisec that must pass for two touch down/up sequences to be detected as consecutive taps.
		public long tapCountInterval=400;

		private boolean inTapRectangle;
		private int tapCount;
		private long lastTapTime;
		private float lastTapX, lastTapY;
		private int lastTapButton, lastTapPointer;
		*/
		public void reset() {
			gestureType=GESTURETYPE.NONE;
			x=-1;
			y=-1;
			dx=0;
			dy=0;
			zoom=1;
			angle=0;
			consumed=true;
			active=false;
		}
		
		protected void begin(Finger f1,Finger f2) {
			if(!f1.isActive() ||!f2.isActive())return;
			consumed=false;
			active=true;
			this.f1=f1;
			this.f2=f2;
			x=(f1.x+f2.x)/2;
			y=(f1.y+f2.y)/2;
			dx=0;
			dy=0;
			gestureStartTime=System.currentTimeMillis();
			tracker1.start(f1.x, f1.y, gestureStartTime);
			tracker2.start(f2.x, f2.y, gestureStartTime);
			v1=tracker1.initialPointer.to(tracker2.initialPointer);
			initialDistance=v1.getMagnitude();
			initialAngle=v1.getAngleWithPositiveXAxis();
			gestureType=GESTURETYPE.PINCH_AND_PAN;
		}
		
		protected void end() {
			this.dx=0;
			this.dy=0;
			f1=null;
			f2=null;
			if(gestureType==GESTURETYPE.PINCH_AND_PAN) {
				if(zoom==1|true) {
					v1.set(tracker1.getVelocityX(),tracker1.getVelocityY());
					v2.set(tracker2.getVelocityX(),tracker2.getVelocityY());
					double th=Math.abs(v1.getAngleBetween(v2));
					if(System.currentTimeMillis()-gestureStartTime<maxFlingDelay) {//&&v1.getMagnitudeSquared()>100 &&v2.getMagnitudeSquared()>100) {
						gestureType=GESTURETYPE.FLINGE;
						this.dx=(v1.x+v2.x)/2;
						this.dy=(v1.y+v2.y)/2;
					} else {
						gestureType=GESTURETYPE.STOP;
					}
				}
				consumed=false;
			}
		}
		
		protected void update() {
			if(f1==null||f2==null)return;
			if(!f1.isActive() ||!f2.isActive()) {
				if(gestureType==GESTURETYPE.PINCH_AND_PAN)end();
				return;
			}
			if(gestureType!=GESTURETYPE.PINCH_AND_PAN)return;
			double x=(f1.x+f2.x)/2;
			double y=(f1.y+f2.y)/2;
			this.dx=(x-this.x);
			this.dy=(y-this.y);
			this.x=x;
			this.y=y;
			tracker1.update(f1.x, f1.y,  System.currentTimeMillis());
			tracker2.update(f2.x, f2.y,  System.currentTimeMillis());
			v1.set(tracker1.getVelocityX(),tracker1.getVelocityY());
			v2.set(tracker2.getVelocityX(),tracker2.getVelocityY());
			double th=Math.abs(v1.getAngleBetween(v2));
			v1.set(f2.x-f1.x,f2.y-f1.y);
			double d=v1.getMagnitude();
	        double a=v1.getAngleWithPositiveXAxis();
			if(th<0.0) {
				zoom=1;
				angle=0;
			}else {
		        zoom=d/initialDistance;
		        angle=a-initialAngle;
		        initialDistance=d;
				if(th>1.2) {
					//dx=0;
					//dy=0;
				}
			}
			initialDistance=d;
			initialAngle=a;
			//if(zoom!=1|| dx!=0||dy!=0)
				consumed=false;
		}
		
		public void consume() {
			consumed=true;
			if(!active||f1==null||f2==null||gestureType==GESTURETYPE.FLINGE||gestureType==GESTURETYPE.STOP) {
				gestureType=GESTURETYPE.NONE;
				active=false;
				//System.out.println("Gesture consumed");
			}
		}
		
		public boolean isConsumed() {
			return consumed;
		}
		
		public boolean isActive() {
			return active && gestureType!=GESTURETYPE.NONE;
		}
		
		private class VelocityTracker {
			int sampleSize = 10;
			double lastX, lastY;
			double deltaX, deltaY;
			long lastTime;
			int numSamples;
			double[] meanX = new double[sampleSize];
			double[] meanY = new double[sampleSize];
			long[] meanTime = new long[sampleSize];
			Vector2 initialPointer=new Vector2();
			public void start (double x, double y, long timeStamp) {
				initialPointer.set(x,y);
				lastX = x;
				lastY = y;
				deltaX = 0;
				deltaY = 0;
				numSamples = 0;
				for (int i = 0; i < sampleSize; i++) {
					meanX[i] = 0;
					meanY[i] = 0;
					meanTime[i] = 0;
				}
				lastTime = timeStamp;
			}

			public void update (double x, double y, long currTime) {
				deltaX = x - lastX;
				deltaY = y - lastY;
				lastX = x;
				lastY = y;
				long deltaTime = currTime - lastTime;
				lastTime = currTime;
				int index = numSamples % sampleSize;
				meanX[index] = deltaX;
				meanY[index] = deltaY;
				meanTime[index] = deltaTime;
				numSamples++;
			}

			public double getVelocityX () {
				double meanX = getAverage(this.meanX, numSamples);
				double meanTime = getAverage(this.meanTime, numSamples) / 1000.0f;
				if (meanTime == 0) return 0;
				return meanX / meanTime;
			}

			public double getVelocityY () {
				double meanY = getAverage(this.meanY, numSamples);
				double meanTime = getAverage(this.meanTime, numSamples) / 1000.0f;
				if (meanTime == 0) return 0;
				return meanY / meanTime;
			}

			private double getAverage (double[] values, int numSamples) {
				numSamples = Math.min(sampleSize, numSamples);
				double sum = 0;
				for (int i = 0; i < numSamples; i++) {
					sum += values[i];
				}
				return sum / numSamples;
			}

			private long getAverage (long[] values, int numSamples) {
				numSamples = Math.min(sampleSize, numSamples);
				long sum = 0;
				for (int i = 0; i < numSamples; i++) {
					sum += values[i];
				}
				if (numSamples == 0) return 0;
				return sum / numSamples;
			}

			private double getSum (double[] values, int numSamples) {
				numSamples = Math.min(sampleSize, numSamples);
				double sum = 0;
				for (int i = 0; i < numSamples; i++) {
					sum += values[i];
				}
				if (numSamples == 0) return 0;
				return sum;
			}
		}
	}