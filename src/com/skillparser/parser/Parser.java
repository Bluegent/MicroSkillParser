package com.skillparser.parser;

import java.util.ArrayList;

import com.skillparser.parser.ExpressionParser.Node;
public class Parser {
	public static final String selfKey ="$";
	public static final String chainKey = "~";
	
	public static Node getTree(String amountString){
		ArrayList<String> tok = ExpressionParser.Tokenizer.tokenize(amountString);
		tok = ExpressionParser.Converter.postfix(tok);
		return ExpressionParser.TreeConverter.convert(tok);
	}
	public static boolean targetSelf(String skill) {
		return skill.trim().startsWith(selfKey);
	}
	
	public static double computeAmount(Node node, Entity caster){
		if(node.isLeaf()) {
			try {
			 return  Double.parseDouble(node.value);
			} catch(Exception e) {
				return caster.getStat(node.value);
			}
		} else {
			return node.operation(computeAmount(node.left,caster), computeAmount(node.right,caster));
		}
		
	}
	
	public static void applySkill(String skill, Entity caster, Entity[] targets) {
		String[] skills = skill.split(Parser.chainKey);
		System.out.println("Skill to apply: \""+skill+"\"");
		for(int i=0;i<skills.length;++i) {
			System.out.println("subskill to apply: \""+skills[i]+"\"");
			String[] tokens = skills[i].trim().split(" ");
			Entity[] skillTargets;
			if(targetSelf(tokens[0]))
				skillTargets = new Entity[]{caster};
			else 
				skillTargets = targets;
			
			String expression = tokens[1].substring(1).replaceAll("\\{|\\}","");
			
			Entity.SkillValue amount = new Entity.SkillValue();
			
			amount.value = computeAmount(getTree(expression),caster);
			String skillType = tokens[0].replaceAll("\\"+Parser.selfKey, "");
			String damageType = String.valueOf(tokens[1].charAt(0));
			System.out.println("Expression: "+expression+" = "+amount.value);
			amount.type = damageType;
			for(int j=0;j<targets.length;++j)
				skillTargets[j].applySkill(skillType, amount);
		}
	}
}
