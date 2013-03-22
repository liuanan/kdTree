package test;

import java.io.IOException;

import entity.Corpus;
import entity.Instance;
import entity.Model;
import action.CorpusReader;
import action.Predicter;
import action.Trainer;

public class KDTreeTest {
	public static void test() throws IOException {
//		Model trainModel = Trainer.train("-i ./data/in -n false");
		Model trainModel = Trainer.train("-i ./data/in -n fales");
		trainModel.saveModel("./data/kdTree");
		
		
		Model model = Model.loadModel("./data/kdTree");
//		Corpus corpus = CorpusReader.readCorpus("./data/test");
		Corpus corpus = CorpusReader.readCorpus("./data/kd_test");
		int all = 0;
		int right = 0;
		for (Instance ins : corpus.getInstanceList()) {
			Predicter.predict(ins, model);
			System.out.println(String.format("%s %s", ins.getLabel(), ins.getPredictLabel()));
			System.out.println("instance: "+ins.getFeatures());
			System.out.println("neighbour: "+ins.getNeighbour().get(0).getFeatures());
			++all;
			if (ins.getLabel().equals(ins.getPredictLabel())) {
				++right;
			}
		}
		System.out.println(right+"/"+all);
	}
	public static void main(String[] args) {
		try {
			test();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
