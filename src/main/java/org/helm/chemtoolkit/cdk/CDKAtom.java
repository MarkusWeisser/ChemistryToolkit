/*******************************************************************************
 * Copyright C 2015, The Pistoia Alliance
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package org.helm.chemtoolkit.cdk;

import org.helm.chemtoolkit.IAtomBase;
import org.helm.chemtoolkit.IBondBase;
import org.openscience.cdk.interfaces.IAtom;

/**
 * @author chistyakov
 *
 */
public class CDKAtom implements IAtomBase {

	protected IAtom atom;

	protected int rGroup;
	
	protected int boundCount;
	


	/**
	 * @param atom
	 */
	public CDKAtom(IAtom atom) {
		this.atom = atom;
	}

	public CDKAtom(IAtom atom, int rGroup) {
		this.atom = atom;
		this.rGroup = rGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.IAtomBase#getIBondCount()
	 */
	@Override
	public int getIBondCount() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.IAtomBase#getIBond(int)
	 */
	@Override
	public IBondBase getIBond(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.IAtomBase#getRgroup()
	 */
	@Override
	public int getRgroup() {

		return rGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.helm.chemtoolkit.IAtomBase#getIAtno()
	 */
	@Override
	public int getIAtno() {
		// TODO Auto-generated method stub
		return 0;
	}

}
