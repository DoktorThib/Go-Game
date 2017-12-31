package fr.ensim_GO_Game;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

public class TestGoLogic{
	private GoLogic object;
	
	@Before
	public void setup() {
		object = new GoLogic(null);
	}
	
	@Test
	public void testIsValidIndex()throws Exception{
		Class[] params = {Integer.class, Integer.class};
		int[] args = {-1, 1};
		int[] args2 = {1, -1};
		Method method = GoLogic.class.getDeclaredMethod("GoLogic", params);
		method.setAccessible(true);
		
		assertEquals(false, method.invoke(object, args));
		assertEquals(false, method.invoke(object, args2));
		
	}
	
}
