package com.skillparser.parser;

import java.util.HashMap;
import java.util.function.Consumer;

public class Entity {
	
	public static class SkillValue {
		public double value;
		public String type;
	}
	
	
	public double  maxHP;
	public double currentHP;
	private HashMap<String, Double> statMap;
	private HashMap<String,Consumer<SkillValue>> skillApplicationMap;
	public Entity(double HP, double str, double dex, double intel) {
		maxHP = HP;
		currentHP = maxHP;
		statMap = new HashMap<String,Double>();
		statMap.put("STR", str);
		statMap.put("INT",intel);
		statMap.put("DEX", dex);
		statMap.put("DEF", str*5);
		statMap.put("MDEF", intel*5);
		skillApplicationMap = new  HashMap<String,Consumer<SkillValue>>();
		skillApplicationMap.put("damage", this::applyDamage);
		skillApplicationMap.put("heal", this::applyHeal);
	}
	
	public void applySkill(String type, SkillValue value) {
		Consumer<SkillValue> func = skillApplicationMap.get(type);
		System.out.println("Type:"+type+", element:"+value.type);
		System.out.println("value: "+value.value+" HP:"+currentHP);
		if(func !=null)
			func.accept(value);
		System.out.println("applied, HP:"+ currentHP);
	}
	
	public void applyDamage(SkillValue value) {
		switch(value.type){
		case "P": {
			double amount = value.value - statMap.get("DEF");
			currentHP -= amount;
		}
		break;
		case "M": {
			double amount = value.value - statMap.get("DEF");
			currentHP -= amount;
		}
		break;
		case "T": {
			currentHP -= value.value;		
		}
		break;
		}
	}

	public void applyHeal(SkillValue value) {
		currentHP += value.value;
	}
	public double getStat(String key){
		if(statMap.containsKey(key))
			return statMap.get(key);
		else
			return 0;
	}
	
}
