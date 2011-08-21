/*
 * Copyright 2011 Peter Abeles
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package gecv.alg.feature.describe.impl;

import gecv.misc.AutoTypeImage;
import gecv.misc.CodeGeneratorBase;
import gecv.misc.CodeGeneratorUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;


/**
 * @author Peter Abeles
 */
public class GenerateImplOrientationHistogram extends CodeGeneratorBase {
	String className;

	PrintStream out;
	AutoTypeImage imageType;

	@Override
	public void generate() throws FileNotFoundException {
		printClass(AutoTypeImage.F32);
		printClass(AutoTypeImage.S16);
		printClass(AutoTypeImage.S32);
	}

	private void printClass( AutoTypeImage imageType ) throws FileNotFoundException {
		this.imageType = imageType;
		className = "ImplOrientationHistogram_"+imageType.getAbbreviatedType();
		out = new PrintStream(new FileOutputStream(className + ".java"));
		printPreamble();
		printFunctions();

		out.print("}\n");
	}

	private void printPreamble() {
		out.print(CodeGeneratorUtil.copyright);
		out.print("package gecv.alg.feature.describe.impl;\n" +
				"\n" +
				"import gecv.alg.InputSanityCheck;\n" +
				"import gecv.alg.feature.describe.OrientationHistogram;\n" +
				"import gecv.struct.image."+imageType.getImageName()+";\n" +
				"\n" +
				"\n" +
				"/**\n" +
				" * <p>\n" +
				" * Implementation of {@link OrientationHistogram} for a specific image type.\n" +
				" * </p>\n" +
				" *\n" +
				" * <p>\n" +
				" * WARNING: Do not modify.  Automatically generated by {@link GenerateImplOrientationHistogram}.\n" +
				" * </p>\n" +
				" *\n" +
				" * @author Peter Abeles\n" +
				" */\n" +
				"public class "+className+" extends OrientationHistogram<"+imageType.getImageName()+"> {\n" +
				"\n" +
				"\tpublic "+className+"(int numAngles , boolean isWeighted ) {\n" +
				"\t\tsuper(numAngles,isWeighted);\n" +
				"\t}\n\n");
	}

	private void printFunctions() {
		printVarious();
		printUnweighted();
		printWeighted();
	}

	private void printVarious() {
		out.print("\t@Override\n" +
				"\tpublic Class<"+imageType.getImageName()+"> getImageType() {\n" +
				"\t\treturn "+imageType.getImageName()+".class;\n" +
				"\t}\n\n");
	}

	private void printUnweighted() {
		String type = imageType.getDataType();
		String bitWise = imageType.getBitWise();

		out.print("\t@Override\n" +
				"\tprotected void computeUnweightedScore() {\n" +
				"\t\t// compute the score for each angle in the histogram\n" +
				"\t\tfor( int y = rect.y0; y < rect.y1; y++ ) {\n" +
				"\t\t\tint indexX = derivX.startIndex + derivX.stride*y + rect.x0;\n" +
				"\t\t\tint indexY = derivY.startIndex + derivY.stride*y + rect.x0;\n" +
				"\n" +
				"\t\t\tfor( int x = rect.x0; x < rect.x1; x++ , indexX++ , indexY++ ) {\n" +
				"\t\t\t\t"+type+" dx = derivX.data[indexX]"+bitWise+";\n" +
				"\t\t\t\t"+type+" dy = derivY.data[indexY]"+bitWise+";\n" +
				"\n" +
				"\t\t\t\tdouble angle = Math.atan2(dy,dx);\n" +
				"\t\t\t\t// compute which discretized angle it is\n" +
				"\t\t\t\tint discreteAngle = (int)((angle + angleRound)/angleDiv) % numAngles;\n" +
				"\t\t\t\t// sum up the \"score\" for this angle\n" +
				"\t\t\t\tsumDerivX[discreteAngle] += dx;\n" +
				"\t\t\t\tsumDerivY[discreteAngle] += dy;\n" +
				"\t\t\t}\n" +
				"\t\t}\n" +
				"\t}\n\n");
	}
	private void printWeighted() {
		String type = imageType.getDataType();
		String bitWise = imageType.getBitWise();
		out.print("\t@Override\n" +
				"\tprotected void computeWeightedScore( int c_x , int c_y ) {\n" +
				"\t\t// compute the score for each angle in the histogram\n" +
				"\t\tfor( int y = rect.y0; y < rect.y1; y++ ) {\n" +
				"\t\t\tint indexX = derivX.startIndex + derivX.stride*y + rect.x0;\n" +
				"\t\t\tint indexY = derivY.startIndex + derivY.stride*y + rect.x0;\n" +
				"\t\t\tint indexW = (y-c_y+radiusScale)*weights.width + rect.x0-c_x+radiusScale;\n" +
				"\n" +
				"\t\t\tfor( int x = rect.x0; x < rect.x1; x++ , indexX++ , indexY++ , indexW++ ) {\n" +
				"\t\t\t\tfloat w = weights.data[indexW];\n" +
				"\n" +
				"\t\t\t\t"+type+" dx = derivX.data[indexX]"+bitWise+";\n" +
				"\t\t\t\t"+type+" dy = derivY.data[indexY]"+bitWise+";\n" +
				"\n" +
				"\t\t\t\tdouble angle = Math.atan2(dy,dx);\n" +
				"\t\t\t\t// compute which discretized angle it is\n" +
				"\t\t\t\tint discreteAngle = (int)((angle + angleRound)/angleDiv) % numAngles;\n" +
				"\t\t\t\t// sum up the \"score\" for this angle\n" +
				"\t\t\t\tsumDerivX[discreteAngle] += w*dx;\n" +
				"\t\t\t\tsumDerivY[discreteAngle] += w*dy;\n" +
				"\t\t\t}\n" +
				"\t\t}\n" +
				"\t}\n\n");
	}

	public static void main( String args[] ) throws FileNotFoundException {
		GenerateImplOrientationHistogram app = new GenerateImplOrientationHistogram();
		app.generate();
	}
}
