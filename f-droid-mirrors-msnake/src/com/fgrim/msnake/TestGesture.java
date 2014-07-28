package com.fgrim.msnake;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import android.os.Environment;
import android.util.Log;
import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import be.ac.ulg.montefiore.run.jahmm.OpdfMultiGaussianFactory;
import be.ac.ulg.montefiore.run.jahmm.io.ObservationSequencesReader;
import be.ac.ulg.montefiore.run.jahmm.io.ObservationVectorReader;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;
import be.ac.ulg.montefiore.run.jahmm.learn.KMeansLearner;

public class TestGesture {
	OpdfMultiGaussianFactory initFactoryPunch = null;
	Reader learnReaderPunch = null;
	List<List<ObservationVector>> learnSequencesPunch = null;
	KMeansLearner<ObservationVector> kMeansLearnerPunch = null;
	Hmm<ObservationVector> initHmmPunch = null;
	Hmm<ObservationVector> learntHmmPunch = null;
	Hmm<ObservationVector> learntHmmScrolldown = null;
	Hmm<ObservationVector> learntHmmMoveRight = null;
	
	String root = Environment.getExternalStorageDirectory().toString();
	File myDir = new File(root + "/Data");
	private Hmm<ObservationVector> learntHmmSend = null;
	private Hmm<ObservationVector> learntHmmBackPress = null;
	private Hmm<ObservationVector> learntHmmRight;


	public void train() {
		myDir.mkdirs();
		// Create HMM for punch gesture
		Boolean exception = false;
		int x = 10;
		while (!exception) {
			try {
				OpdfMultiGaussianFactory initFactoryFly = new OpdfMultiGaussianFactory(
						3);

				Reader learnReaderFly = new FileReader(new File(myDir,
						"fly.seq"));
				List<List<ObservationVector>> learnSequencesFly = ObservationSequencesReader
						.readSequences(new ObservationVectorReader(),
								learnReaderFly);
				learnReaderFly.close();

				KMeansLearner<ObservationVector> kMeansLearnerFly = new KMeansLearner<ObservationVector>(
						x, initFactoryFly, learnSequencesFly);
				// Create an estimation of the HMM (initHmm) using one iteration
				// of the
				// k-Means algorithm
				Hmm<ObservationVector> initHmmFly = kMeansLearnerFly.iterate();
				// Use BaumWelchLearner to create the HMM (learntHmm) from
				// initHmm

				BaumWelchLearner baumWelchLearnerFly = new BaumWelchLearner();
				this.learntHmmPunch = baumWelchLearnerFly.learn(initHmmFly,
						learnSequencesFly);
				exception = true;
				System.out.println(x);
			} catch (Exception e) {
				x--;
				// System.out.println(x);

			}

		}
		// Create HMM for scroll-down gesture
		Boolean exception1 = false;
		int x1 = 10;
		while (!exception1) {
			try {
				OpdfMultiGaussianFactory initFactoryStomp = new OpdfMultiGaussianFactory(
						3);

				Reader learnReaderStomp = new FileReader(new File(myDir,
						"stomp.seq"));
				List<List<ObservationVector>> learnSequencesStomp = ObservationSequencesReader
						.readSequences(new ObservationVectorReader(),
								learnReaderStomp);
				learnReaderStomp.close();

				KMeansLearner<ObservationVector> kMeansLearnerStomp = new KMeansLearner<ObservationVector>(
						x1, initFactoryStomp, learnSequencesStomp);
				// Create an estimation of the HMM (initHmm) using one iteration
				// of the
				// k-Means algorithm
				Hmm<ObservationVector> initHmmStomp = kMeansLearnerStomp
						.iterate();

				// Use BaumWelchLearner to create the HMM (learntHmm) from
				// initHmm
				BaumWelchLearner baumWelchLearnerStomp = new BaumWelchLearner();
				this.learntHmmScrolldown = baumWelchLearnerStomp.learn(
						initHmmStomp, learnSequencesStomp);
				exception1 = true;
				// System.out.println("here1");
				System.out.println(x1);
			} catch (Exception e) {
				x1--;
				// System.out.println(x1);

			}
		}
		// Create HMM for send gesture
		Boolean exception2 = false;
		int x2 = 10;
		while (!exception2) {
			try {
				OpdfMultiGaussianFactory initFactoryMoveLeft = new OpdfMultiGaussianFactory(
						3);

				Reader learnReaderMoveLeft = new FileReader(new File(myDir,
						"left.seq"));
				List<List<ObservationVector>> learnSequencesMoveLeft = ObservationSequencesReader
						.readSequences(new ObservationVectorReader(),
								learnReaderMoveLeft);
				learnReaderMoveLeft.close();

				KMeansLearner<ObservationVector> kMeansLearnerMoveLeft = new KMeansLearner<ObservationVector>(
						x2, initFactoryMoveLeft, learnSequencesMoveLeft);
				// Create an estimation of the HMM (initHmm) using one iteration
				// of the
				// k-Means algorithm
				Hmm<ObservationVector> initHmmMoveLeft = kMeansLearnerMoveLeft
						.iterate();

				// Use BaumWelchLearner to create the HMM (learntHmm) from
				// initHmm
				BaumWelchLearner baumWelchLearnerMoveLeft = new BaumWelchLearner();
				this.learntHmmSend = baumWelchLearnerMoveLeft.learn(
						initHmmMoveLeft, learnSequencesMoveLeft);
				exception2 = true;
				System.out.println(x2);
			} catch (Exception e) {
				x2--;
				// System.out.println(x2);

			}
		}

		// Create HMM for send gesture
				Boolean exception3 = false;
				int x3 = 10;
				while (!exception3) {
					try {
						OpdfMultiGaussianFactory initFactoryMoveRight = new OpdfMultiGaussianFactory(
								3);

						Reader learnReaderMoveRight = new FileReader(new File(myDir,
								"right.seq"));
						List<List<ObservationVector>> learnSequencesMoveRight = ObservationSequencesReader
								.readSequences(new ObservationVectorReader(),
										learnReaderMoveRight);
						learnReaderMoveRight.close();

						KMeansLearner<ObservationVector> kMeansLearnerMoveRight = new KMeansLearner<ObservationVector>(
								x3, initFactoryMoveRight, learnSequencesMoveRight);
						// Create an estimation of the HMM (initHmm) using one iteration
						// of the
						// k-Means algorithm
						Hmm<ObservationVector> initHmmMoveRight = kMeansLearnerMoveRight
								.iterate();

						// Use BaumWelchLearner to create the HMM (learntHmm) from
						// initHmm
						BaumWelchLearner baumWelchLearnerMoveRight = new BaumWelchLearner();
						this.learntHmmRight = baumWelchLearnerMoveRight.learn(
								initHmmMoveRight, learnSequencesMoveRight);
						exception3 = true;
						System.out.println(x3);
					} catch (Exception e) {
						x3--;
						// System.out.println(x2);

					}
				}
			
	

		
		// Create HMM for send gesture
				Boolean exception4 = false;
				int x4 = 10;
				while (!exception4) {
					try {
						OpdfMultiGaussianFactory initFactoryBackPressed = new OpdfMultiGaussianFactory(
								3);

						Reader learnReaderBackPressed = new FileReader(new File(myDir,
								"backpress.seq"));
						List<List<ObservationVector>> learnSequencesBackPressed = ObservationSequencesReader
								.readSequences(new ObservationVectorReader(),
										learnReaderBackPressed);
						learnReaderBackPressed.close();

						KMeansLearner<ObservationVector> kMeansLearnerBackPressed = new KMeansLearner<ObservationVector>(
								x4, initFactoryBackPressed, learnSequencesBackPressed);
						// Create an estimation of the HMM (initHmm) using one iteration
						// of the
						// k-Means algorithm
						Hmm<ObservationVector> initHmmBackPressed = kMeansLearnerBackPressed
								.iterate();

						// Use BaumWelchLearner to create the HMM (learntHmm) from
						// initHmm
						BaumWelchLearner baumWelchLearnerMoveRight = new BaumWelchLearner();
						this.learntHmmBackPress = baumWelchLearnerMoveRight.learn(
								initHmmBackPressed, learnSequencesBackPressed);
						exception4 = true;
						System.out.println(x4);
					} catch (Exception e) {
						x4--;
						// System.out.println(x2);

					}
				}
	}

