/*
 * Copyright (c) 2011-2015, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.alg.fiducial.calib.squares;

/**
 * Edge in the graph which connects square shapes
 *
 * @author Peter Abeles
 */
public class SquareEdge {
	// destinations
	public SquareNode a;
	public SquareNode b;

	// which index in the shape this edge belongs to
	public int sideA;
	public int sideB;

	// the distance between the square's centers
	public double distance;

	/**
	 * Returns the destination node.
	 */
	public SquareNode destination(SquareNode src) {
		if( a == src )
			return b;
		else if( b == src )
			return a;
		else
			throw new IllegalArgumentException("BUG! src is not a or b");
	}

	public int destinationSide(SquareNode src) {
		if( a == src )
			return sideB;
		else if( b == src )
			return sideA;
		else
			throw new IllegalArgumentException("BUG! src is not a or b");
	}

	/**
	 * Discards previous settings.
	 */
	public void reset() {
		a = b = null;
		sideA = sideB = -1;
		distance = -1;
	}
}
