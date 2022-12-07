package com.grg.j3d.sound;


import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
public enum Event {
	
	background("midi.midi", false), breakBlock("break.midi", true);
	private InputStream input;
	private boolean running;
	private String midi;
	private boolean async;


	Event(String midi, boolean async) {
		// TODO Auto-generated constructor stub
		this.midi = midi;
		input = Thread.currentThread().getContextClassLoader().getResourceAsStream(midi);
		if (input == null) {
			input = getClass().getResourceAsStream(midi);
		}
		running = true;
		this.async = async;
	}
	
	public synchronized void play() {
		if (!async) {
			try {
	            Sequencer sequencer = MidiSystem.getSequencer();
	            sequencer.setSequence(MidiSystem.getSequence(input));
	            input = Thread.currentThread().getContextClassLoader().getResourceAsStream(midi);
	    		if (input == null) {
	    			input = getClass().getResourceAsStream(midi);
	    		}
	            sequencer.open();
	            sequencer.start();
	            while(true) {
	                if(sequencer.isRunning() && (running)) {
	                    try {
	                        Thread.sleep(1000); // Check every second
	                    } catch(InterruptedException ignore) {
	                        break;
	                    }
	                } else {
	                    break;
	                }
	            }
	            // Close the MidiDevice & free resources
	            sequencer.stop();
	            sequencer.close();
	        } catch(MidiUnavailableException mue) {
	            System.out.println("Midi device unavailable!");
	        } catch(InvalidMidiDataException imde) {
	            System.out.println("Invalid Midi data!");
	        } catch(IOException ioe) {
	            System.out.println("I/O Error! " + ioe);
	        }
		} else {
			Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
			            Sequencer sequencer = MidiSystem.getSequencer();
			            sequencer.setSequence(MidiSystem.getSequence(input));
			            input = Thread.currentThread().getContextClassLoader().getResourceAsStream(midi);
			    		if (input == null) {
			    			input = getClass().getResourceAsStream(midi);
			    		}
			            sequencer.open();
			            sequencer.start();
			            while(true) {
			                if(sequencer.isRunning() && (running)) {
			                    try {
			                        Thread.sleep(1000); // Check every second
			                    } catch(InterruptedException ignore) {
			                        break;
			                    }
			                } else {
			                    break;
			                }
			            }
			            // Close the MidiDevice & free resources
			            sequencer.stop();
			            sequencer.close();
			        } catch(MidiUnavailableException mue) {
			            System.out.println("Midi device unavailable!");
			        } catch(InvalidMidiDataException imde) {
			            System.out.println("Invalid Midi data!");
			        } catch(IOException ioe) {
			            System.out.println("I/O Error! " + ioe);
			        }
				}
				
			});
			t.setDaemon(true);
			t.start();
		}
		
		
	}

	public void stop() {
		running = false;
		System.out.println("EXIT STOP");
	}
	
	
	public void discard() {
		
		stop();
		System.out.println("EXIT  DISCARD");
	}
	
}