	public String test(File seqfilename) throws Exception {
		Reader testReader = new FileReader(seqfilename);
		List<List<ObservationVector>> testSequences = ObservationSequencesReader
				.readSequences(new ObservationVectorReader(), testReader);
		testReader.close();

		short gesture; // punch = 1, scrolldown = 2, send = 3
		double flyProbability, stompProbability, moveLeftProbability, moveRightProbability, backPressedProbability;
		for (int i = 0; i < testSequences.size(); i++) {

			flyProbability = this.learntHmmPunch.probability(testSequences
					.get(i));
			// System.out.println(this.learntHmmPunch.probability(testSequences.get(i)));
			gesture = 1;
			// System.out.println(this.learntHmmScrolldown);
			stompProbability = this.learntHmmScrolldown
					.probability(testSequences.get(i));

			if (stompProbability > flyProbability) {
				gesture = 2;
			}
			moveLeftProbability = this.learntHmmSend.probability(testSequences
					.get(i));
			// System.out.println(punchProbability +","+scrolldownProbability
			// +","+sendProbability);
			if ((gesture == 1 && moveLeftProbability > flyProbability)
					|| (gesture == 2 && moveLeftProbability > stompProbability)) {
				gesture = 3;
			}

			moveRightProbability = this.learntHmmRight
					.probability(testSequences.get(i));
			if ((gesture == 1 && moveRightProbability > flyProbability)
					|| (gesture == 2 && moveRightProbability > stompProbability)
					|| (gesture == 3 && moveRightProbability > moveLeftProbability)) {
				gesture = 4;
			}
			
			backPressedProbability = this.learntHmmBackPress
					.probability(testSequences.get(i));
			if ((gesture == 1 && backPressedProbability > flyProbability)
					|| (gesture == 2 && backPressedProbability > stompProbability)
					|| (gesture == 3 && backPressedProbability > moveLeftProbability) || (gesture ==4 && backPressedProbability > moveRightProbability)) {
				gesture = 5;
			}

			Log.i("probabilities", flyProbability + "   " + stompProbability
					+ "   " + moveLeftProbability + "   "
					+ moveRightProbability + " "+backPressedProbability);
			if (gesture == 1) {
				// fly gesture
				return "fly";
			} else if (gesture == 2) {
				// stomp gesture
				return "stomp";
			} else if (gesture == 3) {
				// move left
				return "left";
			} else if (gesture == 4) {
				// move right
				return "right";
			}else if (gesture == 5){
				return "back";
			}else {
				return "others";
			}
		}
		return "others";
	}

}