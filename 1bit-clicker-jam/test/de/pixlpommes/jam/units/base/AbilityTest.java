package de.pixlpommes.jam.units.base;

import static org.junit.Assert.*;

import org.junit.Test;

public class AbilityTest {

	@Test
	public void testCreateAttack() {
		Ability a1 = Ability.createAttack(1, 2);
		Ability a2 = Ability.createAttack(-1, 2);
		Ability a3 = Ability.createAttack(1, -2);
		
		// check basic setup @ expample a1
		assertEquals("Attack", a1.getName());
		assertEquals(Ability.Type.MELEE, a1.getType());
		assertEquals(Ability.TargetType.POINT, a1.getTargetType());
		assertEquals(0.0f, a1.getMpCosts(), 0.0001f);
		
		// check a1: damage & time
		assertEquals(2.0f, a1.getDamage(), 0.0001f);
		assertEquals(1.0f, a1.getUseTime(), 0.0001f);
		
		// check a2: damage & time
		assertEquals(2.0f, a2.getDamage(), 0.0001f);
		assertEquals(1.0f, a2.getUseTime(), 0.0001f);
		
		// check a3: damage & time
		assertEquals(2.0f, a3.getDamage(), 0.0001f);
		assertEquals(1.0f, a3.getUseTime(), 0.0001f);
	}

	@Test
	public void testCreateRangeAttack() {
		Ability a = Ability.createRangeAttack(1, 2);
		
		// check basic setup
		assertEquals("Range", a.getName());
		assertEquals(Ability.Type.RANGE, a.getType());
		assertEquals(Ability.TargetType.POINT, a.getTargetType());
		assertEquals(0.0f, a.getMpCosts(), 0.0001f);
		assertEquals(2.0f, a.getDamage(), 0.0001f);
		assertEquals(1.0f, a.getUseTime(), 0.0001f);
	}

	@Test
	public void testCreateHeal() {
		Ability a1 = Ability.createHeal(1, 2);
		Ability a2 = Ability.createHeal(-1, 2);
		Ability a3 = Ability.createHeal(1, -2);
		
		// check basic setup @ expample a1
		assertEquals("Heal", a1.getName());
		assertEquals(Ability.Type.MAGIC, a1.getType());
		assertEquals(Ability.TargetType.POINT, a1.getTargetType());
		assertEquals(1.0f, a1.getMpCosts(), 0.0001f);
		
		// check a1: heal & time
		assertEquals(-2.0f, a1.getDamage(), 0.0001f);
		assertEquals(1.0f, a1.getUseTime(), 0.0001f);
		
		// check a2: heal & time
		assertEquals(-2.0f, a2.getDamage(), 0.0001f);
		assertEquals(1.0f, a2.getUseTime(), 0.0001f);
		
		// check a3: heal & time
		assertEquals(-2.0f, a3.getDamage(), 0.0001f);
		assertEquals(1.0f, a3.getUseTime(), 0.0001f);
	}
	
	

	@Test
	public void testGetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetUseTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetType() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTargetType() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDamage() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMpCosts() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetUseTime() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetType() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetTargetType() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetDamage() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetMpCosts() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

}
