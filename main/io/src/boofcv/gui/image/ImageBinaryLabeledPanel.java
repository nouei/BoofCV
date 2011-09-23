/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://www.boofcv.org).
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

package boofcv.gui.image;

import boofcv.alg.filter.binary.BinaryImageOps;
import boofcv.gui.binary.VisualizeBinaryData;
import boofcv.struct.image.ImageSInt32;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Random;


/**
 * Displays labeled binary images.  In these images each detected object is given a unique number.  Each
 * of those numbers is given a unique color.
 *
 * @author Peter Abeles
 */
public class ImageBinaryLabeledPanel extends JPanel implements MouseListener {

	// the image being displayed
	protected BufferedImage img;
	protected ImageSInt32 labelImage;
	int colors[];

	public ImageBinaryLabeledPanel( ImageSInt32 labelImage, int maxValues , long randSeed ) {
		this();
		this.labelImage = labelImage;
		img = new BufferedImage(labelImage.getWidth(), labelImage.getHeight(),BufferedImage.TYPE_INT_RGB);

		setPreferredSize(new Dimension(labelImage.getWidth(), labelImage.getHeight()));
		setMinimumSize(getPreferredSize());
		setMaximumSize(getPreferredSize());

		Random rand = new Random(randSeed);

		colors = BinaryImageOps.selectRandomColors(maxValues,rand);
		VisualizeBinaryData.renderLabeled(labelImage,img,colors);
	}

	protected ImageBinaryLabeledPanel() {
		addMouseListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		//draw the image
		if (img != null)
			g.drawImage(img, 0, 0, this);
	}

	public void setImage(ImageSInt32 binaryImage) {
		this.labelImage = binaryImage;
	}

	public BufferedImage getImage() {
		return img;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if( labelImage.isInBounds(e.getX(),e.getY()) ) {
			int val = labelImage.get(e.getX(),e.getY());
			System.out.println("Label at ("+e.getX()+","+e.getY()+") = "+val);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
