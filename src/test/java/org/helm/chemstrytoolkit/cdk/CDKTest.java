/*******************************************************************************
 * Copyright C 2015, The Pistoia Alliance
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
package org.helm.chemstrytoolkit.cdk;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.helm.chemistrytoolkit.TestBase;
import org.helm.chemtoolkit.CTKException;
import org.helm.chemtoolkit.CTKSmilesException;
import org.helm.chemtoolkit.ManipulatorFactory;
import org.helm.chemtoolkit.ManipulatorFactory.ManipulatorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * @author chistyakov
 *
 */
public class CDKTest extends TestBase {

  private static final Logger LOG = LoggerFactory.getLogger(CDKTest.class);

  @BeforeSuite(groups = {"CDKTest"})
  public void initialize() throws CTKException, IOException {

    try {
      LOG.debug("initialize");
      manipulator = ManipulatorFactory.buildManipulator(ManipulatorType.CDK);
    } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
        | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new CTKException("unable to invoke a manipulator");
    }

    if (!Files.exists(Paths.get("test-output"))) {
      Files.createDirectories(Paths.get("test-output"));
    }
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void validateSMILESTest() throws CTKException {
    super.validateSMILESTest();
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void getMoleculeInfoTest() throws CTKException, IOException {
    super.getMoleculeInfoTest();
    Assert.assertEquals(testResult, "C21H28N6O4S");

  }

  @Override
  @Test(groups = {"CDKTest"})
  public void convertSMILES2MolFile() throws CTKException, Exception {
    super.convertSMILES2MolFile();
    LOG.debug(testResult);

  }

  @Override
  @Test(groups = {"CDKTest"})
  public void convertMolFile2SMILES() throws CTKException, IOException {
    super.convertMolFile2SMILES();
    LOG.debug("canonical=" + testResult);
    Assert.assertEquals(testResult, "*OCC1OC([H])(N2C=NC3=C(N=CN=C32)N)C(O)C1O*");

  }

  @Override
  @Test(groups = {"CDKTest"})
  public void canonicalizeTest() throws CTKSmilesException, CTKException {
    super.canonicalizeTest();
    Assert.assertEquals(testResult, "O=C1NC(=NC=2C(=NN(C12)C)CC)C3=CC(=CC=C3OCC)S(=O)(=O)N4CCN(C)CC4");

  }

  @Override
  @Test(groups = {"CDKTest"})
  public void renderMolTest() throws CTKException, IOException {
    super.renderMolTest();
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void renderSequenceTest() throws NumberFormatException, CTKException, IOException {
    super.renderSequenceTest();
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void createGroupMolFile() throws CTKException, IOException {
    super.createGroupMolFile();
    LOG.debug(testResult);
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void adeninRiboseMerge() throws IOException, CTKException {
    super.adeninRiboseMerge();
    LOG.debug(testResult);
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void mergeTest() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException,
      CTKException {
    super.mergeTest();
    LOG.debug(testResult);

  }

  @Override
  @Test(groups = {"CDKTest"})
  public void getRibose() throws IOException, CTKException {
    super.getRibose();
    Assert.assertEquals(testResult, "O[C@H]1[C@H](O[C@H](CO[H])[C@H]1O[H])O");
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void aaMerge() throws IOException, CTKException {
    super.aaMerge();
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void mergePhosphatRiboseTest() throws ClassNotFoundException, NoSuchMethodException, SecurityException,
      InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException,
      CTKException {
    super.mergePhosphatRiboseTest();
    LOG.debug(testResult);
  }

  @Override
  @Test(groups = {"CDKTest"})
  public void mergeSelfCycle() throws CTKException, IOException {
    super.mergeSelfCycle();
    LOG.debug(testResult);

  }
}
