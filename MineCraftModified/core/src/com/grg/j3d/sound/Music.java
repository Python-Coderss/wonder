package com.grg.j3d.sound;

public class Music {

	
	
	
	
	public Music() {
		// TODO Auto-generated constructor stub
		
		
		
		
		
		
	}

	public void  event(Event event) {
		// TODO Auto-generated method stub
		
			event.play();
			
			System.out.println("EXIT EVENT");
		
	}

	
	public void dispose() {
		for (Event e : Event.values()) {
			e.discard();
		}
		System.out.println("EXIT m.DISPOSE");
	}
	
	

}
