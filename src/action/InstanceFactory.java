package action;

import java.util.ArrayList;
import java.util.List;

import entity.Instance;


/**
 * 样本工厂
 * @author double
 * @data 2013-03-15
 */
public class InstanceFactory {
	
	public static Instance getInstance(String line) {
		String[] args = line.split(" ");
		List<Double> features = new ArrayList<Double>();
		for (int i = 1; i < args.length; ++i) {
			String[] split = args[i].split(":");
			if (split.length > 1) {
				features.add(Double.parseDouble(split[1]));
			}
			else {
				features.add(Double.parseDouble(split[0]));
			}
		}
		Instance ins = new Instance(features, args[0]);
		return ins;
	}
}
