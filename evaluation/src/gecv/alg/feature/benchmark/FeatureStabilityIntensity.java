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

package gecv.alg.feature.benchmark;

import gecv.alg.filter.basic.GGrayImageOps;
import gecv.core.image.ConvertBufferedImage;
import gecv.struct.image.ImageBase;

import java.awt.image.BufferedImage;
import java.util.List;


/**
 * Checks for stability against changes in illumination by changing the intensity of the input
 * image.
 *
 * @author Peter Abeles
 */
public class FeatureStabilityIntensity<T extends ImageBase>
		extends FeatureStabilityBase<T>
{
	// how much of the original intensity is returned
	double scale[];
	// input image type
	Class<T> imageType;

	public FeatureStabilityIntensity( Class<T> imageType , double ... scale) {
		this.imageType = imageType;
		this.scale = scale.clone();
	}

	@Override
	public List<MetricResult> evaluate( BufferedImage original ,
									 StabilityAlgorithm alg ,
									 StabilityEvaluator<T> evaluator) {
		T image = ConvertBufferedImage.convertFrom(original,null,imageType);
		T adjusted = (T)image._createNew(image.width,image.height);

		evaluator.extractInitial(alg,image);

		List<MetricResult> results = createResultsStorage(evaluator,scale);

		for( int i = 0; i < scale.length; i++ ) {
			GGrayImageOps.stretch(image,scale[i],0,255,adjusted);

			double[]metrics = evaluator.evaluateImage(alg,adjusted, null);

			for( int j = 0; j < results.size(); j++ ) {
				results.get(j).observed[i] = metrics[j];
			}
		}

		return results;
	}

	@Override
	public int getNumberOfObservations() {
		return scale.length;
	}

}