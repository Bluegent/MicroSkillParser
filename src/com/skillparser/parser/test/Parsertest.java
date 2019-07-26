package com.skillparser.parser.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.skillparser.parser.Entity;
import com.skillparser.parser.Parser;

public class Parsertest {
	 Entity dick;
     Entity butt;
     
	@Before 
	public void build() {
		dick = new Entity(100,5,5,5);
		butt = new Entity(1000,5,5,5);	
	}
	
	@Test
	public void simpleSkillTest(){
        Entity targets[] = {butt};
        Parser.applySkill("damage T{STR*10}",dick, targets);
        double intendedHP = butt.maxHP-dick.getStat("STR")*10;
        assertEquals(intendedHP, butt.currentHP);
	}
	
	@Test
	public void chainedSkillTest(){
        Entity targets[] = {butt};
        dick.currentHP = 1;
        double intendedHP = butt.maxHP-dick.getStat("STR")*30;
        double intendedHP2 = dick.currentHP+dick.getStat("INT")*3;
        Parser.applySkill("damage T{STR*30}~$heal M{INT*3}",dick, targets);
        assertEquals(intendedHP, butt.currentHP);
        assertEquals(intendedHP2, dick.currentHP);
	}
}
